package com.poly.goldenbamboo.mappers;

import com.poly.goldenbamboo.dtos.DiscountRequestDTO;
import com.poly.goldenbamboo.dtos.DiscountResponseDTO;
import com.poly.goldenbamboo.entities.DiscountEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring") // Đảm bảo MapStruct tạo ra Spring Component
public interface DiscountMapper {

    // 1. Ánh xạ từ Entity sang DiscountResponseDTO
    // Các trường có cùng tên và kiểu sẽ tự động được ánh xạ.
    // Các trường List trong Entity sẽ bị bỏ qua vì không có trong DTO
    DiscountResponseDTO toResponseDTO(DiscountEntity entity);

    // 2. Ánh xạ từ DiscountRequestDTO sang Entity (khi tạo mới)
    // id, và các List<...> sẽ tự động bị bỏ qua
    DiscountEntity toEntity(DiscountRequestDTO dto);

    // 3. Cập nhật Entity từ DiscountRequestDTO
    @Mapping(target = "id", ignore = true) // Luôn bỏ qua id khi cập nhật từ DTO
    @Mapping(target = "discountCombos", ignore = true) // Không cập nhật các mối quan hệ này qua DTO
    @Mapping(target = "discountDishes", ignore = true) // Không cập nhật các mối quan hệ này qua DTO
    void updateEntityFromDTO(DiscountRequestDTO dto, @MappingTarget DiscountEntity entity);
}