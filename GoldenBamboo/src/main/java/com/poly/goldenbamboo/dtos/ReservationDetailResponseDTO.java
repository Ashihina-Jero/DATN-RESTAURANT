// ReservationDetailResponseDTO.java (Child of Reservation)
package com.poly.goldenbamboo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDetailResponseDTO {
    private int id;
    private int quantity;

    // ID và tên của MenuItem (Combo hoặc Dish)
    private Integer menuItemId;
    private String menuItemName; // Giả định MenuItemEntity có trường 'name'

    // Không cần reservationId ở đây vì DTO này sẽ nằm trong ReservationResponseDTO
    // Hoặc nếu là DTO độc lập, có thể thêm Integer reservationId;
}