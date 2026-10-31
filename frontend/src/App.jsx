import React, { useState } from 'react';
import MainLayout from './layouts/MainLayout';
import DashboardPage from './pages/DashboardPage';
import ReceivingYardPage from './pages/ReceivingYardPage';
import QualityInspectionPage from './pages/QualityInspectionPage';
import DataDiscoveryPage from './pages/DataDiscoveryPage';
import { useHealthCheck } from './hooks/useHealthCheck';

export default function App() {
  const [activeTab, setActiveTab] = useState('dashboard');
  const { data: health, loading, error, refetch } = useHealthCheck(15000);

  return (
    <MainLayout
      activeTab={activeTab}
      onTabChange={setActiveTab}
      health={health}
      loading={loading}
      error={error}
      onRefresh={refetch}
    >
      {activeTab === 'dashboard' && (
        <DashboardPage
          health={health}
          loading={loading}
          error={error}
          onRefresh={refetch}
        />
      )}

      {activeTab === 'receiving' && (
        <ReceivingYardPage />
      )}

      {activeTab === 'inspection' && (
        <QualityInspectionPage />
      )}

      {activeTab === 'discovery' && (
        <DataDiscoveryPage />
      )}
    </MainLayout>
  );
}

