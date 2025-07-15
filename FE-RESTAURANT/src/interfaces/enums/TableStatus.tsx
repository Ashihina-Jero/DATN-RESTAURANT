export const TableStatus = {
  AVAILABLE: 'AVAILABLE',       // Bàn trống, có thể nhận khách
  OCCUPIED: 'OCCUPIED',         // Bàn đang có khách
  MAINTENANCE: 'MAINTENANCE',   // Bàn đang được bảo trì, không sử dụng
} as const;

export type TableStatus = typeof TableStatus[keyof typeof TableStatus];
