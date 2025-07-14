package com.poly.goldenbamboo.mappers;

import com.poly.goldenbamboo.dtos.RoleResponseDTO;
import com.poly.goldenbamboo.entities.RoleEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    // Ánh xạ từ Entity sang RoleResponseDTO
    RoleResponseDTO toResponseDTO(RoleEntity entity);

    // Không cần toEntity(RoleRequestDTO dto)
    // Không cần updateEntityFromDTO(RoleRequestDTO dto, @MappingTarget RoleEntity entity)
}