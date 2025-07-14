package com.poly.goldenbamboo.mappers;

import com.poly.goldenbamboo.dtos.ReservationRequestDTO;
import com.poly.goldenbamboo.dtos.ReservationResponseDTO;
import com.poly.goldenbamboo.entities.ReservationEntity;
import com.poly.goldenbamboo.entities.AccountEntity; // Cần import
import com.poly.goldenbamboo.entities.TableEntity;  // Cần import
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {AccountMapper.class, TableMapper.class, ReservationDetailMapper.class})
public interface ReservationMapper {

    // 1. Ánh xạ từ Entity sang ReservationResponseDTO
    @Mapping(source = "account.id", target = "accountId")
    @Mapping(source = "account.name", target = "accountName") // Giả định AccountEntity có trường 'name'
    @Mapping(source = "table.id", target = "tableId")
    @Mapping(source = "table.name", target = "tableName")     // Giả định TableEntity có trường 'name'
    @Mapping(source = "reservationDetails", target = "reservationDetails") // Map List<ReservationDetailEntity>
    ReservationResponseDTO toResponseDTO(ReservationEntity entity);

    // 2. Ánh xạ từ ReservationRequestDTO sang Entity (khi tạo mới)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "setDateAndTime", ignore = true) // Tự động tạo bởi @CreationTimestamp
    @Mapping(target = "account", ignore = true)       // Sẽ thiết lập trong Service
    @Mapping(target = "table", ignore = true)         // Sẽ thiết lập trong Service
    @Mapping(target = "reservationDetails", ignore = true) // Sẽ xử lý riêng trong Service
    ReservationEntity toEntity(ReservationRequestDTO dto);

    // 3. Cập nhật Entity từ ReservationRequestDTO
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "setDateAndTime", ignore = true) // Không cập nhật thời gian tạo
    @Mapping(target = "account", ignore = true)       // Mối quan hệ ManyToOne xử lý riêng
    @Mapping(target = "table", ignore = true)         // Mối quan hệ ManyToOne xử lý riêng
    @Mapping(target = "reservationDetails", ignore = true) // Sẽ xử lý riêng trong Service (xóa/thêm)
    void updateEntityFromDTO(ReservationRequestDTO dto, @MappingTarget ReservationEntity entity);
}