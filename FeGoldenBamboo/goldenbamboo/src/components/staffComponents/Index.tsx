import React, { useState, useEffect, useCallback } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { orderStaffService } from '../../services/staffService/Index.tsx';
import { toast } from 'react-toastify';

// Import các component con
import TableList from '../../components/staffComponents/TableList.tsx';
import MenuDisplay from '../../components/staffComponents/MenuDisplay.tsx';
import OrderSummary from '../../components/staffComponents/OrderSummary.tsx';

// --- Định nghĩa Interfaces ---
interface OrderDetailDTO {
    id: number;
    orderId: number;
    dishOrComboId: number;
    price: number;
    quantity: number;
    type: boolean;
    name: string | null;
    image: string | null;
}
interface FoodEntity {
    id: number;
    description: string | null;
    image: string | null;
    name: string;
    price: number;
}
interface ComboEntity extends FoodEntity {}
interface CategoryEntity {
    id: number;
    name: string;
}
interface TableEntity {
    id: number;
    number: string;
    status: number;
    branchId: number;
}
interface OrderEntity {
    id: number;
    description: string;
    paymentMethod: string;
}
interface OrderPageData {
    orderDetails: OrderDetailDTO[];
    foods: FoodEntity[];
    combos: ComboEntity[];
    categories: CategoryEntity[];
    tables: TableEntity[];
    order: OrderEntity;
}

export default function Index() {
    // --- State Management ---
    const [orderDetails, setOrderDetails] = useState<OrderDetailDTO[]>([]);
    const [categories, setCategories] = useState<CategoryEntity[]>([]);
    const [foods, setFoods] = useState<FoodEntity[]>([]);
    const [combos, setCombos] = useState<ComboEntity[]>([]);
    const [tables, setTables] = useState<TableEntity[]>([]);
    const [currentOrder, setCurrentOrder] = useState<OrderEntity | null>(null);
    const [error, setError] = useState<string | null>(null);
    const [loading, setLoading] = useState<boolean>(true);
    const [justAddedId, setJustAddedId] = useState<number | null>(null);

    const { branchId, tableId, orderId, categoryId } = useParams<{
        branchId: string;
        tableId: string;
        orderId: string;
        categoryId: string;
    }>();
    const navigate = useNavigate();

    // --- Data Fetching & Logic ---
    const fetchMenu = useCallback(async () => {
        if (!branchId || !tableId || !orderId || !categoryId) return;
        setFoods([]);
        setCombos([]);
        try {
            const data = await orderStaffService.getOrderPageData(parseInt(branchId), parseInt(tableId), parseInt(orderId), parseInt(categoryId));
            if (parseInt(categoryId) === -1) {
                setCombos(data.combos || []);
            } else {
                setFoods(data.foods || []);
            }
        } catch (err: any) {
            toast.error("Lỗi khi tải menu: " + err.message);
        }
    }, [branchId, tableId, orderId, categoryId]);

    const fetchInitialStaticData = useCallback(async () => {
        setLoading(true);
        setError(null);
        try {
            if (!branchId || !tableId || !orderId) return;
            const data: OrderPageData = await orderStaffService.getOrderPageData(parseInt(branchId), parseInt(tableId), parseInt(orderId), -1);
            setTables(data.tables || []);
            setCategories(data.categories || []);
            setOrderDetails(data.orderDetails || []);
            setCurrentOrder(data.order || null);
        } catch (err: any) {
            setError(err.message || "Không thể tải dữ liệu.");
        } finally {
            setLoading(false);
        }
    }, [branchId, tableId, orderId]);

    useEffect(() => {
        fetchInitialStaticData();
    }, [fetchInitialStaticData]);

    useEffect(() => {
        fetchMenu();
    }, [fetchMenu]);

    const fetchOrderDetails = useCallback(async () => {
        if (!orderId) return;
        try {
            const data = await orderStaffService.getOrderDetailData(parseInt(orderId));
            setOrderDetails(data.orderDetails || []);
        } catch (err: any) {
            toast.error("Lỗi khi cập nhật giỏ hàng: " + err.message);
        }
    }, [orderId]);

    // --- Các hàm xử lý sự kiện ---
    const handleAddToOrder = async (item: FoodEntity | ComboEntity, isCombo: boolean) => {
        if (!orderId || !branchId) { toast.error("Không tìm thấy mã đơn hàng."); return; }
        try {
            await orderStaffService.addToOrder({
                orderId: parseInt(orderId), dishOrComboId: item.id, price: item.price,
                quantity: 1, type: isCombo, branchId: parseInt(branchId),
            });
            toast.success(`Đã thêm "${item.name}"`);
            setJustAddedId(item.id);
            setTimeout(() => setJustAddedId(null), 1500);
            fetchOrderDetails();
        } catch (err: any) {
            toast.error("Thêm món thất bại: " + err.message);
        }
    };

    const handleUpdateQuantity = async (detailId: number, newQuantity: number) => {
        if (!orderId) { toast.error("Không tìm thấy mã đơn hàng!"); return; }
        if (newQuantity <= 0) {
            if (window.confirm('Bạn có muốn xóa món này không?')) {
                try {
                    await orderStaffService.removeOrderDetail(detailId);
                    toast.warn("Đã xóa món.");
                    fetchOrderDetails();
                } catch (err: any) {
                    toast.error("Xóa món thất bại: " + err.message);
                }
            }
            return;
        }
        try {
            await orderStaffService.updateOrderDetail(parseInt(orderId), detailId, newQuantity);
            fetchOrderDetails();
        } catch (err: any) {
            toast.error("Cập nhật thất bại: " + err.message);
        }
    };

    const handleCheckout = async () => {
        if (!orderId) { toast.error("Không tìm thấy mã đơn hàng!"); return; }
        if (window.confirm("Bạn có chắc chắn muốn thanh toán đơn hàng này không?")) {
            try {
                const result = await orderStaffService.checkoutOrder(parseInt(orderId));
                toast.success(result.message || "Thanh toán thành công!");
                setTimeout(() => { navigate(`/Staff/Branch/${branchId}/Dashboard`); }, 2000);
            } catch (err: any) {
                toast.error("Thanh toán thất bại: " + err.message);
            }
        }
    };
    
    const handleCreateNewOrder = async () => {
        if (!tableId || !branchId) {
            toast.error("Không tìm thấy thông tin bàn hoặc chi nhánh!");
            return;
        }
        if (window.confirm("Tạo một đơn hàng mới cho bàn này? Đơn hàng hiện tại sẽ được giữ lại.")) {
            try {
                const newOrder = await orderStaffService.forceCreateNewOrder(parseInt(tableId));
                if (newOrder && newOrder.id) {
                    toast.success("Đã tạo đơn hàng mới thành công!");
                    navigate(`/Staff/Branch/${branchId}/Table/${tableId}/Order/${newOrder.id}/Category/-1`);
                }
            } catch (err: any) {
                toast.error("Tạo đơn hàng mới thất bại: " + err.message);
            }
        }
    };

    const handleSwitchTable = async (table: TableEntity) => {
        if (table.id.toString() === tableId) return; 
        try {
            toast.info(`Chuyển đến bàn ${table.number}...`);
            const order = await orderStaffService.findOrCreateOrder(table.id);
            if (order && order.id) {
                navigate(`/Staff/Branch/${branchId}/Table/${table.id}/Order/${order.id}/Category/-1`);
            } else {
                toast.error("Không nhận được thông tin đơn hàng cho bàn đã chọn.");
            }
        } catch (err: any) {
            toast.error("Có lỗi khi chuyển bàn: " + err.message);
        }
    };

    const handleUpdateOrder = async (updatedData: { description: string, paymentMethod: string }) => {
        if (!currentOrder) {
            toast.error("Không có thông tin đơn hàng để cập nhật.");
            return;
        }
        const orderToUpdate = { ...currentOrder, ...updatedData };
        try {
            // Giả sử service của bạn có hàm `updateOrder(orderId, data)`
            await orderStaffService.updateOrder(currentOrder.id, orderToUpdate);
            toast.success("Cập nhật đơn hàng thành công!");
        } catch (err: any) {
            toast.error("Cập nhật thất bại: " + err.message);
        }
    };

    if (loading) return <div className="text-center mt-5">Đang tải dữ liệu...</div>;
    if (error) return <div className="alert alert-danger text-center mt-5">{error}</div>;

    // --- Render ---
    return (
        <div className='container-fluid'>
            <div className='row border'>
                <TableList 
                    tables={tables} 
                    onTableSelect={handleSwitchTable} 
                />
                <MenuDisplay 
                    categories={categories}
                    combos={combos}
                    foods={foods}
                    handleAddToOrder={handleAddToOrder}
                    justAddedId={justAddedId}
                    params={{ branchId, tableId, orderId, categoryId }}
                />
                <OrderSummary 
                    orderData={currentOrder}
                    orderDetails={orderDetails}
                    handleUpdateQuantity={handleUpdateQuantity}
                    handleCheckout={handleCheckout}
                    handleCreateNewOrder={handleCreateNewOrder}
                    handleUpdateOrder={handleUpdateOrder}
                />
            </div>
        </div>
    );
}