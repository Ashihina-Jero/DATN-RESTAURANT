package com.poly.restaurant.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscountDishResponseDTO {
    private int id;

    // Chỉ trả về ID và tên của Discount
    private Integer discountId;
    private String discountName;

    // Chỉ trả về ID và tên của Dish (giả định đây là DishEntity)
    private Integer dishId;
    private String dishName;
}