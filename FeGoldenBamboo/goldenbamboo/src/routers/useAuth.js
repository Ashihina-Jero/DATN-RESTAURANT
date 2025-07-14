import React from "react";
import { Navigate } from "react-router-dom";
// BƯỚC 1: IMPORT THƯ VIỆN VỪA CÀI
import Cookies from "js-cookie";

// Component ProtectedRoute không thay đổi
const ProtectedRoute = ({ requiredRole, children }) => {
  const { userRole } = useAuth();

  if (userRole !== requiredRole) {
    return <Navigate to="/login" replace />;
  }

  return children;
};

export default ProtectedRoute;

// BƯỚC 2: SỬA LẠI HOOK useAuth
export const useAuth = () => {
  // Đọc trực tiếp cookie có tên 'userRole'
  // Tên này phải TRÙNG KHỚP với tên bạn đặt ở backend
  const userRole = Cookies.get("userRole");

  // Nếu cookie có giá trị là 'admin', userRole sẽ là chuỗi "admin"
  // Nếu không tìm thấy cookie, userRole sẽ là undefined

  return { userRole };
};
