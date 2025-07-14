package com.poly.restaurant.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poly.restaurant.dtos.TableRequestDTO;
import com.poly.restaurant.dtos.TableResponseDTO;
import com.poly.restaurant.dtos.TableStatusUpdateDTO;
import com.poly.restaurant.services.TableService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tables")
public class TableController {

    private final TableService tableService;

    @Autowired
    public TableController(TableService tableService) {
        this.tableService = tableService;
    }

    /**
     * Lấy tất cả các bàn.
     * GET /api/tables
     */
    @GetMapping
    public ResponseEntity<List<TableResponseDTO>> getAllTables() {
        List<TableResponseDTO> tables = tableService.getAllTables();
        return new ResponseEntity<>(tables, HttpStatus.OK);
    }

    /**
     * Lấy thông tin một bàn theo ID.
     * GET /api/tables/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<TableResponseDTO> getTableById(@PathVariable int id) {
        TableResponseDTO table = tableService.getTableById(id);
        return new ResponseEntity<>(table, HttpStatus.OK);
    }

    /**
     * Tạo một bàn mới.
     * POST /api/tables
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<TableResponseDTO> createTable(@Valid @RequestBody TableRequestDTO tableDTO) {
        TableResponseDTO createdTable = tableService.createTable(tableDTO);
        return new ResponseEntity<>(createdTable, HttpStatus.CREATED);
    }

    /**
     * Cập nhật thông tin một bàn.
     * PUT /api/tables/{id}
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<TableResponseDTO> updateTable(@PathVariable int id, @Valid @RequestBody TableRequestDTO tableDTO) {
        TableResponseDTO updatedTable = tableService.updateTable(id, tableDTO);
        return new ResponseEntity<>(updatedTable, HttpStatus.OK);
    }

    /**
     * Xóa một bàn.
     * DELETE /api/tables/{id}
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<Void> deleteTable(@PathVariable int id) {
        tableService.deleteTable(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    /**
     * Cập nhật trạng thái của một bàn.
     * PATCH /api/tables/{id}/status
     */
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STAFF')")
    public ResponseEntity<TableResponseDTO> updateTableStatus(
            @PathVariable int id,
            @Valid @RequestBody TableStatusUpdateDTO statusUpdateDTO) {
                
        TableResponseDTO updatedTable = tableService.updateTableStatus(id, statusUpdateDTO.getStatus());
        return ResponseEntity.ok(updatedTable);
    }
}