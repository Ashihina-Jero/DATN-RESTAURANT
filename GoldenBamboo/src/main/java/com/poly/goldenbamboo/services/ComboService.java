package com.poly.goldenbamboo.services;

import com.poly.goldenbamboo.dtos.ComboRequestDTO;
import com.poly.goldenbamboo.dtos.ComboResponseDTO;
import com.poly.goldenbamboo.entities.ComboEntity;
import com.poly.goldenbamboo.entities.ComboDishEntity; // Cần để tạo mối quan hệ
import com.poly.goldenbamboo.entities.DishEntity;   // Cần để tìm Dish
import com.poly.goldenbamboo.exceptions.ResourceNotFoundException;
import com.poly.goldenbamboo.mappers.ComboMapper;
import com.poly.goldenbamboo.repositories.ComboRepository;
import com.poly.goldenbamboo.repositories.ComboDishRepository; // Cần để quản lý ComboDish
import com.poly.goldenbamboo.repositories.DishRepository;     // Cần để tìm Dish

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Để đảm bảo giao dịch cho các thao tác phức tạp

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComboService {

    private final ComboRepository comboRepository;
    private final ComboMapper comboMapper;
    private final DishRepository dishRepository;
    private final ComboDishRepository comboDishRepository;

    @Autowired
    public ComboService(ComboRepository comboRepository, ComboMapper comboMapper,
                        DishRepository dishRepository, ComboDishRepository comboDishRepository) {
        this.comboRepository = comboRepository;
        this.comboMapper = comboMapper;
        this.dishRepository = dishRepository;
        this.comboDishRepository = comboDishRepository;
    }

    public List<ComboResponseDTO> getAllCombos() {
        List<ComboEntity> combos = comboRepository.findAll();
        return combos.stream()
                .map(comboMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public ComboResponseDTO getComboById(int id) {
        ComboEntity combo = comboRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Combo not found with id " + id));
        return comboMapper.toResponseDTO(combo);
    }

    @Transactional // Đảm bảo tất cả các thao tác (lưu combo và comboDishes) là một giao dịch
    public ComboResponseDTO createCombo(ComboRequestDTO comboRequestDTO) {
        ComboEntity comboEntity = comboMapper.toEntity(comboRequestDTO);
        ComboEntity savedCombo = comboRepository.save(comboEntity); // Lưu combo trước để có ID

        // Xử lý các món ăn trong combo (comboDishes)
        if (comboRequestDTO.getDishIds() != null && !comboRequestDTO.getDishIds().isEmpty()) {
            List<ComboDishEntity> comboDishes = new ArrayList<>();
            for (Integer dishId : comboRequestDTO.getDishIds()) {
                DishEntity dish = dishRepository.findById(dishId)
                        .orElseThrow(() -> new ResourceNotFoundException("Dish not found with id " + dishId));

                ComboDishEntity comboDish = new ComboDishEntity();
                comboDish.setCombo(savedCombo); // Thiết lập combo đã lưu
                comboDish.setDish(dish);
                comboDishes.add(comboDish);
            }
            comboDishRepository.saveAll(comboDishes); // Lưu tất cả các ComboDish mới
            savedCombo.setComboDishes(comboDishes); // Cập nhật lại list trong entity đã lưu
        }

        return comboMapper.toResponseDTO(savedCombo);
    }

    @Transactional
    public ComboResponseDTO updateCombo(int id, ComboRequestDTO comboRequestDTO) {
        ComboEntity existingCombo = comboRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Combo not found with id " + id));

        comboMapper.updateEntityFromDTO(comboRequestDTO, existingCombo); // Cập nhật các trường chính của combo

        // Xử lý cập nhật các món ăn trong combo (comboDishes)
        // Đây là cách phổ biến: xóa tất cả các mối quan hệ cũ và tạo lại các mối quan hệ mới
        // Hoặc bạn có thể dùng logic phức tạp hơn để chỉ thêm/xóa những cái cần thiết
        comboDishRepository.deleteAll(existingCombo.getComboDishes()); // Xóa các mối quan hệ cũ
        existingCombo.getComboDishes().clear(); // Dọn dẹp list trong entity

        if (comboRequestDTO.getDishIds() != null && !comboRequestDTO.getDishIds().isEmpty()) {
            List<ComboDishEntity> newComboDishes = new ArrayList<>();
            for (Integer dishId : comboRequestDTO.getDishIds()) {
                DishEntity dish = dishRepository.findById(dishId)
                        .orElseThrow(() -> new ResourceNotFoundException("Dish not found with id " + dishId));

                ComboDishEntity comboDish = new ComboDishEntity();
                comboDish.setCombo(existingCombo); // Sử dụng combo đã tồn tại
                comboDish.setDish(dish);
                newComboDishes.add(comboDish);
            }
            comboDishRepository.saveAll(newComboDishes); // Lưu các ComboDish mới
            existingCombo.setComboDishes(newComboDishes); // Cập nhật list trong entity
        }

        ComboEntity updatedCombo = comboRepository.save(existingCombo); // Lưu lại combo đã được cập nhật
        return comboMapper.toResponseDTO(updatedCombo);
    }

    public void deleteCombo(int id) {
        if (!comboRepository.existsById(id)) {
            throw new ResourceNotFoundException("Combo not found with id " + id);
        }
        // CascadeType.ALL và orphanRemoval=true trên comboDishes và menuCombos
        // sẽ đảm bảo các bản ghi liên quan trong combo_dishes và menu_combos cũng bị xóa khi combo bị xóa.
        comboRepository.deleteById(id);
    }
}