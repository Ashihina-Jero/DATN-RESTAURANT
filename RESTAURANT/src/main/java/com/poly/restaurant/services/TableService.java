package com.poly.restaurant.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poly.restaurant.dtos.TableRequestDTO;
import com.poly.restaurant.dtos.TableResponseDTO;
import com.poly.restaurant.entities.BranchEntity;
import com.poly.restaurant.entities.TableEntity;
import com.poly.restaurant.entities.enums.OrderStatus;
import com.poly.restaurant.entities.enums.TableStatus;
import com.poly.restaurant.exceptions.ResourceNotFoundException;
import com.poly.restaurant.mappers.TableMapper;
import com.poly.restaurant.repositories.BranchRepository;
import com.poly.restaurant.repositories.OrderRepository;
import com.poly.restaurant.repositories.TableRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TableService {

	private final TableRepository tableRepository;
	private final TableMapper tableMapper;
	private final BranchRepository branchRepository;
	private final OrderRepository orderRepository;

	public List<TableResponseDTO> getAllTables() {
		return tableRepository.findAll().stream().map(tableMapper::toResponseDTO).collect(Collectors.toList());
	}

	public TableResponseDTO getTableById(Integer id) {
		TableEntity table = tableRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bàn với ID: " + id));
		return tableMapper.toResponseDTO(table);
	}

	@Transactional
	public TableResponseDTO createTable(TableRequestDTO tableDTO) {
		tableRepository.findByName(tableDTO.getName()).ifPresent(t -> {
			throw new IllegalArgumentException("Tên bàn '" + tableDTO.getName() + "' đã tồn tại.");
		});
		BranchEntity branch = branchRepository.findById(tableDTO.getBranchId()).orElseThrow(
				() -> new ResourceNotFoundException("Không tìm thấy chi nhánh với ID: " + tableDTO.getBranchId()));

		TableEntity tableEntity = tableMapper.toEntity(tableDTO);
		tableEntity.setStatus(TableStatus.AVAILABLE);
		tableEntity.setBranch(branch);

		TableEntity savedTable = tableRepository.save(tableEntity);
		return tableMapper.toResponseDTO(savedTable);
	}

	@Transactional
	public TableResponseDTO updateTable(Integer id, TableRequestDTO tableDTO) {
		TableEntity existingTable = tableRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bàn với ID: " + id));

		if (!existingTable.getName().equals(tableDTO.getName())) {
			tableRepository.findByName(tableDTO.getName()).ifPresent(t -> {
				if (!t.getId().equals(id)) {
					throw new IllegalArgumentException("Tên bàn '" + tableDTO.getName() + "' đã tồn tại.");
				}
			});
		}

		tableMapper.updateEntityFromDTO(tableDTO, existingTable);
		TableEntity updatedTable = tableRepository.save(existingTable);
		return tableMapper.toResponseDTO(updatedTable);
	}

	@Transactional
	public void deleteTable(Integer id) {
		TableEntity tableToDelete = tableRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bàn với ID: " + id));

		if (tableToDelete.getStatus() == TableStatus.OCCUPIED) {
			throw new IllegalStateException("Không thể xóa bàn đang có khách ngồi.");
		}

		tableRepository.delete(tableToDelete);
	}

	@Transactional
	public TableResponseDTO updateTableStatus(Integer id, TableStatus newStatus) {
		TableEntity table = tableRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bàn với ID: " + id));

		// Bạn có thể thêm các quy tắc nghiệp vụ ở đây nếu cần
		// Ví dụ: không cho đổi bàn đang có đơn hàng (OCCUPIED) thành TRỐNG (AVAILABLE)
		// một cách tùy tiện
		if (table.getStatus() == TableStatus.OCCUPIED && newStatus == TableStatus.AVAILABLE) {
			// Kiểm tra xem bàn này còn đơn hàng nào chưa thanh toán không
			// (Cần thêm logic vào OrderRepository)
			boolean hasUnpaidOrders = orderRepository.existsByTableAndStatusNot(table, OrderStatus.COMPLETED);
			if (hasUnpaidOrders) {
				throw new IllegalStateException("Không thể giải phóng bàn khi còn đơn hàng chưa thanh toán.");
			}
		}

		table.setStatus(newStatus);
		TableEntity updatedTable = tableRepository.save(table);
		return tableMapper.toResponseDTO(updatedTable);
	}
}