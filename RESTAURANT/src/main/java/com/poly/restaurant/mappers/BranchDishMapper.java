package com.poly.restaurant.mappers;

import com.poly.restaurant.dtos.BranchDishRequestDTO;
import com.poly.restaurant.entities.BranchDishEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BranchDishMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "branch", ignore = true)
    @Mapping(target = "dish", ignore = true)
    BranchDishEntity toEntity(BranchDishRequestDTO dto);
}