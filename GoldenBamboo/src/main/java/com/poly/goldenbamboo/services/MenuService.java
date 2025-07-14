package com.poly.goldenbamboo.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poly.goldenbamboo.dtos.MenuRequestDTO;
import com.poly.goldenbamboo.dtos.MenuResponseDTO;
import com.poly.goldenbamboo.entities.BranchEntity;
import com.poly.goldenbamboo.entities.MenuEntity;
import com.poly.goldenbamboo.exceptions.ResourceNotFoundException;
import com.poly.goldenbamboo.mappers.MenuMapper;
import com.poly.goldenbamboo.repositories.BranchRepository; // Cần Repository cho Branch
import com.poly.goldenbamboo.repositories.MenuRepository; // Cần Repository cho Menu

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final BranchRepository branchRepository; // Inject BranchRepository
    private final MenuMapper menuMapper;

    @Autowired
    public MenuService(MenuRepository menuRepository, BranchRepository branchRepository, MenuMapper menuMapper) {
        this.menuRepository = menuRepository;
        this.branchRepository = branchRepository;
        this.menuMapper = menuMapper;
    }

    /**
     * Lấy tất cả các menu.
     *
     * @return Danh sách MenuResponseDTO.
     */
    public List<MenuResponseDTO> getAllMenus() {
        List<MenuEntity> menus = menuRepository.findAll();
        return menus.stream()
                .map(menuMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lấy thông tin menu theo ID.
     *
     * @param id ID của menu.
     * @return MenuResponseDTO nếu tìm thấy.
     * @throws ResourceNotFoundException nếu không tìm thấy menu.
     */
    public MenuResponseDTO getMenuById(int id) {
        MenuEntity menu = menuRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu not found with id " + id));
        return menuMapper.toResponseDTO(menu);
    }

    /**
     * Tạo một menu mới.
     *
     * @param menuRequestDTO DTO chứa thông tin menu cần tạo.
     * @return MenuResponseDTO của menu đã tạo.
     * @throws ResourceNotFoundException nếu Branch không tồn tại.
     */
    @Transactional
    public MenuResponseDTO createMenu(MenuRequestDTO menuRequestDTO) {
        MenuEntity menuEntity = menuMapper.toEntity(menuRequestDTO);

        // Xử lý mối quan hệ Branch (ManyToOne)
        if (menuRequestDTO.getBranchId() != null) {
            BranchEntity branch = branchRepository.findById(menuRequestDTO.getBranchId())
                    .orElseThrow(() -> new ResourceNotFoundException("Branch not found with id " + menuRequestDTO.getBranchId()));
            menuEntity.setBranch(branch);
        } else {
            // Nếu branchId là null, có nghĩa là menu này không thuộc về chi nhánh cụ thể nào
            menuEntity.setBranch(null);
        }

        MenuEntity savedMenu = menuRepository.save(menuEntity);
        return menuMapper.toResponseDTO(savedMenu);
    }

    /**
     * Cập nhật thông tin menu.
     *
     * @param id ID của menu cần cập nhật.
     * @param menuRequestDTO DTO chứa thông tin cập nhật.
     * @return MenuResponseDTO của menu đã cập nhật.
     * @throws ResourceNotFoundException nếu menu hoặc Branch không tồn tại.
     */
    @Transactional
    public MenuResponseDTO updateMenu(int id, MenuRequestDTO menuRequestDTO) {
        MenuEntity existingMenu = menuRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu not found with id " + id));

        menuMapper.updateEntityFromDTO(menuRequestDTO, existingMenu);

        // Xử lý cập nhật mối quan hệ Branch (nếu ID thay đổi hoặc được gán/gỡ bỏ)
        if (menuRequestDTO.getBranchId() != null) {
            if (existingMenu.getBranch() == null || !existingMenu.getBranch().getId().equals(menuRequestDTO.getBranchId())) {
                BranchEntity newBranch = branchRepository.findById(menuRequestDTO.getBranchId())
                        .orElseThrow(() -> new ResourceNotFoundException("Branch not found with id " + menuRequestDTO.getBranchId()));
                existingMenu.setBranch(newBranch);
            }
        } else {
            // Nếu client gửi branchId là null, tức là muốn gỡ bỏ liên kết với branch
            existingMenu.setBranch(null);
        }

        MenuEntity updatedMenu = menuRepository.save(existingMenu);
        return menuMapper.toResponseDTO(updatedMenu);
    }

    /**
     * Xóa một menu theo ID.
     *
     * @param id ID của menu cần xóa.
     * @throws ResourceNotFoundException nếu không tìm thấy menu.
     */
    public void deleteMenu(int id) {
        if (!menuRepository.existsById(id)) {
            throw new ResourceNotFoundException("Menu not found with id " + id);
        }
        // CascadeType.ALL và orphanRemoval=true trên menuCombos và menuDishes
        // sẽ đảm bảo các bản ghi liên quan cũng bị xóa khi menu bị xóa.
        menuRepository.deleteById(id);
    }
}