package com.poly.goldenbamboo.repositories;

import com.poly.goldenbamboo.entities.TableEntity;
import com.poly.goldenbamboo.entities.BranchEntity; // Cần import nếu có findByNumberAndBranchId

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TableRepository extends JpaRepository<TableEntity, Integer> {
    // Spring Data JPA sẽ tự động cung cấp các phương thức CRUD cơ bản.

    // Phương thức tùy chỉnh để tìm bàn theo số bàn và ID chi nhánh (cho tính duy nhất)
    Optional<TableEntity> findByNumberAndBranchId(String number, Integer branchId);

    // Hoặc nếu bạn muốn tìm theo đối tượng Branch (tùy thuộc vào cách bạn truyền tham số)
    // Optional<TableEntity> findByNumberAndBranch(String number, BranchEntity branch);
}