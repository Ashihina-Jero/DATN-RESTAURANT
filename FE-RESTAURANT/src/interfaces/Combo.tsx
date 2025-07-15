import type { ComboDish } from './ComboDish.tsx';
import { Decimal } from 'decimal.js';

export interface Combo {
  id: number;
  name: string;
  alias?: ComboDish;
  description?: string;
  image?: string;
  price: number | Decimal; 
  status: boolean;
  comboDishes: ComboDish[];
}