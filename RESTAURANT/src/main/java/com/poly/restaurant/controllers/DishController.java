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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.poly.restaurant.dtos.DishRequestDTO;
import com.poly.restaurant.dtos.DishResponseDTO;
import com.poly.restaurant.services.DishService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/dishes")
public class DishController {

	private final DishService dishService;

	@Autowired
	public DishController(DishService dishService) {
		this.dishService = dishService;
	}

	/**
	 * Lấy tất cả các món ăn. GET /api/dishes
	 *
	 * @return ResponseEntity chứa danh sách DishResponseDTO và HttpStatus.OK.
	 */
	@GetMapping
	public ResponseEntity<List<DishResponseDTO>> getAllDishes() {
		List<DishResponseDTO> dishes = dishService.getAllDishes();
		return new ResponseEntity<>(dishes, HttpStatus.OK);
	}

	/**
	 * Lấy thông tin một món ăn theo ID. GET /api/dishes/{id}
	 *
	 * @param id ID của món ăn cần lấy (kiểu int).
	 * @return ResponseEntity chứa DishResponseDTO và HttpStatus.OK nếu tìm thấy.
	 */
	@GetMapping("/{id}")
	public ResponseEntity<DishResponseDTO> getDishById(@PathVariable int id) {
		DishResponseDTO dish = dishService.getDishById(id);
		return new ResponseEntity<>(dish, HttpStatus.OK);
	}

	/**
	 * Lọc và tìm kiếm món ăn theo nhiều tiêu chí. Endpoint: GET
	 * /api/dishes/filter?name=...&categoryId=...
	 *
	 * @param name       Tên món ăn (không bắt buộc).
	 * @param categoryId ID của danh mục (không bắt buộc).
	 * @return ResponseEntity chứa danh sách DishResponseDTO và HttpStatus.OK.
	 */
	@GetMapping("/filter")
	public ResponseEntity<List<DishResponseDTO>> filterDishes(@RequestParam(required = false) String name,
			@RequestParam(required = false) Integer categoryId) {

		List<DishResponseDTO> dishes = dishService.searchDishes(name, categoryId);
		return new ResponseEntity<>(dishes, HttpStatus.OK);
	}

	/**
	 * Tạo một món ăn mới. POST /api/dishes
	 *
	 * @param dishRequestDTO DTO chứa thông tin món ăn cần tạo.
	 * @return ResponseEntity chứa DishResponseDTO của món ăn đã tạo và
	 *         HttpStatus.CREATED.
	 */
	@PostMapping
	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
	public ResponseEntity<DishResponseDTO> createDish(@Valid @RequestBody DishRequestDTO dishRequestDTO) {
		DishResponseDTO createdDish = dishService.createDish(dishRequestDTO);
		return new ResponseEntity<>(createdDish, HttpStatus.CREATED);
	}

	/**
	 * Cập nhật thông tin một món ăn theo ID. PUT /api/dishes/{id}
	 *
	 * @param id             ID của món ăn cần cập nhật.
	 * @param dishRequestDTO DTO chứa thông tin cập nhật.
	 * @return ResponseEntity chứa DishResponseDTO của món ăn đã cập nhật và
	 *         HttpStatus.OK.
	 */
	@PutMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
	public ResponseEntity<DishResponseDTO> updateDish(@PathVariable int id,
			@Valid @RequestBody DishRequestDTO dishRequestDTO) {
		DishResponseDTO updatedDish = dishService.updateDish(id, dishRequestDTO);
		return new ResponseEntity<>(updatedDish, HttpStatus.OK);
	}

	/**
	 * Xóa một món ăn theo ID. DELETE /api/dishes/{id}
	 *
	 * @param id ID của món ăn cần xóa.
	 * @return ResponseEntity với HttpStatus.NO_CONTENT.
	 */
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
	public ResponseEntity<Void> deleteDish(@PathVariable int id) {
		dishService.deleteDish(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
	}
}