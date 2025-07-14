package com.poly.goldenbamboo.repositories;

import com.poly.goldenbamboo.entities.AccountEntity;
import com.poly.goldenbamboo.entities.TableEntity; // Cần import nếu có existsByTable

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Integer> {
    // Spring Data JPA sẽ tự động cung cấp các phương thức CRUD cơ bản.

    // Nếu bạn muốn kiểm tra sự tồn tại của Account liên quan đến một Table (ví dụ trong TableService delete)
    // boolean existsByTable(TableEntity table); // Đây là ví dụ nếu Account có ManyToOne đến Table

    // Nếu bạn cần tìm Account theo phone (đã đặt unique = true trong Entity)
    // Optional<AccountEntity> findByPhone(String phone);
}