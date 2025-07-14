package com.poly.restaurant.dtos;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationRequestDTO {

    @NotNull(message = "Chi nhánh không được để trống")
    private Integer branchId;

    @NotNull(message = "Bàn không được để trống")
    private Integer tableId;

    @NotNull(message = "Thời gian đặt không được để trống")
    @Future(message = "Thời gian đặt phải ở trong tương lai")
    private LocalDateTime reservationTime;

    @NotNull(message = "Số lượng khách không được để trống")
    @Min(value = 1, message = "Số lượng khách phải lớn hơn 0")
    private int numberOfGuests;

    private String notes;
}