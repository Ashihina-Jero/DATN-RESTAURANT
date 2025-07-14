package com.poly.restaurant.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poly.restaurant.entities.BranchComboEntity;
import com.poly.restaurant.entities.BranchEntity;
import com.poly.restaurant.entities.ComboEntity;

public interface BranchComboRepository extends JpaRepository<BranchComboEntity, Long> {
	Optional<BranchComboEntity> findByBranchAndCombo(BranchEntity branch, ComboEntity combo);
}