package com.poly.restaurant.repositories;

import com.poly.restaurant.entities.ComboEntity;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComboRepository extends JpaRepository<ComboEntity, Integer> {
    // Spring Data JPA sẽ tự động cung cấp các phương thức CRUD cơ bản.
	Optional<ComboEntity> findByName(String name);
}