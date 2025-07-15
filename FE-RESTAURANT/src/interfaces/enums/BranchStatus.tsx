// interface/enums/BranchStatus.ts

export const BranchStatus = {
  ACTIVE: 'ACTIVE',
  INACTIVE: 'INACTIVE',
  CLOSED: 'CLOSED',
} as const;

export type BranchStatus = typeof BranchStatus[keyof typeof BranchStatus];
