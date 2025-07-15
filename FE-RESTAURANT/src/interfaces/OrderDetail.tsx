import { Decimal } from 'decimal.js';

export interface OrderDetail {
  id: number;
  quantity: number;
  price: Decimal;
  name: string;
  discountPercentage?: number;

  menuItemId?: number;
  menuItemName?: string;

  orderId: number;
}
