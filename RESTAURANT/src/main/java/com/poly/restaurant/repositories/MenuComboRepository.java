package com.poly.restaurant.repositories;

import com.poly.restaurant.entities.MenuComboEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuComboRepository extends JpaRepository<MenuComboEntity, Integer> {
    // Tạm thời chưa cần phương thức truy vấn tùy chỉnh.
}