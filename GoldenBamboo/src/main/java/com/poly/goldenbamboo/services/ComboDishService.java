package com.poly.goldenbamboo.services;

import com.poly.goldenbamboo.dtos.ComboDishRequestDTO;
import com.poly.goldenbamboo.dtos.ComboDishResponseDTO;
import com.poly.goldenbamboo.entities.ComboDishEntity;
import com.poly.goldenbamboo.entities.ComboEntity;
import com.poly.goldenbamboo.entities.DishEntity;
import com.poly.goldenbamboo.exceptions.ResourceNotFoundException;
import com.poly.goldenbamboo.mappers.ComboDishMapper;
import com.poly.goldenbamboo.repositories.ComboDishRepository;
import com.poly.goldenbamboo.repositories.ComboRepository; // Cần Repository cho Combo
import com.poly.goldenbamboo.repositories.DishRepository;   // Cần Repository cho Dish
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComboDishService {

    private final ComboDishRepository comboDishRepository;
    private final ComboRepository comboRepository; // Inject ComboRepository
    private final DishRepository dishRepository;   // Inject DishRepository
    private final ComboDishMapper comboDishMapper;

    @Autowired
    public ComboDishService(ComboDishRepository comboDishRepository,
                            ComboRepository comboRepository,
                            DishRepository dishRepository,
                            ComboDishMapper comboDishMapper) {
        this.comboDishRepository = comboDishRepository;
        this.comboRepository = comboRepository;
        this.dishRepository = dishRepository;
        this.comboDishMapper = comboDishMapper;
    }

    /**
     * Lấy tất cả các món ăn trong combo.
     *
     * @return Danh sách ComboDishResponseDTO.
     */
    public List<ComboDishResponseDTO> getAllComboDishes() {
        List<ComboDishEntity> comboDishes = comboDishRepository.findAll();
        return comboDishes.stream()
                .map(comboDishMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lấy thông tin ComboDish theo ID.
     *
     * @param id ID của ComboDish.
     * @return ComboDishResponseDTO nếu tìm thấy.
     * @throws ResourceNotFoundException nếu không tìm thấy.
     */
    public ComboDishResponseDTO getComboDishById(int id) {
        ComboDishEntity comboDish = comboDishRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ComboDish not found with id " + id));
        return comboDishMapper.toResponseDTO(comboDish);
    }

    /**
     * Thêm một món ăn vào combo.
     *
     * @param comboDishRequestDTO DTO chứa thông tin ComboDish cần tạo.
     * @return ComboDishResponseDTO của ComboDish đã tạo.
     * @throws ResourceNotFoundException nếu Combo hoặc Dish không tồn tại.
     * @throws IllegalArgumentException nếu ComboDish đã tồn tại (nếu bạn muốn duy nhất).
     */
    public ComboDishResponseDTO createComboDish(ComboDishRequestDTO comboDishRequestDTO) {
        // 1. Tìm ComboEntity và DishEntity dựa trên IDs
        ComboEntity combo = comboRepository.findById(comboDishRequestDTO.getComboId())
                .orElseThrow(() -> new ResourceNotFoundException("Combo not found with id " + comboDishRequestDTO.getComboId()));
        DishEntity dish = dishRepository.findById(comboDishRequestDTO.getDishId())
                .orElseThrow(() -> new ResourceNotFoundException("Dish not found with id " + comboDishRequestDTO.getDishId()));

        // Tùy chọn: Kiểm tra xem mối quan hệ này đã tồn tại chưa để tránh trùng lặp
        if (comboDishRepository.findByComboAndDish(combo, dish).isPresent()) {
            throw new IllegalArgumentException("This dish is already part of this combo.");
        }

        // 2. Chuyển đổi DTO sang Entity và thiết lập các mối quan hệ
        ComboDishEntity comboDishEntity = comboDishMapper.toEntity(comboDishRequestDTO);
        comboDishEntity.setCombo(combo);
        comboDishEntity.setDish(dish);

        // 3. Lưu Entity
        ComboDishEntity savedComboDish = comboDishRepository.save(comboDishEntity);
        return comboDishMapper.toResponseDTO(savedComboDish);
    }

    /**
     * Cập nhật một món ăn trong combo (thực tế ít dùng cho bảng trung gian, thường là xóa rồi thêm mới).
     *
     * @param id ID của ComboDish cần cập nhật.
     * @param comboDishRequestDTO DTO chứa thông tin cập nhật.
     * @return ComboDishResponseDTO của ComboDish đã cập nhật.
     * @throws ResourceNotFoundException nếu ComboDish, Combo hoặc Dish không tồn tại.
     */
    public ComboDishResponseDTO updateComboDish(int id, ComboDishRequestDTO comboDishRequestDTO) {
        ComboDishEntity existingComboDish = comboDishRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ComboDish not found with id " + id));

        // 1. Cập nhật các trường thông thường (trong trường hợp này không có trường nào ngoài ID quan hệ)
        // comboDishMapper.updateEntityFromDTO(comboDishRequestDTO, existingComboDish); // Nếu có các trường khác cần update

        // 2. Tìm ComboEntity và DishEntity mới (nếu IDs thay đổi)
        ComboEntity newCombo = comboRepository.findById(comboDishRequestDTO.getComboId())
                .orElseThrow(() -> new ResourceNotFoundException("Combo not found with id " + comboDishRequestDTO.getComboId()));
        DishEntity newDish = dishRepository.findById(comboDishRequestDTO.getDishId())
                .orElseThrow(() -> new ResourceNotFoundException("Dish not found with id " + comboDishRequestDTO.getDishId()));

        // Tùy chọn: Kiểm tra xem mối quan hệ mới có bị trùng với mối quan hệ khác không
        if (comboDishRepository.findByComboAndDish(newCombo, newDish).isPresent() &&
            !existingComboDish.getCombo().equals(newCombo) || !existingComboDish.getDish().equals(newDish)) {
            // Chỉ ném lỗi nếu mối quan hệ mới là trùng lặp VÀ khác với mối quan hệ hiện tại
            // Tức là nếu bạn đang cố cập nhật một ComboDish thành một ComboDish khác đã tồn tại
            throw new IllegalArgumentException("This updated combo-dish relationship already exists.");
        }


        // 3. Thiết lập các mối quan hệ mới
        existingComboDish.setCombo(newCombo);
        existingComboDish.setDish(newDish);

        ComboDishEntity updatedComboDish = comboDishRepository.save(existingComboDish);
        return comboDishMapper.toResponseDTO(updatedComboDish);
    }

    /**
     * Xóa một món ăn khỏi combo theo ID của ComboDish.
     *
     * @param id ID của ComboDish cần xóa.
     * @throws ResourceNotFoundException nếu không tìm thấy ComboDish cần xóa.
     */
    public void deleteComboDish(int id) {
        if (!comboDishRepository.existsById(id)) {
            throw new ResourceNotFoundException("ComboDish not found with id " + id);
        }
        comboDishRepository.deleteById(id);
    }
}