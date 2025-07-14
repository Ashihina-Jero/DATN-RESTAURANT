package com.poly.goldenbamboo.repositories;

import com.poly.goldenbamboo.entities.ComboDishEntity;
import com.poly.goldenbamboo.entities.ComboEntity;
import com.poly.goldenbamboo.entities.DishEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ComboDishRepository extends JpaRepository<ComboDishEntity, Integer> {
    // Phương thức tùy chỉnh để tìm một ComboDish dựa trên Combo và Dish (để kiểm tra trùng lặp)
    Optional<ComboDishEntity> findByComboAndDish(ComboEntity combo, DishEntity dish);
}