package com.poly.restaurant.repositories;

import com.poly.restaurant.entities.MenuItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItemEntity, Integer> {
    // Repository này sẽ làm việc với bảng cha (menu_items)
    // và có thể tìm thấy các record của cả DishEntity và ComboEntity
    // thông qua ID của chúng.
}