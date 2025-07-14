package com.poly.restaurant.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscountDishRequestDTO {

    // Không cần id khi tạo mới

    @NotNull(message = "ID Mã giảm giá không được để trống")
    private Integer discountId;

    @NotNull(message = "ID Món ăn không được để trống")
    private Integer dishId; // Giả định đây là DishEntity ID
}