package com.poly.restaurant.dtos;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class MenuDishResponseDTO {
    private Integer dishId;
    private String dishName;
    private BigDecimal price; // Giá của món ăn trong menu này
}