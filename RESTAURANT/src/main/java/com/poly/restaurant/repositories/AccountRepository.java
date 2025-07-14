package com.poly.restaurant.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.poly.restaurant.entities.AccountEntity;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

    Optional<AccountEntity> findByUsername(String username);

    // SỬA LẠI TÊN PHƯƠNG THỨC Ở ĐÂY
    Optional<AccountEntity> findByPhone(String phone);
}