package com.poly.restaurant.dtos;

import com.poly.restaurant.entities.enums.MenuItemStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DishRequestDTO {
    // Không cần id khi tạo mới hoặc cập nhật

    // Các trường kế thừa từ MenuItemEntity (bao gồm cả validations)
    @NotBlank(message = "Tên món ăn không được để trống")
    @Size(max = 100, message = "Tên món ăn không được vượt quá 100 ký tự")
    private String name;

    @Size(max = 1000, message = "Mô tả món ăn không được vượt quá 1000 ký tự")
    private String description;

    private String image;

    @NotNull(message = "Trạng thái món ăn không được để trống")
    private MenuItemStatus status; // Sử dụng enum của MenuItemEntity

    // Nhận ID của Category từ client để thiết lập mối quan hệ
    @NotNull(message = "ID Danh mục không được để trống")
    private Integer categoryId;
}