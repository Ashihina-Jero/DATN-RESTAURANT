
import { useState, type FormEvent } from 'react';
import { useNavigate } from 'react-router-dom';
import { login } from '../services/Auth';

export default  function LoginPage() {
  const navigate = useNavigate();
  // 👇 Thay đổi state
  const [loginIdentifier, setLoginIdentifier] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError(null);

    try {
      // 👇 Gửi đi object với loginIdentifier
      const { token } = await login({ loginIdentifier, password });
      localStorage.setItem('authToken', token);
      navigate('/'); 
    } catch (err) {
      setError('Tên đăng nhập hoặc mật khẩu không chính xác.');
      console.error('Lỗi đăng nhập chi tiết:', err); 
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <h1>Đăng nhập</h1>
      <form onSubmit={handleSubmit}>
        <div>
          {/* 👇 Cập nhật label và input */}
          <label htmlFor="loginIdentifier">Email hoặc Số điện thoại</label>
          <input
            id="loginIdentifier"
            type="text" // Đổi thành text để chấp nhận cả email và sđt
            value={loginIdentifier}
            onChange={(e) => setLoginIdentifier(e.target.value)}
            required
          />
        </div>
        <div>
          <label htmlFor="password">Mật khẩu</label>
          <input
            id="password"
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </div>
        {error && <p style={{ color: 'red' }}>{error}</p>}
        <button type="submit" disabled={loading}>
          {loading ? 'Đang xử lý...' : 'Đăng nhập'}
        </button>
      </form>
    </div>
  );
}