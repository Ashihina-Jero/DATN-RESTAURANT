package com.poly.restaurant.mappers;

import com.poly.restaurant.dtos.MenuRequestDTO;
import com.poly.restaurant.dtos.MenuResponseDTO;
import com.poly.restaurant.entities.MenuEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

// Thêm MenuDishMapper vào uses
@Mapper(componentModel = "spring", uses = {MenuDishMapper.class})
public interface MenuMapper {

    // Sửa lại: Dùng MenuDishMapper để map danh sách món ăn
    @Mapping(source = "branch.id", target = "branchId")
    @Mapping(source = "branch.name", target = "branchName")
    @Mapping(source = "menuDishes", target = "dishes")
    @Mapping(source = "menuCombos", target = "combos")
    MenuResponseDTO toResponseDTO(MenuEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "branch", ignore = true)
    @Mapping(target = "menuCombos", ignore = true)
    @Mapping(target = "menuDishes", ignore = true)
    MenuEntity toEntity(MenuRequestDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "branch", ignore = true)
    @Mapping(target = "menuCombos", ignore = true)
    @Mapping(target = "menuDishes", ignore = true)
    void updateEntityFromDTO(MenuRequestDTO dto, @MappingTarget MenuEntity entity);
}