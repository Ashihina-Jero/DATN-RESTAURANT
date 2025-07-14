package com.poly.goldenbamboo.mappers;

import com.poly.goldenbamboo.dtos.TableRequestDTO;
import com.poly.goldenbamboo.dtos.TableResponseDTO;
import com.poly.goldenbamboo.entities.TableEntity;
import com.poly.goldenbamboo.entities.BranchEntity; // Cần import BranchEntity
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {BranchMapper.class, OrderMapper.class, ReservationMapper.class})
public interface TableMapper {

    // 1. Ánh xạ từ Entity sang TableResponseDTO
    @Mapping(source = "branch.id", target = "branchId")
    @Mapping(source = "branch.name", target = "branchName") // Giả định BranchEntity có trường 'name'
    @Mapping(target = "orders", ignore = true)        // Bỏ qua List<OrderEntity>
    @Mapping(target = "reservations", ignore = true)  // Bỏ qua List<ReservationEntity>
    TableResponseDTO toResponseDTO(TableEntity entity);

    // 2. Ánh xạ từ TableRequestDTO sang Entity (khi tạo mới)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "branch", ignore = true)        // Sẽ thiết lập trong Service dựa vào branchId
    @Mapping(target = "orders", ignore = true)         // Không tạo/cập nhật mối quan hệ này từ RequestDTO
    @Mapping(target = "reservations", ignore = true)   // Không tạo/cập nhật mối quan hệ này từ RequestDTO
    TableEntity toEntity(TableRequestDTO dto);

    // 3. Cập nhật Entity từ TableRequestDTO
    @Mapping(target = "id", ignore = true) // Luôn bỏ qua id khi cập nhật từ DTO
    @Mapping(target = "branch", ignore = true)        // Mối quan hệ ManyToOne xử lý riêng
    @Mapping(target = "orders", ignore = true)         // Không cập nhật mối quan hệ này qua DTO
    @Mapping(target = "reservations", ignore = true)   // Không cập nhật mối quan hệ này qua DTO
    void updateEntityFromDTO(TableRequestDTO dto, @MappingTarget TableEntity entity);
}