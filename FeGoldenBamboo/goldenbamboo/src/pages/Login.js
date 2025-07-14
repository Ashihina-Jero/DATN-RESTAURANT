import React, { useState, useContext } from "react";
import { Link, useNavigate } from "react-router-dom";
import "../pages/Login.css";
import logo from "../assets/logos/VanKhoadark.png";
import LoginService from "../services/authService/LoginService";

export default function Login() {
  const [phone, setPhone] = useState("");
  const [password, setPassword] = useState("");
  const [responseMessage, setResponseMessage] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [phoneError, setPhoneError] = useState("");
  const [passwordError, setPasswordError] = useState("");
  const navigate = useNavigate();

  const validateInput = () => {
    let isValid = true;
    setPhoneError("");
    setPasswordError("");

    if (!phone.trim()) {
      setPhoneError("Vui lòng nhập số điện thoại.");
      isValid = false;
    } else if (!/^\d{9,11}$/.test(phone)) {
      setPhoneError("Số điện thoại không hợp lệ. (9-11 chữ số)");
      isValid = false;
    }

    if (!password) {
      setPasswordError("Vui lòng nhập mật khẩu.");
      isValid = false;
    } else if (password.length < 6) {
      setPasswordError("Mật khẩu phải từ 6 ký tự trở lên.");
      isValid = false;
    }

    return isValid;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!validateInput()) return;

    setIsSubmitting(true);
    setResponseMessage("");

    try {
      const result = await LoginService.login(phone, password);

      if (result.success) {
        navigate("/");
      } else {
        setResponseMessage(result.message || "Đăng nhập thất bại!");
      }
    } catch (error) {
      console.error("Login error:", error);
      setResponseMessage(error.response?.data?.message || "Đã xảy ra lỗi khi đăng nhập.");
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div>
      <div className="login-container">
        <div className="text-center my-2">
          <Link to="/">
            <img src={logo} alt="Logo" className="logo" />
          </Link>
        </div>
        <div className="border rounded p-4">
          <h2>Đăng nhập</h2>
          <form onSubmit={handleSubmit}>
            <div className="mb-3">
              <label className="form-label fw-bold">Số điện thoại</label>
              <input type="text" className="form-control" value={phone} onChange={(e) => setPhone(e.target.value)} />
              {phoneError && <div className="text-danger">{phoneError}</div>}
            </div>
            <div className="mb-3">
              <label className="form-label fw-bold">Mật khẩu</label>
              <input type="password" className="form-control" value={password} onChange={(e) => setPassword(e.target.value)} />
              {passwordError && <div className="text-danger">{passwordError}</div>}
              <p>
                <a className="link-opacity-50 text-decoration-none" href="#">
                  Quên mật khẩu?
                </a>
              </p>
            </div>
            <button
              type="submit"
              className={`btn bg-warning rounded-pill w-75 d-block mx-auto btn-hover ${isSubmitting ? "disabled" : ""}`}
              disabled={isSubmitting}
            >
              {isSubmitting ? "Đang đăng nhập..." : "Đăng nhập"}
            </button>
          </form>
        </div>
        {responseMessage && !phoneError && !passwordError && (
          <div className="alert alert-info mt-3" role="alert">
            {responseMessage}
          </div>
        )}
      </div>
      <hr />
      <small className="d-block text-center">© JAVA 6 - Shopbee - Assignment</small>
    </div>
  );
}
