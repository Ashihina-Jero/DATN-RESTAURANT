package com.poly.restaurant.mappers;

import com.poly.restaurant.dtos.ReservationResponseDTO;
import com.poly.restaurant.entities.ReservationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

    @Mapping(source = "account.name", target = "customerName")
    @Mapping(source = "table.name", target = "tableName")
    @Mapping(source = "branch.name", target = "branchName")
    ReservationResponseDTO toResponseDTO(ReservationEntity reservationEntity);
}