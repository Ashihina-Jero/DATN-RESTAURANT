export const DiscountStatus = {
  ACTIVE: 'ACTIVE',     // Khuyến mãi đang hoạt động
  INACTIVE: 'INACTIVE', // Khuyến mãi bị vô hiệu hóa (tạm ngừng)
  EXPIRED: 'EXPIRED',   // Khuyến mãi đã hết hạn
} as const;

export type DiscountStatus = typeof DiscountStatus[keyof typeof DiscountStatus];