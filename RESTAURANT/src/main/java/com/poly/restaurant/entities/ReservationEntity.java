package com.poly.restaurant.entities;

import com.poly.restaurant.entities.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reservations")
public class ReservationEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Các trường thông tin cốt lõi
    @Column(nullable = false)
    private LocalDateTime reservationTime; // Thời gian khách đặt

    @Column(nullable = false)
    private int numberOfGuests; // Số lượng khách

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status; // Trạng thái

    private String notes; // Ghi chú

    @CreationTimestamp
    @Column(name = "create_at", updatable = false)
    private LocalDateTime createAt;

    // --- Các mối quan hệ ---

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private AccountEntity account;

    // Giữ nguyên @ManyToOne nếu nghiệp vụ của bạn là 1 đặt bàn/1 bàn
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_id", nullable = false)
    private TableEntity table;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    private BranchEntity branch;
}