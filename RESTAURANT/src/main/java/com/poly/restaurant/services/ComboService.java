package com.poly.restaurant.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poly.restaurant.dtos.ComboItemDTO;
import com.poly.restaurant.dtos.ComboRequestDTO;
import com.poly.restaurant.dtos.ComboResponseDTO;
import com.poly.restaurant.entities.ComboDishEntity;
import com.poly.restaurant.entities.ComboEntity;
import com.poly.restaurant.entities.DishEntity;
import com.poly.restaurant.exceptions.ResourceNotFoundException;
import com.poly.restaurant.mappers.ComboMapper;
import com.poly.restaurant.repositories.ComboDishRepository;
import com.poly.restaurant.repositories.ComboRepository;
import com.poly.restaurant.repositories.DishRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor 
public class ComboService {

    private final ComboRepository comboRepository;
    private final DishRepository dishRepository;
    private final ComboMapper comboMapper;
    private final ComboDishRepository comboDishRepository;

    /**
     * Lấy danh sách tất cả các combo.
     */
    public List<ComboResponseDTO> getAllCombos() {
        return comboRepository.findAll().stream()
                .map(comboMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lấy thông tin chi tiết một combo theo ID.
     */
    public ComboResponseDTO getComboById(Integer id) {
        ComboEntity combo = comboRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy combo với ID: " + id));
        return comboMapper.toResponseDTO(combo);
    }

    /**
     * Tạo một combo mới, bao gồm việc tạo các liên kết trong bảng trung gian.
     */
    @Transactional
    public ComboResponseDTO createCombo(ComboRequestDTO requestDTO) {
        // Kiểm tra tên combo đã tồn tại chưa
        comboRepository.findByName(requestDTO.getName()).ifPresent(c -> {
            throw new IllegalArgumentException("Tên combo đã tồn tại.");
        });

        // 1. Map và lưu thông tin chung của Combo
        ComboEntity comboEntity = comboMapper.toEntity(requestDTO);
        ComboEntity savedCombo = comboRepository.save(comboEntity);

        // 2. Duyệt qua danh sách món ăn kèm số lượng
        List<ComboDishEntity> comboDishes = new ArrayList<>();
        for (ComboItemDTO item : requestDTO.getDishes()) {
            DishEntity dish = dishRepository.findById(item.getDishId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy món ăn với ID: " + item.getDishId()));

            ComboDishEntity comboDish = new ComboDishEntity();
            comboDish.setCombo(savedCombo);
            comboDish.setDish(dish);
            comboDish.setQuantity(item.getQuantity());
            comboDishes.add(comboDish);
        }

        // 3. Lưu tất cả các liên kết vào bảng trung gian
        comboDishRepository.saveAll(comboDishes);
        
        savedCombo.setComboDishes(comboDishes);
        return comboMapper.toResponseDTO(savedCombo);
    }
    /**
     * Cập nhật thông tin một combo, bao gồm việc làm mới danh sách các món ăn và số lượng.
     */
    @Transactional
    public ComboResponseDTO updateCombo(Integer id, ComboRequestDTO requestDTO) {
        // 1. Tìm combo hiện có
        ComboEntity existingCombo = comboRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy combo với ID: " + id));

        // 2. Kiểm tra trùng lặp tên combo (nếu tên bị thay đổi)
        if (!existingCombo.getName().equals(requestDTO.getName())) {
            comboRepository.findByName(requestDTO.getName()).ifPresent(c -> {
                if (!c.getId().equals(id)) {
                    throw new IllegalArgumentException("Tên combo '" + requestDTO.getName() + "' đã tồn tại.");
                }
            });
        }

        // 3. Cập nhật các thuộc tính cơ bản của combo
        comboMapper.updateEntityFromDTO(requestDTO, existingCombo);

        // 4. Xóa tất cả các liên kết món ăn cũ trong bảng trung gian
        comboDishRepository.deleteAll(existingCombo.getComboDishes());
        existingCombo.getComboDishes().clear(); // Dọn dẹp danh sách trong entity

        // 5. Tạo và thêm các liên kết món ăn mới dựa trên DTO
        List<ComboDishEntity> updatedComboDishes = new ArrayList<>();
        if (requestDTO.getDishes() != null && !requestDTO.getDishes().isEmpty()) {
            for (ComboItemDTO item : requestDTO.getDishes()) {
                DishEntity dish = dishRepository.findById(item.getDishId())
                        .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy món ăn với ID: " + item.getDishId()));

                ComboDishEntity comboDish = new ComboDishEntity();
                comboDish.setCombo(existingCombo);
                comboDish.setDish(dish);
                comboDish.setQuantity(item.getQuantity());
                updatedComboDishes.add(comboDish);
            }
        }
        
        // 6. Lưu các liên kết mới vào bảng trung gian
        comboDishRepository.saveAll(updatedComboDishes);
        
        // 7. Cập nhật lại danh sách món ăn cho combo và lưu combo
        existingCombo.setComboDishes(updatedComboDishes);
        ComboEntity savedCombo = comboRepository.save(existingCombo);

        return comboMapper.toResponseDTO(savedCombo);
    }


    /**
     * Xóa một combo theo ID.
     */
    @Transactional
    public void deleteCombo(Integer id) {
        if (!comboRepository.existsById(id)) {
            throw new ResourceNotFoundException("Không tìm thấy combo với ID: " + id);
        }
        comboRepository.deleteById(id);
    }
}