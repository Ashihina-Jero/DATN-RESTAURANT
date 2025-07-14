package com.poly.restaurant.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuComboResponseDTO {
    private int id;
    private BigDecimal price;

    // Chỉ trả về ID và tên của Combo
    private Integer comboId;
    private String comboName;

    // Chỉ trả về ID và tên của Menu
    private Integer menuId;
    private String menuName;

    // Có thể hiển thị danh sách giảm giá áp dụng nếu cần thiết.
    // Để giữ DTO gọn, chúng ta sẽ bỏ qua các List<DiscountComboEntity>.
    // Nếu muốn hiển thị, bạn cần ánh xạ List<DiscountComboEntity> sang List<DiscountComboResponseDTO>.
    // private List<DiscountComboResponseDTO> discountCombos;
}