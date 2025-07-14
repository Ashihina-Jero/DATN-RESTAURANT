package com.poly.goldenbamboo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuResponseDTO {
    private int id;
    private String name;
    private Boolean isDefault;

    // Chỉ trả về ID và tên của Branch liên kết
    private Integer branchId;
    private String branchName; // Giả định BranchEntity có trường 'name'

    // Bỏ qua List<MenuComboEntity> và List<MenuDishEntity> để giữ DTO gọn
    // và tránh vòng lặp/dữ liệu thừa. Nếu cần, có thể thêm sau và sử dụng các Mapper tương ứng.
    // private List<MenuComboResponseDTO> menuCombos;
    // private List<MenuDishResponseDTO> menuDishes;
}