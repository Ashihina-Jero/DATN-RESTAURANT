import { useState, useEffect, type FormEvent, type ChangeEvent } from 'react';
import {
  getAllCategories,
  createCategory,
  updateCategory,
  deleteCategory,
} from '../../services/Category'; // Giả sử bạn đã đổi tên file
import { type Category } from '../../interfaces/Category';
import { toast } from 'react-toastify';

export default function CategoryPage() {
  // === STATE ===
  const [categories, setCategories] = useState<Category[]>([]);
  const [newCategoryName, setNewCategoryName] = useState('');
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  // State cho việc sửa: dùng một object để giữ cả id và name
  const [editingCategory, setEditingCategory] = useState<Category | null>(null);

  // === DATA FETCHING ===
  const fetchCategories = async () => {
    try {
      const data = await getAllCategories();
      setCategories(data);
    } catch (err) {
      const errorMessage = 'Không thể tải danh sách danh mục.';
      setError(errorMessage);
      toast.error(errorMessage);
      console.error(err);
    } finally {
      if (loading) setLoading(false);
    }
  };

  useEffect(() => {
    fetchCategories();
  }, []);

  // === HANDLERS ===

  // Xử lý tạo mới
  const handleCreateCategory = async (e: FormEvent) => {
    e.preventDefault();
    if (!newCategoryName.trim()) {
      toast.warn('Tên danh mục không được để trống.');
      return;
    }
    try {
      await createCategory({ name: newCategoryName });
      toast.success('Tạo danh mục thành công!');
      setNewCategoryName('');
      fetchCategories(); // Tải lại danh sách
    } catch (err) {
      toast.error('Lỗi khi tạo danh mục.');
      console.error(err);
    }
  };

  // Xử lý xóa
  const handleDeleteCategory = async (id: number) => {
    if (!window.confirm('Bạn có chắc chắn muốn xóa danh mục này không?')) {
      return;
    }
    try {
      await deleteCategory(id);
      toast.success('Xóa danh mục thành công!');
      fetchCategories(); // Tải lại danh sách
    } catch (err) {
      toast.error('Lỗi khi xóa danh mục.');
      console.error(err);
    }
  };

  // Bắt đầu chỉnh sửa
  const handleEditClick = (category: Category) => {
    setEditingCategory({ ...category }); // Tạo một bản sao để chỉnh sửa
  };

  // Hủy bỏ chỉnh sửa
  const handleCancelEdit = () => {
    setEditingCategory(null);
  };

  // Cập nhật giá trị khi đang gõ trong ô sửa
  const handleEditingInputChange = (e: ChangeEvent<HTMLInputElement>) => {
    if (editingCategory) {
      setEditingCategory({
        ...editingCategory,
        name: e.target.value,
      });
    }
  };

  // Gửi yêu cầu cập nhật
  const handleUpdateCategory = async (e: FormEvent) => {
    e.preventDefault();
    if (!editingCategory || !editingCategory.name.trim()) {
      toast.warn('Tên danh mục không được để trống.');
      return;
    }
    try {
      await updateCategory(editingCategory.id, { name: editingCategory.name });
      toast.success('Cập nhật danh mục thành công!');
      setEditingCategory(null); // Thoát khỏi chế độ sửa
      fetchCategories(); // Tải lại danh sách
    } catch (err) {
      toast.error('Lỗi khi cập nhật danh mục.');
      console.error(err);
    }
  };

  // === RENDER LOGIC ===
  if (loading) {
    return (
      <div className="d-flex justify-content-center align-items-center" style={{ height: '100vh' }}>
        <div className="spinner-border text-primary" role="status">
          <span className="visually-hidden">Loading...</span>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="container mt-4">
        <div className="alert alert-danger" role="alert">
          <strong>Lỗi:</strong> {error}
        </div>
      </div>
    );
  }

  return (
    <div className="container mt-4">
      <h1 className="mb-4">Quản lý Danh mục</h1>

      {/* Form thêm mới */}
      <div className="card mb-4">
        <div className="card-body">
          <h5 className="card-title">Thêm danh mục mới</h5>
          <form onSubmit={handleCreateCategory}>
            <div className="input-group">
              <input
                type="text"
                className="form-control"
                value={newCategoryName}
                onChange={(e) => setNewCategoryName(e.target.value)}
                placeholder="Tên danh mục mới"
              />
              <button className="btn btn-primary" type="submit">Thêm mới</button>
            </div>
          </form>
        </div>
      </div>

      {/* Bảng hiển thị danh sách */}
      <div className="card">
        <div className="card-body">
            <h5 className="card-title">Danh sách danh mục</h5>
            <table className="table table-striped table-bordered table-hover align-middle">
              <thead className="table-dark">
                <tr>
                  <th scope="col" style={{ width: '10%' }}>ID</th>
                  <th scope="col">Tên Danh mục</th>
                  <th scope="col" style={{ width: '20%' }} className="text-center">Hành động</th>
                </tr>
              </thead>
              <tbody>
                {categories.map((category) => (
                  <tr key={category.id}>
                    {editingCategory?.id === category.id ? (
                      // Chế độ SỬA
                      <>
                        <td>{category.id}</td>
                        <td colSpan={2}>
                          <form onSubmit={handleUpdateCategory} className="d-flex gap-2">
                            <input
                              type="text"
                              className="form-control"
                              value={editingCategory.name}
                              onChange={handleEditingInputChange}
                              autoFocus
                            />
                            <button type="submit" className="btn btn-success btn-sm">Lưu</button>
                            <button type="button" className="btn btn-secondary btn-sm" onClick={handleCancelEdit}>Hủy</button>
                          </form>
                        </td>
                      </>
                    ) : (
                      // Chế độ XEM
                      <>
                        <td>{category.id}</td>
                        <td>{category.name}</td>
                        <td className="text-center">
                          <button
                            type="button"
                            className="btn btn-warning btn-sm me-2"
                            onClick={() => handleEditClick(category)}
                          >
                            Sửa
                          </button>
                          <button
                            type="button"
                            className="btn btn-danger btn-sm"
                            onClick={() => handleDeleteCategory(category.id)}
                          >
                            Xóa
                          </button>
                        </td>
                      </>
                    )}
                  </tr>
                ))}
              </tbody>
            </table>
        </div>
      </div>
    </div>
  );
}
