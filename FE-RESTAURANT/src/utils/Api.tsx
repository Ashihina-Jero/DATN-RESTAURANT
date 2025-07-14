import axios from 'axios';

const apiClient = axios.create({
  baseURL: 'http://localhost:8080', // URL backend của bạn
  headers: {
    'Content-Type': 'application/json',
  },
});

// Interceptor để tự động đính kèm token vào các request cần thiết
apiClient.interceptors.request.use(
  (config) => {
    // ✅ KIỂM TRA ĐIỀU KIỆN
    // Nếu URL của request KHÔNG phải là của auth, thì mới đính kèm token
    if (config.url && !config.url.includes('/api/auth/')) {
      const token = localStorage.getItem('authToken'); // Hoặc key bạn dùng để lưu token
      if (token) {
        config.headers.Authorization = `Bearer ${token}`;
      }
    }
    
    return config; // Trả về config để request được tiếp tục
  },
  (error) => {
    return Promise.reject(error);
  }
);

export default apiClient;
