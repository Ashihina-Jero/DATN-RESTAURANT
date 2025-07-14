package com.poly.goldenbamboo.controllers;

import com.poly.goldenbamboo.dtos.MenuRequestDTO;
import com.poly.goldenbamboo.dtos.MenuResponseDTO;
import com.poly.goldenbamboo.services.MenuService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Đánh dấu đây là một Controller REST
@RequestMapping("/api/menus") // Đường dẫn gốc cho tất cả các endpoint trong Controller này
public class MenuController {

    private final MenuService menuService;

    @Autowired
    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    /**
     * Lấy tất cả các menu.
     * GET /api/menus
     *
     * @return ResponseEntity chứa danh sách MenuResponseDTO và HttpStatus.OK.
     */
    @GetMapping
    public ResponseEntity<List<MenuResponseDTO>> getAllMenus() {
        List<MenuResponseDTO> menus = menuService.getAllMenus();
        return new ResponseEntity<>(menus, HttpStatus.OK);
    }

    /**
     * Lấy thông tin một menu theo ID.
     * GET /api/menus/{id}
     *
     * @param id ID của menu cần lấy.
     * @return ResponseEntity chứa MenuResponseDTO và HttpStatus.OK nếu tìm thấy,
     * hoặc HttpStatus.NOT_FOUND nếu không tìm thấy (được xử lý bởi ControllerAdvice).
     */
    @GetMapping("/{id}")
    public ResponseEntity<MenuResponseDTO> getMenuById(@PathVariable int id) {
        MenuResponseDTO menu = menuService.getMenuById(id);
        return new ResponseEntity<>(menu, HttpStatus.OK);
    }

    /**
     * Tạo một menu mới.
     * POST /api/menus
     *
     * @param menuRequestDTO DTO chứa thông tin menu cần tạo.
     * @return ResponseEntity chứa MenuResponseDTO của menu đã tạo và HttpStatus.CREATED.
     */
    @PostMapping
    public ResponseEntity<MenuResponseDTO> createMenu(@Valid @RequestBody MenuRequestDTO menuRequestDTO) {
        MenuResponseDTO createdMenu = menuService.createMenu(menuRequestDTO);
        return new ResponseEntity<>(createdMenu, HttpStatus.CREATED);
    }

    /**
     * Cập nhật thông tin một menu theo ID.
     * PUT /api/menus/{id}
     *
     * @param id ID của menu cần cập nhật.
     * @param menuRequestDTO DTO chứa thông tin cập nhật.
     * @return ResponseEntity chứa MenuResponseDTO của menu đã cập nhật và HttpStatus.OK.
     * hoặc HttpStatus.NOT_FOUND nếu không tìm thấy (được xử lý bởi ControllerAdvice).
     */
    @PutMapping("/{id}")
    public ResponseEntity<MenuResponseDTO> updateMenu(@PathVariable int id, @Valid @RequestBody MenuRequestDTO menuRequestDTO) {
        MenuResponseDTO updatedMenu = menuService.updateMenu(id, menuRequestDTO);
        return new ResponseEntity<>(updatedMenu, HttpStatus.OK);
    }

    /**
     * Xóa một menu theo ID.
     * DELETE /api/menus/{id}
     *
     * @param id ID của menu cần xóa.
     * @return ResponseEntity với HttpStatus.NO_CONTENT.
     * hoặc HttpStatus.NOT_FOUND nếu không tìm thấy (được xử lý bởi ControllerAdvice).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenu(@PathVariable int id) {
        menuService.deleteMenu(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
    }
}