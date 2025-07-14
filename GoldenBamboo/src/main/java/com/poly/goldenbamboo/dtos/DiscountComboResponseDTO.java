package com.poly.goldenbamboo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscountComboResponseDTO {
    private int id;
    private BigDecimal discountPercentage;

    // Chỉ trả về ID và tên của Combo (giả định đây là ComboEntity)
    private Integer comboId;
    private String comboName;

    // Chỉ trả về ID và tên của Discount
    private Integer discountId;
    private String discountName;
}