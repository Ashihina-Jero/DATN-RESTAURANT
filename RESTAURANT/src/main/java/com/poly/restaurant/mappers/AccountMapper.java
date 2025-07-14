package com.poly.restaurant.mappers;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.poly.restaurant.dtos.AccountRequestDTO;
import com.poly.restaurant.dtos.AccountResponseDTO;
import com.poly.restaurant.entities.AccountEntity;
import com.poly.restaurant.entities.RoleEntity;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    // 1. Ánh xạ từ Entity sang AccountResponseDTO
	@Mapping(source = "branch.id", target = "branchId")
    @Mapping(source = "branch.name", target = "branchName")
    @Mapping(source  = "roles", target = "roles")
    AccountResponseDTO toResponseDTO(AccountEntity entity);

    // 2. Ánh xạ từ AccountRequestDTO sang Entity (khi tạo mới)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true) // Vai trò sẽ được gán thủ công trong Service
    @Mapping(target = "branch", ignore = true) // Chi nhánh sẽ được gán thủ công trong Service
    // Các trường khác không có trong DTO sẽ tự động được bỏ qua
    AccountEntity toEntity(AccountRequestDTO dto);

    // 3. Cập nhật Entity từ AccountRequestDTO
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createAt", ignore = true)
    @Mapping(target = "updateAt", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "reservations", ignore = true)
    @Mapping(target = "branch", ignore = true)
    @Mapping(target = "roles", ignore = true) // Không cập nhật vai trò qua đây
    void updateEntityFromDTO(AccountRequestDTO dto, @MappingTarget AccountEntity entity);
    
    default List<String> mapRoles(Set<RoleEntity> roles) {
        if (roles == null) {
            return java.util.Collections.emptyList();
        }
        return roles.stream()
                    .map(RoleEntity::getName)
                    .collect(Collectors.toList());
    }
}