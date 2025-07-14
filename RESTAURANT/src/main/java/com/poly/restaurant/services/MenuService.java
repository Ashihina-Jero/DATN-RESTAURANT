package com.poly.restaurant.services;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poly.restaurant.dtos.AddComboToMenuRequestDTO;
import com.poly.restaurant.dtos.AddDishToMenuRequestDTO;
import com.poly.restaurant.dtos.MenuRequestDTO;
import com.poly.restaurant.dtos.MenuResponseDTO;
import com.poly.restaurant.entities.BranchEntity;
import com.poly.restaurant.entities.ComboEntity;
import com.poly.restaurant.entities.DishEntity;
import com.poly.restaurant.entities.MenuComboEntity;
import com.poly.restaurant.entities.MenuDishEntity;
import com.poly.restaurant.entities.MenuEntity;
import com.poly.restaurant.exceptions.ResourceNotFoundException;
import com.poly.restaurant.mappers.MenuMapper;
import com.poly.restaurant.repositories.BranchRepository;
import com.poly.restaurant.repositories.ComboRepository;
import com.poly.restaurant.repositories.DishRepository;
import com.poly.restaurant.repositories.MenuComboRepository;
import com.poly.restaurant.repositories.MenuDishRepository;
import com.poly.restaurant.repositories.MenuRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final BranchRepository branchRepository;
    private final DishRepository dishRepository;
    private final MenuDishRepository menuDishRepository;
    private final MenuMapper menuMapper;
    private final ComboRepository comboRepository;
    private final MenuComboRepository menuComboRepository;

    public MenuResponseDTO getMenuById(Integer menuId) {
        MenuEntity menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy menu với ID: " + menuId));
        return menuMapper.toResponseDTO(menu);
    }

    @Transactional
    public MenuResponseDTO createMenu(MenuRequestDTO request) {
        MenuEntity menu = menuMapper.toEntity(request);
        // Gán chi nhánh nếu có
        if (request.getBranchId() != null) {
            BranchEntity branch = branchRepository.findById(request.getBranchId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy chi nhánh: " + request.getBranchId()));
            menu.setBranch(branch);
        }
        MenuEntity savedMenu = menuRepository.save(menu);
        return menuMapper.toResponseDTO(savedMenu);
    }

    @Transactional
    public MenuResponseDTO addDishToMenu(Integer menuId, AddDishToMenuRequestDTO request) {
        MenuEntity menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy menu: " + menuId));
        DishEntity dish = dishRepository.findById(request.getDishId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy món ăn: " + request.getDishId()));

        // Tạo thực thể trung gian MenuDishEntity
        MenuDishEntity menuDish = new MenuDishEntity();
        menuDish.setMenu(menu);
        menuDish.setDish(dish);

        menuDishRepository.save(menuDish);

        // Trả về menu đã được cập nhật
        return menuMapper.toResponseDTO(menuRepository.findById(menuId).get());
    }
    
    /**
     * Thêm một combo vào menu
     */
    @Transactional
    public MenuResponseDTO addComboToMenu(Integer menuId, AddComboToMenuRequestDTO request) {
        MenuEntity menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy menu: " + menuId));
        ComboEntity combo = comboRepository.findById(request.getComboId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy combo: " + request.getComboId()));

        MenuComboEntity menuCombo = new MenuComboEntity();
        menuCombo.setMenu(menu);
        menuCombo.setCombo(combo);


        menuComboRepository.save(menuCombo);

        return getMenuById(menuId);
    }
}