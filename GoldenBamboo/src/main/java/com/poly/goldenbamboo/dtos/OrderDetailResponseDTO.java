package com.poly.goldenbamboo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailResponseDTO {
    private Integer id;
    private int quantity;
    private BigDecimal price;
    private String name; // Tên món/combo tại thời điểm đặt hàng
    private Integer discountPercentage;

    // Thay vì itemId và isCombo, chúng ta sẽ có menuItemId và menuItemName
    private Integer menuItemId;
    private String menuItemName; // Tên MenuItem tại thời điểm đặt hàng (có thể khác name nếu tên đổi)

    // Chỉ trả về ID của Order liên kết, không phải toàn bộ OrderEntity
    private Integer orderId;
}