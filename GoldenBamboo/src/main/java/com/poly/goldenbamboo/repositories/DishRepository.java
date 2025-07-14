package com.poly.goldenbamboo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poly.goldenbamboo.entities.DishEntity;

@Repository
public interface DishRepository extends JpaRepository<DishEntity, Integer> {
    // Spring Data JPA sẽ tự động cung cấp các phương thức CRUD cơ bản như save, findById, findAll, deleteById.

    // Bạn có thể thêm các phương thức tìm kiếm tùy chỉnh ở đây nếu cần,
    // ví dụ:
    // Optional<DishEntity> findByName(String name);
    // List<DishEntity> findByCategory(CategoryEntity category);
}