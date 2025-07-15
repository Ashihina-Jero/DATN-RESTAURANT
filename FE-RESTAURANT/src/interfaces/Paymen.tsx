import { Decimal } from 'decimal.js';
import type { Order } from './Order.tsx';

export interface PaymentResponse {
  order: Order;
  changeDue: Decimal;
}
