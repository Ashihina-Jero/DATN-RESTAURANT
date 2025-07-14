import React from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faLockOpen, faLock, faUserClock, faQuestionCircle } from '@fortawesome/free-solid-svg-icons';

// Định nghĩa Table interface
interface Table {
    id: number;
    number: string;
    status: number;
    branchId: number;
}

// Định nghĩa props - THÊM onStatusChange
interface TableListProps {
    tables: Table[];
    onTableSelect?: (table: Table) => void;
    // (MỚI) Hàm xử lý khi thay đổi trạng thái bàn, có thể có hoặc không
    onStatusChange?: (table: Table, newStatus: number) => void; 
}

// Hàm trợ giúp để lấy style dựa trên trạng thái bàn (không đổi)
const getTableStyle = (status: number) => {
    switch (status) {
        case 0: return { icon: faLockOpen, color: 'success', text: 'Trống' };
        case 1: return { icon: faLock, color: 'warning', text: 'Có khách' };
        case 3: return { icon: faUserClock, color: 'danger', text: 'Cần xử lý' };
        default: return { icon: faQuestionCircle, color: 'secondary', text: 'Không xác định' };
    }
};

export default function TableList({ tables, onTableSelect, onStatusChange }: TableListProps) {
    const isClickable = !!onTableSelect;

    return (
        <div className='col-2 border'>
            <h1 className='text-center mt-3'>Bàn</h1>
            <div className='row p-2'>
                {Array.isArray(tables) && tables.length > 0 ? (
                    tables.map((table) => {
                        const style = getTableStyle(table.status);
                        return (
                            <div className="col-lg-6 col-md-12 col-6 mb-3" key={table.id}>
                                <div 
                                    className={`card h-100 text-center border-${style.color}`} 
                                    onClick={isClickable ? () => onTableSelect(table) : undefined}
                                    style={{ 
                                        cursor: isClickable ? 'pointer' : 'default', 
                                        transition: 'transform 0.2s' 
                                    }}
                                    onMouseOver={e => { if(isClickable) e.currentTarget.style.transform = 'scale(1.05)'; }}
                                    onMouseOut={e => { if(isClickable) e.currentTarget.style.transform = 'scale(1)'; }}
                                >
                                    <div className={`card-body d-flex flex-column justify-content-center align-items-center text-${style.color} p-2`}>
                                        <FontAwesomeIcon icon={style.icon} size="2x" />
                                        <h6 className="card-title mt-2 mb-0">Bàn {table.number}</h6>
                                        <p className="card-text"><small>{style.text}</small></p>
                                    </div>
                                    
                                    {/* (MỚI) KHU VỰC THAY ĐỔI TRẠNG THÁI */}
                                    {/* Chỉ hiển thị nếu có hàm onStatusChange được truyền vào */}
                                    {onStatusChange && (
                                        <div className="card-footer p-1 bg-transparent border-top-0">
                                            <div className="d-flex justify-content-around">
                                                {/* Nút chuyển sang 'Trống' */}
                                                <button 
                                                    className="btn btn-sm btn-outline-success" 
                                                    title="Chuyển sang Trống"
                                                    onClick={(e) => {
                                                        e.stopPropagation(); // Ngăn sự kiện click của card
                                                        onStatusChange(table, 0);
                                                    }}
                                                >
                                                    <FontAwesomeIcon icon={faLockOpen} />
                                                </button>
                                                {/* Nút chuyển sang 'Có khách' */}
                                                <button 
                                                    className="btn btn-sm btn-outline-warning" 
                                                    title="Chuyển sang Có khách"
                                                    onClick={(e) => {
                                                        e.stopPropagation(); // Ngăn sự kiện click của card
                                                        onStatusChange(table, 1);
                                                    }}
                                                >
                                                    <FontAwesomeIcon icon={faLock} />
                                                </button>
                                                {/* Nút chuyển sang 'Cần xử lý' */}
                                                <button 
                                                    className="btn btn-sm btn-outline-danger" 
                                                    title="Chuyển sang Cần xử lý"
                                                    onClick={(e) => {
                                                        e.stopPropagation(); // Ngăn sự kiện click của card
                                                        onStatusChange(table, 3);
                                                    }}
                                                >
                                                    <FontAwesomeIcon icon={faUserClock} />
                                                </button>
                                            </div>
                                        </div>
                                    )}
                                </div>
                            </div>
                        );
                    })
                ) : (
                    <div className='col-12 text-center'>Không có bàn nào.</div>
                )}
            </div>
        </div>
    );
}