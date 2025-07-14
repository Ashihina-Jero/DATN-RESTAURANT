package com.poly.restaurant.mappers;

import com.poly.restaurant.dtos.ComboRequestDTO;
import com.poly.restaurant.dtos.ComboResponseDTO;
import com.poly.restaurant.entities.ComboEntity;
import com.poly.restaurant.entities.enums.MenuItemStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {ComboDishMapper.class})
public interface ComboMapper {

    ComboResponseDTO toResponseDTO(ComboEntity entity);

    // MapStruct sẽ tự động dùng phương thức mapBooleanToStatus() bên dưới
    // để chuyển đổi trường 'status' từ boolean sang Enum.
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "comboDishes", ignore = true)
    ComboEntity toEntity(ComboRequestDTO dto);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "comboDishes", ignore = true)
    void updateEntityFromDTO(ComboRequestDTO dto, @MappingTarget ComboEntity entity);

    /**
     * Dạy MapStruct cách chuyển từ Enum MenuItemStatus -> boolean
     */
    default boolean mapStatus(MenuItemStatus status) {
        return status == MenuItemStatus.ACTIVE;
    }

    /**
     * Dạy MapStruct cách chuyển từ boolean -> Enum MenuItemStatus
     */
    default MenuItemStatus mapBooleanToStatus(boolean status) {
        return status ? MenuItemStatus.ACTIVE : MenuItemStatus.INACTIVE;
    }
}