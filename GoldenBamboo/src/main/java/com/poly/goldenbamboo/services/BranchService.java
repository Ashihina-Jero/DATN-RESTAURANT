package com.poly.goldenbamboo.services;

import com.poly.goldenbamboo.dtos.BranchRequestDTO;
import com.poly.goldenbamboo.dtos.BranchResponseDTO;
import com.poly.goldenbamboo.entities.BranchEntity;
import com.poly.goldenbamboo.exceptions.ResourceNotFoundException; // Đảm bảo đã tạo class này
import com.poly.goldenbamboo.mappers.BranchMapper;
import com.poly.goldenbamboo.repositories.BranchRepository; // Đảm bảo đã tạo interface này
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BranchService {

    private final BranchRepository branchRepository;
    private final BranchMapper branchMapper;

    @Autowired
    public BranchService(BranchRepository branchRepository, BranchMapper branchMapper) {
        this.branchRepository = branchRepository;
        this.branchMapper = branchMapper;
    }

    /**
     * Lấy tất cả các chi nhánh.
     *
     * @return Danh sách BranchResponseDTO.
     */
    public List<BranchResponseDTO> getAllBranches() {
        List<BranchEntity> branches = branchRepository.findAll();
        return branches.stream()
                .map(branchMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lấy thông tin chi nhánh theo ID.
     *
     * @param id ID của chi nhánh.
     * @return BranchResponseDTO nếu tìm thấy.
     * @throws ResourceNotFoundException nếu không tìm thấy chi nhánh.
     */
    public BranchResponseDTO getBranchById(int id) {
        BranchEntity branch = branchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found with id " + id));
        return branchMapper.toResponseDTO(branch);
    }

    /**
     * Tạo một chi nhánh mới.
     *
     * @param branchRequestDTO DTO chứa thông tin chi nhánh cần tạo.
     * @return BranchResponseDTO của chi nhánh đã tạo.
     */
    public BranchResponseDTO createBranch(BranchRequestDTO branchRequestDTO) {
        BranchEntity branchEntity = branchMapper.toEntity(branchRequestDTO);
        // Có thể thêm logic nghiệp vụ khác ở đây trước khi lưu
        BranchEntity savedBranch = branchRepository.save(branchEntity);
        return branchMapper.toResponseDTO(savedBranch);
    }

    /**
     * Cập nhật thông tin chi nhánh.
     *
     * @param id ID của chi nhánh cần cập nhật.
     * @param branchRequestDTO DTO chứa thông tin cập nhật.
     * @return BranchResponseDTO của chi nhánh đã cập nhật.
     * @throws ResourceNotFoundException nếu không tìm thấy chi nhánh cần cập nhật.
     */
    public BranchResponseDTO updateBranch(int id, BranchRequestDTO branchRequestDTO) {
        BranchEntity existingBranch = branchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found with id " + id));

        // Sử dụng mapper để cập nhật các trường từ DTO vào Entity đã tồn tại
        branchMapper.updateEntityFromDTO(branchRequestDTO, existingBranch);

        // Có thể thêm logic nghiệp vụ khác ở đây trước khi lưu
        BranchEntity updatedBranch = branchRepository.save(existingBranch);
        return branchMapper.toResponseDTO(updatedBranch);
    }

    /**
     * Xóa một chi nhánh theo ID.
     *
     * @param id ID của chi nhánh cần xóa.
     * @throws ResourceNotFoundException nếu không tìm thấy chi nhánh cần xóa.
     */
    public void deleteBranch(int id) {
        if (!branchRepository.existsById(id)) {
            throw new ResourceNotFoundException("Branch not found with id " + id);
        }
        // Thêm logic kiểm tra ràng buộc ở đây (ví dụ: không cho xóa nếu có tài khoản, menu, order liên quan)
        // Nếu không có, bạn có thể xóa thẳng
        branchRepository.deleteById(id);
    }
}