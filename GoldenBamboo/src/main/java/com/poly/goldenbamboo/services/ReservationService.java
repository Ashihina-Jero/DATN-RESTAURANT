package com.poly.goldenbamboo.services;

import com.poly.goldenbamboo.dtos.ReservationDetailRequestDTO;
import com.poly.goldenbamboo.dtos.ReservationRequestDTO;
import com.poly.goldenbamboo.dtos.ReservationResponseDTO;
import com.poly.goldenbamboo.entities.ReservationEntity;
import com.poly.goldenbamboo.entities.AccountEntity;
import com.poly.goldenbamboo.entities.TableEntity;
import com.poly.goldenbamboo.entities.ReservationDetailEntity;
import com.poly.goldenbamboo.entities.MenuItemEntity; // Cần để lấy thông tin cho ReservationDetail
import com.poly.goldenbamboo.exceptions.ResourceNotFoundException;
import com.poly.goldenbamboo.mappers.ReservationMapper;
import com.poly.goldenbamboo.mappers.ReservationDetailMapper;
import com.poly.goldenbamboo.repositories.ReservationRepository;
import com.poly.goldenbamboo.repositories.AccountRepository;
import com.poly.goldenbamboo.repositories.TableRepository;
import com.poly.goldenbamboo.repositories.ReservationDetailRepository;
import com.poly.goldenbamboo.repositories.MenuItemRepository; // Hoặc ComboRepository/DishRepository

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final AccountRepository accountRepository;
    private final TableRepository tableRepository;
    private final ReservationDetailRepository reservationDetailRepository;
    private final MenuItemRepository menuItemRepository;
    private final ReservationMapper reservationMapper;
    private final ReservationDetailMapper reservationDetailMapper;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository, AccountRepository accountRepository,
                              TableRepository tableRepository, ReservationDetailRepository reservationDetailRepository,
                              MenuItemRepository menuItemRepository, ReservationMapper reservationMapper,
                              ReservationDetailMapper reservationDetailMapper) {
        this.reservationRepository = reservationRepository;
        this.accountRepository = accountRepository;
        this.tableRepository = tableRepository;
        this.reservationDetailRepository = reservationDetailRepository;
        this.menuItemRepository = menuItemRepository;
        this.reservationMapper = reservationMapper;
        this.reservationDetailMapper = reservationDetailMapper;
    }

    /**
     * Lấy tất cả các đặt bàn.
     *
     * @return Danh sách ReservationResponseDTO.
     */
    public List<ReservationResponseDTO> getAllReservations() {
        List<ReservationEntity> reservations = reservationRepository.findAll();
        return reservations.stream()
                .map(reservationMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lấy thông tin đặt bàn theo ID.
     *
     * @param id ID của đặt bàn.
     * @return ReservationResponseDTO nếu tìm thấy.
     * @throws ResourceNotFoundException nếu không tìm thấy đặt bàn.
     */
    public ReservationResponseDTO getReservationById(int id) {
        ReservationEntity reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id " + id));
        return reservationMapper.toResponseDTO(reservation);
    }

    /**
     * Tạo một đặt bàn mới.
     *
     * @param reservationRequestDTO DTO chứa thông tin đặt bàn cần tạo.
     * @return ReservationResponseDTO của đặt bàn đã tạo.
     * @throws ResourceNotFoundException nếu Account, hoặc Table không tồn tại.
     */
    @Transactional
    public ReservationResponseDTO createReservation(ReservationRequestDTO reservationRequestDTO) {
        ReservationEntity reservationEntity = reservationMapper.toEntity(reservationRequestDTO);

        // 1. Xử lý mối quan hệ Account
        AccountEntity account = accountRepository.findById(reservationRequestDTO.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id " + reservationRequestDTO.getAccountId()));
        reservationEntity.setAccount(account);

        // 2. Xử lý mối quan hệ Table
        TableEntity table = tableRepository.findById(reservationRequestDTO.getTableId())
                .orElseThrow(() -> new ResourceNotFoundException("Table not found with id " + reservationRequestDTO.getTableId()));
        reservationEntity.setTable(table);
        // Có thể cập nhật trạng thái bàn ở đây nếu cần (ví dụ: đang chờ)
        // table.setStatus(TableStatus.RESERVED);
        // tableRepository.save(table);

        // Lưu Reservation trước để có ID
        ReservationEntity savedReservation = reservationRepository.save(reservationEntity);

        // 3. Xử lý ReservationDetails
        List<ReservationDetailEntity> reservationDetails = new ArrayList<>();
        if (reservationRequestDTO.getReservationDetails() != null && !reservationRequestDTO.getReservationDetails().isEmpty()) {
            for (ReservationDetailRequestDTO detailDTO : reservationRequestDTO.getReservationDetails()) {
                MenuItemEntity menuItem = menuItemRepository.findById(detailDTO.getMenuItemId())
                        .orElseThrow(() -> new ResourceNotFoundException("MenuItem not found with id " + detailDTO.getMenuItemId()));

                ReservationDetailEntity reservationDetail = reservationDetailMapper.toEntity(detailDTO);
                reservationDetail.setReservation(savedReservation); // Thiết lập reservation đã lưu
                reservationDetail.setMenuItem(menuItem);
                reservationDetails.add(reservationDetail);
            }
            reservationDetailRepository.saveAll(reservationDetails); // Lưu tất cả chi tiết đặt bàn
            savedReservation.setReservationDetails(reservationDetails); // Cập nhật danh sách chi tiết vào Reservation
        }

        return reservationMapper.toResponseDTO(savedReservation);
    }

    /**
     * Cập nhật thông tin đặt bàn.
     *
     * @param id ID của đặt bàn cần cập nhật.
     * @param reservationRequestDTO DTO chứa thông tin cập nhật.
     * @return ReservationResponseDTO của đặt bàn đã cập nhật.
     * @throws ResourceNotFoundException nếu Reservation, Account hoặc Table không tồn tại.
     */
    @Transactional
    public ReservationResponseDTO updateReservation(int id, ReservationRequestDTO reservationRequestDTO) {
        ReservationEntity existingReservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id " + id));

        reservationMapper.updateEntityFromDTO(reservationRequestDTO, existingReservation); // Cập nhật các trường chính

        // 1. Xử lý cập nhật mối quan hệ Account
        if (!existingReservation.getAccount().getId().equals(reservationRequestDTO.getAccountId())) {
            AccountEntity newAccount = accountRepository.findById(reservationRequestDTO.getAccountId())
                    .orElseThrow(() -> new ResourceNotFoundException("Account not found with id " + reservationRequestDTO.getAccountId()));
            existingReservation.setAccount(newAccount);
        }

        // 2. Xử lý cập nhật mối quan hệ Table
        if (!existingReservation.getTable().getId().equals(reservationRequestDTO.getTableId())) {
            TableEntity newTable = tableRepository.findById(reservationRequestDTO.getTableId())
                    .orElseThrow(() -> new ResourceNotFoundException("Table not found with id " + reservationRequestDTO.getTableId()));
            existingReservation.setTable(newTable);
            // Có thể cập nhật trạng thái bàn cũ/mới nếu bàn thay đổi
        }

        // 3. Xử lý cập nhật ReservationDetails
        // Cách phổ biến là xóa tất cả các chi tiết cũ và tạo lại các chi tiết mới
        reservationDetailRepository.deleteAll(existingReservation.getReservationDetails()); // Xóa các chi tiết cũ
        existingReservation.getReservationDetails().clear(); // Dọn dẹp list trong entity

        if (reservationRequestDTO.getReservationDetails() != null && !reservationRequestDTO.getReservationDetails().isEmpty()) {
            List<ReservationDetailEntity> newReservationDetails = new ArrayList<>();
            for (ReservationDetailRequestDTO detailDTO : reservationRequestDTO.getReservationDetails()) {
                MenuItemEntity menuItem = menuItemRepository.findById(detailDTO.getMenuItemId())
                        .orElseThrow(() -> new ResourceNotFoundException("MenuItem not found with id " + detailDTO.getMenuItemId()));

                ReservationDetailEntity reservationDetail = reservationDetailMapper.toEntity(detailDTO);
                reservationDetail.setReservation(existingReservation); // Thiết lập reservation cha
                reservationDetail.setMenuItem(menuItem);
                newReservationDetails.add(reservationDetail);
            }
            reservationDetailRepository.saveAll(newReservationDetails); // Lưu các ReservationDetail mới
            existingReservation.setReservationDetails(newReservationDetails); // Cập nhật danh sách mới vào Reservation
        }

        ReservationEntity updatedReservation = reservationRepository.save(existingReservation); // Lưu lại Reservation đã được cập nhật
        return reservationMapper.toResponseDTO(updatedReservation);
    }

    /**
     * Xóa một đặt bàn theo ID.
     *
     * @param id ID của đặt bàn cần xóa.
     * @throws ResourceNotFoundException nếu không tìm thấy đặt bàn.
     */
    public void deleteReservation(int id) {
        if (!reservationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Reservation not found with id " + id);
        }
        // CascadeType.ALL và orphanRemoval=true trên reservationDetails sẽ đảm bảo các chi tiết đặt bàn cũng bị xóa.
        reservationRepository.deleteById(id);
    }
}