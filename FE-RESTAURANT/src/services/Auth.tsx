import apiClient from '../utils/Api.tsx';

// Interface cho dữ liệu đăng nhập
export interface LoginCredentials {
  loginIdentifier: string;
  password: string;
}

// Interface cho dữ liệu đăng ký
export interface RegisterData {
  name: string;
  phone: string;
  password: string;
}

// ✅ Lỗi xảy ra vì thiếu hoặc sai định nghĩa này
// Interface cho dữ liệu trả về sau khi login/register thành công
export interface AuthResponse {
  token: string;
}

// Hàm login trả về một Promise chứa AuthResponse
export const login = async (credentials: LoginCredentials): Promise<AuthResponse> => {
  const response = await apiClient.post<AuthResponse>('/api/auth/login', credentials);
  return response.data;
};

// Hàm register cũng trả về một Promise chứa AuthResponse
export const register = async (data: RegisterData): Promise<AuthResponse> => {
  const response = await apiClient.post<AuthResponse>('/api/auth/register-user', data);
  return response.data;
};