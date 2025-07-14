package com.poly.goldenbamboo.mappers;

import com.poly.goldenbamboo.dtos.ComboRequestDTO;
import com.poly.goldenbamboo.dtos.ComboResponseDTO;
import com.poly.goldenbamboo.entities.ComboEntity;
import com.poly.goldenbamboo.entities.ComboDishEntity; // Cần import để ánh xạ list
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {ComboDishMapper.class}) // uses ComboDishMapper để ánh xạ List<ComboDishEntity>
public interface ComboMapper {

    // 1. Ánh xạ từ Entity sang ComboResponseDTO
    // Các trường kế thừa (id, name, description, image, price, status) sẽ tự động được ánh xạ
    @Mapping(target = "menuCombos", ignore = true) // Bỏ qua mối quan hệ MenuCombo
    // Map list của ComboDishEntity sang List của ComboDishResponseDTO
    @Mapping(source = "comboDishes", target = "comboDishes")
    ComboResponseDTO toResponseDTO(ComboEntity entity);

    // 2. Ánh xạ từ ComboRequestDTO sang Entity (khi tạo mới)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createAt", ignore = true) // Giả định MenuItemEntity có createAt/updateAt
    @Mapping(target = "updateAt", ignore = true) // nếu không có thì bỏ qua dòng này
    @Mapping(target = "comboDishes", ignore = true) // Sẽ xử lý trong Service dựa vào dishIds
    @Mapping(target = "menuCombos", ignore = true) // Không tạo mối quan hệ này từ RequestDTO
    @Mapping(target = "dishIds", ignore = true) // DTO có dishIds nhưng Entity không có, nên ignore
    ComboEntity toEntity(ComboRequestDTO dto);

    // 3. Cập nhật Entity từ ComboRequestDTO
    @Mapping(target = "id", ignore = true) // Không cho phép cập nhật ID
    @Mapping(target = "createAt", ignore = true) // Không cập nhật thời gian tạo
    @Mapping(target = "updateAt", ignore = true) // Có thể cập nhật tự động bằng @UpdateTimestamp
    @Mapping(target = "comboDishes", ignore = true) // Sẽ xử lý trong Service dựa vào dishIds
    @Mapping(target = "menuCombos", ignore = true) // Không cập nhật mối quan hệ này qua DTO
    @Mapping(target = "dishIds", ignore = true) // DTO có dishIds nhưng Entity không có, nên ignore
    void updateEntityFromDTO(ComboRequestDTO dto, @MappingTarget ComboEntity entity);
}