package com.poly.goldenbamboo.mappers;

import com.poly.goldenbamboo.dtos.ReservationDetailRequestDTO;
import com.poly.goldenbamboo.dtos.ReservationDetailResponseDTO;
import com.poly.goldenbamboo.entities.ReservationDetailEntity;
import com.poly.goldenbamboo.entities.MenuItemEntity; // Cần import MenuItemEntity
import com.poly.goldenbamboo.entities.ReservationEntity; // Cần import ReservationEntity
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {MenuItemMapper.class, ReservationMapper.class}) // uses các mapper liên quan
public interface ReservationDetailMapper {

    // 1. Ánh xạ từ Entity sang ReservationDetailResponseDTO
    @Mapping(source = "menuItem.id", target = "menuItemId")
    @Mapping(source = "menuItem.name", target = "menuItemName") // Giả định MenuItemEntity có trường 'name'
    // Không cần mapping reservationId nếu DTO này nằm trong ReservationResponseDTO
    // @Mapping(source = "reservation.id", target = "reservationId")
    ReservationDetailResponseDTO toResponseDTO(ReservationDetailEntity entity);

    // 2. Ánh xạ từ ReservationDetailRequestDTO sang Entity (khi tạo mới)
    // Các trường menuItem và reservation cần được xử lý riêng trong Service
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "menuItem", ignore = true)    // Sẽ thiết lập trong Service dựa vào menuItemId
    @Mapping(target = "reservation", ignore = true) // Sẽ thiết lập trong Service
    ReservationDetailEntity toEntity(ReservationDetailRequestDTO dto);

    // 3. Cập nhật Entity từ ReservationDetailRequestDTO
    @Mapping(target = "id", ignore = true) // Luôn bỏ qua id khi cập nhật từ DTO
    @Mapping(target = "menuItem", ignore = true)    // Mối quan hệ ManyToOne xử lý riêng
    @Mapping(target = "reservation", ignore = true) // Mối quan hệ ManyToOne xử lý riêng
    void updateEntityFromDTO(ReservationDetailRequestDTO dto, @MappingTarget ReservationDetailEntity entity);
}