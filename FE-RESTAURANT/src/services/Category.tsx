import apiClient from '../utils/Api.tsx'; 
import { type Category } from '../interfaces/Category.tsx';

// Interface cho dữ liệu tạo mới
interface CreateCategoryData {
  name: string;
}

// Lấy tất cả danh mục
export const getAllCategories = async (): Promise<Category[]> => {
  const response = await apiClient.get<Category[]>('/api/categories');
  return response.data;
};

// Tạo một danh mục mới
export const createCategory = async (data: CreateCategoryData): Promise<Category> => {
  const response = await apiClient.post<Category>('/api/categories', data);
  return response.data;
};

// (Sau này bạn sẽ thêm hàm update, delete ở đây)