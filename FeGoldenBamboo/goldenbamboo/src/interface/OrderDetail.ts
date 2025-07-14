export interface Food {
  id: number;
  description: string;
  image: string | null;
  name: string;
  price: number;
  status: boolean;
}

export interface Category {
  id: number;
  name: string;
  status: boolean;
}

export interface Table {
  id: number;
  number: number;
  status: boolean;
}

export interface DataState {
  orderDetails: any[];             // Chưa có cấu trúc cụ thể, để `any[]` tạm thời
  foods: Food[];
  combos: any | null;              // Chưa có cấu trúc cụ thể, để `any | null` tạm thời
  categories: Category[];
  table: Table;
}

export interface OrderDetailResponse {
  id: number;
  dishOrComboId: number;
  price: number;
  quantity: number;
  type: boolean;
  discountPercentage: number;
}