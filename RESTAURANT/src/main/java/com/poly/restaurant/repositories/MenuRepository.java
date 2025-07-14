package com.poly.restaurant.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poly.restaurant.entities.MenuEntity;

@Repository
public interface MenuRepository extends JpaRepository<MenuEntity, Integer> {
    // Spring Data JPA sẽ tự động cung cấp các phương thức CRUD cơ bản.
}