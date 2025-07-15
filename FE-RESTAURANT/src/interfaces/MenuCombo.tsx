import { Decimal } from 'decimal.js';

export interface MenuCombo {
  id: number;
  price: Decimal;
  comboId?: number;
  comboName?: string;
  menuId?: number;
  menuName?: string;
}