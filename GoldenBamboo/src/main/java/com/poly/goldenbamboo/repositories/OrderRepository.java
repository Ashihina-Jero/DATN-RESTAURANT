package com.poly.goldenbamboo.repositories;

import com.poly.goldenbamboo.entities.OrderEntity;
import com.poly.goldenbamboo.entities.TableEntity; // Cần import nếu có existsByTable

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {
    // Spring Data JPA sẽ tự động cung cấp các phương thức CRUD cơ bản.

    // Phương thức tùy chỉnh để kiểm tra xem có đơn hàng nào liên quan đến một bàn cụ thể không
    boolean existsByTable(TableEntity table);
}