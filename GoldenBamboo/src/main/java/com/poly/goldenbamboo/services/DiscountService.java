package com.poly.goldenbamboo.services;

import com.poly.goldenbamboo.dtos.DiscountRequestDTO;
import com.poly.goldenbamboo.dtos.DiscountResponseDTO;
import com.poly.goldenbamboo.entities.DiscountEntity;
import com.poly.goldenbamboo.entities.enums.DiscountStatus;
import com.poly.goldenbamboo.exceptions.ResourceNotFoundException;
import com.poly.goldenbamboo.mappers.DiscountMapper;
import com.poly.goldenbamboo.repositories.DiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DiscountService {

    private final DiscountRepository discountRepository;
    private final DiscountMapper discountMapper;

    @Autowired
    public DiscountService(DiscountRepository discountRepository, DiscountMapper discountMapper) {
        this.discountRepository = discountRepository;
        this.discountMapper = discountMapper;
    }

    /**
     * Lấy tất cả các chương trình giảm giá.
     *
     * @return Danh sách DiscountResponseDTO.
     */
    public List<DiscountResponseDTO> getAllDiscounts() {
        List<DiscountEntity> discounts = discountRepository.findAll();
        return discounts.stream()
                .map(discountMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lấy thông tin giảm giá theo ID.
     *
     * @param id ID của chương trình giảm giá.
     * @return DiscountResponseDTO nếu tìm thấy.
     * @throws ResourceNotFoundException nếu không tìm thấy chương trình giảm giá.
     */
    public DiscountResponseDTO getDiscountById(int id) {
        DiscountEntity discount = discountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Discount not found with id " + id));
        return discountMapper.toResponseDTO(discount);
    }

    /**
     * Tạo một chương trình giảm giá mới.
     *
     * @param discountRequestDTO DTO chứa thông tin giảm giá cần tạo.
     * @return DiscountResponseDTO của chương trình giảm giá đã tạo.
     * @throws IllegalArgumentException nếu mã giảm giá đã tồn tại hoặc ngày kết thúc trước ngày bắt đầu.
     */
    @Transactional
    public DiscountResponseDTO createDiscount(DiscountRequestDTO discountRequestDTO) {
        // Kiểm tra mã giảm giá duy nhất
        if (discountRepository.findByCode(discountRequestDTO.getCode()).isPresent()) {
            throw new IllegalArgumentException("Discount with code '" + discountRequestDTO.getCode() + "' already exists.");
        }

        // Kiểm tra ngày hợp lệ
        if (discountRequestDTO.getEndDate().isBefore(discountRequestDTO.getStartDate())) {
            throw new IllegalArgumentException("End date cannot be before start date.");
        }

        DiscountEntity discountEntity = discountMapper.toEntity(discountRequestDTO);

        // Cập nhật trạng thái dựa trên ngày hiện tại (tùy chọn, có thể xử lý bằng cron job hoặc khi truy vấn)
        updateDiscountStatus(discountEntity);

        DiscountEntity savedDiscount = discountRepository.save(discountEntity);
        return discountMapper.toResponseDTO(savedDiscount);
    }

    /**
     * Cập nhật thông tin chương trình giảm giá.
     *
     * @param id ID của chương trình giảm giá cần cập nhật.
     * @param discountRequestDTO DTO chứa thông tin cập nhật.
     * @return DiscountResponseDTO của chương trình giảm giá đã cập nhật.
     * @throws ResourceNotFoundException nếu không tìm thấy chương trình giảm giá.
     * @throws IllegalArgumentException nếu mã giảm giá cập nhật đã tồn tại (trừ chính nó) hoặc ngày không hợp lệ.
     */
    @Transactional
    public DiscountResponseDTO updateDiscount(int id, DiscountRequestDTO discountRequestDTO) {
        DiscountEntity existingDiscount = discountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Discount not found with id " + id));

        // Kiểm tra mã giảm giá duy nhất (trừ chính bản ghi đang cập nhật)
        if (!existingDiscount.getCode().equals(discountRequestDTO.getCode())) {
            discountRepository.findByCode(discountRequestDTO.getCode())
                    .ifPresent(discountWithSameCode -> {
                        if (discountWithSameCode.getId() != existingDiscount.getId()) {
                            throw new IllegalArgumentException("Discount with code '" + discountRequestDTO.getCode() + "' already exists.");
                        }
                    });
        }

        // Kiểm tra ngày hợp lệ
        if (discountRequestDTO.getEndDate().isBefore(discountRequestDTO.getStartDate())) {
            throw new IllegalArgumentException("End date cannot be before start date.");
        }

        discountMapper.updateEntityFromDTO(discountRequestDTO, existingDiscount); // Cập nhật các trường chính

        // Cập nhật trạng thái dựa trên ngày hiện tại (tùy chọn)
        updateDiscountStatus(existingDiscount);

        DiscountEntity updatedDiscount = discountRepository.save(existingDiscount);
        return discountMapper.toResponseDTO(updatedDiscount);
    }

    /**
     * Xóa một chương trình giảm giá theo ID.
     *
     * @param id ID của chương trình giảm giá cần xóa.
     * @throws ResourceNotFoundException nếu không tìm thấy chương trình giảm giá.
     */
    public void deleteDiscount(int id) {
        if (!discountRepository.existsById(id)) {
            throw new ResourceNotFoundException("Discount not found with id " + id);
        }
        // Với CascadeType.ALL và orphanRemoval=true, các DiscountComboEntity và DiscountDishEntity
        // liên quan sẽ tự động bị xóa khi DiscountEntity bị xóa.
        discountRepository.deleteById(id);
    }

    /**
     * Cập nhật trạng thái giảm giá dựa trên ngày hiện tại.
     * Phương thức nội bộ, có thể được gọi khi tạo/cập nhật hoặc bởi một cron job.
     *
     * @param discount DiscountEntity cần cập nhật trạng thái.
     */
    private void updateDiscountStatus(DiscountEntity discount) {
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(discount.getStartDate())) {
            discount.setStatus(DiscountStatus.UPCOMING);
        } else if (now.isAfter(discount.getEndDate())) {
            discount.setStatus(DiscountStatus.EXPIRED);
        } else {
            discount.setStatus(DiscountStatus.ACTIVE);
        }
    }
}