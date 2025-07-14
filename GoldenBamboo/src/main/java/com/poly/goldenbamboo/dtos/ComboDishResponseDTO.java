package com.poly.goldenbamboo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComboDishResponseDTO {
    private int id;

    // Chỉ trả về ID và tên của Combo để tránh vòng lặp và lộ thông tin quá nhiều
    private Integer comboId;
    private String comboName;

    // Chỉ trả về ID và tên của Dish
    private Integer dishId;
    private String dishName;
}