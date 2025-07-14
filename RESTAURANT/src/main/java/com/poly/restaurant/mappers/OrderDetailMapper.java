package com.poly.restaurant.mappers;

import com.poly.restaurant.dtos.OrderDetailRequestDTO;
import com.poly.restaurant.dtos.OrderDetailResponseDTO;
import com.poly.restaurant.entities.OrderDetailEntity;
import com.poly.restaurant.entities.MenuItemEntity; // Cần import MenuItemEntity
import com.poly.restaurant.entities.OrderEntity; // Cần import OrderEntity
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {MenuItemMapper.class, OrderMapper.class}) // uses MenuItemMapper (nếu có) và OrderMapper
public interface OrderDetailMapper {

    // 1. Ánh xạ từ Entity sang OrderDetailResponseDTO
    @Mapping(source = "menuItem.id", target = "menuItemId")
    @Mapping(source = "menuItem.name", target = "menuItemName") // Giả định MenuItemEntity có trường 'name'
    @Mapping(source = "order.id", target = "orderId")
    OrderDetailResponseDTO toResponseDTO(OrderDetailEntity entity);

    // 2. Ánh xạ từ OrderDetailRequestDTO sang Entity (khi tạo mới)
    // Các trường menuItem và order cần được xử lý riêng trong Service
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "menuItem", ignore = true) // Sẽ thiết lập trong Service dựa vào menuItemId
    @Mapping(target = "order", ignore = true)    // Sẽ thiết lập trong Service
    OrderDetailEntity toEntity(OrderDetailRequestDTO dto);

    // 3. Cập nhật Entity từ OrderDetailRequestDTO
    @Mapping(target = "id", ignore = true) // Luôn bỏ qua id khi cập nhật từ DTO
    @Mapping(target = "menuItem", ignore = true) // Mối quan hệ ManyToOne xử lý riêng
    @Mapping(target = "order", ignore = true)    // Mối quan hệ ManyToOne xử lý riêng
    void updateEntityFromDTO(OrderDetailRequestDTO dto, @MappingTarget OrderDetailEntity entity);
}