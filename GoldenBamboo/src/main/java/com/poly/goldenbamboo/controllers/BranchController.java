package com.poly.goldenbamboo.controllers;

import com.poly.goldenbamboo.dtos.BranchRequestDTO;
import com.poly.goldenbamboo.dtos.BranchResponseDTO;
import com.poly.goldenbamboo.services.BranchService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@RestController
//@RequestMapping("/api/branches") 
public class BranchController {

    private final BranchService branchService;

    @Autowired
    public BranchController(BranchService branchService) {
        this.branchService = branchService;
    }

    /**
     * Lấy tất cả các chi nhánh.
     * GET /api/branches
     *
     * @return ResponseEntity chứa danh sách BranchResponseDTO và HttpStatus.OK.
     */
    @GetMapping
    public ResponseEntity<List<BranchResponseDTO>> getAllBranches() {
        List<BranchResponseDTO> branches = branchService.getAllBranches();
        return new ResponseEntity<>(branches, HttpStatus.OK);
    }

    /**
     * Lấy thông tin một chi nhánh theo ID.
     * GET /api/branches/{id}
     *
     * @param id ID của chi nhánh cần lấy.
     * @return ResponseEntity chứa BranchResponseDTO và HttpStatus.OK nếu tìm thấy,
     * hoặc HttpStatus.NOT_FOUND nếu không tìm thấy (được xử lý bởi ControllerAdvice).
     */
    @GetMapping("/{id}")
    public ResponseEntity<BranchResponseDTO> getBranchById(@PathVariable int id) {
        BranchResponseDTO branch = branchService.getBranchById(id);
        return new ResponseEntity<>(branch, HttpStatus.OK);
    }

    /**
     * Tạo một chi nhánh mới.
     * POST /api/branches
     *
     * @param branchRequestDTO DTO chứa thông tin chi nhánh cần tạo.
     * @return ResponseEntity chứa BranchResponseDTO của chi nhánh đã tạo và HttpStatus.CREATED.
     */
    @PostMapping
    public ResponseEntity<BranchResponseDTO> createBranch(@Valid @RequestBody BranchRequestDTO branchRequestDTO) {
        BranchResponseDTO createdBranch = branchService.createBranch(branchRequestDTO);
        return new ResponseEntity<>(createdBranch, HttpStatus.CREATED);
    }

    /**
     * Cập nhật thông tin một chi nhánh theo ID.
     * PUT /api/branches/{id}
     *
     * @param id ID của chi nhánh cần cập nhật.
     * @param branchRequestDTO DTO chứa thông tin cập nhật.
     * @return ResponseEntity chứa BranchResponseDTO của chi nhánh đã cập nhật và HttpStatus.OK.
     * hoặc HttpStatus.NOT_FOUND nếu không tìm thấy (được xử lý bởi ControllerAdvice).
     */
    @PutMapping("/{id}")
    public ResponseEntity<BranchResponseDTO> updateBranch(@PathVariable int id, @Valid @RequestBody BranchRequestDTO branchRequestDTO) {
        BranchResponseDTO updatedBranch = branchService.updateBranch(id, branchRequestDTO);
        return new ResponseEntity<>(updatedBranch, HttpStatus.OK);
    }

    /**
     * Xóa một chi nhánh theo ID.
     * DELETE /api/branches/{id}
     *
     * @param id ID của chi nhánh cần xóa.
     * @return ResponseEntity với HttpStatus.NO_CONTENT.
     * hoặc HttpStatus.NOT_FOUND nếu không tìm thấy (được xử lý bởi ControllerAdvice).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBranch(@PathVariable int id) {
        branchService.deleteBranch(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
    }
}