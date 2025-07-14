package com.poly.goldenbamboo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuDishResponseDTO {
    private Integer id; // Sửa Integer để khớp với Entity
    private BigDecimal price;
    private Integer discountPercentage;

    // Chỉ trả về ID và tên của Dish
    private Integer dishId;
    private String dishName;

    // Chỉ trả về ID và tên của Menu
    private Integer menuId;
    private String menuName;

    // Có thể hiển thị danh sách giảm giá áp dụng nếu cần thiết.
    // Để giữ DTO gọn, chúng ta sẽ bỏ qua các List<DiscountDishEntity>.
    // Nếu muốn hiển thị, bạn cần ánh xạ List<DiscountDishEntity> sang List<DiscountDishResponseDTO>.
    // private List<DiscountDishResponseDTO> discountDishes;
}