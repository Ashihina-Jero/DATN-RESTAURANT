package com.poly.restaurant.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailRequestDTO {

    // Không cần id khi tạo mới

    @NotNull(message = "ID của món/combo không được để trống")
    private Integer menuItemId; // ID của MenuItem (Combo hoặc Dish)

    @Min(value = 1, message = "Số lượng phải ít nhất là 1")
    private int quantity;

    @NotNull(message = "Giá không được để trống")
    private BigDecimal price; // Giá tại thời điểm đặt hàng (thường được backend tự điền)

    @NotBlank(message = "Tên không được để trống")
    private String name; // Tên tại thời điểm đặt hàng (thường được backend tự điền)

    private Integer discountPercentage; // % giảm giá tại thời điểm đặt hàng (thường được backend tự điền, có thể null)

    // Không cần orderId ở đây, vì OrderDetail sẽ được tạo trong ngữ cảnh của một Order cụ thể
    // OrderId sẽ được thiết lập trong Service khi thêm vào một Order.
}