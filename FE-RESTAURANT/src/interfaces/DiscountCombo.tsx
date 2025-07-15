export type BigDecimal = number;

export interface DiscountCombo {
  id: number;
  discountPercentage: BigDecimal;
  comboId?: number;
  comboName?: string;
  discountId?: number;
  discountName?: string;
}   