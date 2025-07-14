package com.poly.goldenbamboo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poly.goldenbamboo.entities.RoleEntity;

public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {
	boolean existsByName(String name);
}
