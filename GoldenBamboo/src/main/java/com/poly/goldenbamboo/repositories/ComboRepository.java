package com.poly.goldenbamboo.repositories;

import com.poly.goldenbamboo.entities.ComboEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComboRepository extends JpaRepository<ComboEntity, Integer> {
    // Spring Data JPA sẽ tự động cung cấp các phương thức CRUD cơ bản.
}