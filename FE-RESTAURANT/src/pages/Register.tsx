import { useState, type FormEvent } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { register } from '../services/Auth';
import { isAxiosError } from 'axios';

export default function RegisterPage() {
  const [name, setName] = useState('');
  const [phone, setPhone] = useState('');
  const [password, setPassword] = useState('');
  
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    setSuccess(null);

    try {
      await register({ name, phone, password });
      setSuccess('Đăng ký thành công! Bạn có thể đăng nhập ngay bây giờ.');
      navigate('/');
    } catch (err) { // Không cần dùng ': any' nữa
      let errorMessage = 'Đã có lỗi xảy ra. Vui lòng thử lại.';
      
      // Kiểm tra xem đây có phải là lỗi từ Axios không
      if (isAxiosError(err)) {
        // Nếu phải, ta có thể truy cập an toàn vào err.response
        errorMessage = err.response?.data?.message || errorMessage;
      }
      
      setError(errorMessage);
      console.error(err); // Vẫn có thể log lỗi đầy đủ để debug
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <h1>Đăng ký tài khoản</h1>
      <form onSubmit={handleSubmit}>
        <div>
          <label htmlFor="fullName">Họ và tên</label>
          <input
            id="fullName" type="text" value={name}
            onChange={(e) => setName(e.target.value)} required
          />
        </div>
        <div>
          <label htmlFor="loginIdentifier">Email hoặc Số điện thoại</label>
          <input
            id="loginIdentifier" type="text" value={phone}
            onChange={(e) => setPhone(e.target.value)} required
          />
        </div>
        <div>
          <label htmlFor="password">Mật khẩu</label>
          <input
            id="password" type="password" value={password}
            onChange={(e) => setPassword(e.target.value)} required
          />
        </div>

        {/* Thông báo lỗi hoặc thành công */}
        {error && <p style={{ color: 'red' }}>{error}</p>}
        {success && <p style={{ color: 'green' }}>{success}</p>}

        <button type="submit" disabled={loading}>
          {loading ? 'Đang xử lý...' : 'Đăng ký'}
        </button>
      </form>
      <p>
        Đã có tài khoản? <Link to="/login">Đăng nhập</Link>
      </p>
    </div>
  );
}