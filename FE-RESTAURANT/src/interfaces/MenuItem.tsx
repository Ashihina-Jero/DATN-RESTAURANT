import { Decimal } from 'decimal.js';
import { MenuItemStatus } from './enums/MenuItemStatus';

export interface MenuItem {
  id?: number;
  name?: string;
  description?: string;
  image?: string;
  price?: Decimal;
  status?: MenuItemStatus;
}