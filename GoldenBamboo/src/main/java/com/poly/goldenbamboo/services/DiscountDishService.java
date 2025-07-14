package com.poly.goldenbamboo.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poly.goldenbamboo.dtos.DiscountDishRequestDTO;
import com.poly.goldenbamboo.dtos.DiscountDishResponseDTO;
import com.poly.goldenbamboo.entities.DiscountDishEntity;
import com.poly.goldenbamboo.entities.DiscountEntity;
import com.poly.goldenbamboo.entities.DishEntity; // Giả định là DishEntity
import com.poly.goldenbamboo.exceptions.ResourceNotFoundException;
import com.poly.goldenbamboo.mappers.DiscountDishMapper;
import com.poly.goldenbamboo.repositories.DiscountDishRepository;
import com.poly.goldenbamboo.repositories.DiscountRepository;
import com.poly.goldenbamboo.repositories.DishRepository; // Cần Repository cho Dish

@Service
public class DiscountDishService {

    private final DiscountDishRepository discountDishRepository;
    private final DiscountRepository discountRepository;
    private final DishRepository dishRepository; // Inject DishRepository
    private final DiscountDishMapper discountDishMapper;

    @Autowired
    public DiscountDishService(DiscountDishRepository discountDishRepository,
                               DiscountRepository discountRepository,
                               DishRepository dishRepository,
                               DiscountDishMapper discountDishMapper) {
        this.discountDishRepository = discountDishRepository;
        this.discountRepository = discountRepository;
        this.dishRepository = dishRepository;
        this.discountDishMapper = discountDishMapper;
    }

    /**
     * Lấy tất cả các giảm giá áp dụng cho món ăn.
     *
     * @return Danh sách DiscountDishResponseDTO.
     */
    public List<DiscountDishResponseDTO> getAllDiscountDishes() {
        List<DiscountDishEntity> discountDishes = discountDishRepository.findAll();
        return discountDishes.stream()
                .map(discountDishMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lấy thông tin DiscountDish theo ID.
     *
     * @param id ID của DiscountDish.
     * @return DiscountDishResponseDTO nếu tìm thấy.
     * @throws ResourceNotFoundException nếu không tìm thấy.
     */
    public DiscountDishResponseDTO getDiscountDishById(int id) {
        DiscountDishEntity discountDish = discountDishRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DiscountDish not found with id " + id));
        return discountDishMapper.toResponseDTO(discountDish);
    }

    /**
     * Tạo một liên kết giảm giá cho món ăn mới.
     *
     * @param discountDishRequestDTO DTO chứa thông tin DiscountDish cần tạo.
     * @return DiscountDishResponseDTO của DiscountDish đã tạo.
     * @throws ResourceNotFoundException nếu Discount hoặc Dish không tồn tại.
     * @throws IllegalArgumentException nếu mối quan hệ đã tồn tại (nếu bạn muốn duy nhất).
     */
    @Transactional
    public DiscountDishResponseDTO createDiscountDish(DiscountDishRequestDTO discountDishRequestDTO) {
        // 1. Tìm DiscountEntity và DishEntity dựa trên IDs
        DiscountEntity discount = discountRepository.findById(discountDishRequestDTO.getDiscountId())
                .orElseThrow(() -> new ResourceNotFoundException("Discount not found with id " + discountDishRequestDTO.getDiscountId()));
        DishEntity dish = dishRepository.findById(discountDishRequestDTO.getDishId())
                .orElseThrow(() -> new ResourceNotFoundException("Dish not found with id " + discountDishRequestDTO.getDishId()));

        // Tùy chọn: Kiểm tra xem mối quan hệ này đã tồn tại chưa để tránh trùng lặp
        if (discountDishRepository.findByDiscountAndDish(discount, dish).isPresent()) {
            throw new IllegalArgumentException("This discount is already applied to this dish.");
        }

        // 2. Chuyển đổi DTO sang Entity và thiết lập các mối quan hệ
        DiscountDishEntity discountDishEntity = discountDishMapper.toEntity(discountDishRequestDTO);
        discountDishEntity.setDiscount(discount);
        discountDishEntity.setDish(dish);

        // 3. Lưu Entity
        DiscountDishEntity savedDiscountDish = discountDishRepository.save(discountDishEntity);
        return discountDishMapper.toResponseDTO(savedDiscountDish);
    }

    /**
     * Cập nhật thông tin giảm giá cho món ăn.
     *
     * @param id ID của DiscountDish cần cập nhật.
     * @param discountDishRequestDTO DTO chứa thông tin cập nhật.
     * @return DiscountDishResponseDTO của DiscountDish đã cập nhật.
     * @throws ResourceNotFoundException nếu DiscountDish, Discount hoặc Dish không tồn tại.
     */
    @Transactional
    public DiscountDishResponseDTO updateDiscountDish(int id, DiscountDishRequestDTO discountDishRequestDTO) {
        DiscountDishEntity existingDiscountDish = discountDishRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DiscountDish not found with id " + id));

        // Không có trường nào ngoài các khóa ngoại, nên không cần updateEntityFromDTO ở đây
        // discountDishMapper.updateEntityFromDTO(discountDishRequestDTO, existingDiscountDish);

        // Xử lý cập nhật mối quan hệ Discount (nếu ID thay đổi)
        if (!existingDiscountDish.getDiscount().getId().equals(discountDishRequestDTO.getDiscountId())) {
            DiscountEntity newDiscount = discountRepository.findById(discountDishRequestDTO.getDiscountId())
                    .orElseThrow(() -> new ResourceNotFoundException("Discount not found with id " + discountDishRequestDTO.getDiscountId()));
            existingDiscountDish.setDiscount(newDiscount);
        }

        // Xử lý cập nhật mối quan hệ Dish (nếu ID thay đổi)
        if (!existingDiscountDish.getDish().getId().equals(discountDishRequestDTO.getDishId())) {
            DishEntity newDish = dishRepository.findById(discountDishRequestDTO.getDishId())
                    .orElseThrow(() -> new ResourceNotFoundException("Dish not found with id " + discountDishRequestDTO.getDishId()));
            existingDiscountDish.setDish(newDish);
        }

        // Tùy chọn: Kiểm tra trùng lặp nếu cả discount và dish đều thay đổi và tạo ra một cặp trùng lặp mới
        if (!existingDiscountDish.getDiscount().getId().equals(discountDishRequestDTO.getDiscountId()) ||
            !existingDiscountDish.getDish().getId().equals(discountDishRequestDTO.getDishId())) {
            if (discountDishRepository.findByDiscountAndDish(existingDiscountDish.getDiscount(), existingDiscountDish.getDish()).isPresent()) {
                // Tránh cập nhật thành một cặp Discount-Dish đã tồn tại dưới một ID DiscountDish khác
                // Lưu ý: logic này có thể cần điều chỉnh tùy theo business rules của bạn
            }
        }

        DiscountDishEntity updatedDiscountDish = discountDishRepository.save(existingDiscountDish);
        return discountDishMapper.toResponseDTO(updatedDiscountDish);
    }

    /**
     * Xóa một liên kết giảm giá cho món ăn.
     *
     * @param id ID của DiscountDish cần xóa.
     * @throws ResourceNotFoundException nếu không tìm thấy.
     */
    public void deleteDiscountDish(int id) {
        if (!discountDishRepository.existsById(id)) {
            throw new ResourceNotFoundException("DiscountDish not found with id " + id);
        }
        discountDishRepository.deleteById(id);
    }
}