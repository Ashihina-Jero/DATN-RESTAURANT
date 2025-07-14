package com.poly.goldenbamboo.dtos;

import com.poly.goldenbamboo.entities.enums.DiscountStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscountRequestDTO {

    // Không cần id khi tạo mới

    @NotBlank(message = "Tên mã giảm giá không được để trống")
    @Size(max = 100, message = "Tên mã giảm giá không được vượt quá 100 ký tự")
    private String name;

    @NotNull(message = "Ngày bắt đầu không được để trống")
    @DateTimeFormat(pattern = "dd/MM/yyyy") // Đảm bảo format phù hợp khi nhận từ request
    private LocalDate startDate;

    @NotNull(message = "Ngày kết thúc không được để trống")
    @DateTimeFormat(pattern = "dd/MM/yyyy") // Đảm bảo format phù hợp khi nhận từ request
    private LocalDate endDate;

    @NotNull(message = "Trạng thái giảm giá không được để trống")
    private DiscountStatus status;
}