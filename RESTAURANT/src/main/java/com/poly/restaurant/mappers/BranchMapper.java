package com.poly.restaurant.mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.poly.restaurant.dtos.BranchRequestDTO;
import com.poly.restaurant.dtos.BranchResponseDTO;
import com.poly.restaurant.entities.BranchEntity;

@Mapper(componentModel = "spring")
public interface BranchMapper {
    
    BranchResponseDTO toBranchResponseDTO(BranchEntity branch);
    
    BranchEntity toBranch(BranchRequestDTO requestDTO);

    /**
     * Dùng để cập nhật, nó sẽ ghi đè các trường từ DTO vào Entity đã có.
     * Các trường không có trong DTO sẽ được bỏ qua (ignore = true).
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "accounts", ignore = true)
    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "reservations", ignore = true)
    @Mapping(target = "tables", ignore = true)
    @Mapping(target = "menus", ignore = true)
    void updateBranchFromDto(BranchRequestDTO dto, @MappingTarget BranchEntity entity);
}