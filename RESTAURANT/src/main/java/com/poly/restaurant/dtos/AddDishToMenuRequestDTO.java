package com.poly.restaurant.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class AddDishToMenuRequestDTO {
    @NotNull
    private Integer dishId;

    // Giá đặc biệt cho món ăn trong menu này
    private BigDecimal price;
}