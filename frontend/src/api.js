import axios from 'axios';

const api = axios.create({
  baseURL: '/api',
  withCredentials: true,
  headers: { 'Content-Type': 'application/json' },
});

export const getProducts = () => api.get('/products');
export const getProduct  = (id) => api.get(`/products/${id}`);
export const searchProducts = (keyword) => api.get(`/search?keyword=${encodeURIComponent(keyword)}`);
export const getDashboard   = () => api.get('/dashboard');
export const recordPurchase = (id) => api.post(`/purchase/${id}`);
export const analyzeProduct = (id) => api.post(`/products/${id}/ai-analyze`);

export default api;
