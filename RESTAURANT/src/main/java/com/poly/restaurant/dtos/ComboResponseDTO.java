package com.poly.restaurant.dtos;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComboResponseDTO {
    // Các trường kế thừa từ MenuItemEntity
    private int id;
    private String name;
    private String description;
    private String image;
    private BigDecimal price;
    private boolean status;

    // Các trường riêng của Combo (nếu có, ở đây không có thêm trường trực tiếp nào)

    // Để hiển thị các món ăn trong combo (có thể là danh sách ID hoặc DTO đơn giản)
    private List<ComboDishResponseDTO> comboDishes; // Sẽ map từ ComboDishEntity sang ComboDishResponseDTO

    // Bỏ qua List<MenuComboEntity> vì thường không cần thiết khi hiển thị Combo
}