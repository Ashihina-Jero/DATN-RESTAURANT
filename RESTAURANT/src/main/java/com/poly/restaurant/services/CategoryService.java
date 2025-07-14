package com.poly.restaurant.services; 

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; 

import com.poly.restaurant.dtos.CategoryRequestDTO;
import com.poly.restaurant.dtos.CategoryResponseDTO;
import com.poly.restaurant.entities.CategoryEntity;
import com.poly.restaurant.entities.DishEntity;
import com.poly.restaurant.exceptions.ResourceNotFoundException;
import com.poly.restaurant.mappers.BranchComboMapper;
import com.poly.restaurant.mappers.BranchDishMapper;
import com.poly.restaurant.mappers.BranchMapper;
import com.poly.restaurant.mappers.CategoryMapper;
import com.poly.restaurant.repositories.BranchComboRepository;
import com.poly.restaurant.repositories.BranchDishRepository;
import com.poly.restaurant.repositories.BranchRepository;
import com.poly.restaurant.repositories.CategoryRepository;
import com.poly.restaurant.repositories.ComboRepository;
import com.poly.restaurant.repositories.DishRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

	private final CategoryRepository categoryRepository;
	private final CategoryMapper categoryMapper;
	private final DishRepository dishRepository;


	public List<CategoryResponseDTO> getAllCategories() {
		List<CategoryEntity> categories = categoryRepository.findAll();
		return categories.stream().map(categoryMapper::toResponseDTO).collect(Collectors.toList());
	}

	public CategoryResponseDTO getCategoryById(int id) {
		CategoryEntity category = categoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục với ID: " + id)); // <-- Đã đổi
		return categoryMapper.toResponseDTO(category);
	}

	@Transactional // Thêm @Transactional cho các phương thức ghi/sửa/xóa
	public CategoryResponseDTO createCategory(CategoryRequestDTO categoryRequestDTO) {
		if (categoryRepository.findByName(categoryRequestDTO.getName()).isPresent()) {
			throw new IllegalArgumentException(
					"Tên danh mục '" + categoryRequestDTO.getName() + "' đã tồn tại."); // <-- Đã đổi
		}
		CategoryEntity categoryEntity = categoryMapper.toEntity(categoryRequestDTO);
		CategoryEntity savedCategory = categoryRepository.save(categoryEntity);
		return categoryMapper.toResponseDTO(savedCategory);
	}

	@Transactional
	public CategoryResponseDTO updateCategory(int id, CategoryRequestDTO categoryRequestDTO) {
		CategoryEntity existingCategory = categoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục với ID: " + id)); // <-- Đã đổi
		// KIỂM TRA TÊN DANH MỤC TRÙNG LẶP KHI CẬP NHẬT
		// Chỉ kiểm tra nếu tên được thay đổi
		if (!existingCategory.getName().equals(categoryRequestDTO.getName())) {
			categoryRepository.findByName(categoryRequestDTO.getName()).ifPresent(foundCategory -> {
				// Nếu tìm thấy một danh mục khác có cùng tên (không phải chính nó)
				if (foundCategory.getId() != existingCategory.getId()) {
					throw new IllegalArgumentException(
							"Tên danh mục '" + categoryRequestDTO.getName() + "' đã tồn tại."); // <-- Đã đổi
				}
			});
		}
		categoryMapper.updateEntityFromDTO(categoryRequestDTO, existingCategory);

		CategoryEntity updatedCategory = categoryRepository.save(existingCategory);
		return categoryMapper.toResponseDTO(updatedCategory);
	}

	@Transactional
    public void deleteCategory(int id) {
        CategoryEntity categoryToDelete = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục với ID: " + id));

        List<DishEntity> associatedDishes = categoryToDelete.getDishes();
        // Kiểm tra xem List có null không trước khi duyệt (mặc dù new ArrayList() đã tránh điều này)
        if (associatedDishes != null && !associatedDishes.isEmpty()) {
            for (DishEntity dish : associatedDishes) {
                dish.setCategory(null); // Đặt category của Dish thành null
                this.dishRepository.save(dish); // <-- SỬ DỤNG this.dishRepository.save(dish)
            }
        }
        
        categoryRepository.delete(categoryToDelete);
    }
}