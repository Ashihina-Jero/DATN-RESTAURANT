package com.poly.goldenbamboo.mappers;

import com.poly.goldenbamboo.dtos.MenuComboRequestDTO;
import com.poly.goldenbamboo.dtos.MenuComboResponseDTO;
import com.poly.goldenbamboo.entities.MenuComboEntity;
import com.poly.goldenbamboo.entities.ComboEntity; // Cần import ComboEntity
import com.poly.goldenbamboo.entities.MenuEntity;   // Cần import MenuEntity
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {ComboMapper.class, MenuMapper.class, DiscountComboMapper.class})
public interface MenuComboMapper {

    // 1. Ánh xạ từ Entity sang MenuComboResponseDTO
    @Mapping(source = "combo.id", target = "comboId")
    @Mapping(source = "combo.name", target = "comboName") // Giả định ComboEntity có trường 'name'
    @Mapping(source = "menu.id", target = "menuId")
    @Mapping(source = "menu.name", target = "menuName")   // Giả định MenuEntity có trường 'name'
    @Mapping(target = "discountCombos", ignore = true) // Bỏ qua List<DiscountComboEntity>
    MenuComboResponseDTO toResponseDTO(MenuComboEntity entity);

    // 2. Ánh xạ từ MenuComboRequestDTO sang Entity (khi tạo mới)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "combo", ignore = true)        // Sẽ thiết lập trong Service dựa vào comboId
    @Mapping(target = "menu", ignore = true)         // Sẽ thiết lập trong Service dựa vào menuId
    @Mapping(target = "discountCombos", ignore = true) // Không tạo/cập nhật mối quan hệ này từ RequestDTO
    MenuComboEntity toEntity(MenuComboRequestDTO dto);

    // 3. Cập nhật Entity từ MenuComboRequestDTO
    @Mapping(target = "id", ignore = true) // Luôn bỏ qua id khi cập nhật từ DTO
    @Mapping(target = "combo", ignore = true)        // Mối quan hệ ManyToOne xử lý riêng
    @Mapping(target = "menu", ignore = true)         // Mối quan hệ ManyToOne xử lý riêng
    @Mapping(target = "discountCombos", ignore = true) // Không cập nhật mối quan hệ này qua DTO
    void updateEntityFromDTO(MenuComboRequestDTO dto, @MappingTarget MenuComboEntity entity);
}