package com.poly.restaurant.mappers;

import com.poly.restaurant.dtos.DiscountDishRequestDTO;
import com.poly.restaurant.dtos.DiscountDishResponseDTO;
import com.poly.restaurant.entities.DiscountDishEntity;
import com.poly.restaurant.entities.DiscountEntity; // Cần import
import com.poly.restaurant.entities.DishEntity;   // Giả định là DishEntity
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {DiscountMapper.class, DishMapper.class}) // uses các mapper liên quan
public interface DiscountDishMapper {

    // 1. Ánh xạ từ Entity sang DiscountDishResponseDTO
    @Mapping(source = "discount.id", target = "discountId")
    @Mapping(source = "discount.name", target = "discountName") // Giả định DiscountEntity có trường 'name'
    @Mapping(source = "dish.id", target = "dishId")
    @Mapping(source = "dish.name", target = "dishName")   // Giả định DishEntity có trường 'name'
    DiscountDishResponseDTO toResponseDTO(DiscountDishEntity entity);

    // 2. Ánh xạ từ DiscountDishRequestDTO sang Entity (khi tạo mới)
    // Các trường discount và dish cần được xử lý riêng trong Service
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "discount", ignore = true) // Sẽ thiết lập trong Service dựa vào discountId
    @Mapping(target = "dish", ignore = true)     // Sẽ thiết lập trong Service dựa vào dishId
    DiscountDishEntity toEntity(DiscountDishRequestDTO dto);

    // 3. Cập nhật Entity từ DiscountDishRequestDTO
    @Mapping(target = "id", ignore = true) // Luôn bỏ qua id khi cập nhật từ DTO
    @Mapping(target = "discount", ignore = true) // Mối quan hệ ManyToOne xử lý riêng
    @Mapping(target = "dish", ignore = true)     // Mối quan hệ ManyToOne xử lý riêng
    void updateEntityFromDTO(DiscountDishRequestDTO dto, @MappingTarget DiscountDishEntity entity);
}