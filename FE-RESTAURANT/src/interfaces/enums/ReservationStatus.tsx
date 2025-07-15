export const ReservationStatus = {
  PENDING: 'PENDING',       // Đặt bàn đang chờ xác nhận
  CONFIRMED: 'CONFIRMED',   // Đặt bàn đã được xác nhận
  CANCELLED: 'CANCELLED',   // Đặt bàn đã bị hủy
} as const;

export type ReservationStatus = typeof ReservationStatus[keyof typeof ReservationStatus];
