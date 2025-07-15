// enums/PaymentMethod.ts
export const PaymentMethod = {
  CASH: 'CASH',
  CARD: 'CARD',
  MOMO: 'MOMO',
} as const;

export type PaymentMethod = typeof PaymentMethod[keyof typeof PaymentMethod];
