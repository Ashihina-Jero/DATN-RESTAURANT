import { Decimal } from 'decimal.js';

export interface MenuDish {
  dishId?: number;
  dishName?: string;
  price?: Decimal;
}