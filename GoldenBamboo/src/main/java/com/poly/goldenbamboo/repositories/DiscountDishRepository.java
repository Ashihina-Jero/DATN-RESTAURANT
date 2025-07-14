package com.poly.goldenbamboo.repositories;

import com.poly.goldenbamboo.entities.DiscountDishEntity;
import com.poly.goldenbamboo.entities.DiscountEntity;
import com.poly.goldenbamboo.entities.DishEntity; // Giả định là DishEntity
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DiscountDishRepository extends JpaRepository<DiscountDishEntity, Integer> {
    // Phương thức tùy chỉnh để tìm một DiscountDish dựa trên Discount và Dish (để kiểm tra trùng lặp)
    Optional<DiscountDishEntity> findByDiscountAndDish(DiscountEntity discount, DishEntity dish);
}