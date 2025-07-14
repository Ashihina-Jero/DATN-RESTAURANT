package com.poly.restaurant.repositories;

import com.poly.restaurant.entities.OrderDetailEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetailEntity, Long> {
    // Spring Data JPA sẽ tự động cung cấp các phương thức CRUD cơ bản.

    // Nếu bạn muốn kiểm tra xem có OrderDetail nào liên quan đến một Order cụ thể không
    // boolean existsByOrder(OrderEntity order);
}