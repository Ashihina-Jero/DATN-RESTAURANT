package com.poly.restaurant.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poly.restaurant.entities.BranchDishEntity;
import com.poly.restaurant.entities.BranchEntity;
import com.poly.restaurant.entities.DishEntity;

public interface BranchDishRepository extends JpaRepository<BranchDishEntity, Long> {
	Optional<BranchDishEntity> findByBranchAndDish(BranchEntity branch, DishEntity dish);
}