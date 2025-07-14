package com.poly.goldenbamboo.dtos;

import com.poly.goldenbamboo.entities.enums.MenuItemStatus; // Kế thừa từ MenuItemEntity
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
// Không cần List<> imports nếu không bao gồm các List quan hệ
// import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DishResponseDTO {
    // Các trường kế thừa từ MenuItemEntity
    private Integer id;
    private String name;
    private String description;
    private String image;
    private BigDecimal price;
    private MenuItemStatus status; // Sử dụng enum của MenuItemEntity

    // Chỉ trả về ID và tên của Category liên kết
    private Integer categoryId;
    private String categoryName; // Giả định CategoryEntity có trường 'name'

    // Bỏ qua List<ComboDishEntity> và List<MenuDishEntity> để giữ DTO gọn
    // và tránh vòng lặp/dữ liệu thừa. Nếu cần, có thể thêm sau và sử dụng các Mapper tương ứng.
    // private List<ComboDishResponseDTO> comboDishes;
    // private List<MenuDishResponseDTO> menuDishes;
}