package com.poly.restaurant.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poly.restaurant.dtos.DishRequestDTO;
import com.poly.restaurant.dtos.DishResponseDTO;
import com.poly.restaurant.entities.CategoryEntity;
import com.poly.restaurant.entities.DishEntity;
import com.poly.restaurant.exceptions.ResourceNotFoundException;
import com.poly.restaurant.mappers.DishMapper;
import com.poly.restaurant.repositories.CategoryRepository;
import com.poly.restaurant.repositories.DishRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DishService {

    private final DishRepository dishRepository;
    private final CategoryRepository categoryRepository;
    private final DishMapper dishMapper;

    /**
     * Lấy tất cả các món ăn.
     */
    public List<DishResponseDTO> getAllDishes() {
        List<DishEntity> dishes = dishRepository.findAll();
        return dishes.stream()
                .map(dishMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lấy một món ăn theo ID.
     */
    public DishResponseDTO getDishById(Integer id) {
        DishEntity dish = dishRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy món ăn với ID: " + id));
        return dishMapper.toResponseDTO(dish);
    }
    
    /**
     * Tìm kiếm món ăn theo các tiêu chí linh hoạt (tên và/hoặc danh mục).
     * @param name Tên món ăn để tìm kiếm (không bắt buộc).
     * @param categoryId ID của danh mục để lọc (không bắt buộc).
     * @return Danh sách DishResponseDTO phù hợp.
     */
    public List<DishResponseDTO> searchDishes(String name, Integer categoryId) {
        List<DishEntity> dishes = dishRepository.searchByCriteria(name, categoryId);
        return dishes.stream()
                .map(dishMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Tạo một món ăn mới.
     * Cần có categoryId hợp lệ trong DTO.
     */
    @Transactional
    public DishResponseDTO createDish(DishRequestDTO dishRequestDTO) {
        // Kiểm tra tên món ăn đã tồn tại chưa
        if (dishRepository.findByName(dishRequestDTO.getName()).isPresent()) {
            throw new IllegalArgumentException("Tên món ăn '" + dishRequestDTO.getName() + "' đã tồn tại.");
        }

        // Tìm danh mục (category) tương ứng
        CategoryEntity category = categoryRepository.findById(dishRequestDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục với ID: " + dishRequestDTO.getCategoryId()));

        // Chuyển đổi từ DTO sang Entity và gán danh mục
        DishEntity dishEntity = dishMapper.toEntity(dishRequestDTO);
        dishEntity.setCategory(category);

        // Lưu vào database và trả về DTO
        DishEntity savedDish = dishRepository.save(dishEntity);
        return dishMapper.toResponseDTO(savedDish);
    }

    /**
     * Cập nhật thông tin một món ăn.
     */
    @Transactional
    public DishResponseDTO updateDish(Integer id, DishRequestDTO dishRequestDTO) {
        // Tìm món ăn hiện tại
        DishEntity existingDish = dishRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy món ăn với ID: " + id));

        // Kiểm tra tên món ăn trùng lặp nếu tên có thay đổi
        if (!existingDish.getName().equals(dishRequestDTO.getName())) {
            dishRepository.findByName(dishRequestDTO.getName()).ifPresent(foundDish -> {
                if (foundDish.getId() != existingDish.getId()) {
                    throw new IllegalArgumentException("Tên món ăn '" + dishRequestDTO.getName() + "' đã tồn tại.");
                }
            });
        }
        
        // Kiểm tra và cập nhật danh mục nếu có sự thay đổi
        Integer newCategoryId = dishRequestDTO.getCategoryId();
        if (newCategoryId != null && (existingDish.getCategory() == null || !newCategoryId.equals(existingDish.getCategory().getId()))) {
             CategoryEntity newCategory = categoryRepository.findById(newCategoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục với ID: " + newCategoryId));
            existingDish.setCategory(newCategory);
        }

        // Cập nhật các trường khác từ DTO
        dishMapper.updateEntityFromDTO(dishRequestDTO, existingDish);

        // Lưu thay đổi và trả về DTO
        DishEntity updatedDish = dishRepository.save(existingDish);
        return dishMapper.toResponseDTO(updatedDish);
    }

    /**
     * Xóa một món ăn theo ID.
     */
    @Transactional
    public void deleteDish(Integer id) {
        // Kiểm tra món ăn có tồn tại không trước khi xóa
        if (!dishRepository.existsById(id)) {
            throw new ResourceNotFoundException("Không tìm thấy món ăn với ID: " + id);
        }
        dishRepository.deleteById(id);
    }
}