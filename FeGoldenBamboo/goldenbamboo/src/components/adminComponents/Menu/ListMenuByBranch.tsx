import React, { useEffect, useState } from 'react';
import { getMenusByBranch, Menu } from '../../../services/adminService/Menu.tsx'; // Đảm bảo đường dẫn này đúng
import { Link } from 'react-router-dom'; 
export default function MenuList() {
  const [menus, setMenus] = useState<Menu[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null); // Thêm state để xử lý lỗi

  // Bạn cần một branchId để gọi API. Ví dụ:
  const branchId = 1; // Thay đổi ID này bằng ID chi nhánh thực tế bạn muốn tải menu

  useEffect(() => {
    const fetchMenus = async () => {
      try {
        setLoading(true);
        setError(null); // Reset lỗi trước mỗi lần fetch
        const data = await getMenusByBranch(branchId); // Gọi hàm với branchId
        setMenus(data);
      } catch (err) {
        console.error("Lỗi khi tải danh sách menu:", err);
        setError("Không thể tải danh sách menu. Vui lòng thử lại."); // Thông báo lỗi cho người dùng
      } finally {
        setLoading(false);
      }
    };

    fetchMenus();
  }, [branchId]); // Thêm branchId vào dependency array nếu nó có thể thay đổi

  return (
    <div className="container mt-4">
      <h3>Danh sách Menu</h3>
      {loading ? (
        <div>Đang tải...</div>
      ) : error ? ( // Hiển thị lỗi nếu có
        <div className="alert alert-danger">{error}</div>
      ) : (
        <ul className="list-group">
          {menus.length > 0 ? (
            menus.map(menu => (
              <li key={menu.id} className="list-group-item d-flex justify-content-between align-items-center">
                {menu.name}
                {menu.isDefault && <span className="badge bg-primary">Mặc định</span>}
              <Link
                to={`/Admin/Branch/${branchId}/Menu/Edit/${menu.id}`}
                className="btn btn-sm btn-outline-warning"
              >
                Sửa
              </Link>
              </li>
              
            ))
          ) : (
            <div className="alert alert-info mt-3">Không có menu nào</div>
          )}
        </ul>
      )}
    </div>
  );
}