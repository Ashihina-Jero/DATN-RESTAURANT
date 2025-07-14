// src/services/CategoryService.ts
import apiClient from '../utils/Api';
import { type Category } from '../interfaces/Category';

// Dữ liệu để TẠO MỚI: là Category nhưng bỏ đi thuộc tính 'id'
type CreateCategoryData = Omit<Category, 'id'>;

// Dữ liệu để CẬP NHẬT: là Category nhưng tất cả các trường đều không bắt buộc
// và thường không cần gửi id trong body
type UpdateCategoryData = Partial<Omit<Category, 'id'>>;


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

// Cập nhật một danh mục
export const updateCategory = async (id: number, data: UpdateCategoryData): Promise<Category> => {
  const response = await apiClient.put<Category>(`/api/categories/${id}`, data);
  return response.data;
};

// Xóa một danh mục
export const deleteCategory = async (id: number): Promise<void> => {
  await apiClient.delete(`/api/categories/${id}`);
};