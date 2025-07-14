package com.poly.goldenbamboo.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Đảm bảo giao dịch

import com.poly.goldenbamboo.dtos.DiscountComboRequestDTO;
import com.poly.goldenbamboo.dtos.DiscountComboResponseDTO;
import com.poly.goldenbamboo.entities.ComboEntity;    // Giả định là ComboEntity
import com.poly.goldenbamboo.entities.DiscountComboEntity;
import com.poly.goldenbamboo.entities.DiscountEntity; // Cần import
import com.poly.goldenbamboo.exceptions.ResourceNotFoundException;
import com.poly.goldenbamboo.mappers.DiscountComboMapper;
import com.poly.goldenbamboo.repositories.ComboRepository;    // Cần Repository cho Combo
import com.poly.goldenbamboo.repositories.DiscountComboRepository;
import com.poly.goldenbamboo.repositories.DiscountRepository; // Cần Repository cho Discount

@Service
public class DiscountComboService {

    private final DiscountComboRepository discountComboRepository;
    private final ComboRepository comboRepository;    // Inject ComboRepository
    private final DiscountRepository discountRepository; // Inject DiscountRepository
    private final DiscountComboMapper discountComboMapper;

    @Autowired
    public DiscountComboService(DiscountComboRepository discountComboRepository,
                                ComboRepository comboRepository,
                                DiscountRepository discountRepository,
                                DiscountComboMapper discountComboMapper) {
        this.discountComboRepository = discountComboRepository;
        this.comboRepository = comboRepository;
        this.discountRepository = discountRepository;
        this.discountComboMapper = discountComboMapper;
    }

    /**
     * Lấy tất cả các giảm giá áp dụng cho combo.
     *
     * @return Danh sách DiscountComboResponseDTO.
     */
    public List<DiscountComboResponseDTO> getAllDiscountCombos() {
        List<DiscountComboEntity> discountCombos = discountComboRepository.findAll();
        return discountCombos.stream()
                .map(discountComboMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lấy thông tin DiscountCombo theo ID.
     *
     * @param id ID của DiscountCombo.
     * @return DiscountComboResponseDTO nếu tìm thấy.
     * @throws ResourceNotFoundException nếu không tìm thấy.
     */
    public DiscountComboResponseDTO getDiscountComboById(int id) {
        DiscountComboEntity discountCombo = discountComboRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DiscountCombo not found with id " + id));
        return discountComboMapper.toResponseDTO(discountCombo);
    }

    /**
     * Tạo một liên kết giảm giá cho combo mới.
     *
     * @param discountComboRequestDTO DTO chứa thông tin DiscountCombo cần tạo.
     * @return DiscountComboResponseDTO của DiscountCombo đã tạo.
     * @throws ResourceNotFoundException nếu Combo hoặc Discount không tồn tại.
     * @throws IllegalArgumentException nếu mối quan hệ đã tồn tại (nếu bạn muốn duy nhất).
     */
    @Transactional
    public DiscountComboResponseDTO createDiscountCombo(DiscountComboRequestDTO discountComboRequestDTO) {
        // 1. Tìm ComboEntity và DiscountEntity dựa trên IDs
        ComboEntity combo = comboRepository.findById(discountComboRequestDTO.getComboId())
                .orElseThrow(() -> new ResourceNotFoundException("Combo not found with id " + discountComboRequestDTO.getComboId()));
        DiscountEntity discount = discountRepository.findById(discountComboRequestDTO.getDiscountId())
                .orElseThrow(() -> new ResourceNotFoundException("Discount not found with id " + discountComboRequestDTO.getDiscountId()));

        // Tùy chọn: Kiểm tra xem mối quan hệ này đã tồn tại chưa để tránh trùng lặp
        if (discountComboRepository.findByComboAndDiscount(combo, discount).isPresent()) {
            throw new IllegalArgumentException("This discount is already applied to this combo.");
        }

        // 2. Chuyển đổi DTO sang Entity và thiết lập các mối quan hệ
        DiscountComboEntity discountComboEntity = discountComboMapper.toEntity(discountComboRequestDTO);
        discountComboEntity.setCombo(combo);
        discountComboEntity.setDiscount(discount);

        // 3. Lưu Entity
        DiscountComboEntity savedDiscountCombo = discountComboRepository.save(discountComboEntity);
        return discountComboMapper.toResponseDTO(savedDiscountCombo);
    }

    /**
     * Cập nhật thông tin giảm giá cho combo.
     *
     * @param id ID của DiscountCombo cần cập nhật.
     * @param discountComboRequestDTO DTO chứa thông tin cập nhật.
     * @return DiscountComboResponseDTO của DiscountCombo đã cập nhật.
     * @throws ResourceNotFoundException nếu DiscountCombo, Combo hoặc Discount không tồn tại.
     */
    @Transactional
    public DiscountComboResponseDTO updateDiscountCombo(int id, DiscountComboRequestDTO discountComboRequestDTO) {
        DiscountComboEntity existingDiscountCombo = discountComboRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DiscountCombo not found with id " + id));

        // Cập nhật các trường thông thường (ví dụ: discountPercentage)
        discountComboMapper.updateEntityFromDTO(discountComboRequestDTO, existingDiscountCombo);

        // Xử lý cập nhật mối quan hệ Combo (nếu ID thay đổi)
        if (!existingDiscountCombo.getCombo().getId().equals(discountComboRequestDTO.getComboId())) {
            ComboEntity newCombo = comboRepository.findById(discountComboRequestDTO.getComboId())
                    .orElseThrow(() -> new ResourceNotFoundException("Combo not found with id " + discountComboRequestDTO.getComboId()));
            existingDiscountCombo.setCombo(newCombo);
        }

        // Xử lý cập nhật mối quan hệ Discount (nếu ID thay đổi)
        if (!existingDiscountCombo.getDiscount().getId().equals(discountComboRequestDTO.getDiscountId())) {
            DiscountEntity newDiscount = discountRepository.findById(discountComboRequestDTO.getDiscountId())
                    .orElseThrow(() -> new ResourceNotFoundException("Discount not found with id " + discountComboRequestDTO.getDiscountId()));
            existingDiscountCombo.setDiscount(newDiscount);
        }

        // Tùy chọn: Kiểm tra trùng lặp nếu cả combo và discount đều thay đổi và tạo ra một cặp trùng lặp mới
        if (!existingDiscountCombo.getCombo().getId().equals(discountComboRequestDTO.getComboId()) ||
            !existingDiscountCombo.getDiscount().getId().equals(discountComboRequestDTO.getDiscountId())) {
            if (discountComboRepository.findByComboAndDiscount(existingDiscountCombo.getCombo(), existingDiscountCombo.getDiscount()).isPresent()) {
                 // Tránh cập nhật thành một cặp Combo-Discount đã tồn tại dưới một ID DiscountCombo khác
                 // Lưu ý: logic này có thể cần điều chỉnh tùy theo business rules của bạn
                 // Ví dụ: có thể cho phép nhiều DiscountCombo cho cùng 1 cặp Combo-Discount nếu chúng có thể khác nhau ở discountPercentage
            }
        }


        DiscountComboEntity updatedDiscountCombo = discountComboRepository.save(existingDiscountCombo);
        return discountComboMapper.toResponseDTO(updatedDiscountCombo);
    }

    /**
     * Xóa một liên kết giảm giá cho combo.
     *
     * @param id ID của DiscountCombo cần xóa.
     * @throws ResourceNotFoundException nếu không tìm thấy.
     */
    public void deleteDiscountCombo(int id) {
        if (!discountComboRepository.existsById(id)) {
            throw new ResourceNotFoundException("DiscountCombo not found with id " + id);
        }
        discountComboRepository.deleteById(id);
    }
}