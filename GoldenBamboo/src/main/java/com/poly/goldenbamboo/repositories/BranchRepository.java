package com.poly.goldenbamboo.repositories;

import com.poly.goldenbamboo.entities.BranchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BranchRepository extends JpaRepository<BranchEntity, Integer> {
    // Spring Data JPA sẽ tự động cung cấp các phương thức CRUD cơ bản.
}