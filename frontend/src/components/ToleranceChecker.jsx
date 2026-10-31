import React, { useState } from 'react';
import { evaluateTolerance } from '../utils/formatters';
import { Scale, CheckCircle2, AlertTriangle, XCircle } from 'lucide-react';

export default function ToleranceChecker({ defaultTolerance = 5.0 }) {
  const [ordered, setOrdered] = useState('1000');
  const [received, setReceived] = useState('1020');
  const [tolerance, setTolerance] = useState(defaultTolerance.toString());

  const result = evaluateTolerance(ordered, received, tolerance);

  return (
    <div style={{
      background: '#1e293b',
      borderRadius: '12px',
      padding: '24px',
      border: '1px solid #334155'
    }}>
      <div style={{ display: 'flex', alignItems: 'center', gap: '10px', marginBottom: '16px' }}>
        <Scale size={20} color="#38bdf8" />
        <h3 style={{ fontSize: '16px', fontWeight: 600, color: '#f8fafc' }}>
          Interactive Receiving Quantity Tolerance Validator
        </h3>
      </div>
      <p style={{ fontSize: '13px', color: '#94a3b8', marginBottom: '20px' }}>
        Enforces manufacturing rule: <strong>Inclusive Tolerance Bounds [Ordered &times; (1 &plusmn; tol%)]</strong>. Received quantities within bounds proceed to Quarantine; out-of-bracket shipments flag variance alerts.
      </p>

      <div style={{
        display: 'grid',
        gridTemplateColumns: 'repeat(auto-fit, minmax(180px, 1fr))',
        gap: '16px',
        marginBottom: '20px'
      }}>
        <div>
          <label style={{ display: 'block', fontSize: '12px', color: '#94a3b8', marginBottom: '6px' }}>
            PO Ordered Quantity (KG)
          </label>
          <input
            type="number"
            value={ordered}
            onChange={(e) => setOrdered(e.target.value)}
            style={{
              width: '100%',
              background: '#0f172a',
              border: '1px solid #334155',
              padding: '10px 12px',
              borderRadius: '8px',
              color: '#f8fafc',
              fontSize: '14px',
              fontFamily: 'monospace'
            }}
          />
        </div>

        <div>
          <label style={{ display: 'block', fontSize: '12px', color: '#94a3b8', marginBottom: '6px' }}>
            Actual Received Quantity (KG)
          </label>
          <input
            type="number"
            value={received}
            onChange={(e) => setReceived(e.target.value)}
            style={{
              width: '100%',
              background: '#0f172a',
              border: '1px solid #334155',
              padding: '10px 12px',
              borderRadius: '8px',
              color: '#f8fafc',
              fontSize: '14px',
              fontFamily: 'monospace'
            }}
          />
        </div>

        <div>
          <label style={{ display: 'block', fontSize: '12px', color: '#94a3b8', marginBottom: '6px' }}>
            Tolerance Limit (&plusmn;%)
          </label>
          <input
            type="number"
            step="0.1"
            value={tolerance}
            onChange={(e) => setTolerance(e.target.value)}
            style={{
              width: '100%',
              background: '#0f172a',
              border: '1px solid #334155',
              padding: '10px 12px',
              borderRadius: '8px',
              color: '#f8fafc',
              fontSize: '14px',
              fontFamily: 'monospace'
            }}
          />
        </div>
      </div>

      <div style={{
        background: result.isWithin ? 'rgba(16, 185, 129, 0.1)' : 'rgba(239, 68, 68, 0.1)',
        border: `1px solid ${result.isWithin ? '#10b981' : '#ef4444'}`,
        borderRadius: '8px',
        padding: '16px',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'space-between',
        flexWrap: 'wrap',
        gap: '12px'
      }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
          {result.isWithin ? (
            <CheckCircle2 size={24} color="#10b981" />
          ) : (
            <XCircle size={24} color="#ef4444" />
          )}
          <div>
            <div style={{
              fontWeight: 600,
              fontSize: '14px',
              color: result.isWithin ? '#34d399' : '#f87171'
            }}>
              {result.label}
            </div>
            <div style={{ fontSize: '12px', color: '#94a3b8', marginTop: '2px' }}>
              Variance: <strong>{result.variancePercent}%</strong> | Valid Range: <strong>{result.minAllowed} KG</strong> to <strong>{result.maxAllowed} KG</strong>
            </div>
          </div>
        </div>

        <span style={{
          fontSize: '12px',
          fontWeight: 700,
          padding: '4px 12px',
          borderRadius: '999px',
          background: result.isWithin ? '#10b98125' : '#ef444425',
          color: result.isWithin ? '#34d399' : '#f87171'
        }}>
          {result.status}
        </span>
      </div>
    </div>
  );
}

