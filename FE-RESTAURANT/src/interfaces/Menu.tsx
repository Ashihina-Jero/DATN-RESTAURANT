import type { MenuDish } from './MenuDish.tsx';
import type { MenuCombo } from './MenuCombo.tsx';

export interface Menu {
  id: number;
  name: string;
  isDefault: boolean;
  branchId: number;
  branchName: string;
  dishes: MenuDish[];
  combos: MenuCombo[];
}
