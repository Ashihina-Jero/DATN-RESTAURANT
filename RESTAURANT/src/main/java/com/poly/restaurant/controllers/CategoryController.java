package com.poly.restaurant.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poly.restaurant.dtos.CategoryRequestDTO;
import com.poly.restaurant.dtos.CategoryResponseDTO;
import com.poly.restaurant.services.CategoryService;

import jakarta.validation.Valid;

@RestController // Đánh dấu đây là một Controller REST
@RequestMapping("/api/categories") // Đường dẫn gốc cho tất cả các endpoint trong Controller này
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * Lấy tất cả các danh mục.
     * GET /api/categories
     *
     * @return ResponseEntity chứa danh sách CategoryResponseDTO và HttpStatus.OK.
     */
    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories() {
        List<CategoryResponseDTO> categories = categoryService.getAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    /**
     * Lấy thông tin một danh mục theo ID.
     * GET /api/categories/{id}
     *
     * @param id ID của danh mục cần lấy.
     * @return ResponseEntity chứa CategoryResponseDTO và HttpStatus.OK nếu tìm thấy,
     * hoặc HttpStatus.NOT_FOUND nếu không tìm thấy (được xử lý bởi ControllerAdvice).
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> getCategoryById(@PathVariable int id) {
        CategoryResponseDTO category = categoryService.getCategoryById(id);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    /**
     * Tạo một danh mục mới.
     * POST /api/categories
     *
     * @param categoryRequestDTO DTO chứa thông tin danh mục cần tạo.
     * @return ResponseEntity chứa CategoryResponseDTO của danh mục đã tạo và HttpStatus.CREATED.
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<CategoryResponseDTO> createCategory(@Valid @RequestBody CategoryRequestDTO categoryRequestDTO) {
        CategoryResponseDTO createdCategory = categoryService.createCategory(categoryRequestDTO);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    /**
     * Cập nhật thông tin một danh mục theo ID.
     * PUT /api/categories/{id}
     *
     * @param id ID của danh mục cần cập nhật.
     * @param categoryRequestDTO DTO chứa thông tin cập nhật.
     * @return ResponseEntity chứa CategoryResponseDTO của danh mục đã cập nhật và HttpStatus.OK.
     * hoặc HttpStatus.NOT_FOUND nếu không tìm thấy (được xử lý bởi ControllerAdvice).
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<CategoryResponseDTO> updateCategory(@PathVariable int id, @Valid @RequestBody CategoryRequestDTO categoryRequestDTO) {
        CategoryResponseDTO updatedCategory = categoryService.updateCategory(id, categoryRequestDTO);
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }

    /**
     * Xóa một danh mục theo ID.
     * DELETE /api/categories/{id}
     *
     * @param id ID của danh mục cần xóa.
     * @return ResponseEntity với HttpStatus.NO_CONTENT.
     * hoặc HttpStatus.NOT_FOUND nếu không tìm thấy (được xử lý bởi ControllerAdvice).
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Void> deleteCategory(@PathVariable int id) {
        categoryService.deleteCategory(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); 
    }
}