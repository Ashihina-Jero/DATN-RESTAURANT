package com.poly.restaurant.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poly.restaurant.entities.RoleEntity;

public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {
	Optional<RoleEntity> findByName(String name);
}
