package com.poly.goldenbamboo.mappers;

import com.poly.goldenbamboo.dtos.ComboDishRequestDTO;
import com.poly.goldenbamboo.dtos.ComboDishResponseDTO;
import com.poly.goldenbamboo.entities.ComboDishEntity;
import com.poly.goldenbamboo.entities.ComboEntity; // Cần import
import com.poly.goldenbamboo.entities.DishEntity;   // Cần import
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {ComboMapper.class, DishMapper.class}) // uses các mapper liên quan
public interface ComboDishMapper {

    // 1. Ánh xạ từ Entity sang ComboDishResponseDTO
    @Mapping(source = "combo.id", target = "comboId")
    @Mapping(source = "combo.name", target = "comboName") // Giả định ComboEntity có trường 'name'
    @Mapping(source = "dish.id", target = "dishId")
    @Mapping(source = "dish.name", target = "dishName")   // Giả định DishEntity có trường 'name'
    ComboDishResponseDTO toResponseDTO(ComboDishEntity entity);

    // 2. Ánh xạ từ ComboDishRequestDTO sang Entity (khi tạo mới)
    // Các trường combo và dish cần được xử lý riêng trong Service
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "combo", ignore = true) // Sẽ thiết lập trong Service dựa vào comboId
    @Mapping(target = "dish", ignore = true)   // Sẽ thiết lập trong Service dựa vào dishId
    ComboDishEntity toEntity(ComboDishRequestDTO dto);

    // 3. Cập nhật Entity từ ComboDishRequestDTO (ít phổ biến cho bảng trung gian, nhưng vẫn có thể làm)
    @Mapping(target = "id", ignore = true) // Luôn bỏ qua id khi cập nhật từ DTO
    @Mapping(target = "combo", ignore = true) // Mối quan hệ ManyToOne xử lý riêng
    @Mapping(target = "dish", ignore = true)   // Mối quan hệ ManyToOne xử lý riêng
    void updateEntityFromDTO(ComboDishRequestDTO dto, @MappingTarget ComboDishEntity entity);
}