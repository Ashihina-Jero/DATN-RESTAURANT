import type { TableStatus } from './enums/TableStatus.tsx';

export interface Table {
  id: number;
  name: string;
  status: TableStatus;
  branchId: number;
  branchName: string;
}
