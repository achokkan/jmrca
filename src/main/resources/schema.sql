-- JMCRA Database Schema (PostgreSQL)
-- This schema stores scan history and results for the Web UI.

CREATE TABLE IF NOT EXISTS scans (
    id UUID PRIMARY KEY,
    repository_url TEXT NOT NULL,
    commit_sha VARCHAR(40),
    branch TEXT,
    status VARCHAR(20) NOT NULL, -- STARTED, COMPLETED, FAILED
    health_score NUMERIC(5, 2),
    gate_violated BOOLEAN DEFAULT FALSE,
    report    TEXT,        -- ✅ Large JSON text
    started_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP
);

-- Index for faster history retrieval
CREATE INDEX IF NOT EXISTS idx_scans_started_at ON scans(started_at DESC);
