package com.poly.goldenbamboo.mappers;

import com.poly.goldenbamboo.dtos.DishRequestDTO;
import com.poly.goldenbamboo.dtos.DishResponseDTO;
import com.poly.goldenbamboo.entities.DishEntity;
import com.poly.goldenbamboo.entities.CategoryEntity; // Cần import CategoryEntity
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class, ComboDishMapper.class, MenuDishMapper.class, DiscountDishMapper.class})
public interface DishMapper {

    // 1. Ánh xạ từ Entity sang DishResponseDTO
    // Các trường kế thừa từ MenuItemEntity sẽ tự động được ánh xạ
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName") // Giả định CategoryEntity có trường 'name'
    @Mapping(target = "comboDishes", ignore = true)     // Bỏ qua List<ComboDishEntity>
    @Mapping(target = "menuDishes", ignore = true)      // Bỏ qua List<MenuDishEntity>
    @Mapping(target = "discountDishes", ignore = true) // Bỏ qua List<DiscountDishEntity>
    DishResponseDTO toResponseDTO(DishEntity entity);

    // 2. Ánh xạ từ DishRequestDTO sang Entity (khi tạo mới)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)       // Sẽ thiết lập trong Service dựa vào categoryId
    @Mapping(target = "comboDishes", ignore = true)    // Không tạo/cập nhật mối quan hệ này từ RequestDTO
    @Mapping(target = "menuDishes", ignore = true)     // Không tạo/cập nhật mối quan hệ này từ RequestDTO
    @Mapping(target = "discountDishes", ignore = true) // Không tạo/cập nhật mối quan hệ này từ RequestDTO
    DishEntity toEntity(DishRequestDTO dto);

    // 3. Cập nhật Entity từ DishRequestDTO
    @Mapping(target = "id", ignore = true) // Luôn bỏ qua id khi cập nhật từ DTO
    @Mapping(target = "category", ignore = true)       // Mối quan hệ ManyToOne xử lý riêng
    @Mapping(target = "comboDishes", ignore = true)    // Không cập nhật mối quan hệ này qua DTO
    @Mapping(target = "menuDishes", ignore = true)     // Không cập nhật mối quan hệ này qua DTO
    @Mapping(target = "discountDishes", ignore = true) // Không cập nhật mối quan hệ này qua DTO
    void updateEntityFromDTO(DishRequestDTO dto, @MappingTarget DishEntity entity);
}