package com.poly.restaurant.repositories;

import com.poly.restaurant.entities.BranchEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional; // Thêm import

public interface BranchRepository extends JpaRepository<BranchEntity, Integer> {
    // Thêm phương thức này để tìm chi nhánh theo tên
    Optional<BranchEntity> findByName(String name);
}