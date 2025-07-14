import React, { useEffect, useState, useCallback } from "react";
import { api } from '../services/staffService/apiService.tsx'; // Đảm bảo đường dẫn này đúng

// --- Định nghĩa Interface cho dữ liệu ---
interface Category {
    id: number;
    name: string;
}
interface Table {
    id: number;
    name: string;
    status: number;
}
interface MenuItem {
    id: number;
    name: string;
    image: string;
    price: number;
    isCombo: boolean;
}
interface OrderDetail {
    id: number;
    name: string;
    quantity: number;
    price: number;
}
interface Order {
    id: number;
    orderDetails: OrderDetail[];
    description: string;
    paymentMethod: string;
    totalAmount: number;
}

function StaffPage() {
    // --- Quản lý State ---
    const [branchId, setBranchId] = useState<number | null>(null);
    const [tables, setTables] = useState<Table[]>([]);
    const [categories, setCategories] = useState<Category[]>([]);
    const [menuItems, setMenuItems] = useState<MenuItem[]>([]);
    const [currentOrder, setCurrentOrder] = useState<Order | null>(null);
    const [activeCategoryId, setActiveCategoryId] = useState<number | null>(null);

    // --- Logic gọi API ---

    // 1. Lấy dữ liệu cơ bản khi trang vừa tải
    useEffect(() => {
        api.getMyBranch().then(branch => {
            if (branch && branch.id) {
                setBranchId(branch.id);
            }
        });
        api.getCategories().then(setCategories);
    }, []);

    // 2. Lấy dữ liệu phụ thuộc vào chi nhánh (chỉ chạy khi `branchId` có giá trị)
    useEffect(() => {
        if (branchId) {
            api.getTables(branchId).then(setTables);
            api.getMenuItems(branchId, null).then(setMenuItems); // Lấy tất cả món lúc đầu
        }
    }, [branchId]); // <-- Dependency array: Chạy lại khi branchId thay đổi

    // --- Các hàm xử lý sự kiện ---

    // Hàm lấy lại order mới nhất và cập nhật state
    const refreshCurrentOrder = useCallback(async (orderId: number) => {
        const updatedOrder = await api.getOrderById(orderId);
        setCurrentOrder(updatedOrder);
    }, []); // Dependency rỗng vì hàm này không phụ thuộc state/props nào


   // Khi click vào một bàn
    const handleTableClick = useCallback(async (tableId: number) => {
        const order = await api.findOrCreateOrder(tableId);
        console.log("DỮ LIỆU ĐƠN HÀNG KHI CLICK BÀN:", order); // <--- THÊM VÀO ĐÂY
        setCurrentOrder(order);
    }, []);

    // Khi click vào một danh mục
    const handleCategoryClick = useCallback((categoryId: number | null) => {
        if (branchId) {
            api.getMenuItems(branchId, categoryId).then(setMenuItems);
            setActiveCategoryId(categoryId);
        }
    }, [branchId]);
    
    
    // Khi thêm một món vào đơn hàng
    const handleAddItem = useCallback(async (item: MenuItem) => {
        if (currentOrder) {
            // BẠN ĐANG THIẾU KHỐI CODE NÀY
            const request = {
                menuItemId: item.id,
                quantity: 1,       // Mặc định thêm 1 món
                isCombo: item.isCombo
            };

            // Giờ thì biến `request` đã tồn tại và hợp lệ
            await api.addItemToOrder(currentOrder.id, request);
            
            refreshCurrentOrder(currentOrder.id); // Cập nhật lại giỏ hàng
        } else {
            alert("Vui lòng chọn bàn trước khi thêm món!");
        }
    }, [currentOrder, refreshCurrentOrder]); // <-- Thêm refreshCurrentOrder vào đây

    // Khi cập nhật số lượng
    const handleUpdateQuantity = useCallback(async (detailId: number, newQuantity: number) => {
        if (currentOrder) {
            await api.updateQuantity(currentOrder.id, detailId, newQuantity);
            refreshCurrentOrder(currentOrder.id); // Cập nhật lại giỏ hàng
        }
    }, [currentOrder]);
    
    // --- Phần JSX để render giao diện ---
    return (
        <div className="container-fluid">
            <div className="row vh-100">

                {/* CỘT BÊN TRÁI: DANH SÁCH BÀN */}
                <div className="col-2 border-end p-2" style={{maxHeight: '100vh', overflowY: 'auto'}}>
                    <h4 className="text-center mb-3">Bàn</h4>
                    <div className="d-grid gap-2">
                        {tables.map(table => {
                            let statusClass = 'btn-secondary';
                            let statusText = 'Không rõ';

                            switch (table.status) {
                                case 0: statusClass = 'btn-success'; statusText = 'Trống'; break;
                                case 1: statusClass = 'btn-warning'; statusText = 'Có khách'; break;
                                case 2: statusClass = 'btn-danger'; statusText = 'Cần xử lý'; break;
                            }

                            // BẠN CẦN THÊM `return` Ở ĐÂY
                            return (
                                <button
                                    key={table.id}
                                    className={`btn ${statusClass}`}
                                    onClick={() => handleTableClick(table.id)}
                                >
                                    {table.name} ({statusText})
                                </button>
                            );
                        })}
                    </div>
                </div>

                {/* CỘT GIỮA: MENU */}
                <div className="col-7 border-end p-2">
                    <div className="mb-3">
                        <button className={`btn ${activeCategoryId === null ? 'btn-primary' : 'btn-outline-secondary'} me-2`} onClick={() => handleCategoryClick(null)}>Tất cả</button>
                        {categories.map(category => (
                            <button key={category.id} className={`btn ${activeCategoryId === category.id ? 'btn-primary' : 'btn-outline-secondary'} me-2`} onClick={() => handleCategoryClick(category.id)}>
                                {category.name}
                            </button>
                        ))}
                    </div>
                    <hr />
                    <div className="row g-3" style={{maxHeight: '85vh', overflowY: 'auto'}}>
                        {menuItems.map(item => (
                            <div key={item.id} className="col-md-3">
                                <div className="card h-100" onClick={() => handleAddItem(item)} style={{cursor: 'pointer'}}>
                                    <img src={item.image || 'https://placehold.co/600x400'} className="card-img-top" alt={item.name} style={{height: '120px', objectFit: 'cover'}}/>
                                    <div className="card-body p-2">
                                        <h6 className="card-title" style={{fontSize: '0.9rem'}}>{item.name}</h6>
                                        <p className="card-text fw-bold">{(item.price || 0).toLocaleString('vi-VN')} VNĐ</p>
                                    </div>
                                </div>
                            </div>
                        ))}
                    </div>
                </div>

                {/* CỘT PHẢI: CHI TIẾT ĐƠN HÀNG */}
                <div className="col-3 p-2 d-flex flex-column" style={{maxHeight: '100vh'}}>
                    {currentOrder ? (
                        <>
                            <div className="flex-grow-1" style={{overflowY: 'auto'}}>
                                <h5 className="text-center">Chi tiết ĐH #{currentOrder.id}</h5>
                                <table className="table table-sm">
                                    <thead>
                                        <tr><th>Tên</th><th className="text-center">SL</th><th className="text-end">Giá</th></tr>
                                    </thead>
                                    <tbody>
                                        {currentOrder.orderDetails?.map(detail => (
                                            <tr key={detail.id}>
                                                <td style={{fontSize: '0.9rem'}}>{detail.name}</td>
                                                <td className="d-flex justify-content-center align-items-center border-0 px-0">
                                                    <button className="btn btn-outline-danger btn-sm py-0 px-1" onClick={() => handleUpdateQuantity(detail.id, detail.quantity - 1)}>-</button>
                                                    <span className="mx-2 fw-bold">{detail.quantity}</span>
                                                    <button className="btn btn-outline-success btn-sm py-0 px-1" onClick={() => handleUpdateQuantity(detail.id, detail.quantity + 1)}>+</button>
                                                </td>
                                                <td className="text-end">{((detail.price || 0) * detail.quantity).toLocaleString('vi-VN')}</td>
                                            </tr>
                                        ))}
                                    </tbody>
                                </table>
                            </div>
                            <div className="mt-auto pt-2 border-top">
                                <h4 className="text-end">
                                    Tổng: {(currentOrder.totalAmount || 0).toLocaleString('vi-VN')} VNĐ
                                </h4>
                                <div className="d-grid gap-2">
                                     <button className='btn btn-success'>Thanh toán</button>
                                     <button className='btn btn-primary'>Lưu thay đổi</button>
                                </div>
                            </div>
                        </>
                    ) : (
                        <div className="d-flex align-items-center justify-content-center h-100">
                            <p className="text-muted">Vui lòng chọn bàn</p>
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
}

export default StaffPage;