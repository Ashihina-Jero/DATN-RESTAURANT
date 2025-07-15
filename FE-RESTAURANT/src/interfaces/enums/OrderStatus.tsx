
export const OrderStatus = {
  PENDING: 'PENDING',     // Đơn hàng đang chờ xử lý hoặc chưa thanh toán
  PAID: 'PAID',           // Đơn hàng đã được thanh toán
  CANCELLED: 'CANCELLED', // Đơn hàng đã bị hủy
} as const;

export type OrderStatus = typeof OrderStatus[keyof typeof OrderStatus];
