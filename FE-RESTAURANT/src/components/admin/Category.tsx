import { useState, useEffect, type FormEvent } from 'react';
import { getAllCategories, createCategory } from '../../services/Category';
import { type Category } from '../../interfaces/Category';
import { toast } from 'react-toastify';

export default function CategoryPage() {
  // State để lưu danh sách các danh mục
  const [categories, setCategories] = useState<Category[]>([]);
  // State cho việc thêm mới danh mục
  const [newCategoryName, setNewCategoryName] = useState('');
  // State cho trạng thái tải dữ liệu
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  // Hàm để tải lại danh sách danh mục
  const fetchCategories = async () => {
    try {
      setLoading(true);
      const data = await getAllCategories();
      setCategories(data);
    } catch (err) {
      setError('Không thể tải danh sách danh mục.');
      toast.error('Không thể tải danh sách danh mục.');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  // Dùng useEffect để gọi API lấy danh sách khi component được render lần đầu
  useEffect(() => {
    fetchCategories();
  }, []);

  // Hàm xử lý khi submit form tạo mới
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
      fetchCategories(); 
    } catch (err) {
      toast.error('Lỗi khi tạo danh mục.');
      console.error(err);
    }
  };

  // Hiển thị trạng thái tải
  if (loading) {
    return <div>Đang tải...</div>;
  }

  // Hiển thị lỗi nếu có
  if (error) {
    return <div>Lỗi: {error}</div>;
  }

  // Giao diện chính
  return (
    <div>
      <h1>Quản lý Danh mục</h1>

      {/* Form thêm mới */}
      <form onSubmit={handleCreateCategory}>
        <input
          type="text"
          value={newCategoryName}
          onChange={(e) => setNewCategoryName(e.target.value)}
          placeholder="Tên danh mục mới"
        />
        <button type="submit">Thêm mới</button>
      </form>

      {/* Bảng hiển thị danh sách */}
      <table border={1} style={{ width: '100%', marginTop: '1rem' }}>
        <thead>
          <tr>
            <th>ID</th>
            <th>Tên Danh mục</th>
            <th>Hành động</th>
          </tr>
        </thead>
        <tbody>
          {categories.map((category) => (
            <tr key={category.id}>
              <td>{category.id}</td>
              <td>{category.name}</td>
              <td>
                <button>Sửa</button>
                <button>Xóa</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}