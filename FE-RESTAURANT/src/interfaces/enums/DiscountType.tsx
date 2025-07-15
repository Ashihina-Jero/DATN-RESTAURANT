export const DiscountType = {
  PERCENTAGE: 'PERCENTAGE',     // Giảm theo phần trăm, ví dụ: 10% off
  FIXED_AMOUNT: 'FIXED_AMOUNT', // Giảm số tiền cố định, ví dụ: giảm 50.000đ
} as const;

export type DiscountType = typeof DiscountType[keyof typeof DiscountType];