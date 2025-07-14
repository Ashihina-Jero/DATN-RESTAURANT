package com.poly.goldenbamboo.dtos;

import com.poly.goldenbamboo.entities.enums.DiscountStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscountResponseDTO {
    private int id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private DiscountStatus status;

    // Có thể bao gồm danh sách các Combo/Dish được giảm giá nếu cần thiết,
    // nhưng thường chỉ cần tổng hợp hoặc chi tiết qua API riêng.
    // Để giữ DTO gọn, chúng ta sẽ bỏ qua các List<DiscountComboEntity> và List<DiscountDishEntity>.
    // Nếu muốn hiển thị, bạn cần ánh xạ List<DiscountComboEntity> sang List<DiscountComboResponseDTO>
    // và List<DiscountDishEntity> sang List<DiscountDishResponseDTO> trong Mapper.
    // Ví dụ:
    // private List<DiscountComboResponseDTO> discountCombos;
    // private List<DiscountDishResponseDTO> discountDishes;
}