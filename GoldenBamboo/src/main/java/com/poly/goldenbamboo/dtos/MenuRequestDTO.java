package com.poly.goldenbamboo.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuRequestDTO {

    // Không cần id khi tạo mới

    @NotBlank(message = "Tên menu không được để trống")
    @Size(max = 100, message = "Tên menu không được vượt quá 100 ký tự")
    private String name;

    @NotNull(message = "Trạng thái mặc định không được để trống")
    private Boolean isDefault;

    // Nhận ID của branch từ client để thiết lập mối quan hệ
    private Integer branchId; // Có thể null
}