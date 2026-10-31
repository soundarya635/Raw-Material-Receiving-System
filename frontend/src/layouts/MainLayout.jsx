import React from 'react';
import { ShieldCheck, Truck, FlaskConical, Layers, Database } from 'lucide-react';
import HealthStatusBadge from '../components/HealthStatusBadge';

export default function MainLayout({ activeTab, onTabChange, health, loading, error, onRefresh, children }) {
  const tabs = [
    { id: 'dashboard', label: 'System Overview & Health', icon: ShieldCheck },
    { id: 'receiving', label: 'Yard Receiving Simulation', icon: Truck },
    { id: 'inspection', label: 'Quality Inspection Workbench', icon: FlaskConical },
    { id: 'discovery', label: 'Data Discovery & Pipeline', icon: Layers },
  ];

  return (
    <div style={{ minHeight: '100vh', display: 'flex', flexDirection: 'column' }}>
      {/* Header */}
      <header style={{
        background: '#1e293b',
        borderBottom: '1px solid #334155',
        padding: '16px 32px',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'space-between',
        flexWrap: 'wrap',
        gap: '16px'
      }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '14px' }}>
          <div style={{
            background: 'linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%)',
            padding: '10px',
            borderRadius: '10px',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            boxShadow: '0 4px 12px rgba(59, 130, 246, 0.3)'
          }}>
            <ShieldCheck size={26} color="#ffffff" />
          </div>
          <div>
            <h1 style={{ fontSize: '18px', fontWeight: 700, color: '#f8fafc', letterSpacing: '-0.02em' }}>
              Raw Material Receiving Yard & QC System
            </h1>
            <p style={{ fontSize: '12px', color: '#94a3b8' }}>
              Phase 1: Architecture Foundation &bull; PostgreSQL 18 &bull; Flyway Versioning &bull; Spring Boot 3.3
            </p>
          </div>
        </div>

        <HealthStatusBadge
          health={health}
          loading={loading}
          error={error}
          onRefresh={onRefresh}
        />
      </header>

      {/* Navigation Sub-Bar */}
      <nav style={{
        background: '#0f172a',
        borderBottom: '1px solid #1e293b',
        padding: '0 32px',
        display: 'flex',
        gap: '8px',
        overflowX: 'auto'
      }}>
        {tabs.map((tab) => {
          const Icon = tab.icon;
          const isActive = activeTab === tab.id;
          return (
            <button
              key={tab.id}
              onClick={() => onTabChange(tab.id)}
              style={{
                display: 'flex',
                alignItems: 'center',
                gap: '8px',
                padding: '14px 18px',
                fontSize: '13px',
                fontWeight: 600,
                color: isActive ? '#38bdf8' : '#94a3b8',
                background: 'transparent',
                borderBottom: `2px solid ${isActive ? '#38bdf8' : 'transparent'}`,
                transition: 'all 0.15s ease'
              }}
            >
              <Icon size={16} />
              <span>{tab.label}</span>
            </button>
          );
        })}
      </nav>

      {/* Main Content Area */}
      <main style={{ flex: 1, padding: '32px', maxWidth: '1400px', margin: '0 auto', width: '100%' }}>
        {children}
      </main>

      {/* Footer */}
      <footer style={{
        background: '#1e293b',
        borderTop: '1px solid #334155',
        padding: '16px 32px',
        fontSize: '12px',
        color: '#64748b',
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center',
        flexWrap: 'wrap',
        gap: '12px'
      }}>
        <div>
          <span>&copy; 2026 Raw Material Quality Control Subsystem. Follows ISA-95 / ISO 9001 Receiving Invariants.</span>
        </div>
        <div style={{ display: 'flex', alignItems: 'center', gap: '16px' }}>
          <span>PostgreSQL: <strong>{health?.databaseProductName || 'PostgreSQL 18'}</strong></span>
          <span>Flyway: <strong>{health?.flywayMigrationVersion || 'V1__initial_schema.sql'}</strong></span>
          <span>Uptime: <strong>{health?.uptimeSeconds ? `${health.uptimeSeconds}s` : 'Active'}</strong></span>
        </div>
      </footer>
    </div>
  );
}

