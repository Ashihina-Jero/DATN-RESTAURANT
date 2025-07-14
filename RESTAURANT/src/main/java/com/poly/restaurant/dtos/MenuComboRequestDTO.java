package com.poly.restaurant.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuComboRequestDTO {

    // Không cần id khi tạo mới

    @NotNull(message = "ID Combo không được để trống")
    private Integer comboId;

    @NotNull(message = "ID Menu không được để trống")
    private Integer menuId;

    private BigDecimal price; // Price có thể null nếu được tính toán tự động
    private Integer discountPercentage; // DiscountPercentage có thể null
}