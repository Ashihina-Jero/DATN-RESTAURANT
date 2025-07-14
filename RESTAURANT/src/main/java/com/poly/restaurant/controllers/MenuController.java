package com.poly.restaurant.controllers;

import com.poly.restaurant.dtos.AddComboToMenuRequestDTO;
import com.poly.restaurant.dtos.AddDishToMenuRequestDTO; // Thêm import này
import com.poly.restaurant.dtos.MenuRequestDTO;
import com.poly.restaurant.dtos.MenuResponseDTO;
import com.poly.restaurant.services.MenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*; // Thêm import này

@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    /**
     * API để tạo một menu mới
     */
    @PostMapping // <-- API BẠN ĐANG CẦN
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<MenuResponseDTO> createMenu(@Valid @RequestBody MenuRequestDTO request) {
        MenuResponseDTO createdMenu = menuService.createMenu(request);
        return new ResponseEntity<>(createdMenu, HttpStatus.CREATED);
    }

    /**
     * API để lấy chi tiết một menu
     */
    @GetMapping("/{id}")
    public ResponseEntity<MenuResponseDTO> getMenuById(@PathVariable Integer id) {
        MenuResponseDTO menu = menuService.getMenuById(id);
        return ResponseEntity.ok(menu);
    }

    /**
     * API để thêm món ăn vào menu
     */
    @PostMapping("/{menuId}/dishes")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<MenuResponseDTO> addDishToMenu(
            @PathVariable Integer menuId,
            @Valid @RequestBody AddDishToMenuRequestDTO request) {
        MenuResponseDTO updatedMenu = menuService.addDishToMenu(menuId, request);
        return ResponseEntity.ok(updatedMenu);
    }

    /**
     * API để thêm combo vào menu
     */
    @PostMapping("/{menuId}/combos")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<MenuResponseDTO> addComboToMenu(
            @PathVariable Integer menuId, // Sửa lại kiểu dữ liệu thành Long
            @Valid @RequestBody AddComboToMenuRequestDTO request) {
        MenuResponseDTO updatedMenu = menuService.addComboToMenu(menuId, request);
        return ResponseEntity.ok(updatedMenu);
    }
}