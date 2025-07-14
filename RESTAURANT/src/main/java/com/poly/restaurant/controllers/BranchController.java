package com.poly.restaurant.controllers;

import java.util.List;

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

import com.poly.restaurant.dtos.BranchComboRequestDTO;
import com.poly.restaurant.dtos.BranchDishRequestDTO;
import com.poly.restaurant.dtos.BranchRequestDTO;
import com.poly.restaurant.dtos.BranchResponseDTO;
import com.poly.restaurant.services.BranchService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/branches")
@RequiredArgsConstructor
public class BranchController {

	private final BranchService branchService;

	/**
	 * Lấy danh sách tất cả chi nhánh. Cho phép tất cả nhân viên đã đăng nhập truy
	 * cập.
	 */
	@GetMapping
	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STAFF')")
	public ResponseEntity<List<BranchResponseDTO>> getAllBranches() {
		List<BranchResponseDTO> branches = branchService.getAllBranches();
		return ResponseEntity.ok(branches);
	}

	/**
	 * Lấy chi tiết một chi nhánh.
	 */
	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STAFF')")
	public ResponseEntity<BranchResponseDTO> getBranchById(@PathVariable Integer id) {
		BranchResponseDTO branch = branchService.getBranchById(id);
		return ResponseEntity.ok(branch);
	}

	/**
	 * Tạo chi nhánh mới. Chỉ cho phép ADMIN và MANAGER.
	 */
	@PostMapping
	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
	public ResponseEntity<BranchResponseDTO> createBranch(@Valid @RequestBody BranchRequestDTO requestDTO) {
		BranchResponseDTO createdBranch = branchService.createBranch(requestDTO);
		return new ResponseEntity<>(createdBranch, HttpStatus.CREATED);
	}

	/**
	 * Cập nhật thông tin chi nhánh. Chỉ cho phép ADMIN và MANAGER.
	 */
	@PutMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
	public ResponseEntity<BranchResponseDTO> updateBranch(@PathVariable Integer id,
			@Valid @RequestBody BranchRequestDTO requestDTO) {
		BranchResponseDTO updatedBranch = branchService.updateBranch(id, requestDTO);
		return ResponseEntity.ok(updatedBranch);
	}

	/**
	 * Xóa một chi nhánh. Chỉ cho phép ADMIN.
	 */
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> deleteBranch(@PathVariable Integer id) {
		branchService.deleteBranch(id);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/dishes")
	@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
	public ResponseEntity<?> addDishToBranch(@Valid @RequestBody BranchDishRequestDTO request) {
		branchService.addDishToBranch(request);
		return ResponseEntity.ok("Đã thêm món ăn vào chi nhánh thành công.");
	}

	@PostMapping("/combos")
	@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
	public ResponseEntity<?> addComboToBranch(@Valid @RequestBody BranchComboRequestDTO request) {
		branchService.addComboToBranch(request);
		return ResponseEntity.ok("Đã thêm combo vào chi nhánh thành công.");
	}
}