import React from 'react';

export default function MetricCard({ title, value, subtitle, icon: Icon, color = '#3b82f6', badge }) {
  return (
    <div style={{
      background: '#1e293b',
      borderRadius: '12px',
      padding: '20px',
      border: '1px solid #334155',
      display: 'flex',
      flexDirection: 'column',
      justifyContent: 'space-between',
      position: 'relative',
      overflow: 'hidden',
      boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.2)'
    }}>
      <div style={{
        position: 'absolute',
        top: 0,
        left: 0,
        right: 0,
        height: '3px',
        backgroundColor: color
      }} />

      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
        <div>
          <span style={{ fontSize: '13px', color: '#94a3b8', fontWeight: 500, textTransform: 'uppercase', letterSpacing: '0.05em' }}>
            {title}
          </span>
          <div style={{ fontSize: '28px', fontWeight: 700, color: '#f8fafc', marginTop: '4px' }}>
            {value}
          </div>
        </div>
        <div style={{
          background: `${color}1a`,
          padding: '10px',
          borderRadius: '10px',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center'
        }}>
          <Icon size={22} color={color} />
        </div>
      </div>

      <div style={{ marginTop: '16px', display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
        <span style={{ fontSize: '12px', color: '#64748b' }}>{subtitle}</span>
        {badge && (
          <span style={{
            fontSize: '11px',
            fontWeight: 600,
            padding: '2px 8px',
            borderRadius: '999px',
            background: `${color}25`,
            color: color
          }}>
            {badge}
          </span>
        )}
      </div>
    </div>
  );
}

