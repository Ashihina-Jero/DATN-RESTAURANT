package com.poly.goldenbamboo.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp; // Nếu client gửi timestamp
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequestDTO {

    // Không cần id và setDateAndTime khi tạo mới (setDateAndTime sẽ được tạo tự động)

    // Nếu bạn muốn client gửi thời gian đặt bàn, uncomment dòng này:
    // @NotNull(message = "Thời gian đặt bàn không được để trống")
    // private Timestamp setDateAndTime;

    // Nhận ID của Account
    @NotNull(message = "ID Tài khoản không được để trống")
    private Integer accountId;

    // Nhận ID của Table
    @NotNull(message = "ID Bàn không được để trống")
    private Integer tableId;

    // Danh sách các chi tiết đặt bàn
    private List<ReservationDetailRequestDTO> reservationDetails;
}