package com.poly.restaurant.dtos;

import com.poly.restaurant.entities.enums.MenuItemStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemResponseDTO {
    private Integer id;
    private String name;
    private String description;
    private String image;
    private BigDecimal price;
    private MenuItemStatus status;
    // Không có các trường đặc thù của Combo hay Dish
}