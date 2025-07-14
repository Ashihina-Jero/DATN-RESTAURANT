package com.poly.restaurant.mappers;

import com.poly.restaurant.dtos.CategoryRequestDTO;
import com.poly.restaurant.dtos.CategoryResponseDTO;
import com.poly.restaurant.entities.CategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring") // Đảm bảo MapStruct tạo ra Spring Component
public interface CategoryMapper {

    // 1. Ánh xạ từ Entity sang CategoryResponseDTO
    CategoryResponseDTO toResponseDTO(CategoryEntity entity);

    // 2. Ánh xạ từ CategoryRequestDTO sang Entity (khi tạo mới)
    // List<DishEntity> sẽ tự động bị bỏ qua
    CategoryEntity toEntity(CategoryRequestDTO dto);

    // 3. Cập nhật Entity từ CategoryRequestDTO
    @Mapping(target = "id", ignore = true) // Luôn bỏ qua id khi cập nhật từ DTO
    @Mapping(target = "dishes", ignore = true) // Không cập nhật danh sách món ăn qua DTO này
    void updateEntityFromDTO(CategoryRequestDTO dto, @MappingTarget CategoryEntity entity);
}