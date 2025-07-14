import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import  Login   from './pages/Login';
import Register from './pages/Register';
import UserLayout from './layouts/UserLayout';
import Home from './components/user/Home';
import Admin from './routers/Admin';
import NotFoundPage from './pages/NotFoundPage';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css'; 
function App() {
  return (
    <Router>
      <ToastContainer
        position="top-right"
        autoClose={3000}
        hideProgressBar={false}
      />
      <Routes>
        {/* Route cho trang đăng nhập (không dùng layout chung) */}
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        {/* Các route khác có thể sử dụng layout chung */}
        <Route path="/" element={<UserLayout />}>
          <Route index element={<Home />} />
        </Route> 
        <Route path="/admin/*" element={<Admin />} />
        {/* Route cho các trang không tìm thấy */}
        <Route path="*" element={<NotFoundPage />} />







      </Routes>
    </Router>
  );
}

export default App;