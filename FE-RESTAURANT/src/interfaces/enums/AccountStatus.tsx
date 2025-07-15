// Enum-like object
export const AccountStatus = {
  ACTIVE: 'active',
  INACTIVE: 'inactive',
} as const;

// Type union từ giá trị của object
export type AccountStatus = typeof AccountStatus[keyof typeof AccountStatus];
