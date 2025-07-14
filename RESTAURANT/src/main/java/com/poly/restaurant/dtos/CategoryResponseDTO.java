package com.poly.restaurant.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponseDTO {
    private int id;
    private String name;
    // Bỏ qua List<DishEntity> dishes để tránh vòng lặp và dữ liệu thừa
}