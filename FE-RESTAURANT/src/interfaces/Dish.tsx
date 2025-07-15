import { MenuItemStatus } from './enums/MenuItemStatus';

export interface Dish {
  id?: number; 
  name?: string;
  description?: string;
  image?: string;
  status?: MenuItemStatus; 
  categoryId?: number;
  categoryName?: string;
}   