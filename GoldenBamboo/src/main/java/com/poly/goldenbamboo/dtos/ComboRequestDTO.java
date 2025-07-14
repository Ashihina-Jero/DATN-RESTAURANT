package com.poly.goldenbamboo.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List; // Để nhận list các món ăn sẽ có trong combo

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComboRequestDTO {
    // Không cần id khi tạo mới hoặc cập nhật

    // Các trường kế thừa từ MenuItemEntity
    @NotBlank(message = "Tên combo không được để trống")
    @Size(max = 255, message = "Tên combo không được vượt quá 255 ký tự")
    private String name;

    @Size(max = 1000, message = "Mô tả combo không được vượt quá 1000 ký tự")
    private String description;

    private String image;

    @NotNull(message = "Giá combo không được để trống")
    @PositiveOrZero(message = "Giá combo phải là số dương hoặc 0")
    private double price;

    private boolean status;

    // Khi tạo/cập nhật combo, có thể muốn thêm/bớt các món ăn ngay lập tức
    // Sử dụng ComboDishRequestDTO hoặc chỉ là danh sách các dishId
    // Tôi sẽ dùng List<Integer> dishIds để đơn giản hóa việc gửi dữ liệu từ client
    // và xử lý tạo ComboDishEntity trong Service.
    private List<Integer> dishIds; // Danh sách các ID món ăn trong combo
}