package com.poly.goldenbamboo.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poly.goldenbamboo.dtos.TableRequestDTO;
import com.poly.goldenbamboo.dtos.TableResponseDTO;
import com.poly.goldenbamboo.entities.BranchEntity;
import com.poly.goldenbamboo.entities.TableEntity;
import com.poly.goldenbamboo.entities.enums.TableStatus;
import com.poly.goldenbamboo.exceptions.ResourceNotFoundException;
import com.poly.goldenbamboo.mappers.TableMapper;
import com.poly.goldenbamboo.repositories.BranchRepository; // Cần Repository cho Branch
import com.poly.goldenbamboo.repositories.TableRepository; // Cần Repository cho Table

@Service
public class TableService {

    private final TableRepository tableRepository;
    private final BranchRepository branchRepository; // Inject BranchRepository
    private final TableMapper tableMapper;

    @Autowired
    public TableService(TableRepository tableRepository, BranchRepository branchRepository, TableMapper tableMapper) {
        this.tableRepository = tableRepository;
        this.branchRepository = branchRepository;
        this.tableMapper = tableMapper;
    }

    /**
     * Lấy tất cả các bàn.
     *
     * @return Danh sách TableResponseDTO.
     */
    public List<TableResponseDTO> getAllTables() {
        List<TableEntity> tables = tableRepository.findAll();
        return tables.stream()
                .map(tableMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lấy thông tin bàn theo ID.
     *
     * @param id ID của bàn.
     * @return TableResponseDTO nếu tìm thấy.
     * @throws ResourceNotFoundException nếu không tìm thấy bàn.
     */
    public TableResponseDTO getTableById(int id) {
        TableEntity table = tableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Table not found with id " + id));
        return tableMapper.toResponseDTO(table);
    }

    /**
     * Tạo một bàn mới.
     *
     * @param tableRequestDTO DTO chứa thông tin bàn cần tạo.
     * @return TableResponseDTO của bàn đã tạo.
     * @throws ResourceNotFoundException nếu Branch không tồn tại.
     * @throws IllegalArgumentException nếu số bàn đã tồn tại.
     */
    @Transactional
    public TableResponseDTO createTable(TableRequestDTO tableRequestDTO) {
        // Kiểm tra xem số bàn đã tồn tại trong chi nhánh này chưa (tùy chọn)
        if (tableRepository.findByNumberAndBranchId(tableRequestDTO.getNumber(), tableRequestDTO.getBranchId()).isPresent()) {
            throw new IllegalArgumentException("Table with number '" + tableRequestDTO.getNumber() + "' already exists in this branch.");
        }

        TableEntity tableEntity = tableMapper.toEntity(tableRequestDTO);

        // Xử lý mối quan hệ Branch (ManyToOne)
        BranchEntity branch = branchRepository.findById(tableRequestDTO.getBranchId())
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found with id " + tableRequestDTO.getBranchId()));
        tableEntity.setBranch(branch);

        // Đảm bảo trạng thái mặc định nếu không được cung cấp (tùy chọn)
        if (tableEntity.getStatus() == null) {
            tableEntity.setStatus(TableStatus.AVAILABLE); // Mặc định là AVAILABLE
        }

        TableEntity savedTable = tableRepository.save(tableEntity);
        return tableMapper.toResponseDTO(savedTable);
    }

    /**
     * Cập nhật thông tin bàn.
     *
     * @param id ID của bàn cần cập nhật.
     * @param tableRequestDTO DTO chứa thông tin cập nhật.
     * @return TableResponseDTO của bàn đã cập nhật.
     * @throws ResourceNotFoundException nếu Table hoặc Branch không tồn tại.
     * @throws IllegalArgumentException nếu số bàn cập nhật đã tồn tại ở chi nhánh đó.
     */
    @Transactional
    public TableResponseDTO updateTable(int id, TableRequestDTO tableRequestDTO) {
        TableEntity existingTable = tableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Table not found with id " + id));

        // Kiểm tra xem số bàn mới có trùng với bàn khác trong cùng chi nhánh không
        if (!existingTable.getNumber().equals(tableRequestDTO.getNumber()) ||
            !existingTable.getBranch().getId().equals(tableRequestDTO.getBranchId())) {
            // Chỉ kiểm tra nếu số bàn HOẶC chi nhánh thay đổi
            tableRepository.findByNumberAndBranchId(tableRequestDTO.getNumber(), tableRequestDTO.getBranchId())
                .ifPresent(tableWithSameNumber -> {
                    if (tableWithSameNumber.getId() != existingTable.getId()) { // Đảm bảo không so sánh với chính nó
                        throw new IllegalArgumentException("Table with number '" + tableRequestDTO.getNumber() + "' already exists in this branch.");
                    }
                });
        }

        tableMapper.updateEntityFromDTO(tableRequestDTO, existingTable); // Cập nhật các trường chính

        // Xử lý cập nhật mối quan hệ Branch (nếu ID thay đổi)
        if (!existingTable.getBranch().getId().equals(tableRequestDTO.getBranchId())) {
            BranchEntity newBranch = branchRepository.findById(tableRequestDTO.getBranchId())
                    .orElseThrow(() -> new ResourceNotFoundException("Branch not found with id " + tableRequestDTO.getBranchId()));
            existingTable.setBranch(newBranch);
        }

        TableEntity updatedTable = tableRepository.save(existingTable);
        return tableMapper.toResponseDTO(updatedTable);
    }

    /**
     * Xóa một bàn theo ID.
     *
     * @param id ID của bàn cần xóa.
     * @throws ResourceNotFoundException nếu không tìm thấy bàn.
     * @throws IllegalStateException nếu bàn đang có đơn hàng hoặc đặt chỗ liên quan.
     */
    public void deleteTable(int id) {
        TableEntity tableToDelete = tableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Table not found with id " + id));

        // Logic nghiệp vụ: Kiểm tra nếu bàn đang được sử dụng (có order hoặc reservation)
        // Nếu các List orders và reservations được tải LAZY, bạn cần truy cập chúng để kích hoạt tải dữ liệu
        // Hoặc sử dụng các phương thức existsByTable trong OrderRepository/ReservationRepository
        if (!tableToDelete.getOrders().isEmpty()) { // Sẽ kích hoạt tải lazy nếu cần
            throw new IllegalStateException("Cannot delete table: There are existing orders associated with it.");
        }
        if (!tableToDelete.getReservations().isEmpty()) { // Sẽ kích hoạt tải lazy nếu cần
            throw new IllegalStateException("Cannot delete table: There are existing reservations associated with it.");
        }
        // Hoặc cách hiệu quả hơn bằng Repository nếu các phương thức tương ứng được tạo:
        // if (orderRepository.existsByTable(tableToDelete)) { ... }
        // if (reservationRepository.existsByTable(tableToDelete)) { ... }

        tableRepository.deleteById(id);
    }
}