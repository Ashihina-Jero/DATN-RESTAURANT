package com.poly.restaurant.mappers;

import java.math.BigDecimal;
import java.util.Optional;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.poly.restaurant.dtos.MenuComboRequestDTO;
import com.poly.restaurant.dtos.MenuComboResponseDTO;
import com.poly.restaurant.entities.BranchComboEntity;
import com.poly.restaurant.entities.MenuComboEntity;

@Mapper(componentModel = "spring")
public interface MenuComboMapper {

    // 1. Ánh xạ từ Entity sang MenuComboResponseDTO
    @Mapping(source = "combo.id", target = "comboId")
    @Mapping(source = "combo.name", target = "comboName")
    @Mapping(source = "menu.id", target = "menuId")
    @Mapping(source = "menu.name", target = "menuName")
    @Mapping(source = ".", target = "price")
    MenuComboResponseDTO toResponseDTO(MenuComboEntity entity);

    // 2. Ánh xạ từ MenuComboRequestDTO sang Entity (khi tạo mới)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "combo", ignore = true)
    @Mapping(target = "menu", ignore = true)
    MenuComboEntity toEntity(MenuComboRequestDTO dto);

    // 3. Cập nhật Entity từ MenuComboRequestDTO
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "combo", ignore = true)
    @Mapping(target = "menu", ignore = true)
    void updateEntityFromDTO(MenuComboRequestDTO dto, @MappingTarget MenuComboEntity entity);
    
    /**
     * Hàm này sẽ chứa logic quyết định giá cuối cùng của combo.
     */
    default BigDecimal getFinalPrice(MenuComboEntity menuCombo) {
        if (menuCombo == null) return null;

        // Ưu tiên 1: Giá ghi đè trên menu (nếu có)
        if (menuCombo.getPrice() != null) {
            return menuCombo.getPrice();
        }

        // Ưu tiên 2: Giá tại chi nhánh
        if (menuCombo.getCombo() != null && menuCombo.getMenu().getBranch() != null) {
            Optional<BranchComboEntity> branchCombo = menuCombo.getMenu().getBranch().getBranchCombos()
                    .stream()
                    .filter(bc -> bc.getCombo().getId().equals(menuCombo.getCombo().getId()))
                    .findFirst();

            if (branchCombo.isPresent()) {
                return branchCombo.get().getPrice();
            }
        }

        return null;
    }
}