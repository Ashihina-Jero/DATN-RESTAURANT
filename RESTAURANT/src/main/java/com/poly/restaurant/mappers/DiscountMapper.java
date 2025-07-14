package com.poly.restaurant.mappers;

import com.poly.restaurant.dtos.DiscountResponseDTO;
import com.poly.restaurant.entities.*; // Import tất cả entity cần thiết
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface DiscountMapper {

    // MapStruct sẽ tự động tìm các phương thức default bên dưới để thực hiện ánh xạ
    @Mapping(source = "branches", target = "branchNames")
    @Mapping(source = "discountDishes", target = "dishNames")
    @Mapping(source = "discountCombos", target = "comboNames")
    DiscountResponseDTO toResponseDTO(DiscountEntity entity);

    // --- CÁC PHƯƠNG THỨC DEFAULT ĐỂ CHUYỂN ĐỔI ---

    /**
     * Chuyển đổi một Set các BranchEntity thành một List các tên chi nhánh (String)
     */
    default List<String> mapBranchesToNames(Set<BranchEntity> branches) {
        if (branches == null) {
            return Collections.emptyList();
        }
        return branches.stream()
                .map(BranchEntity::getName)
                .collect(Collectors.toList());
    }

    /**
     * Chuyển đổi một List các DiscountDishEntity thành một List các tên món ăn (String)
     */
    default List<String> mapDishesToNames(List<DiscountDishEntity> discountDishes) {
        if (discountDishes == null) {
            return Collections.emptyList();
        }
        return discountDishes.stream()
                .map(discountDish -> discountDish.getDish().getName())
                .collect(Collectors.toList());
    }

    /**
     * Chuyển đổi một List các DiscountComboEntity thành một List các tên combo (String)
     */
    default List<String> mapCombosToNames(List<DiscountComboEntity> discountCombos) {
        if (discountCombos == null) {
            return Collections.emptyList();
        }
        return discountCombos.stream()
                .map(discountCombo -> discountCombo.getCombo().getName())
                .collect(Collectors.toList());
    }
}