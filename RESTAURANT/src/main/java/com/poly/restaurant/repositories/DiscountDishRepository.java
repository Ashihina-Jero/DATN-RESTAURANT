package com.poly.restaurant.repositories;

import com.poly.restaurant.entities.DiscountDishEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountDishRepository extends JpaRepository<DiscountDishEntity, Long> {
}