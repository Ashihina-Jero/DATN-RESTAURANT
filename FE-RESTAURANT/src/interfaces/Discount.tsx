import { Decimal } from 'decimal.js';
import { DiscountType } from './enums/DiscountType.tsx';
import { DiscountStatus } from './enums/DiscountStatus.tsx';

export interface Discount {
  id?: number;
  name?: string;
  type?: DiscountType;
  value?: Decimal;
  startDate?: Date;
  endDate?: Date;
  status?: DiscountStatus;
  branchNames?: string[];
  dishNames?: string[];
  comboNames?: string[];
}