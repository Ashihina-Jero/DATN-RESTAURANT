import React from "react";
import { Navigate } from "react-router-dom";
import { useAuth } from "./useAuth"; // Đây là một hook tự tạo để lấy thông tin user

// Component này nhận vào 2 props:
// 1. requiredRole: Vai trò yêu cầu để truy cập
// 2. children: Component sẽ được hiển thị nếu vai trò hợp lệ (ví dụ: <AdminRouter />)
const ProtectedRoute = ({ requiredRole, children }) => {
  // Lấy vai trò của người dùng hiện tại từ một hook tùy chỉnh
  // Hook này có thể đọc từ localStorage, cookie hoặc context
  const { userRole } = useAuth();

  // Kiểm tra nếu vai trò của người dùng không khớp với vai trò yêu cầu
  if (userRole !== requiredRole) {
    // Nếu không khớp, điều hướng về trang đăng nhập hoặc trang "không có   quyền"
    // `replace` để người dùng không thể nhấn "Back" lại trang cũ
    return <Navigate to="/login" replace />;
  }

  // Nếu vai trò hợp lệ, hiển thị component con
  return children;
};

export default ProtectedRoute;
