import React, { useState, useEffect, useCallback } from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faLockOpen, faLock, faUserClock, faQuestionCircle } from '@fortawesome/free-solid-svg-icons';
import { orderStaffService } from '../../services/staffService/Index.tsx';
import { toast } from 'react-toastify';
import { useNavigate, useParams } from 'react-router-dom';

// Định nghĩa kiểu dữ liệu cho bàn
interface Table {
    id: number;
    number: string;
    status: number;
    branchId: number;
    quantity: number;
}

// Hàm trợ giúp để lấy style dựa trên trạng thái bàn
const getTableStyle = (status: number) => {
    switch (status) {
        case 0: return { icon: faLockOpen, color: 'success', text: 'Trống' };
        case 1: return { icon: faLock, color: 'warning', text: 'Có khách' };
        case 3: return { icon: faUserClock, color: 'danger', text: 'Cần xử lý' };
        default: return { icon: faQuestionCircle, color: 'secondary', text: 'Không xác định' };
    }
};

export default function StaffDashboard() {
    const [tables, setTables] = useState<Table[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);
    const navigate = useNavigate();
    const { branchId } = useParams<{ branchId: string }>();

    const fetchTables = useCallback(async () => {
        if (!branchId) {
            setError("Không tìm thấy ID chi nhánh trong đường dẫn.");
            setLoading(false);
            return;
        }
        setLoading(true);
        try {
            const numericBranchId = parseInt(branchId);
            const data = await orderStaffService.getTablesByBranch(numericBranchId);
            setTables(data || []);
        } catch (err: any) {
            setError(err.message);
            toast.error("Không thể tải danh sách bàn: " + err.message);
        } finally {
            setLoading(false);
        }
    }, [branchId]);

    useEffect(() => {
        fetchTables();
    }, [fetchTables]);

    // Hành động chính: Chọn bàn để gọi món
    const handleTableSelect = async (table: Table) => {
        try {
            toast.info(`Đang xử lý bàn ${table.number}...`);
            const order = await orderStaffService.findOrCreateOrder(table.id);
            if (order && order.id) {
                navigate(`/Staff/Branch/${branchId}/Table/${table.id}/Order/${order.id}/Category/-1`);
            } else {
                toast.error("Không nhận được thông tin đơn hàng.");
            }
        } catch (err: any) {
            toast.error("Có lỗi xảy ra: " + err.message);
        }
    };

    // MỚI: Hàm xử lý thay đổi trạng thái bàn
    const handleStatusChange = async (tableToUpdate: Table, newStatus: number) => {
        // Nếu trạng thái đã là trạng thái mới thì không làm gì cả
        if (tableToUpdate.status === newStatus) return;

        toast.info(`Đang cập nhật trạng thái bàn ${tableToUpdate.number}...`);
        try {
            // Gọi API để cập nhật dưới database
            await orderStaffService.updateTableStatus(tableToUpdate.id, newStatus);
            
            // Cập nhật lại giao diện ngay lập tức
            setTables(currentTables => 
                currentTables.map(table =>
                    table.id === tableToUpdate.id ? { ...table, status: newStatus } : table
                )
            );
            toast.success("Cập nhật trạng thái thành công!");

        } catch (err: any) {
            toast.error("Cập nhật thất bại: " + err.message);
        }
    };

    if (loading) return <div className="text-center mt-5"><h3>Đang tải sơ đồ bàn...</h3></div>;
    if (error) return <div className="alert alert-danger">Lỗi: {error}</div>;

    return (
        <div className="container mt-4">
            <h1 className="text-center mb-4">Sơ đồ bàn</h1>
            <div className="row">
                {tables.map(table => {
                    const style = getTableStyle(table.status);
                    return (
                        <div className="col-lg-2 col-md-3 col-sm-4 col-6 mb-4" key={table.id}>
                            <div 
                                className={`card h-100 text-center border-${style.color}`} 
                                // Bấm vào vùng chính sẽ thực hiện hành động chính
                                onClick={() => handleTableSelect(table)}
                                style={{ cursor: 'pointer', transition: 'transform 0.2s' }}
                                onMouseOver={e => e.currentTarget.style.transform = 'scale(1.05)'}
                                onMouseOut={e => e.currentTarget.style.transform = 'scale(1)'}
                            >
                                <div className={`card-body d-flex flex-column justify-content-center align-items-center text-${style.color}`}>
                                    <FontAwesomeIcon icon={style.icon} size="3x" />
                                    <h5 className="card-title mt-3 mb-1">Bàn {table.number}</h5>
                                    <p className="card-text"><small>{style.text}</small></p>
                                </div>
                                
                                {/* MỚI: Khu vực các nút bấm đổi trạng thái nhanh */}
                                <div className="card-footer p-1 bg-transparent border-top-0">
                                    <div className="d-flex justify-content-around">
                                        <button 
                                            className="btn btn-sm btn-outline-success" 
                                            title="Chuyển sang Trống"
                                            onClick={(e) => {
                                                e.stopPropagation(); // Ngăn sự kiện của card
                                                handleStatusChange(table, 0);
                                            }}
                                        >
                                            <FontAwesomeIcon icon={faLockOpen} />
                                        </button>
                                        <button 
                                            className="btn btn-sm btn-outline-danger" 
                                            title="Chuyển sang Cần xử lý"
                                            onClick={(e) => {
                                                e.stopPropagation(); // Ngăn sự kiện của card
                                                handleStatusChange(table, 3);
                                            }}
                                        >
                                            <FontAwesomeIcon icon={faUserClock} />
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    );
                })}
            </div>
        </div>
    );
}