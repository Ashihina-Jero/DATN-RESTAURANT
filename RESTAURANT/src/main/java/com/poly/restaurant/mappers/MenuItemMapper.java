package com.poly.restaurant.mappers;

import com.poly.restaurant.dtos.MenuItemResponseDTO;
import com.poly.restaurant.entities.MenuItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MenuItemMapper {

    // Phương thức ánh xạ từ MenuItemEntity sang MenuItemResponseDTO
    // MapStruct sẽ tự động ánh xạ các trường có cùng tên và kiểu
    MenuItemResponseDTO toResponseDTO(MenuItemEntity entity);

    // Lưu ý:
    // - Không có toEntity(MenuItemRequestDTO dto) vì MenuItemEntity là abstract và không tạo trực tiếp
    // - Không có updateEntityFromDTO(MenuItemRequestDTO dto, @MappingTarget MenuItemEntity entity)
    //   vì MenuItemEntity là abstract và việc cập nhật được xử lý qua các lớp con.
    // - Mapper này chủ yếu hữu ích cho việc ánh xạ đọc (read-only) các trường chung.
}