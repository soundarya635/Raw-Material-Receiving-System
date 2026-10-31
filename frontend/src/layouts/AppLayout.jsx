import React, { useState, useEffect } from 'react';
import { NavLink, Outlet } from 'react-router-dom';
import { 
  LayoutDashboard, 
  FileText, 
  Truck, 
  FlaskConical, 
  Package, 
  UploadCloud, 
  Database,
  CheckCircle2,
  AlertCircle
} from 'lucide-react';
import { getHealth } from '../services/api';

export default function AppLayout() {
  const [health, setHealth] = useState(null);
  const [isDbOnline, setIsDbOnline] = useState(false);

  useEffect(() => {
    const checkStatus = () => {
      getHealth()
        .then((res) => {
          if (res?.success) {
            setHealth(res.data);
            setIsDbOnline(res.data?.databaseConnected);
          }
        })
        .catch(() => {
          setIsDbOnline(false);
        });
    };

    checkStatus();
    const interval = setInterval(checkStatus, 15000);
    return () => clearInterval(interval);
  }, []);

  return (
    <div className="app-container">
      {/* Sidebar Navigation */}
      <aside className="sidebar">
        <div className="sidebar-header">
          <div className="sidebar-title">RawMaterialQC</div>
          <div className="sidebar-subtitle">Receiving & Quality Yard</div>
        </div>

        <nav className="sidebar-nav">
          <NavLink to="/" end className={({ isActive }) => `nav-link ${isActive ? 'active' : ''}`}>
            <LayoutDashboard size={18} />
            <span>Dashboard</span>
          </NavLink>

          <NavLink to="/purchase-orders" className={({ isActive }) => `nav-link ${isActive ? 'active' : ''}`}>
            <FileText size={18} />
            <span>Purchase Orders</span>
          </NavLink>

          <NavLink to="/goods-receiving" className={({ isActive }) => `nav-link ${isActive ? 'active' : ''}`}>
            <Truck size={18} />
            <span>Goods Receiving</span>
          </NavLink>

          <NavLink to="/quality-inspection" className={({ isActive }) => `nav-link ${isActive ? 'active' : ''}`}>
            <FlaskConical size={18} />
            <span>Quality Inspection</span>
          </NavLink>

          <NavLink to="/inventory" className={({ isActive }) => `nav-link ${isActive ? 'active' : ''}`}>
            <Package size={18} />
            <span>Inventory</span>
          </NavLink>

          <NavLink to="/data-import" className={({ isActive }) => `nav-link ${isActive ? 'active' : ''}`}>
            <UploadCloud size={18} />
            <span>Data Import</span>
          </NavLink>
        </nav>

        {/* Database Status Footer in Sidebar */}
        <div style={{ padding: '16px 20px', borderTop: '1px solid #1e293b', fontSize: '12px' }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '8px', color: isDbOnline ? '#34d399' : '#f87171' }}>
            <Database size={14} />
            <span>{isDbOnline ? 'PostgreSQL 18 Connected' : 'PostgreSQL Offline'}</span>
          </div>
          <div style={{ color: '#64748b', fontSize: '11px', marginTop: '4px' }}>
            Flyway V1 Schema Active
          </div>
        </div>
      </aside>

      {/* Main App Content */}
      <div className="main-content">
        <header className="top-header">
          <div>
            <span style={{ fontSize: '14px', fontWeight: 600, color: '#334155' }}>
              Raw Material Receiving & Quality Inspection System (MVP)
            </span>
          </div>

          <div style={{ display: 'flex', alignItems: 'center', gap: '12px', fontSize: '12px' }}>
            <span style={{
              display: 'inline-flex',
              alignItems: 'center',
              gap: '6px',
              padding: '4px 10px',
              borderRadius: '999px',
              backgroundColor: isDbOnline ? '#dcfce7' : '#fee2e2',
              color: isDbOnline ? '#15803d' : '#b91c1c',
              fontWeight: 600
            }}>
              {isDbOnline ? <CheckCircle2 size={13} /> : <AlertCircle size={13} />}
              {isDbOnline ? 'System Operational' : 'Backend Connecting'}
            </span>
            <span style={{ color: '#64748b' }}>Tolerance: &plusmn;5%</span>
          </div>
        </header>

        <main className="page-body">
          <Outlet />
        </main>
      </div>
    </div>
  );
}

