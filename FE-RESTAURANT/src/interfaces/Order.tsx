import type { OrderStatus } from './enums/OrderStatus';
import type { PaymentMethod } from './enums/PaymentMethod';
import type { OrderDetail } from './OrderDetail';

export interface Order {
  id: number;
  orderDate: string; 
  paymentMethod: PaymentMethod;
  prepay: number; 
  status: OrderStatus;
  description?: string;
  totalAmount: number;

  accountId: number;
  accountName: string;

  branchId: number;
  branchName: string;

  tableId?: number | null;
  tableName?: string;

  orderDetails: OrderDetail[];
}
