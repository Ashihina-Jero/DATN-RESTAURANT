package com.poly.restaurant.mappers;

import com.poly.restaurant.dtos.DiscountComboRequestDTO;
import com.poly.restaurant.dtos.DiscountComboResponseDTO;
import com.poly.restaurant.entities.DiscountComboEntity;
import com.poly.restaurant.entities.ComboEntity;    // Giả định là ComboEntity
import com.poly.restaurant.entities.DiscountEntity; // Cần import
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {ComboMapper.class, DiscountMapper.class}) // uses các mapper liên quan
public interface DiscountComboMapper {

    // 1. Ánh xạ từ Entity sang DiscountComboResponseDTO
    @Mapping(source = "combo.id", target = "comboId")
    @Mapping(source = "combo.name", target = "comboName") // Giả định ComboEntity có trường 'name'
    @Mapping(source = "discount.id", target = "discountId")
    @Mapping(source = "discount.name", target = "discountName") // Giả định DiscountEntity có trường 'name'
    DiscountComboResponseDTO toResponseDTO(DiscountComboEntity entity);

    // 2. Ánh xạ từ DiscountComboRequestDTO sang Entity (khi tạo mới)
    // Các trường combo và discount cần được xử lý riêng trong Service
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "combo", ignore = true)    // Sẽ thiết lập trong Service dựa vào comboId
    @Mapping(target = "discount", ignore = true) // Sẽ thiết lập trong Service dựa vào discountId
    DiscountComboEntity toEntity(DiscountComboRequestDTO dto);

    // 3. Cập nhật Entity từ DiscountComboRequestDTO
    @Mapping(target = "id", ignore = true) // Luôn bỏ qua id khi cập nhật từ DTO
    @Mapping(target = "combo", ignore = true)    // Mối quan hệ ManyToOne xử lý riêng
    @Mapping(target = "discount", ignore = true) // Mối quan hệ ManyToOne xử lý riêng
    void updateEntityFromDTO(DiscountComboRequestDTO dto, @MappingTarget DiscountComboEntity entity);
}