package com.poly.restaurant.mappers;

import com.poly.restaurant.dtos.TableRequestDTO;
import com.poly.restaurant.dtos.TableResponseDTO;
import com.poly.restaurant.entities.TableEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TableMapper {

    /**
     * Chuyển đổi từ Entity sang DTO.
     * Thêm logic để đếm số lượng đơn hàng và đặt chỗ.
     */
    @Mapping(source = "branch.id", target = "branchId")
    @Mapping(source = "branch.name", target = "branchName")
    TableResponseDTO toResponseDTO(TableEntity entity);

    /**
     * Chuyển đổi từ DTO sang Entity để tạo mới.
     * Bỏ qua các thuộc tính sẽ được Service xử lý.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "branch", ignore = true)
    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "reservations", ignore = true)
    TableEntity toEntity(TableRequestDTO dto);

    /**
     * Cập nhật một Entity đã tồn tại từ DTO.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "branch", ignore = true)
    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "reservations", ignore = true)
    void updateEntityFromDTO(TableRequestDTO dto, @MappingTarget TableEntity entity);
}