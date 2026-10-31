import React from 'react';
import { Activity, Database, CheckCircle2, AlertOctagon, RefreshCw } from 'lucide-react';

export default function HealthStatusBadge({ health, loading, error, onRefresh }) {
  const isHealthy = !error && health && health.status === 'UP' && health.databaseConnected;

  return (
    <div style={{
      display: 'flex',
      alignItems: 'center',
      gap: '12px',
      background: 'rgba(30, 41, 59, 0.7)',
      padding: '8px 16px',
      borderRadius: '8px',
      border: `1px solid ${isHealthy ? 'rgba(16, 185, 129, 0.3)' : 'rgba(239, 68, 68, 0.3)'}`
    }}>
      <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
        <span style={{
          width: '10px',
          height: '10px',
          borderRadius: '50%',
          backgroundColor: isHealthy ? '#10b981' : '#ef4444',
          boxShadow: isHealthy ? '0 0 10px #10b981' : '0 0 10px #ef4444'
        }} />
        <span style={{ fontSize: '13px', fontWeight: 600, color: isHealthy ? '#34d399' : '#f87171' }}>
          {loading ? 'Checking...' : isHealthy ? 'Backend & DB Online' : 'Backend Disconnected'}
        </span>
      </div>

      <div style={{ height: '16px', width: '1px', background: '#334155' }} />

      <div style={{ display: 'flex', alignItems: 'center', gap: '6px', fontSize: '12px', color: '#94a3b8' }}>
        <Database size={14} color={health?.databaseConnected ? '#10b981' : '#ef4444'} />
        <span>{health?.databaseProductName ? `${health.databaseProductName} Connected` : 'PostgreSQL Waiting'}</span>
      </div>

      <button
        onClick={onRefresh}
        title="Refresh Health Status"
        style={{
          background: 'transparent',
          border: 'none',
          color: '#94a3b8',
          display: 'flex',
          alignItems: 'center',
          padding: '4px',
          borderRadius: '4px',
          cursor: 'pointer',
        }}
      >
        <RefreshCw size={14} className={loading ? 'spin' : ''} style={{ animation: loading ? 'spin 1s linear infinite' : 'none' }} />
      </button>

      <style>{`
        @keyframes spin { 100% { transform: rotate(360deg); } }
      `}</style>
    </div>
  );
}

