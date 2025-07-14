package com.poly.goldenbamboo.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.poly.goldenbamboo.dtos.MenuDishRequestDTO;
import com.poly.goldenbamboo.dtos.MenuDishResponseDTO;
import com.poly.goldenbamboo.entities.MenuDishEntity;

@Mapper(componentModel = "spring", uses = {DishMapper.class, MenuMapper.class, DiscountDishMapper.class})
public interface MenuDishMapper {

    // 1. Ánh xạ từ Entity sang MenuDishResponseDTO
    @Mapping(source = "dish.id", target = "dishId")
    @Mapping(source = "dish.name", target = "dishName") // Giả định DishEntity có trường 'name'
    @Mapping(source = "menu.id", target = "menuId")
    @Mapping(source = "menu.name", target = "menuName")   // Giả định MenuEntity có trường 'name'
    @Mapping(target = "discountDishes", ignore = true) // Bỏ qua List<DiscountDishEntity>
    MenuDishResponseDTO toResponseDTO(MenuDishEntity entity);

    // 2. Ánh xạ từ MenuDishRequestDTO sang Entity (khi tạo mới)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dish", ignore = true)         // Sẽ thiết lập trong Service dựa vào dishId
    @Mapping(target = "menu", ignore = true)         // Sẽ thiết lập trong Service dựa vào menuId
    @Mapping(target = "discountDishes", ignore = true) // Không tạo/cập nhật mối quan hệ này từ RequestDTO
    MenuDishEntity toEntity(MenuDishRequestDTO dto);

    // 3. Cập nhật Entity từ MenuDishRequestDTO
    @Mapping(target = "id", ignore = true) // Luôn bỏ qua id khi cập nhật từ DTO
    @Mapping(target = "dish", ignore = true)         // Mối quan hệ ManyToOne xử lý riêng
    @Mapping(target = "menu", ignore = true)         // Mối quan hệ ManyToOne xử lý riêng
    @Mapping(target = "discountDishes", ignore = true) // Không cập nhật mối quan hệ này qua DTO
    void updateEntityFromDTO(MenuDishRequestDTO dto, @MappingTarget MenuDishEntity entity);
}