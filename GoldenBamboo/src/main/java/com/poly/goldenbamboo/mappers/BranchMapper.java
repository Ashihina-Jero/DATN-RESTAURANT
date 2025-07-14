package com.poly.goldenbamboo.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.poly.goldenbamboo.dtos.BranchRequestDTO;
import com.poly.goldenbamboo.dtos.BranchResponseDTO;
import com.poly.goldenbamboo.entities.BranchEntity;


@Mapper(componentModel = "spring") // Đảm bảo MapStruct tạo ra Spring Component để có thể @Autowired
public interface BranchMapper {

    // 1. Ánh xạ từ Entity sang BranchResponseDTO
    // Các trường có cùng tên và kiểu sẽ tự động được ánh xạ.
    // Các trường List trong Entity sẽ bị bỏ qua vì không có trong DTO
    BranchResponseDTO toResponseDTO(BranchEntity entity);

    // 2. Ánh xạ từ BranchRequestDTO sang Entity (khi tạo mới)
    // id sẽ không được ánh xạ vì không có trong RequestDTO, điều này là đúng khi tạo mới.
    BranchEntity toEntity(BranchRequestDTO dto);

    // 3. Cập nhật Entity từ BranchRequestDTO
    // Phương thức này sẽ copy các thuộc tính từ DTO vào một Entity đã tồn tại
    // Điều này hữu ích khi cập nhật một đối tượng mà không muốn tạo mới hoàn toàn
    @Mapping(target = "id", ignore = true) // Luôn bỏ qua id khi cập nhật từ DTO để tránh ghi đè ID
    @Mapping(target = "accounts", ignore = true) // Bỏ qua các mối quan hệ liên kết
    @Mapping(target = "menus", ignore = true)
    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "tables", ignore = true)
    void updateEntityFromDTO(BranchRequestDTO dto, @MappingTarget BranchEntity entity);
}