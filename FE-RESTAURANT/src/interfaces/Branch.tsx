import { BranchStatus } from './enums/BranchStatus';

export interface Branch {
  id: number;
  name: string;
  address: string;
  phone: string;
  description: string;
  status: BranchStatus;
}
