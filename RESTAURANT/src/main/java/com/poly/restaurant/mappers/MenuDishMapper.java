package com.poly.restaurant.mappers;

import com.poly.restaurant.dtos.MenuDishResponseDTO;
import com.poly.restaurant.entities.BranchDishEntity;
import com.poly.restaurant.entities.MenuDishEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.math.BigDecimal;
import java.util.Optional;

@Mapper(componentModel = "spring")
public interface MenuDishMapper {
    @Mapping(source = "dish.id", target = "dishId")
    @Mapping(source = "dish.name", target = "dishName")
    @Mapping(source = ".", target = "price") // Dùng "." để truyền cả object
    MenuDishResponseDTO toResponseDTO(MenuDishEntity menuDishEntity);
    
    /**
     * Hàm này sẽ chứa logic quyết định giá cuối cùng của món ăn.
     */
    default BigDecimal getFinalPrice(MenuDishEntity menuDish) {
        if (menuDish == null) return null;

        // Ưu tiên 1: Giá ghi đè trên menu (nếu có)
        if (menuDish.getPrice() != null) {
            return menuDish.getPrice();
        }

        // Ưu tiên 2: Giá tại chi nhánh (Code đã sửa)
        if (menuDish.getDish() != null && menuDish.getMenu().getBranch() != null) {
            return menuDish.getMenu().getBranch().getBranchDishes()
                    .stream()
                    .filter(bd -> bd.getDish().getId().equals(menuDish.getDish().getId()))
                    .findFirst()
                    .map(BranchDishEntity::getPrice) // Lấy giá nếu tìm thấy
                    .orElse(null); // Trả về null nếu không tìm thấy
        }
        
        // Trả về null nếu không tìm thấy giá nào
        return null;
    }
}