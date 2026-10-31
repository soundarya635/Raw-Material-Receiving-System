import React, { useState } from 'react';
import { FlaskConical, CheckCircle2, XCircle, AlertTriangle, ArrowRight, ShieldCheck, UserCheck } from 'lucide-react';

export default function QualityInspectionPage() {
  const [viscosity, setViscosity] = useState('13200'); // Valid: 11000 - 15000
  const [eew, setEew] = useState('186.5'); // Valid: 182 - 192
  const [waterContent, setWaterContent] = useState('0.04'); // Valid: 0.00 - 0.10
  const [visualClarity, setVisualClarity] = useState('PASS');

  const [receivedQty] = useState(5000);
  const [approvedQty, setApprovedQty] = useState(5000);
  const [rejectedQty, setRejectedQty] = useState(0);
  const [dispositionDecision, setDispositionDecision] = useState('APPROVE_FULL');
  const [dispositionCommitted, setDispositionCommitted] = useState(false);

  // Inclusive limit checks
  const isViscosityPass = parseFloat(viscosity) >= 11000 && parseFloat(viscosity) <= 15000;
  const isEewPass = parseFloat(eew) >= 182 && parseFloat(eew) <= 192;
  const isWaterPass = parseFloat(waterContent) >= 0.00 && parseFloat(waterContent) <= 0.10;
  const isVisualPass = visualClarity === 'PASS';

  const allMandatoryPass = isViscosityPass && isEewPass && isWaterPass && isVisualPass;

  const handleDecisionChange = (decision) => {
    setDispositionDecision(decision);
    if (decision === 'APPROVE_FULL') {
      setApprovedQty(receivedQty);
      setRejectedQty(0);
    } else if (decision === 'REJECT_FULL') {
      setApprovedQty(0);
      setRejectedQty(receivedQty);
    } else if (decision === 'APPROVE_PARTIAL') {
      setApprovedQty(4200);
      setRejectedQty(800);
    }
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
      <div>
        <h2 style={{ fontSize: '20px', fontWeight: 700, color: '#f8fafc' }}>
          Quality Inspection & Authorized Disposition Station
        </h2>
        <p style={{ fontSize: '13px', color: '#94a3b8' }}>
          Stage 3 & 4: Laboratory test recording, inclusive specification boundary evaluation, mandatory failure locking, and authorized disposition sign-off.
        </p>
      </div>

      <div style={{
        display: 'grid',
        gridTemplateColumns: 'repeat(auto-fit, minmax(340px, 1fr))',
        gap: '24px'
      }}>
        {/* Lab Testing Bench */}
        <div style={{
          background: '#1e293b',
          borderRadius: '12px',
          padding: '24px',
          border: '1px solid #334155',
          display: 'flex',
          flexDirection: 'column',
          gap: '18px'
        }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
            <h3 style={{ fontSize: '15px', fontWeight: 600, color: '#f8fafc', display: 'flex', alignItems: 'center', gap: '8px' }}>
              <FlaskConical size={18} color="#8b5cf6" /> Material Quality Specifications (Epoxy Resin)
            </h3>
            <span style={{ fontSize: '11px', color: '#94a3b8', background: '#0f172a', padding: '3px 8px', borderRadius: '4px' }}>
              Lot: LOT-20261031-0081
            </span>
          </div>

          {/* Test 1 */}
          <div style={{ background: '#0f172a', padding: '14px', borderRadius: '8px', border: '1px solid #334155' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: '13px', marginBottom: '6px' }}>
              <span style={{ color: '#f8fafc', fontWeight: 500 }}>Viscosity at 25&deg;C (mPa.s)</span>
              <span style={{ color: isViscosityPass ? '#34d399' : '#f87171', fontWeight: 600 }}>
                {isViscosityPass ? 'COMPLIANT' : 'FAIL'}
              </span>
            </div>
            <div style={{ fontSize: '11px', color: '#64748b', marginBottom: '8px' }}>
              Spec: 11000.00 - 15000.00 (Inclusive) &bull; Mandatory
            </div>
            <input
              type="number"
              value={viscosity}
              onChange={(e) => setViscosity(e.target.value)}
              style={{ width: '100%', background: '#1e293b', border: '1px solid #334155', padding: '6px 10px', borderRadius: '6px', color: '#f8fafc', fontSize: '13px' }}
            />
          </div>

          {/* Test 2 */}
          <div style={{ background: '#0f172a', padding: '14px', borderRadius: '8px', border: '1px solid #334155' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: '13px', marginBottom: '6px' }}>
              <span style={{ color: '#f8fafc', fontWeight: 500 }}>Epoxide Equivalent Weight (g/eq)</span>
              <span style={{ color: isEewPass ? '#34d399' : '#f87171', fontWeight: 600 }}>
                {isEewPass ? 'COMPLIANT' : 'FAIL'}
              </span>
            </div>
            <div style={{ fontSize: '11px', color: '#64748b', marginBottom: '8px' }}>
              Spec: 182.00 - 192.00 (Inclusive) &bull; Mandatory
            </div>
            <input
              type="number"
              step="0.1"
              value={eew}
              onChange={(e) => setEew(e.target.value)}
              style={{ width: '100%', background: '#1e293b', border: '1px solid #334155', padding: '6px 10px', borderRadius: '6px', color: '#f8fafc', fontSize: '13px' }}
            />
          </div>

          {/* Test 3 */}
          <div style={{ background: '#0f172a', padding: '14px', borderRadius: '8px', border: '1px solid #334155' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: '13px', marginBottom: '6px' }}>
              <span style={{ color: '#f8fafc', fontWeight: 500 }}>Water Content (% wt)</span>
              <span style={{ color: isWaterPass ? '#34d399' : '#f87171', fontWeight: 600 }}>
                {isWaterPass ? 'COMPLIANT' : 'FAIL'}
              </span>
            </div>
            <div style={{ fontSize: '11px', color: '#64748b', marginBottom: '8px' }}>
              Spec: 0.00 - 0.10 (Inclusive) &bull; Mandatory
            </div>
            <input
              type="number"
              step="0.01"
              value={waterContent}
              onChange={(e) => setWaterContent(e.target.value)}
              style={{ width: '100%', background: '#1e293b', border: '1px solid #334155', padding: '6px 10px', borderRadius: '6px', color: '#f8fafc', fontSize: '13px' }}
            />
          </div>
        </div>

        {/* Disposition & Release Control */}
        <div style={{
          background: '#1e293b',
          borderRadius: '12px',
          padding: '24px',
          border: '1px solid #334155',
          display: 'flex',
          flexDirection: 'column',
          gap: '20px'
        }}>
          <h3 style={{ fontSize: '15px', fontWeight: 600, color: '#f8fafc', display: 'flex', alignItems: 'center', gap: '8px' }}>
            <UserCheck size={18} color="#10b981" /> Authorized Quality Disposition Decision
          </h3>

          {!allMandatoryPass && (
            <div style={{
              background: '#ef444415',
              border: '1px solid #ef4444',
              borderRadius: '8px',
              padding: '12px',
              display: 'flex',
              gap: '10px',
              alignItems: 'center'
            }}>
              <AlertTriangle size={20} color="#ef4444" />
              <div style={{ fontSize: '12px', color: '#f87171' }}>
                <strong>Mandatory QC Failure Detected:</strong> System locks full approval disposition until all mandatory test specifications pass.
              </div>
            </div>
          )}

          <div>
            <label style={{ fontSize: '12px', color: '#94a3b8', display: 'block', marginBottom: '8px' }}>
              Disposition Decision Selection
            </label>
            <div style={{ display: 'grid', gridTemplateColumns: 'repeat(3, 1fr)', gap: '8px' }}>
              {[
                { id: 'APPROVE_FULL', label: 'Full Acceptance', disabled: !allMandatoryPass },
                { id: 'APPROVE_PARTIAL', label: 'Partial Accept', disabled: false },
                { id: 'REJECT_FULL', label: 'Full Rejection', disabled: false },
              ].map((opt) => (
                <button
                  key={opt.id}
                  type="button"
                  disabled={opt.disabled}
                  onClick={() => handleDecisionChange(opt.id)}
                  style={{
                    padding: '10px 8px',
                    borderRadius: '6px',
                    fontSize: '12px',
                    fontWeight: 600,
                    cursor: opt.disabled ? 'not-allowed' : 'pointer',
                    background: dispositionDecision === opt.id ? '#3b82f6' : '#0f172a',
                    color: opt.disabled ? '#475569' : dispositionDecision === opt.id ? '#ffffff' : '#94a3b8',
                    border: '1px solid #334155',
                    opacity: opt.disabled ? 0.4 : 1
                  }}
                >
                  {opt.label}
                </button>
              ))}
            </div>
          </div>

          {/* Mass Conservation Balance */}
          <div style={{
            background: '#0f172a',
            padding: '16px',
            borderRadius: '8px',
            border: '1px solid #334155'
          }}>
            <div style={{ fontSize: '12px', color: '#64748b', marginBottom: '8px', textTransform: 'uppercase', fontWeight: 600 }}>
              Mass Balance Accounting Rule
            </div>
            <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: '13px', color: '#cbd5e1' }}>
              <span>Total Received:</span>
              <strong>{receivedQty} KG</strong>
            </div>
            <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: '13px', color: '#34d399', marginTop: '4px' }}>
              <span>Approved (Releaseable):</span>
              <strong>{approvedQty} KG</strong>
            </div>
            <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: '13px', color: '#f87171', marginTop: '4px' }}>
              <span>Rejected (Quarantined/Return):</span>
              <strong>{rejectedQty} KG</strong>
            </div>
          </div>

          <button
            type="button"
            onClick={() => setDispositionCommitted(true)}
            style={{
              background: '#10b981',
              color: '#ffffff',
              padding: '12px',
              borderRadius: '8px',
              fontWeight: 600,
              fontSize: '14px',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              gap: '8px'
            }}
          >
            <ShieldCheck size={18} /> Authorize Disposition & Release Stock
          </button>

          {dispositionCommitted && (
            <div style={{
              background: '#10b98115',
              border: '1px solid #10b981',
              borderRadius: '8px',
              padding: '14px',
              fontSize: '12px',
              color: '#34d399'
            }}>
              <strong>Disposition Signed & Recorded:</strong> {approvedQty} KG released into active production warehouse. {rejectedQty > 0 ? `${rejectedQty} KG redirected to Quarantine Return Bay.` : ''} Traceable transaction reference generated.
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

