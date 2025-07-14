package com.poly.goldenbamboo.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poly.goldenbamboo.dtos.DishRequestDTO;
import com.poly.goldenbamboo.dtos.DishResponseDTO;
import com.poly.goldenbamboo.entities.CategoryEntity;
import com.poly.goldenbamboo.entities.DishEntity;
import com.poly.goldenbamboo.exceptions.ResourceNotFoundException;
import com.poly.goldenbamboo.mappers.DishMapper;
import com.poly.goldenbamboo.repositories.CategoryRepository; // Cần Repository cho Category
import com.poly.goldenbamboo.repositories.DishRepository; // Cần Repository cho Dish

@Service
public class DishService {

    private final DishRepository dishRepository;
    private final CategoryRepository categoryRepository; // Inject CategoryRepository
    private final DishMapper dishMapper;

    @Autowired
    public DishService(DishRepository dishRepository, CategoryRepository categoryRepository, DishMapper dishMapper) {
        this.dishRepository = dishRepository;
        this.categoryRepository = categoryRepository;
        this.dishMapper = dishMapper;
    }

    /**
     * Lấy tất cả các món ăn.
     *
     * @return Danh sách DishResponseDTO.
     */
    public List<DishResponseDTO> getAllDishes() {
        List<DishEntity> dishes = dishRepository.findAll();
        return dishes.stream()
                .map(dishMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lấy thông tin món ăn theo ID.
     *
     * @param id ID của món ăn.
     * @return DishResponseDTO nếu tìm thấy.
     * @throws ResourceNotFoundException nếu không tìm thấy món ăn.
     */
    public DishResponseDTO getDishById(int id) {
        DishEntity dish = dishRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dish not found with id " + id));
        return dishMapper.toResponseDTO(dish);
    }

    /**
     * Tạo một món ăn mới.
     *
     * @param dishRequestDTO DTO chứa thông tin món ăn cần tạo.
     * @return DishResponseDTO của món ăn đã tạo.
     * @throws ResourceNotFoundException nếu Category không tồn tại.
     */
    @Transactional
    public DishResponseDTO createDish(DishRequestDTO dishRequestDTO) {
        DishEntity dishEntity = dishMapper.toEntity(dishRequestDTO);

        // Xử lý mối quan hệ Category (ManyToOne)
        CategoryEntity category = categoryRepository.findById(dishRequestDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + dishRequestDTO.getCategoryId()));
        dishEntity.setCategory(category);

        DishEntity savedDish = dishRepository.save(dishEntity);
        return dishMapper.toResponseDTO(savedDish);
    }

    /**
     * Cập nhật thông tin món ăn.
     *
     * @param id ID của món ăn cần cập nhật.
     * @param dishRequestDTO DTO chứa thông tin cập nhật.
     * @return DishResponseDTO của món ăn đã cập nhật.
     * @throws ResourceNotFoundException nếu món ăn hoặc Category không tồn tại.
     */
    @Transactional
    public DishResponseDTO updateDish(int id, DishRequestDTO dishRequestDTO) {
        DishEntity existingDish = dishRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dish not found with id " + id));

        dishMapper.updateEntityFromDTO(dishRequestDTO, existingDish); // Cập nhật các trường chính

        // Xử lý cập nhật mối quan hệ Category (nếu ID thay đổi)
        if (!existingDish.getCategory().getId().equals(dishRequestDTO.getCategoryId())) {
            CategoryEntity newCategory = categoryRepository.findById(dishRequestDTO.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + dishRequestDTO.getCategoryId()));
            existingDish.setCategory(newCategory);
        }

        DishEntity updatedDish = dishRepository.save(existingDish);
        return dishMapper.toResponseDTO(updatedDish);
    }

    /**
     * Xóa một món ăn theo ID.
     *
     * @param id ID của món ăn cần xóa.
     * @throws ResourceNotFoundException nếu không tìm thấy món ăn.
     */
    public void deleteDish(int id) {
        if (!dishRepository.existsById(id)) {
            throw new ResourceNotFoundException("Dish not found with id " + id);
        }
        // CascadeType.ALL và orphanRemoval=true trên comboDishes và menuDishes
        // sẽ đảm bảo các bản ghi liên quan cũng bị xóa khi món ăn bị xóa.
        dishRepository.deleteById(id);
    }
}