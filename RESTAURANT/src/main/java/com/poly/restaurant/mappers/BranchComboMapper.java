package com.poly.restaurant.mappers;

import com.poly.restaurant.dtos.BranchComboRequestDTO;
import com.poly.restaurant.entities.BranchComboEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BranchComboMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "branch", ignore = true) // Sẽ được set trong service
    @Mapping(target = "combo", ignore = true)  // Sẽ được set trong service
    BranchComboEntity toEntity(BranchComboRequestDTO dto);
}