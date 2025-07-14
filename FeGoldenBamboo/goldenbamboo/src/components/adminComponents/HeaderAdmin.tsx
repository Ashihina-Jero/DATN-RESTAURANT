import React, { useState } from 'react'
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faBell } from "@fortawesome/free-solid-svg-icons";
import { Link } from 'react-router-dom';

export default function HeaderAdmin() {
  const [showDropdown, setShowDropdown] = useState(false);

  return (
    <div className='bg-secondary d-flex justify-content-end align-items-center p-2 shadow-sm' style={{ minHeight: 64 }}>
      <div className="dropdown me-3">
        <button
          className="btn btn-light dropdown-toggle fw-bold px-4"
          type="button"
          data-bs-toggle="dropdown"
          aria-expanded="false"
        >
          Lê Văn Khoa
        </button>
        <ul className="dropdown-menu">
          <li><Link className="dropdown-item" to="#">Thông tin cá nhân</Link></li>
          <li><Link className="dropdown-item" to="#">Đổi mật khẩu</Link></li>
          <li><Link className="dropdown-item" to="#">Đăng xuất</Link></li>
        </ul>
      </div>
      <Link to="/Admin/UserInfo" className="me-3">
        <img
          src="https://placehold.co/300x300"
          className="rounded-circle border border-2 border-white"
          alt="avatar"
          width="48"
          height="48"
          style={{ objectFit: "cover", boxShadow: "0 2px 8px rgba(0,0,0,0.1)" }}
        />
      </Link>
      <div
        className="position-relative"
        onMouseEnter={() => setShowDropdown(true)}
        onMouseLeave={() => setShowDropdown(false)}
        style={{ cursor: "pointer" }}
      >
        <span className="bg-white rounded-circle d-flex align-items-center justify-content-center" style={{ width: 40, height: 40 }}>
          <FontAwesomeIcon icon={faBell} size="lg" color="#0d6efd" />
        </span>
        {showDropdown && (
          <div
            className="dropdown-menu show"
            style={{
              position: "absolute",
              right: 0,
              top: "120%",
              minWidth: "260px",
              zIndex: 1000,
              boxShadow: "0 4px 16px rgba(0,0,0,0.15)",
              borderRadius: 8,
              padding: "0.5rem 0"
            }}
          >
            <span className="dropdown-item-text text-center text-muted py-2">Không có thông báo mới</span>
          </div>
        )}
      </div>
    </div>
  )
}