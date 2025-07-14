package com.poly.restaurant.mappers;

import com.poly.restaurant.dtos.DishRequestDTO;
import com.poly.restaurant.dtos.DishResponseDTO;
import com.poly.restaurant.entities.DishEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Mapper chịu trách nhiệm chuyển đổi giữa DishEntity và các DTO liên quan.
 * Tuân thủ nguyên tắc: Service sẽ xử lý logic và các mối quan hệ phức tạp.
 */
@Mapper(componentModel = "spring", uses = {
        CategoryMapper.class,
        ComboDishMapper.class,
        MenuDishMapper.class,
        DiscountDishMapper.class
})
public interface DishMapper {

    /**
     * Chuyển đổi từ Entity sang DTO để trả về cho client.
     * Chỉ map các trường cần thiết và làm phẳng cấu trúc (flattening).
     *
     * @param entity Đối tượng DishEntity từ database.
     * @return Một đối tượng DishResponseDTO đơn giản.
     */
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    DishResponseDTO toResponseDTO(DishEntity entity);

    /**
     * Chuyển đổi từ DTO sang Entity để tạo một đối tượng mới.
     * Bỏ qua ID (do database tự sinh) và các mối quan hệ (sẽ được Service xử lý).
     *
     * @param dto Dữ liệu đầu vào từ client.
     * @return Một đối tượng DishEntity chưa được quản lý (transient).
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "comboDishes", ignore = true)
    @Mapping(target = "menuDishes", ignore = true)
    DishEntity toEntity(DishRequestDTO dto);

    /**
     * Cập nhật một Entity đã tồn tại từ dữ liệu trong DTO.
     * Tuyệt đối không cập nhật ID. Các mối quan hệ sẽ được xử lý riêng trong Service.
     *
     * @param dto    Dữ liệu cập nhật từ client.
     * @param entity Đối tượng DishEntity đã được lấy từ database.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "comboDishes", ignore = true)
    @Mapping(target = "menuDishes", ignore = true)
    void updateEntityFromDTO(DishRequestDTO dto, @MappingTarget DishEntity entity);
}