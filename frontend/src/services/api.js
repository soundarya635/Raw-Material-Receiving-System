import axios from 'axios';

const api = axios.create({
  baseURL: '/api',
  headers: {
    'Content-Type': 'application/json',
  },
});

// Response interceptor to unwrap data
api.interceptors.response.use(
  (response) => response.data,
  (error) => {
    const message = error.response?.data?.message || error.message || 'An error occurred';
    return Promise.reject(new Error(message));
  }
);

// Health
export const getHealth = () => api.get('/health');

// Dashboard
export const getDashboardSummary = () => api.get('/dashboard/summary');

// Purchase Orders
export const getPurchaseOrders = () => api.get('/purchase-orders');
export const getPurchaseOrderByPoNumber = (poNumber) => api.get(`/purchase-orders/${poNumber}`);
export const searchPurchaseOrders = (query) => api.get(`/purchase-orders/search?query=${encodeURIComponent(query)}`);
export const createPurchaseOrder = (data) => api.post('/purchase-orders', data);

// Goods Receiving
export const getGoodsReceipts = () => api.get('/goods-receipts');
export const getGoodsReceiptByGrn = (grnNumber) => api.get(`/goods-receipts/${grnNumber}`);
export const recordGoodsReceipt = (data) => api.post('/goods-receipts', data);

// Quality Inspection
export const getQualityInspections = () => api.get('/quality-inspections');
export const getQualityInspectionByLot = (lotNumber) => api.get(`/quality-inspections/lot/${lotNumber}`);
export const getQuarantinedLots = () => api.get('/quality-inspections/quarantined-lots');
export const recordQualityInspection = (data) => api.post('/quality-inspections', data);
export const submitDisposition = (data) => api.post('/quality-inspections/disposition', data);

// Inventory
export const getInventory = () => api.get('/inventory');
export const getInventoryByLot = (lotNumber) => api.get(`/inventory/lot/${lotNumber}`);

// Data Import (Multipart)
export const importExcel = (file) => {
  const formData = new FormData();
  formData.append('file', file);
  return api.post('/import/excel', formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
  });
};

export const importCsv = (file) => {
  const formData = new FormData();
  formData.append('file', file);
  return api.post('/import/csv', formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
  });
};

export const importPdfCoa = (file, lotNumber = '') => {
  const formData = new FormData();
  formData.append('file', file);
  if (lotNumber) {
    formData.append('lotNumber', lotNumber);
  }
  return api.post('/import/pdf', formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
  });
};

export default api;
