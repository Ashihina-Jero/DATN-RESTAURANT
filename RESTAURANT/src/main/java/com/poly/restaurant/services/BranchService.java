package com.poly.restaurant.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poly.restaurant.dtos.BranchComboRequestDTO;
import com.poly.restaurant.dtos.BranchDishRequestDTO;
import com.poly.restaurant.dtos.BranchRequestDTO;
import com.poly.restaurant.dtos.BranchResponseDTO;
import com.poly.restaurant.entities.BranchComboEntity;
import com.poly.restaurant.entities.BranchDishEntity;
import com.poly.restaurant.entities.BranchEntity;
import com.poly.restaurant.entities.ComboEntity;
import com.poly.restaurant.entities.DishEntity;
import com.poly.restaurant.exceptions.ResourceNotFoundException;
import com.poly.restaurant.mappers.BranchComboMapper;
import com.poly.restaurant.mappers.BranchDishMapper;
import com.poly.restaurant.mappers.BranchMapper;
import com.poly.restaurant.repositories.BranchComboRepository;
import com.poly.restaurant.repositories.BranchDishRepository;
import com.poly.restaurant.repositories.BranchRepository;
import com.poly.restaurant.repositories.ComboRepository;
import com.poly.restaurant.repositories.DishRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BranchService {

	private final BranchRepository branchRepository;
	private final BranchMapper branchMapper;

	private final DishRepository dishRepository;
	private final ComboRepository comboRepository;

	private final BranchDishMapper branchDishMapper;
	private final BranchComboMapper branchComboMapper;

	private final BranchDishRepository branchDishRepository;
	private final BranchComboRepository branchComboRepository;

	/**
	 * Lấy danh sách tất cả chi nhánh
	 */
	public List<BranchResponseDTO> getAllBranches() {
		return branchRepository.findAll().stream().map(branchMapper::toBranchResponseDTO).collect(Collectors.toList());
	}

	/**
	 * Lấy thông tin chi tiết một chi nhánh theo ID
	 */
	public BranchResponseDTO getBranchById(Integer id) {
		BranchEntity branch = branchRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy chi nhánh với ID: " + id));
		return branchMapper.toBranchResponseDTO(branch);
	}

	/**
	 * Tạo một chi nhánh mới
	 */
	@Transactional
	public BranchResponseDTO createBranch(BranchRequestDTO requestDTO) {
		// Kiểm tra xem tên chi nhánh đã tồn tại chưa
		branchRepository.findByName(requestDTO.getName()).ifPresent(branch -> {
			throw new IllegalStateException("Tên chi nhánh đã tồn tại.");
		});

		BranchEntity branch = branchMapper.toBranch(requestDTO);
		BranchEntity savedBranch = branchRepository.save(branch);
		return branchMapper.toBranchResponseDTO(savedBranch);
	}

	/**
	 * Cập nhật thông tin một chi nhánh
	 */
	@Transactional
	public BranchResponseDTO updateBranch(Integer id, BranchRequestDTO requestDTO) {
		BranchEntity existingBranch = branchRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy chi nhánh với ID: " + id));

		// Dùng mapper để cập nhật các trường từ DTO vào entity đã có
		branchMapper.updateBranchFromDto(requestDTO, existingBranch);

		BranchEntity updatedBranch = branchRepository.save(existingBranch);
		return branchMapper.toBranchResponseDTO(updatedBranch);
	}

	/**
	 * Xóa một chi nhánh
	 */
	@Transactional
	public void deleteBranch(Integer id) {
		if (!branchRepository.existsById(id)) {
			throw new ResourceNotFoundException("Không tìm thấy chi nhánh với ID: " + id);
		}
		branchRepository.deleteById(id);
	}

	@Transactional
	public BranchDishEntity addDishToBranch(BranchDishRequestDTO request) {
		BranchEntity branch = branchRepository.findById(request.getBranchId())
				.orElseThrow(() -> new RuntimeException("Không tìm thấy chi nhánh"));

		DishEntity dish = dishRepository.findById(request.getDishId())
				.orElseThrow(() -> new RuntimeException("Không tìm thấy món ăn"));

		BranchDishEntity branchDish = branchDishMapper.toEntity(request);
		branchDish.setBranch(branch);
		branchDish.setDish(dish);

		return branchDishRepository.save(branchDish);
	}

	@Transactional
	public BranchComboEntity addComboToBranch(BranchComboRequestDTO request) {
		BranchEntity branch = branchRepository.findById(request.getBranchId())
				.orElseThrow(() -> new RuntimeException("Không tìm thấy chi nhánh"));

		ComboEntity combo = comboRepository.findById(request.getComboId())
				.orElseThrow(() -> new RuntimeException("Không tìm thấy combo"));

		BranchComboEntity branchCombo = branchComboMapper.toEntity(request);
		branchCombo.setBranch(branch);
		branchCombo.setCombo(combo);

		return branchComboRepository.save(branchCombo);
	}
}