package com.poly.restaurant.dtos;

import com.poly.restaurant.entities.enums.ReservationStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationResponseDTO {

    private Long id;
    private LocalDateTime reservationTime;
    private int numberOfGuests;
    private String notes;
    private ReservationStatus status;
    private LocalDateTime createAt;

    // Trả về thông tin cơ bản, tránh trả về cả object lồng nhau phức tạp
    private String customerName;
    private String tableName;
    private String branchName;
}