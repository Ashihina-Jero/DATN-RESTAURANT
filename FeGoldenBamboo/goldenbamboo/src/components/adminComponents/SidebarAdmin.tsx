import React from 'react';
import { Link } from 'react-router-dom';

export default function SidebarAdmin() {
  return (
    <div>
      <nav className="sidebar bg-light py-3">
        <h5 className="mb-4 text-primary">Quản trị hệ thống</h5>
        <ul className="list-group list-group-flush">

          {/* Quản lý Chi nhánh */}
          <li className="list-group-item border-0 px-0">
            <div className="fw-bold" data-bs-toggle="collapse" data-bs-target="#branchMenu" style={{cursor: "pointer"}}>
              🏢 Quản lý Chi nhánh
            </div>
            <ul className="collapse list-unstyled ms-3 my-2" id="branchMenu">
              <li><Link to="/Admin/Branch/List" className="text-decoration-none">📂Xem danh sách</Link></li>
              <li><Link to="/Admin/Branch/Create" className="text-decoration-none">➕Thêm mới</Link></li>
              <li><Link to="/Admin/Branch/Update/1" className="text-decoration-none">✏️Sửa</Link></li>
              <li><Link to="/Admin/Branch/Delete/1" className="text-decoration-none">🗑️Xóa</Link></li>
            </ul>
          </li>

          {/* Quản lý Bàn ăn */}
          <li className="list-group-item border-0 px-0">
            <div className="fw-bold" data-bs-toggle="collapse" data-bs-target="#tableMenu" style={{cursor: "pointer"}}>
              🍽️ Quản lý Bàn ăn
            </div>
            <ul className="collapse list-unstyled ms-3 my-2" id="tableMenu">
              <li><Link to="/Admin/Table/List" className="text-decoration-none">📂Xem danh sách</Link></li>
              <li><Link to="/Admin/Table/Create" className="text-decoration-none">➕Thêm mới</Link></li>
              <li><Link to="/Admin/Table/Update/1" className="text-decoration-none">✏️Sửa</Link></li>
              <li><Link to="/Admin/Table/Delete/1" className="text-decoration-none">🗑️Xóa</Link></li>
            </ul>
          </li>

          {/* Quản lý Thể loại món ăn */}
          <li className="list-group-item border-0 px-0">
            <div className="fw-bold" data-bs-toggle="collapse" data-bs-target="#categoryMenu" style={{cursor: "pointer"}}>
              📂 Quản lý Thể loại món ăn
            </div>
            <ul className="collapse list-unstyled ms-3 my-2" id="categoryMenu">
              <li><Link to="/Admin/Category/List" className="text-decoration-none">📂Xem danh sách</Link></li>
              <li><Link to="/Admin/Category/Create" className="text-decoration-none">➕Thêm mới</Link></li>
              <li><Link to="/Admin/Category/Update/1" className="text-decoration-none">✏️Sửa</Link></li>
              <li><Link to="/Admin/Category/Delete/1" className="text-decoration-none">🗑️Xóa</Link></li>
            </ul>
          </li>

          {/* Quản lý Món ăn */}
          <li className="list-group-item border-0 px-0">
            <div className="fw-bold" data-bs-toggle="collapse" data-bs-target="#foodMenu" style={{cursor: "pointer"}}>
              🍜 Quản lý Món ăn
            </div>
            <ul className="collapse list-unstyled ms-3 my-2" id="foodMenu">
              <li><Link to="/Admin/Food/List" className="text-decoration-none">📂Xem danh sách</Link></li>
              <li><Link to="/Admin/Food/Create" className="text-decoration-none">➕Thêm mới</Link></li>
              <li><Link to="/Admin/Food/Update/1" className="text-decoration-none">✏️Sửa</Link></li>
              <li><Link to="/Admin/Food/Delete/1" className="text-decoration-none">🗑️Xóa</Link></li>
            </ul>
          </li>

          {/* Quản lý Combo */}
          <li className="list-group-item border-0 px-0">
            <div className="fw-bold" data-bs-toggle="collapse" data-bs-target="#comboMenu" style={{cursor: "pointer"}}>
              🥗 Quản lý Combo
            </div>
            <ul className="collapse list-unstyled ms-3 my-2" id="comboMenu">
              <li><Link to="/Admin/Combo/List" className="text-decoration-none">📂Xem danh sách</Link></li>
              <li><Link to="/Admin/Combo/Create" className="text-decoration-none">➕Thêm mới</Link></li>
              <li><Link to="/Admin/Combo/Update/1" className="text-decoration-none">✏️Sửa</Link></li>
              <li><Link to="/Admin/Combo/Delete/1" className="text-decoration-none">🗑️Xóa</Link></li>
            </ul>
          </li>

          {/* Quản lý Combo chi tiết */}
          <li className="list-group-item border-0 px-0">
            <div className="fw-bold" data-bs-toggle="collapse" data-bs-target="#comboDetailMenu" style={{cursor: "pointer"}}>
              🍱 Quản lý Combo chi tiết
            </div>
            <ul className="collapse list-unstyled ms-3 my-2" id="comboDetailMenu">
              <li><Link to="/Admin/ComboDetail/List" className="text-decoration-none">📂Xem danh sách</Link></li>
              <li><Link to="/Admin/ComboDetail/Create" className="text-decoration-none">➕Thêm mới</Link></li>
              <li><Link to="/Admin/ComboDetail/Update/1" className="text-decoration-none">✏️Sửa</Link></li>
              <li><Link to="/Admin/ComboDetail/Delete/1" className="text-decoration-none">🗑️Xóa</Link></li>
            </ul>
          </li>

          {/* Quản lý Menu */}
          <li className="list-group-item border-0 px-0">
            <div className="fw-bold" data-bs-toggle="collapse" data-bs-target="#menuMenu" style={{cursor: "pointer"}}>
              📋 Quản lý Menu
            </div>
            <ul className="collapse list-unstyled ms-3 my-2" id="menuMenu">
              <li><Link to="/Admin/Menu/List" className="text-decoration-none">📂Xem danh sách</Link></li>
              <li><Link to="/Admin/Menu/Create" className="text-decoration-none">➕Thêm mới</Link></li>
              <li><Link to="/Admin/Menu/Update/1" className="text-decoration-none">✏️Sửa</Link></li>
              <li><Link to="/Admin/Menu/Delete/1" className="text-decoration-none">🗑️Xóa</Link></li>
            </ul>
          </li>

          {/* Quản lý Menu chi tiết */}
          <li className="list-group-item border-0 px-0">
            <div className="fw-bold" data-bs-toggle="collapse" data-bs-target="#menuDetailMenu" style={{cursor: "pointer"}}>
              📑 Quản lý Menu chi tiết
            </div>
            <ul className="collapse list-unstyled ms-3 my-2" id="menuDetailMenu">
              <li><Link to="/Admin/MenuDetail/List" className="text-decoration-none">📂Xem danh sách</Link></li>
              <li><Link to="/Admin/MenuDetail/Create" className="text-decoration-none">➕Thêm mới</Link></li>
              <li><Link to="/Admin/MenuDetail/Update/1" className="text-decoration-none">✏️Sửa</Link></li>
              <li><Link to="/Admin/MenuDetail/Delete/1" className="text-decoration-none">🗑️Xóa</Link></li>
            </ul>
          </li>

          {/* Quản lý Giảm giá */}
          <li className="list-group-item border-0 px-0">
            <div className="fw-bold" data-bs-toggle="collapse" data-bs-target="#discountMenu" style={{cursor: "pointer"}}>
              💸 Quản lý Giảm giá
            </div>
            <ul className="collapse list-unstyled ms-3 my-2" id="discountMenu">
              <li><Link to="/Admin/Discount/List" className="text-decoration-none">📂Xem danh sách</Link></li>
              <li><Link to="/Admin/Discount/Create" className="text-decoration-none">➕Thêm mới</Link></li>
              <li><Link to="/Admin/Discount/Update/1" className="text-decoration-none">✏️Sửa</Link></li>
              <li><Link to="/Admin/Discount/Delete/1" className="text-decoration-none">🗑️Xóa</Link></li>
            </ul>
          </li>

          {/* Quản lý Giảm giá chi tiết */}
          <li className="list-group-item border-0 px-0">
            <div className="fw-bold" data-bs-toggle="collapse" data-bs-target="#discountDetailMenu" style={{cursor: "pointer"}}>
              🔖 Quản lý Giảm giá chi tiết
            </div>
            <ul className="collapse list-unstyled ms-3 my-2" id="discountDetailMenu">
              <li><Link to="/Admin/DiscountDetail/List" className="text-decoration-none">📂Xem danh sách</Link></li>
              <li><Link to="/Admin/DiscountDetail/Create" className="text-decoration-none">➕Thêm mới</Link></li>
              <li><Link to="/Admin/DiscountDetail/Update/1" className="text-decoration-none">✏️Sửa</Link></li>
              <li><Link to="/Admin/DiscountDetail/Delete/1" className="text-decoration-none">🗑️Xóa</Link></li>
            </ul>
          </li>

          {/* Thông tin tài khoản */}
          <li className="list-group-item border-0 px-0">
            <Link to="/Admin/UserInfo" className="text-decoration-none">👤 Thông tin tài khoản</Link>
          </li>
        </ul>
      </nav>
    </div>
  );
}