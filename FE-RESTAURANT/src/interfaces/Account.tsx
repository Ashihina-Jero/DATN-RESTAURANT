import { AccountStatus } from './enums/AccountStatus.tsx';

export interface Account {
  name: string;
  password: string;
  phone: string;
  status: AccountStatus;
  branchId?: number | null;
  roleId?: number | null;
}
//moxx