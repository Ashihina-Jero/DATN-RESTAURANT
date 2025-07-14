package com.poly.restaurant.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDetailRequestDTO {

    // Không cần id khi tạo mới

    @NotNull(message = "Số lượng không được để trống")
    @Positive(message = "Số lượng phải lớn hơn 0")
    private int quantity;

    @NotNull(message = "ID của món/combo không được để trống")
    private Integer menuItemId; // ID của MenuItem (Combo hoặc Dish)

    // Không cần reservationId ở đây, vì ReservationDetail sẽ được tạo trong ngữ cảnh của một Reservation cụ thể
}