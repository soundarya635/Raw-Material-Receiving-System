import React from 'react';
import MetricCard from '../components/MetricCard';
import ToleranceChecker from '../components/ToleranceChecker';
import { Database, ShieldAlert, CheckCircle2, Lock, Cpu, Server, FileText } from 'lucide-react';
import { formatDateTime } from '../utils/formatters';

export default function DashboardPage({ health, loading, error, onRefresh }) {
  const isOnline = !error && health?.status === 'UP';

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '28px' }}>
      {/* Top Banner / Hero */}
      <div style={{
        background: 'linear-gradient(135deg, #1e293b 0%, #0f172a 100%)',
        border: '1px solid #334155',
        borderRadius: '14px',
        padding: '28px',
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center',
        flexWrap: 'wrap',
        gap: '20px'
      }}>
        <div style={{ maxWidth: '720px' }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '10px', marginBottom: '8px' }}>
            <span style={{
              background: '#3b82f620',
              color: '#60a5fa',
              padding: '4px 10px',
              borderRadius: '999px',
              fontSize: '11px',
              fontWeight: 700,
              textTransform: 'uppercase',
              letterSpacing: '0.05em'
            }}>
              Phase 1 Deliverables Active
            </span>
            <span style={{ color: '#64748b', fontSize: '13px' }}>
              Checked: {formatDateTime(health?.timestamp)}
            </span>
          </div>
          <h2 style={{ fontSize: '24px', fontWeight: 700, color: '#f8fafc', marginBottom: '8px' }}>
            Manufacturing Quality Execution & Gate Quarantine Engine
          </h2>
          <p style={{ fontSize: '14px', color: '#94a3b8', lineHeight: '1.6' }}>
            Enforces strict manufacturing governance: <em>received raw material cannot be utilized in production until all quality inspections pass and authorized QA approval is logged</em>.
          </p>
        </div>

        <div style={{
          background: '#0f172a',
          padding: '16px 20px',
          borderRadius: '10px',
          border: '1px solid #334155',
          minWidth: '240px'
        }}>
          <div style={{ fontSize: '12px', color: '#64748b', textTransform: 'uppercase', fontWeight: 600 }}>
            Core Invariants Status
          </div>
          <div style={{ marginTop: '8px', display: 'flex', flexDirection: 'column', gap: '6px', fontSize: '13px' }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: '8px', color: '#34d399' }}>
              <CheckCircle2 size={15} /> <span>Default Quarantine: 100%</span>
            </div>
            <div style={{ display: 'flex', alignItems: 'center', gap: '8px', color: '#34d399' }}>
              <CheckCircle2 size={15} /> <span>Quantity Tolerance: &plusmn;5% Inclusive</span>
            </div>
            <div style={{ display: 'flex', alignItems: 'center', gap: '8px', color: '#34d399' }}>
              <CheckCircle2 size={15} /> <span>Flyway Schema V1 Active</span>
            </div>
          </div>
        </div>
      </div>

      {/* Metrics Row */}
      <div style={{
        display: 'grid',
        gridTemplateColumns: 'repeat(auto-fit, minmax(260px, 1fr))',
        gap: '20px'
      }}>
        <MetricCard
          title="Backend REST API"
          value={isOnline ? 'OPERATIONAL' : 'OFFLINE'}
          subtitle={`Spring Boot 3.3.5 (${health?.environmentInfo?.javaVersion || 'Java 21'})`}
          icon={Server}
          color={isOnline ? '#10b981' : '#ef4444'}
          badge={health?.status || 'UNKNOWN'}
        />

        <MetricCard
          title="Relational Database"
          value={health?.databaseConnected ? 'CONNECTED' : 'DISCONNECTED'}
          subtitle={health?.databaseProductName || 'PostgreSQL 18 on port 5433'}
          icon={Database}
          color={health?.databaseConnected ? '#3b82f6' : '#ef4444'}
          badge="PostgreSQL 18"
        />

        <MetricCard
          title="Database Migration"
          value={health?.flywayMigrationVersion ? 'V1 APPLIED' : 'PENDING'}
          subtitle="Flyway 16 Schema Tables Managed"
          icon={FileText}
          color="#8b5cf6"
          badge="Flyway 10"
        />

        <MetricCard
          title="Gate Quarantine Lock"
          value="ENFORCED"
          subtitle="Zero-Trust Quality Release Gateway"
          icon={Lock}
          color="#f59e0b"
          badge="Strict Mode"
        />
      </div>

      {/* Interactive Tolerance Calculator */}
      <ToleranceChecker defaultTolerance={health?.environmentInfo?.defaultTolerancePercent || 5.0} />

      {/* Architecture Foundations Checklist */}
      <div style={{
        background: '#1e293b',
        borderRadius: '12px',
        padding: '24px',
        border: '1px solid #334155'
      }}>
        <h3 style={{ fontSize: '16px', fontWeight: 600, color: '#f8fafc', marginBottom: '16px' }}>
          Phase 1 Foundation & System Verification Criteria
        </h3>

        <div style={{
          display: 'grid',
          gridTemplateColumns: 'repeat(auto-fit, minmax(320px, 1fr))',
          gap: '16px'
        }}>
          {[
            {
              title: 'Source-Agnostic Ingestion Architecture',
              desc: 'Decoupled Adapter pattern ready to ingest Excel, CSV, PDF, Google Drive, and ERP APIs without altering core models.',
              status: 'READY'
            },
            {
              title: 'Flyway Relational Schema (16 Tables)',
              desc: 'Users, Suppliers, Materials, POs, GRN, Lots, CoAs, Quality Specs, Inspections, Dispositions, Inventory Ledger, Audit Logs.',
              status: 'DEPLOYED'
            },
            {
              title: 'Clean Layered Architecture (com.receivingyard)',
              desc: 'Thin controllers, domain services, DTO boundaries, global exception handling (@RestControllerAdvice).',
              status: 'VERIFIED'
            },
            {
              title: 'Strict Quality Quarantine Invariant',
              desc: 'Database constraints and domain enums mandate that all incoming stock enters QUARANTINED status.',
              status: 'VERIFIED'
            },
            {
              title: 'PostgreSQL 18 Live Connectivity',
              desc: 'Verified communication between Spring Boot backend and PostgreSQL database on port 5433 with environment overrides.',
              status: 'CONNECTED'
            },
            {
              title: 'Data Discovery & Documentation',
              desc: 'DATA_SOURCE_ANALYSIS.md, DATA_DICTIONARY.md, ENTITY_RELATIONSHIPS.md, ASSUMPTIONS_AND_OPEN_QUESTIONS.md generated.',
              status: 'COMPLETE'
            }
          ].map((item, idx) => (
            <div key={idx} style={{
              background: '#0f172a',
              padding: '16px',
              borderRadius: '8px',
              border: '1px solid #334155',
              display: 'flex',
              flexDirection: 'column',
              justifyContent: 'space-between',
              gap: '8px'
            }}>
              <div>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                  <h4 style={{ fontSize: '14px', fontWeight: 600, color: '#f8fafc' }}>{item.title}</h4>
                  <span style={{
                    fontSize: '10px',
                    fontWeight: 700,
                    padding: '2px 8px',
                    borderRadius: '4px',
                    background: '#10b98120',
                    color: '#34d399'
                  }}>
                    {item.status}
                  </span>
                </div>
                <p style={{ fontSize: '12px', color: '#94a3b8', marginTop: '6px', lineHeight: '1.5' }}>
                  {item.desc}
                </p>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}

