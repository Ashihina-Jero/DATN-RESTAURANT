package com.poly.restaurant.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.poly.restaurant.entities.DishEntity;

@Repository
public interface DishRepository extends JpaRepository<DishEntity, Integer> {
	// Spring Data JPA sẽ tự động cung cấp các phương thức CRUD cơ bản như save,
	// findById, findAll, deleteById.

	// Bạn có thể thêm các phương thức tìm kiếm tùy chỉnh ở đây nếu cần,
	// ví dụ:
	Optional<DishEntity> findByName(String name);

	/**
	 * Tìm kiếm Dish theo nhiều tiêu chí. Query sẽ bỏ qua điều kiện nếu tham số
	 * tương ứng là null.
	 * 
	 * @param name       Tên món ăn (có thể là null).
	 * @param categoryId ID của danh mục (có thể là null).
	 * @return Danh sách các DishEntity phù hợp.
	 */
	@Query("SELECT d FROM DishEntity d WHERE "
			+ "(:name IS NULL OR LOWER(d.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND "
			+ "(:categoryId IS NULL OR d.category.id = :categoryId)")
	List<DishEntity> searchByCriteria(@Param("name") String name, @Param("categoryId") Integer categoryId);
}