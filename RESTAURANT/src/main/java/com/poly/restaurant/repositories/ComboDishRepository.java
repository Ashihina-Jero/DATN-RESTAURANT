package com.poly.restaurant.repositories;

import com.poly.restaurant.entities.ComboDishEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComboDishRepository extends JpaRepository<ComboDishEntity, Integer> {
    // Các phương thức CRUD cơ bản như save(), saveAll(), delete() đã có sẵn.
}