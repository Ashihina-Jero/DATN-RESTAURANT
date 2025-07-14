package com.poly.goldenbamboo.dtos;

import com.poly.goldenbamboo.entities.enums.TableStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableRequestDTO {

    // Không cần id khi tạo mới

    @NotBlank(message = "Số bàn không được để trống")
    @Size(max = 10, message = "Số bàn không được vượt quá 10 ký tự")
    private String number;

    @NotNull(message = "Trạng thái bàn không được để trống")
    private TableStatus status;

    // Nhận ID của Branch từ client để thiết lập mối quan hệ
    @NotNull(message = "ID Chi nhánh không được để trống")
    private Integer branchId;
}