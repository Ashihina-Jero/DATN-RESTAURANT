export const MenuItemStatus = {
  ACTIVE: 'ACTIVE',             // Đang được bán và hiển thị
  INACTIVE: 'INACTIVE',         // Không hoạt động, không hiển thị
  OUT_OF_STOCK: 'OUT_OF_STOCK', // Hết hàng tạm thời
  DISCONTINUED: 'DISCONTINUED', // Ngừng kinh doanh vĩnh viễn
} as const;

export type MenuItemStatus = typeof MenuItemStatus[keyof typeof MenuItemStatus];
