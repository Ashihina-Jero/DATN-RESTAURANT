package com.poly.goldenbamboo.repositories;

import com.poly.goldenbamboo.entities.ReservationDetailEntity;
import com.poly.goldenbamboo.entities.ReservationEntity; // Cần import nếu có existsByReservation

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationDetailRepository extends JpaRepository<ReservationDetailEntity, Integer> {
    // Spring Data JPA sẽ tự động cung cấp các phương thức CRUD cơ bản.

    // Nếu bạn muốn kiểm tra xem có ReservationDetail nào liên quan đến một Reservation cụ thể không
    // boolean existsByReservation(ReservationEntity reservation);
}