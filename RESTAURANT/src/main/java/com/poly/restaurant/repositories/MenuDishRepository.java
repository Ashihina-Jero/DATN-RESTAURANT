package com.poly.restaurant.repositories;

import com.poly.restaurant.entities.MenuDishEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuDishRepository extends JpaRepository<MenuDishEntity, Integer> {
}