import { BrowserRouter, Routes, Route } from "react-router-dom";
// MỚI: Import ToastContainer và CSS của nó
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

import ClientRouter from "./routers/ClientRouter";

import AdminRouter from "./routers/AdminRouter";
import Login from "./pages/Login.js";
import Register from "./pages/Register.tsx";
import Index from "./pages/Index.js";
import UserInfo from "./pages/UserInfo.tsx";

import StaffPage from "./pages/StaffPage.tsx";

import axios from "axios";
axios.defaults.withCredentials = true;
function App() {
  return (
    <BrowserRouter>
      {/* MỚI: Đặt ToastContainer ở đây, bên ngoài Routes */}
      <ToastContainer
        position="top-right"
        autoClose={3000}
        hideProgressBar={false}
        newestOnTop={false}
        closeOnClick
        rtl={false}
        pauseOnFocusLoss
        draggable
        pauseOnHover
        theme="light"
      />

      <Routes>
        <Route path="/Login" element={<Login />} />
        <Route path="/Register" element={<Register />} />
        <Route path="/UserInfo" element={<UserInfo />} />
        <Route path="/" element={<Index />} />
        <Route path="/Client/*" element={<ClientRouter />} />
        <Route path="/Admin/*" element={<AdminRouter />} />
        <Route path="/Manager/*" element={<AdminRouter />} />
        <Route path="/Staff" element={<StaffPage />} />
        {/* <Route path="/Staff/Branch/1/Tables" element={<Staff />} />
        <Route path="/Staff/Branch/:branchId/Table/:tableId/Order/:orderId/Category/:categoryId" element={<Staff />} /> */}
      </Routes>
    </BrowserRouter>
  );
}

export default App;
