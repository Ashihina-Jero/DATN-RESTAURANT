package com.poly.restaurant.mappers;

import com.poly.restaurant.dtos.OrderRequestDTO;
import com.poly.restaurant.dtos.OrderResponseDTO;
import com.poly.restaurant.entities.OrderEntity;
import com.poly.restaurant.entities.AccountEntity; // Cần import
import com.poly.restaurant.entities.BranchEntity; // Cần import
import com.poly.restaurant.entities.TableEntity;  // Cần import
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {AccountMapper.class, BranchMapper.class, TableMapper.class, OrderDetailMapper.class})
public interface OrderMapper {

    // 1. Ánh xạ từ Entity sang OrderResponseDTO
    @Mapping(source = "account.id", target = "accountId")
    @Mapping(source = "account.name", target = "accountName") // Giả định AccountEntity có trường 'name'
    @Mapping(source = "branch.id", target = "branchId")
    @Mapping(source = "branch.name", target = "branchName")   // Giả định BranchEntity có trường 'name'
    @Mapping(source = "table.id", target = "tableId")
    @Mapping(source = "table.name", target = "tableName")     // Giả định TableEntity có trường 'name' (có thể null)
    @Mapping(source = "orderDetails", target = "orderDetails") // Map List<OrderDetailEntity> sang List<OrderDetailResponseDTO>
    OrderResponseDTO toResponseDTO(OrderEntity entity);

    // 2. Ánh xạ từ OrderRequestDTO sang Entity (khi tạo mới)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderDate", ignore = true) // Tự động tạo bởi @CreationTimestamp
    @Mapping(target = "account", ignore = true)   // Sẽ thiết lập trong Service
    @Mapping(target = "branch", ignore = true)    // Sẽ thiết lập trong Service
    @Mapping(target = "table", ignore = true)     // Sẽ thiết lập trong Service
    @Mapping(target = "orderDetails", ignore = true) // Sẽ xử lý riêng trong Service
    OrderEntity toEntity(OrderRequestDTO dto);

    // 3. Cập nhật Entity từ OrderRequestDTO
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderDate", ignore = true) // Không cập nhật ngày tạo
    @Mapping(target = "account", ignore = true)   // Mối quan hệ ManyToOne xử lý riêng
    @Mapping(target = "branch", ignore = true)    // Mối quan hệ ManyToOne xử lý riêng
    @Mapping(target = "table", ignore = true)     // Mối quan hệ ManyToOne xử lý riêng
    @Mapping(target = "orderDetails", ignore = true) // Sẽ xử lý riêng trong Service (xóa/thêm)
    void updateEntityFromDTO(OrderRequestDTO dto, @MappingTarget OrderEntity entity);
}