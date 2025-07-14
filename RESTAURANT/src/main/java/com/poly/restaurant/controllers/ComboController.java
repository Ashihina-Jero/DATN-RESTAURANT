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

import com.poly.restaurant.dtos.ComboRequestDTO;
import com.poly.restaurant.dtos.ComboResponseDTO;
import com.poly.restaurant.services.ComboService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/combos") // Đường dẫn gốc cho các API của Combo
public class ComboController {

    private final ComboService comboService;

    @Autowired
    public ComboController(ComboService comboService) {
        this.comboService = comboService;
    }

    /**
     * Lấy tất cả các combo.
     * GET /api/combos
     *
     * @return ResponseEntity chứa danh sách ComboResponseDTO và HttpStatus.OK.
     */
    @GetMapping
    public ResponseEntity<List<ComboResponseDTO>> getAllCombos() {
        List<ComboResponseDTO> combos = comboService.getAllCombos();
        return new ResponseEntity<>(combos, HttpStatus.OK);
    }

    /**
     * Lấy thông tin một combo theo ID.
     * GET /api/combos/{id}
     *
     * @param id ID của combo cần lấy.
     * @return ResponseEntity chứa ComboResponseDTO và HttpStatus.OK nếu tìm thấy.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ComboResponseDTO> getComboById(@PathVariable int id) {
        ComboResponseDTO combo = comboService.getComboById(id);
        return new ResponseEntity<>(combo, HttpStatus.OK);
    }

    /**
     * Tạo một combo mới.
     * POST /api/combos
     *
     * @param comboRequestDTO DTO chứa thông tin combo cần tạo.
     * @return ResponseEntity chứa ComboResponseDTO của combo đã tạo và HttpStatus.CREATED.
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ComboResponseDTO> createCombo(@Valid @RequestBody ComboRequestDTO request) {
        ComboResponseDTO createdCombo = comboService.createCombo(request);
        return new ResponseEntity<>(createdCombo, HttpStatus.CREATED);
    }

    /**
     * Cập nhật thông tin một combo theo ID.
     * PUT /api/combos/{id}
     *
     * @param id              ID của combo cần cập nhật.
     * @param comboRequestDTO DTO chứa thông tin cập nhật.
     * @return ResponseEntity chứa ComboResponseDTO của combo đã cập nhật và HttpStatus.OK.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ComboResponseDTO> updateCombo(@PathVariable int id, @Valid @RequestBody ComboRequestDTO comboRequestDTO) {
        ComboResponseDTO updatedCombo = comboService.updateCombo(id, comboRequestDTO);
        return new ResponseEntity<>(updatedCombo, HttpStatus.OK);
    }

    /**
     * Xóa một combo theo ID.
     * DELETE /api/combos/{id}
     *
     * @param id ID của combo cần xóa.
     * @return ResponseEntity với HttpStatus.NO_CONTENT.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<Void> deleteCombo(@PathVariable int id) {
        comboService.deleteCombo(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}