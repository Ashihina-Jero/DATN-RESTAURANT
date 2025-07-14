package com.poly.goldenbamboo.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poly.goldenbamboo.dtos.MenuDishRequestDTO;
import com.poly.goldenbamboo.dtos.MenuDishResponseDTO;
import com.poly.goldenbamboo.entities.DishEntity;
import com.poly.goldenbamboo.entities.MenuDishEntity;
import com.poly.goldenbamboo.entities.MenuEntity;
import com.poly.goldenbamboo.exceptions.ResourceNotFoundException;
import com.poly.goldenbamboo.mappers.MenuDishMapper;
import com.poly.goldenbamboo.repositories.DishRepository;   // Cần Repository cho Dish
import com.poly.goldenbamboo.repositories.MenuDishRepository;
import com.poly.goldenbamboo.repositories.MenuRepository;   // Cần Repository cho Menu

@Service
public class MenuDishService {

    private final MenuDishRepository menuDishRepository;
    private final DishRepository dishRepository;
    private final MenuRepository menuRepository;
    private final MenuDishMapper menuDishMapper;

    @Autowired
    public MenuDishService(MenuDishRepository menuDishRepository,
                           DishRepository dishRepository,
                           MenuRepository menuRepository,
                           MenuDishMapper menuDishMapper) {
        this.menuDishRepository = menuDishRepository;
        this.dishRepository = dishRepository;
        this.menuRepository = menuRepository;
        this.menuDishMapper = menuDishMapper;
    }

    /**
     * Lấy tất cả các món ăn trong menu.
     *
     * @return Danh sách MenuDishResponseDTO.
     */
    public List<MenuDishResponseDTO> getAllMenuDishes() {
        List<MenuDishEntity> menuDishes = menuDishRepository.findAll();
        return menuDishes.stream()
                .map(menuDishMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lấy thông tin MenuDish theo ID.
     *
     * @param id ID của MenuDish.
     * @return MenuDishResponseDTO nếu tìm thấy.
     * @throws ResourceNotFoundException nếu không tìm thấy.
     */
    public MenuDishResponseDTO getMenuDishById(int id) {
        MenuDishEntity menuDish = menuDishRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MenuDish not found with id " + id));
        return menuDishMapper.toResponseDTO(menuDish);
    }

    /**
     * Thêm một món ăn vào menu.
     *
     * @param menuDishRequestDTO DTO chứa thông tin MenuDish cần tạo.
     * @return MenuDishResponseDTO của MenuDish đã tạo.
     * @throws ResourceNotFoundException nếu Dish hoặc Menu không tồn tại.
     * @throws IllegalArgumentException nếu mối quan hệ đã tồn tại (nếu bạn muốn duy nhất).
     */
    @Transactional
    public MenuDishResponseDTO createMenuDish(MenuDishRequestDTO menuDishRequestDTO) {
        // 1. Tìm DishEntity và MenuEntity dựa trên IDs
        DishEntity dish = dishRepository.findById(menuDishRequestDTO.getDishId())
                .orElseThrow(() -> new ResourceNotFoundException("Dish not found with id " + menuDishRequestDTO.getDishId()));
        MenuEntity menu = menuRepository.findById(menuDishRequestDTO.getMenuId())
                .orElseThrow(() -> new ResourceNotFoundException("Menu not found with id " + menuDishRequestDTO.getMenuId()));

        // Tùy chọn: Kiểm tra xem mối quan hệ này đã tồn tại chưa để tránh trùng lặp (nếu mỗi cặp Dish-Menu là duy nhất)
        if (menuDishRepository.findByDishAndMenu(dish, menu).isPresent()) {
            throw new IllegalArgumentException("This dish is already part of this menu.");
        }

        // 2. Chuyển đổi DTO sang Entity và thiết lập các mối quan hệ
        MenuDishEntity menuDishEntity = menuDishMapper.toEntity(menuDishRequestDTO);
        menuDishEntity.setDish(dish);
        menuDishEntity.setMenu(menu);

        // 3. Lưu Entity
        MenuDishEntity savedMenuDish = menuDishRepository.save(menuDishEntity);
        return menuDishMapper.toResponseDTO(savedMenuDish);
    }

    /**
     * Cập nhật thông tin của một món ăn trong menu.
     *
     * @param id ID của MenuDish cần cập nhật.
     * @param menuDishRequestDTO DTO chứa thông tin cập nhật.
     * @return MenuDishResponseDTO của MenuDish đã cập nhật.
     * @throws ResourceNotFoundException nếu MenuDish, Dish hoặc Menu không tồn tại.
     */
    @Transactional
    public MenuDishResponseDTO updateMenuDish(int id, MenuDishRequestDTO menuDishRequestDTO) {
        MenuDishEntity existingMenuDish = menuDishRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MenuDish not found with id " + id));

        // Cập nhật các trường thông thường (price, discountPercentage)
        menuDishMapper.updateEntityFromDTO(menuDishRequestDTO, existingMenuDish);

        // Xử lý cập nhật mối quan hệ Dish (nếu ID thay đổi)
        if (!existingMenuDish.getDish().getId().equals(menuDishRequestDTO.getDishId())) {
            DishEntity newDish = dishRepository.findById(menuDishRequestDTO.getDishId())
                    .orElseThrow(() -> new ResourceNotFoundException("Dish not found with id " + menuDishRequestDTO.getDishId()));
            existingMenuDish.setDish(newDish);
        }

        // Xử lý cập nhật mối quan hệ Menu (nếu ID thay đổi)
        if (!existingMenuDish.getMenu().getId().equals(menuDishRequestDTO.getMenuId())) {
            MenuEntity newMenu = menuRepository.findById(menuDishRequestDTO.getMenuId())
                    .orElseThrow(() -> new ResourceNotFoundException("Menu not found with id " + menuDishRequestDTO.getMenuId()));
            existingMenuDish.setMenu(newMenu);
        }

        // Tùy chọn: Kiểm tra trùng lặp nếu cặp Dish-Menu thay đổi và tạo ra một cặp trùng lặp mới
        if (!existingMenuDish.getDish().getId().equals(menuDishRequestDTO.getDishId()) ||
            !existingMenuDish.getMenu().getId().equals(menuDishRequestDTO.getMenuId())) {
            if (menuDishRepository.findByDishAndMenu(existingMenuDish.getDish(), existingMenuDish.getMenu()).isPresent()) {
                // throw new IllegalArgumentException("This updated dish-menu relationship already exists.");
            }
        }

        MenuDishEntity updatedMenuDish = menuDishRepository.save(existingMenuDish);
        return menuDishMapper.toResponseDTO(updatedMenuDish);
    }

    /**
     * Xóa một món ăn khỏi menu theo ID của MenuDish.
     *
     * @param id ID của MenuDish cần xóa.
     * @throws ResourceNotFoundException nếu không tìm thấy.
     */
    public void deleteMenuDish(int id) {
        if (!menuDishRepository.existsById(id)) {
            throw new ResourceNotFoundException("MenuDish not found with id " + id);
        }
        // CascadeType.ALL và orphanRemoval=true trên discountDishes sẽ đảm bảo các bản ghi liên quan cũng bị xóa.
        menuDishRepository.deleteById(id);
    }
}