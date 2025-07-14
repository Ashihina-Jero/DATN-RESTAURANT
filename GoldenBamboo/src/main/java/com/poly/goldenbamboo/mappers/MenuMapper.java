package com.poly.goldenbamboo.mappers;

import com.poly.goldenbamboo.dtos.MenuRequestDTO;
import com.poly.goldenbamboo.dtos.MenuResponseDTO;
import com.poly.goldenbamboo.entities.MenuEntity;
import com.poly.goldenbamboo.entities.BranchEntity; // Cần import BranchEntity
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {BranchMapper.class, MenuComboMapper.class, MenuDishMapper.class})
public interface MenuMapper {

    // 1. Ánh xạ từ Entity sang MenuResponseDTO
    @Mapping(source = "branch.id", target = "branchId")
    @Mapping(source = "branch.name", target = "branchName") // Giả định BranchEntity có trường 'name'
    @Mapping(target = "menuCombos", ignore = true) // Bỏ qua List<MenuComboEntity>
    @Mapping(target = "menuDishes", ignore = true) // Bỏ qua List<MenuDishEntity>
    MenuResponseDTO toResponseDTO(MenuEntity entity);

    // 2. Ánh xạ từ MenuRequestDTO sang Entity (khi tạo mới)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "branch", ignore = true)     // Sẽ thiết lập trong Service dựa vào branchId
    @Mapping(target = "menuCombos", ignore = true) // Không tạo/cập nhật mối quan hệ này từ RequestDTO
    @Mapping(target = "menuDishes", ignore = true) // Không tạo/cập nhật mối quan hệ này từ RequestDTO
    MenuEntity toEntity(MenuRequestDTO dto);

    // 3. Cập nhật Entity từ MenuRequestDTO
    @Mapping(target = "id", ignore = true) // Luôn bỏ qua id khi cập nhật từ DTO
    @Mapping(target = "branch", ignore = true)     // Mối quan hệ ManyToOne xử lý riêng
    @Mapping(target = "menuCombos", ignore = true) // Không cập nhật mối quan hệ này qua DTO
    @Mapping(target = "menuDishes", ignore = true) // Không cập nhật mối quan hệ này qua DTO
    void updateEntityFromDTO(MenuRequestDTO dto, @MappingTarget MenuEntity entity);
}