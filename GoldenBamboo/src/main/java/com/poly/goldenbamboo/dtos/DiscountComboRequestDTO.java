package com.poly.goldenbamboo.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscountComboRequestDTO {

    // Không cần id khi tạo mới

    @NotNull(message = "Phần trăm giảm giá không được để trống")
    @PositiveOrZero(message = "Phần trăm giảm giá phải lớn hơn hoặc bằng 0")
    private BigDecimal discountPercentage;

    @NotNull(message = "ID Combo không được để trống")
    private Integer comboId; // Giả định đây là ComboEntity ID

    @NotNull(message = "ID Mã giảm giá không được để trống")
    private Integer discountId;
}