import React, { useState } from 'react';
import { Truck, Package, ShieldAlert, CheckCircle2, FileUp, AlertTriangle, ArrowRight } from 'lucide-react';
import { evaluateTolerance } from '../utils/formatters';

export default function ReceivingYardPage() {
  const [poNumber, setPoNumber] = useState('PO-2026-10492');
  const [materialCode, setMaterialCode] = useState('MAT-RES-101');
  const [orderedQty, setOrderedQty] = useState('5000');
  const [receivedQty, setReceivedQty] = useState('5150');
  const [vehicleNo, setVehicleNo] = useState('DL-01-AB-4491');
  const [driverName, setDriverName] = useState('Marcus Vance');
  const [deliveryNote, setDeliveryNote] = useState('DN-88421');
  const [supplierLot, setSupplierLot] = useState('LOT-VND-9921');
  const [coaAttached, setCoaAttached] = useState(true);
  const [receiptCreated, setReceiptCreated] = useState(false);

  const toleranceEval = evaluateTolerance(orderedQty, receivedQty, 5.0);

  const handleSimulateIntake = (e) => {
    e.preventDefault();
    setReceiptCreated(true);
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
      <div>
        <h2 style={{ fontSize: '20px', fontWeight: 700, color: '#f8fafc' }}>
          Receiving Yard Intake & Goods Receipt (GRN)
        </h2>
        <p style={{ fontSize: '13px', color: '#94a3b8' }}>
          Stage 1 & 2: Physical truck arrival, PO line item reconciliation, inclusive variance verification, and immediate quarantine custody assignment.
        </p>
      </div>

      <div style={{
        display: 'grid',
        gridTemplateColumns: 'repeat(auto-fit, minmax(340px, 1fr))',
        gap: '24px'
      }}>
        {/* Intake Form */}
        <form onSubmit={handleSimulateIntake} style={{
          background: '#1e293b',
          borderRadius: '12px',
          padding: '24px',
          border: '1px solid #334155',
          display: 'flex',
          flexDirection: 'column',
          gap: '16px'
        }}>
          <h3 style={{ fontSize: '15px', fontWeight: 600, color: '#f8fafc', display: 'flex', alignItems: 'center', gap: '8px' }}>
            <Truck size={18} color="#38bdf8" /> Gate Truck Check-In Form
          </h3>

          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '12px' }}>
            <div>
              <label style={{ fontSize: '12px', color: '#94a3b8', display: 'block', marginBottom: '4px' }}>Purchase Order #</label>
              <input
                type="text"
                value={poNumber}
                onChange={(e) => setPoNumber(e.target.value)}
                style={{ width: '100%', background: '#0f172a', border: '1px solid #334155', padding: '8px 10px', borderRadius: '6px', color: '#f8fafc', fontSize: '13px' }}
              />
            </div>
            <div>
              <label style={{ fontSize: '12px', color: '#94a3b8', display: 'block', marginBottom: '4px' }}>Material SKU</label>
              <input
                type="text"
                value={materialCode}
                onChange={(e) => setMaterialCode(e.target.value)}
                style={{ width: '100%', background: '#0f172a', border: '1px solid #334155', padding: '8px 10px', borderRadius: '6px', color: '#f8fafc', fontSize: '13px' }}
              />
            </div>
          </div>

          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '12px' }}>
            <div>
              <label style={{ fontSize: '12px', color: '#94a3b8', display: 'block', marginBottom: '4px' }}>Ordered Qty (KG)</label>
              <input
                type="number"
                value={orderedQty}
                onChange={(e) => setOrderedQty(e.target.value)}
                style={{ width: '100%', background: '#0f172a', border: '1px solid #334155', padding: '8px 10px', borderRadius: '6px', color: '#f8fafc', fontSize: '13px' }}
              />
            </div>
            <div>
              <label style={{ fontSize: '12px', color: '#94a3b8', display: 'block', marginBottom: '4px' }}>Weighbridge Qty (KG)</label>
              <input
                type="number"
                value={receivedQty}
                onChange={(e) => setReceivedQty(e.target.value)}
                style={{ width: '100%', background: '#0f172a', border: '1px solid #334155', padding: '8px 10px', borderRadius: '6px', color: '#f8fafc', fontSize: '13px' }}
              />
            </div>
          </div>

          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '12px' }}>
            <div>
              <label style={{ fontSize: '12px', color: '#94a3b8', display: 'block', marginBottom: '4px' }}>Vehicle / Plate #</label>
              <input
                type="text"
                value={vehicleNo}
                onChange={(e) => setVehicleNo(e.target.value)}
                style={{ width: '100%', background: '#0f172a', border: '1px solid #334155', padding: '8px 10px', borderRadius: '6px', color: '#f8fafc', fontSize: '13px' }}
              />
            </div>
            <div>
              <label style={{ fontSize: '12px', color: '#94a3b8', display: 'block', marginBottom: '4px' }}>Driver Name</label>
              <input
                type="text"
                value={driverName}
                onChange={(e) => setDriverName(e.target.value)}
                style={{ width: '100%', background: '#0f172a', border: '1px solid #334155', padding: '8px 10px', borderRadius: '6px', color: '#f8fafc', fontSize: '13px' }}
              />
            </div>
          </div>

          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '12px' }}>
            <div>
              <label style={{ fontSize: '12px', color: '#94a3b8', display: 'block', marginBottom: '4px' }}>Delivery Challan #</label>
              <input
                type="text"
                value={deliveryNote}
                onChange={(e) => setDeliveryNote(e.target.value)}
                style={{ width: '100%', background: '#0f172a', border: '1px solid #334155', padding: '8px 10px', borderRadius: '6px', color: '#f8fafc', fontSize: '13px' }}
              />
            </div>
            <div>
              <label style={{ fontSize: '12px', color: '#94a3b8', display: 'block', marginBottom: '4px' }}>Supplier Lot #</label>
              <input
                type="text"
                value={supplierLot}
                onChange={(e) => setSupplierLot(e.target.value)}
                style={{ width: '100%', background: '#0f172a', border: '1px solid #334155', padding: '8px 10px', borderRadius: '6px', color: '#f8fafc', fontSize: '13px' }}
              />
            </div>
          </div>

          <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginTop: '6px' }}>
            <input
              type="checkbox"
              id="coaCheck"
              checked={coaAttached}
              onChange={(e) => setCoaAttached(e.target.checked)}
              style={{ accentColor: '#3b82f6', width: '16px', height: '16px' }}
            />
            <label htmlFor="coaCheck" style={{ fontSize: '13px', color: '#cbd5e1' }}>
              Supplier Certificate of Analysis (CoA) attached & verified
            </label>
          </div>

          <button
            type="submit"
            style={{
              background: '#3b82f6',
              color: '#ffffff',
              padding: '12px',
              borderRadius: '8px',
              fontWeight: 600,
              fontSize: '14px',
              marginTop: '10px',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              gap: '8px'
            }}
          >
            Record Gate Arrival & Generate Quarantined Lot
          </button>
        </form>

        {/* Real-time Validation & Outcome */}
        <div style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
          {/* Tolerance Status Box */}
          <div style={{
            background: '#1e293b',
            borderRadius: '12px',
            padding: '24px',
            border: '1px solid #334155'
          }}>
            <h3 style={{ fontSize: '15px', fontWeight: 600, color: '#f8fafc', marginBottom: '12px' }}>
              PO Matching & Tolerance Verification
            </h3>
            <div style={{
              background: toleranceEval.isWithin ? '#10b98115' : '#ef444415',
              border: `1px solid ${toleranceEval.isWithin ? '#10b981' : '#ef4444'}`,
              borderRadius: '8px',
              padding: '14px',
              marginBottom: '16px'
            }}>
              <div style={{ fontWeight: 600, fontSize: '13px', color: toleranceEval.isWithin ? '#34d399' : '#f87171' }}>
                {toleranceEval.label}
              </div>
              <div style={{ fontSize: '12px', color: '#94a3b8', marginTop: '4px' }}>
                Ordered: {orderedQty} KG &bull; Received: {receivedQty} KG &bull; Allowed Variance: &plusmn;5% [{toleranceEval.minAllowed} to {toleranceEval.maxAllowed} KG]
              </div>
            </div>

            <div style={{ fontSize: '12px', color: '#64748b', lineHeight: '1.6' }}>
              &bull; <strong>Strict Rule:</strong> Even when quantity is 100% matching, material is NOT released to active inventory.<br/>
              &bull; Material is immediately partitioned into a Quarantined Lot record pending mandatory lab testing.
            </div>
          </div>

          {/* Simulated Lot Creation Card */}
          {receiptCreated && (
            <div style={{
              background: '#0f172a',
              borderRadius: '12px',
              padding: '20px',
              border: '1px solid #f59e0b',
              boxShadow: '0 0 15px rgba(245, 158, 11, 0.15)'
            }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: '10px', marginBottom: '12px' }}>
                <ShieldAlert size={22} color="#f59e0b" />
                <div>
                  <h4 style={{ fontSize: '14px', fontWeight: 700, color: '#f59e0b' }}>
                    LOT CREATED UNDER STRICT QUARANTINE
                  </h4>
                  <span style={{ fontSize: '11px', color: '#94a3b8' }}>
                    Lot ID: LOT-20261031-0081 &bull; GRN: GRN-2026-0081
                  </span>
                </div>
              </div>

              <div style={{
                background: '#1e293b',
                padding: '12px',
                borderRadius: '6px',
                fontSize: '12px',
                fontFamily: 'monospace',
                display: 'grid',
                gridTemplateColumns: '1fr 1fr',
                gap: '8px',
                color: '#cbd5e1'
              }}>
                <div>Received: <strong>{receivedQty} KG</strong></div>
                <div style={{ color: '#f59e0b' }}>Quarantined: <strong>{receivedQty} KG (100%)</strong></div>
                <div>Approved: <strong>0.000 KG</strong></div>
                <div>Released to Prod: <strong>0.000 KG (LOCKED)</strong></div>
              </div>

              <div style={{ marginTop: '14px', fontSize: '12px', color: '#94a3b8' }}>
                Next Required Action: Quality sample extraction & Laboratory testing before any disposition can be authorized.
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

