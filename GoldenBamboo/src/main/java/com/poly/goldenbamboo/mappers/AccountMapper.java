package com.poly.goldenbamboo.mappers;

import com.poly.goldenbamboo.dtos.AccountRequestDTO;
import com.poly.goldenbamboo.dtos.AccountResponseDTO;
import com.poly.goldenbamboo.entities.AccountEntity;
import com.poly.goldenbamboo.entities.BranchEntity; // Cần import BranchEntity
import com.poly.goldenbamboo.entities.RoleEntity; // Cần import RoleEntity
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget; // Để cập nhật Entity

@Mapper(componentModel = "spring", uses = {BranchMapper.class, RoleMapper.class}) // Thêm uses để MapStruct biết cách ánh xạ các đối tượng liên quan
public interface AccountMapper {

    // 1. Ánh xạ từ Entity sang AccountResponseDTO
    @Mapping(source = "branch.id", target = "branchId")       // Lấy id của branch
    @Mapping(source = "branch.name", target = "branchName")   // Lấy tên của branch
    @Mapping(source = "role.id", target = "roleId")           // Lấy id của role
    @Mapping(source = "role.name", target = "roleName")       // Lấy tên của role
    @Mapping(target = "password", ignore = true)              // Bỏ qua trường password
    AccountResponseDTO toResponseDTO(AccountEntity entity);

    // 2. Ánh xạ từ AccountRequestDTO sang Entity (khi tạo mới)
    // Các trường branch và role cần được xử lý riêng trong Service hoặc thông qua Custom Mapping
    // @Mapping(target = "branch", ignore = true) // Sẽ thiết lập trong Service dựa vào branchId
    // @Mapping(target = "role", ignore = true)   // Sẽ thiết lập trong Service dựa vào roleId
    // @Mapping(target = "id", ignore = true)
    // @Mapping(target = "createAt", ignore = true)
    // @Mapping(target = "updateAt", ignore = true)
    // @Mapping(target = "orders", ignore = true)
    // @Mapping(target = "reservations", ignore = true)
    AccountEntity toEntity(AccountRequestDTO dto); // Các ignore trên là mặc định nếu không có source tương ứng

    // 3. Cập nhật Entity từ AccountRequestDTO
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createAt", ignore = true)
    @Mapping(target = "updateAt", ignore = true)
    @Mapping(target = "password", ignore = true)       // Mật khẩu thường được cập nhật qua API riêng hoặc xử lý đặc biệt
    @Mapping(target = "orders", ignore = true)         // Không cập nhật các mối quan hệ OneToMany qua DTO này
    @Mapping(target = "reservations", ignore = true)
    @Mapping(target = "branch", ignore = true)         // Mối quan hệ ManyToOne cũng xử lý riêng
    @Mapping(target = "role", ignore = true)
    void updateEntityFromDTO(AccountRequestDTO dto, @MappingTarget AccountEntity entity);
}