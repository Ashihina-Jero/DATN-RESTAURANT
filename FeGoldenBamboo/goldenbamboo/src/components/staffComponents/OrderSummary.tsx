import React, { useState, useEffect } from 'react';

// Định nghĩa props
interface OrderSummaryProps {
    orderData: any; 
    orderDetails: any[];
    handleUpdateOrder: (updatedData: { description: string, paymentMethod: string }) => void;
    handleUpdateQuantity: (detailId: number, newQuantity: number) => void;
    handleCheckout: () => void;
    handleCreateNewOrder: () => void;
}

export default function OrderSummary({ 
    orderData,
    orderDetails, 
    handleUpdateOrder,
    handleUpdateQuantity, 
    handleCheckout, 
    handleCreateNewOrder
}: OrderSummaryProps) {
    
    const [description, setDescription] = useState('');
    const [paymentMethod, setPaymentMethod] = useState('Tiền mặt');

    useEffect(() => {
        if (orderData) {
            setDescription(orderData.description || '');
            setPaymentMethod(orderData.paymentMethod || 'Tiền mặt');
        }
    }, [orderData]);

    const totalAmount = orderDetails.reduce((total, item) => total + (item.price * item.quantity), 0);
    
    const onSaveChanges = () => {
        handleUpdateOrder({ description, paymentMethod });
    };

    if (!orderData) {
        return <div className='col-3 border'><p className="p-3">Đang tải thông tin đơn hàng...</p></div>;
    }

    return (
        <div className='col-3 border d-flex flex-column'>
            <div className='flex-grow-1 p-2' style={{maxHeight: '80vh', overflowY: 'auto'}}>
                <h5 className='text-center mt-2'>Chi tiết đơn hàng #{orderData.id}</h5>
                <table className="table table-bordered mt-3">
                    <thead>
                        <tr>
                            <th>#</th>
                            <th>Tên</th>
                            <th>SL</th>
                            <th>Giá</th>
                        </tr>
                    </thead>
                    <tbody>
                        {orderDetails.length > 0 ? (
                            orderDetails.map((detail, index) => (
                                <tr key={detail.id || index}>
                                    <td>{index + 1}</td>
                                    <td>{detail.name || 'N/A'}</td>
<td className="d-flex justify-content-around align-items-center p-1 border-0">
    {detail.quantity === 1 ? (
        <button
            className="btn btn-outline-danger btn-sm py-0 px-2"
            onClick={() => handleUpdateQuantity(detail.id, 0)}
            title="Xóa món"
        >🗑️
        </button>
    ) : (
        <button
            className="btn btn-outline-danger btn-sm py-0 px-2"
            onClick={() => handleUpdateQuantity(detail.id, detail.quantity - 1)}
        >-
        </button>
    )}
    <span className="mx-2 fw-bold">{detail.quantity}</span>
    <button
        className="btn btn-outline-success btn-sm py-0 px-2"
        onClick={() => handleUpdateQuantity(detail.id, detail.quantity + 1)}
    >
        +
    </button>
</td>

                                    <td>{(detail.price * detail.quantity).toLocaleString('vi-VN')}</td>
                                </tr>
                            ))
                        ) : (
                            <tr>
                                <td colSpan={4} className="text-center">Chưa có món nào.</td>
                            </tr>
                        )}
                    </tbody>
                </table>
                <p className='fw-bold'>Ghi chú:</p>
                <textarea 
                    className='form-control mb-3' 
                    placeholder='Nhập ghi chú...'
                    rows={2}
                    value={description}
                    onChange={(e) => setDescription(e.target.value)}
                />

                <p className='fw-bold'>Phương thức thanh toán:</p>
                <select 
                    className='form-select mb-3'
                    value={paymentMethod}
                    onChange={(e) => setPaymentMethod(e.target.value)}
                >
                    <option value="Tiền mặt">Tiền mặt</option>
                    <option value="Chuyển khoản">Chuyển khoản</option>
                    <option value="Thẻ">Thẻ</option>
                </select>
                 <button className='btn btn-outline-primary w-100 mb-3' onClick={onSaveChanges}>
                    Lưu thay đổi
                </button>
            </div>

            <div className='mb-2 p-2 border-top'>
                <h4 className="text-end">Tổng tiền: {totalAmount.toLocaleString('vi-VN')} VNĐ</h4>
                <div className="d-grid gap-2">
                    <button className='btn btn-success' onClick={handleCheckout}>Thanh toán</button>
                    <button className='btn btn-primary'>Xác nhận</button>
                    <div className="row g-2">
                        <div className="col">
                            <button onClick={handleCreateNewOrder} className='btn btn-info w-100'>Tạo mới</button>
                        </div>
                        <div className="col">
                            <button className='btn btn-danger w-100'>Hủy đơn</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}