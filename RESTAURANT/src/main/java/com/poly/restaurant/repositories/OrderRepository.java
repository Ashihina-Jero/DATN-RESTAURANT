package com.poly.restaurant.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poly.restaurant.entities.OrderEntity;
import com.poly.restaurant.entities.TableEntity;
import com.poly.restaurant.entities.enums.OrderStatus;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    // Spring Data JPA sẽ tự động cung cấp các phương thức CRUD cơ bản.
    // Các phương thức tìm kiếm phức tạp hơn (ví dụ: tìm theo ngày, theo trạng thái)
    // có thể được thêm vào đây sau này nếu cần.
	 boolean existsByTableAndStatusNot(TableEntity table, OrderStatus status);
	
}