package com.poly.goldenbamboo.repositories;

import com.poly.goldenbamboo.entities.MenuDishEntity;
import com.poly.goldenbamboo.entities.DishEntity;
import com.poly.goldenbamboo.entities.MenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MenuDishRepository extends JpaRepository<MenuDishEntity, Integer> {
    // Phương thức tùy chỉnh để tìm một MenuDish dựa trên Dish và Menu (để kiểm tra trùng lặp)
    Optional<MenuDishEntity> findByDishAndMenu(DishEntity dish, MenuEntity menu);
}