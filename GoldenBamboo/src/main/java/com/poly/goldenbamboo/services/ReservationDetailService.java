package com.poly.goldenbamboo.services;

import com.poly.goldenbamboo.dtos.ReservationDetailRequestDTO;
import com.poly.goldenbamboo.dtos.ReservationDetailResponseDTO;
import com.poly.goldenbamboo.entities.ReservationDetailEntity;
import com.poly.goldenbamboo.entities.MenuItemEntity;
import com.poly.goldenbamboo.entities.ReservationEntity;
import com.poly.goldenbamboo.exceptions.ResourceNotFoundException;
import com.poly.goldenbamboo.mappers.ReservationDetailMapper;
import com.poly.goldenbamboo.repositories.ReservationDetailRepository;
import com.poly.goldenbamboo.repositories.MenuItemRepository; // Giả định bạn có MenuItemRepository
import com.poly.goldenbamboo.repositories.ReservationRepository; // Cần Repository cho Reservation

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationDetailService {

    private final ReservationDetailRepository reservationDetailRepository;
    private final MenuItemRepository menuItemRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationDetailMapper reservationDetailMapper;

    @Autowired
    public ReservationDetailService(ReservationDetailRepository reservationDetailRepository,
                                    MenuItemRepository menuItemRepository,
                                    ReservationRepository reservationRepository,
                                    ReservationDetailMapper reservationDetailMapper) {
        this.reservationDetailRepository = reservationDetailRepository;
        this.menuItemRepository = menuItemRepository;
        this.reservationRepository = reservationRepository;
        this.reservationDetailMapper = reservationDetailMapper;
    }

    /**
     * Lấy tất cả các chi tiết đặt bàn.
     *
     * @return Danh sách ReservationDetailResponseDTO.
     */
    public List<ReservationDetailResponseDTO> getAllReservationDetails() {
        List<ReservationDetailEntity> reservationDetails = reservationDetailRepository.findAll();
        return reservationDetails.stream()
                .map(reservationDetailMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lấy thông tin chi tiết đặt bàn theo ID.
     *
     * @param id ID của chi tiết đặt bàn.
     * @return ReservationDetailResponseDTO nếu tìm thấy.
     * @throws ResourceNotFoundException nếu không tìm thấy.
     */
    public ReservationDetailResponseDTO getReservationDetailById(int id) {
        ReservationDetailEntity reservationDetail = reservationDetailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ReservationDetail not found with id " + id));
        return reservationDetailMapper.toResponseDTO(reservationDetail);
    }

    /**
     * Tạo một chi tiết đặt bàn mới.
     * Lưu ý: Thường được gọi từ ReservationService khi tạo/cập nhật Reservation.
     *
     * @param reservationId ID của đặt bàn cha.
     * @param reservationDetailRequestDTO DTO chứa thông tin chi tiết đặt bàn.
     * @return ReservationDetailResponseDTO của chi tiết đặt bàn đã tạo.
     * @throws ResourceNotFoundException nếu Reservation hoặc MenuItem không tồn tại.
     */
    @Transactional
    public ReservationDetailResponseDTO createReservationDetail(int reservationId, ReservationDetailRequestDTO reservationDetailRequestDTO) {
        // 1. Tìm ReservationEntity cha
        ReservationEntity reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id " + reservationId));

        // 2. Tìm MenuItemEntity (Combo hoặc Dish)
        MenuItemEntity menuItem = menuItemRepository.findById(reservationDetailRequestDTO.getMenuItemId())
                .orElseThrow(() -> new ResourceNotFoundException("MenuItem not found with id " + reservationDetailRequestDTO.getMenuItemId()));

        // 3. Chuyển đổi DTO sang Entity và thiết lập các mối quan hệ
        ReservationDetailEntity reservationDetailEntity = reservationDetailMapper.toEntity(reservationDetailRequestDTO);
        reservationDetailEntity.setReservation(reservation);
        reservationDetailEntity.setMenuItem(menuItem);

        // 4. Lưu Entity
        ReservationDetailEntity savedReservationDetail = reservationDetailRepository.save(reservationDetailEntity);
        return reservationDetailMapper.toResponseDTO(savedReservationDetail);
    }

    /**
     * Cập nhật thông tin chi tiết đặt bàn.
     * Lưu ý: Thường được gọi từ ReservationService khi tạo/cập nhật Reservation.
     *
     * @param id ID của chi tiết đặt bàn cần cập nhật.
     * @param reservationDetailRequestDTO DTO chứa thông tin cập nhật.
     * @return ReservationDetailResponseDTO của chi tiết đặt bàn đã cập nhật.
     * @throws ResourceNotFoundException nếu ReservationDetail hoặc MenuItem không tồn tại.
     */
    @Transactional
    public ReservationDetailResponseDTO updateReservationDetail(int id, ReservationDetailRequestDTO reservationDetailRequestDTO) {
        ReservationDetailEntity existingReservationDetail = reservationDetailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ReservationDetail not found with id " + id));

        // Cập nhật các trường thông thường
        reservationDetailMapper.updateEntityFromDTO(reservationDetailRequestDTO, existingReservationDetail);

        // Xử lý cập nhật MenuItem (nếu ID thay đổi)
        if (!existingReservationDetail.getMenuItem().getId().equals(reservationDetailRequestDTO.getMenuItemId())) {
            MenuItemEntity newMenuItem = menuItemRepository.findById(reservationDetailRequestDTO.getMenuItemId())
                    .orElseThrow(() -> new ResourceNotFoundException("MenuItem not found with id " + reservationDetailRequestDTO.getMenuItemId()));
            existingReservationDetail.setMenuItem(newMenuItem);
        }

        ReservationDetailEntity updatedReservationDetail = reservationDetailRepository.save(existingReservationDetail);
        return reservationDetailMapper.toResponseDTO(updatedReservationDetail);
    }

    /**
     * Xóa một chi tiết đặt bàn.
     * Lưu ý: Thường được gọi từ ReservationService khi xóa một Reservation hoặc loại bỏ một item khỏi Reservation.
     *
     * @param id ID của chi tiết đặt bàn cần xóa.
     * @throws ResourceNotFoundException nếu không tìm thấy.
     */
    public void deleteReservationDetail(int id) {
        if (!reservationDetailRepository.existsById(id)) {
            throw new ResourceNotFoundException("ReservationDetail not found with id " + id);
        }
        reservationDetailRepository.deleteById(id);
    }
}