import { Routes, Route } from 'react-router-dom';
import AdminLayout from '../layouts/AdminLayout';
import NotFoundPage from '../pages/NotFoundPage';
import  Category  from '../components/admin/Category';
export default function Admin() {
  return (
    <Routes>
      <Route path="/" element={<AdminLayout />}>
        {/* Các route dành cho quản trị viên */}
        <Route index element={<div>Admin Home</div>} />
              {/* Thêm các route khác ở đây */}
        <Route path="categories" element={<Category />} />
      </Route>
      <Route path="*" element={<NotFoundPage />} />
    </Routes>
  )
}
