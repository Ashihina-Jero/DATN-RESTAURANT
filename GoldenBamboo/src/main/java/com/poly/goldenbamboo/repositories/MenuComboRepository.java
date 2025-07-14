package com.poly.goldenbamboo.repositories;

import com.poly.goldenbamboo.entities.MenuComboEntity;
import com.poly.goldenbamboo.entities.ComboEntity;
import com.poly.goldenbamboo.entities.MenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MenuComboRepository extends JpaRepository<MenuComboEntity, Integer> {
    // Phương thức tùy chỉnh để tìm một MenuCombo dựa trên Combo và Menu (để kiểm tra trùng lặp)
    Optional<MenuComboEntity> findByComboAndMenu(ComboEntity combo, MenuEntity menu);
}