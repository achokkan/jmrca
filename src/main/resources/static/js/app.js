/**
 * JMCRA Dashboard — app.js
 * Modal system: backdrop click, ESC key, scroll lock.
 * Report view: split-pane with search + severity filter.
 */

// ── State ─────────────────────────────────────────────────────────────────────
let currentReport = null;
let activeFilter  = { search: '', severity: 'ALL' };
let activeCardIdx = null;

// ── Modal helpers ─────────────────────────────────────────────────────────────
function openModal(id) {
    const el = document.getElementById(id);
    if (!el) return;
    el.classList.add('active');
    document.body.style.overflow = 'hidden';
}

function closeModal(id) {
    const el = document.getElementById(id);
    if (!el) return;
    el.classList.remove('active');
    // Only re-enable scroll if no other modals are open
    if (!document.querySelector('.modal.active')) {
        document.body.style.overflow = '';
    }
}

function closeAllModals() {
    document.querySelectorAll('.modal.active').forEach(m => m.classList.remove('active'));
    document.body.style.overflow = '';
}

// ── Bootstrap ─────────────────────────────────────────────────────────────────
document.addEventListener('DOMContentLoaded', () => {
    fetchScans();

    // New Scan button
    document.getElementById('btn-new-scan').addEventListener('click', () => openModal('modal-scan'));
    document.getElementById('btn-refresh').addEventListener('click', fetchScans);
    document.getElementById('btn-start-scan').addEventListener('click', triggerLocalScan);

    // Close buttons (any .close-modal inside any .modal)
    document.addEventListener('click', e => {
        if (e.target.closest('.close-modal')) {
            const modal = e.target.closest('.modal');
            if (modal) closeModal(modal.id);
        }
    });

    // Backdrop click — close if clicking directly on the .modal overlay
    document.addEventListener('click', e => {
        if (e.target.classList.contains('modal')) {
            closeModal(e.target.id);
        }
    });

    // ESC key
    document.addEventListener('keydown', e => {
        if (e.key === 'Escape') closeAllModals();
    });

    // Report search
    document.getElementById('report-search-input')?.addEventListener('input', e => {
        activeFilter.search = e.target.value.toLowerCase().trim();
        renderFindingList();
    });

    // Report severity filters
    document.getElementById('report-filter-tags')?.addEventListener('click', e => {
        const tab = e.target.closest('.filter-tab');
        if (!tab) return;
        document.querySelectorAll('.filter-tab').forEach(t => t.classList.remove('active'));
        tab.classList.add('active');
        activeFilter.severity = tab.dataset.sev;
        renderFindingList();
    });
});

// ── Scan history ──────────────────────────────────────────────────────────────
async function fetchScans() {
    try {
        const scans = await fetch('/api/scans').then(r => r.json());
        renderScanTable(scans);
        updateStats(scans);
    } catch (err) {
        console.error('fetchScans failed:', err);
    }
}

function renderScanTable(scans) {
    const tbody = document.getElementById('scan-tbody');
    if (!scans.length) {
        tbody.innerHTML = '<tr><td colspan="5" style="text-align:center;padding:24px;color:var(--text-dim)">No scans yet.</td></tr>';
        return;
    }
    tbody.innerHTML = scans.map(s => `
        <tr class="scan-row">
            <td><span class="badge badge-${statusClass(s.status)}">${s.status}</span></td>
            <td>
                <div style="font-weight:600">${trunc(s.repositoryUrl, 42)}</div>
                <div style="font-size:12px;color:var(--text-dim)">${s.branch} @ ${s.commitSha ? s.commitSha.slice(0,7) : 'n/a'}</div>
            </td>
            <td class="score-cell ${scoreClass(s.healthScore)}">${s.healthScore != null ? (+s.healthScore).toFixed(1) : '—'}</td>
            <td style="font-size:13px">${new Date(s.startedAt).toLocaleString()}</td>
            <td>
                <button class="btn btn-secondary btn-sm"
                    onclick="viewReport('${s.id}')"
                    ${s.status !== 'COMPLETED' ? 'disabled' : ''}>View Report</button>
            </td>
        </tr>
    `).join('');
}

function updateStats(scans) {
    const done = scans.filter(s => s.status === 'COMPLETED');
    document.getElementById('stat-total').textContent = scans.length;
    document.getElementById('stat-rate').textContent =
        scans.length ? Math.round(done.length / scans.length * 100) + '%' : '0%';
    if (done.length) {
        const avg = done.reduce((a, s) => a + (s.healthScore || 0), 0) / done.length;
        const el = document.getElementById('stat-score');
        el.textContent = avg.toFixed(1);
        el.className = `stat-value highlight ${scoreClass(avg)}`;
    }
}

// ── Local scan trigger ────────────────────────────────────────────────────────
async function triggerLocalScan() {
    const path    = document.getElementById('scan-path').value.trim();
    const profile = document.getElementById('scan-profile').value;
    if (!path) { alert('Please provide a local path.'); return; }

    const btn = document.getElementById('btn-start-scan');
    btn.disabled = true;
    btn.textContent = 'Triggering…';

    try {
        const res = await fetch('/webhook/local', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ path, profile })
        });
        if (res.ok) {
            closeModal('modal-scan');
            fetchScans();
            const iv = setInterval(fetchScans, 3000);
            setTimeout(() => clearInterval(iv), 30000);
        } else {
            const err = await res.json().catch(() => ({}));
            alert('Failed: ' + (err.error || res.statusText));
        }
    } catch (e) {
        alert('Network error: ' + e.message);
    } finally {
        btn.disabled = false;
        btn.textContent = 'Start Analysis';
    }
}

// ── Report view ───────────────────────────────────────────────────────────────
async function viewReport(scanId) {
    try {
        const report = await fetch(`/api/scans/${scanId}/report`).then(r => r.json());
        currentReport = report;
        activeFilter  = { search: '', severity: 'ALL' };
        activeCardIdx = null;

        // Reset controls
        const si = document.getElementById('report-search-input');
        if (si) si.value = '';
        document.querySelectorAll('.filter-tab').forEach(t =>
            t.classList.toggle('active', t.dataset.sev === 'ALL'));

        // Meta
        document.getElementById('report-repo').textContent   = report.repositoryUrl || '—';
        document.getElementById('report-branch').textContent = report.branch || '—';

        // Score pill
        const scoreEl = document.getElementById('lbl-score');
        if (scoreEl) {
            const s = report.healthScore?.score ?? null;
            scoreEl.textContent  = s != null ? s.toFixed(1) : '—';
            scoreEl.className    = 'rm-score-val ' + scoreClass(s);
        }

        // Count pills
        const findings = report.findings || [];
        const critCount = findings.filter(f => f.severity === 'CRITICAL').length;
        document.getElementById('lbl-total').textContent    = findings.length;
        document.getElementById('lbl-critical').textContent = critCount;

        // Default detail
        document.getElementById('report-details-panel').innerHTML = `
            <div class="rm-detail-empty">
                <i data-lucide="file-search"></i>
                <p>Select a finding on the left to inspect it</p>
            </div>`;

        renderFindingList();
        openModal('modal-report');
        if (window.lucide) lucide.createIcons();
    } catch (e) {
        console.error(e);
        alert('Failed to load report.');
    }
}

function renderFindingList() {
    const list   = document.getElementById('report-finding-list');
    const findings = (currentReport?.findings || []);

    const filtered = findings.filter(f => {
        const q = activeFilter.search;
        const matchSearch   = !q ||
            (f.ruleId  || '').toLowerCase().includes(q) ||
            (f.title   || '').toLowerCase().includes(q) ||
            (f.file|| '').toLowerCase().includes(q);
        const matchSeverity = activeFilter.severity === 'ALL' || f.severity === activeFilter.severity;
        return matchSearch && matchSeverity;
    });

    if (!filtered.length) {
        list.innerHTML = '<div class="rm-list-empty">No matching findings.</div>';
        return;
    }

    list.innerHTML = filtered.map(f => {
        const origIdx = findings.indexOf(f);
        const isActive = origIdx === activeCardIdx;
        return `
        <div class="rm-card sev-${(f.severity||'').toLowerCase()} ${isActive ? 'active' : ''}"
             onclick="showFinding(${origIdx})">
            <div class="rm-card-top">
                <span class="rm-card-id">${f.ruleId || ''}</span>
                <span class="badge badge-${sevClass(f.severity)}">${f.severity || ''}</span>
            </div>
            <div class="rm-card-title">${esc(f.title)}</div>
            <div class="rm-card-file">${trunc(f.file || '', 44)}</div>
        </div>`;
    }).join('');
}

function showFinding(idx) {
    activeCardIdx = idx;
    renderFindingList();           // re-render to update active highlight

    const f       = currentReport.findings[idx];
    const panel   = document.getElementById('report-details-panel');
    const lineRef = f.line || '?';

    panel.innerHTML = `
        <div class="rm-detail-content">
            <div class="rm-d-header">
                <h2>${esc(f.title)}</h2>
                <div class="rm-d-meta">
                    <span class="badge badge-${sevClass(f.severity)}">${f.severity}</span>
                    <code class="rm-d-filepath">${esc(f.file)}:${lineRef}</code>
                    ${f.gateViolated ? '<span class="badge badge-failed">GATE VIOLATED</span>' : ''}
                </div>
            </div>

            <h4>Description</h4>
            <div class="rm-d-desc">${esc(f.message || '—')}</div>

            ${f.remediation ? `
                <h4>Suggested Fix</h4>
                <pre class="rm-d-fix">${esc(f.remediation)}</pre>
            ` : ''}

            ${f.metadata && Object.keys(f.metadata).length ? `
                <h4>Metadata</h4>
                <pre class="rm-d-meta-json">${esc(JSON.stringify(f.metadata, null, 2))}</pre>
            ` : ''}
        </div>`;

    panel.scrollTop = 0;
    if (window.lucide) lucide.createIcons();
}

// ── Helpers ───────────────────────────────────────────────────────────────────
function statusClass(s = '') {
    const l = s.toLowerCase();
    if (l === 'completed') return 'success';
    if (l === 'started')   return 'working';
    return 'failed';
}

function scoreClass(n) {
    if (n == null) return '';
    if (n >= 90)   return 'score-high';
    if (n >= 70)   return 'score-medium';
    return 'score-low';
}

function sevClass(sev = '') {
    if (sev === 'CRITICAL' || sev === 'HIGH') return 'failed';
    if (sev === 'MEDIUM')  return 'working';
    return 'success';
}

function trunc(s = '', n) {
    return s.length > n ? s.slice(0, n - 1) + '…' : s;
}

function esc(text = '') {
    const d = document.createElement('div');
    d.textContent = text;
    return d.innerHTML;
}
