package com.poly.goldenbamboo.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "reservations") // Tên bảng theo quy ước (chữ thường, số nhiều)
public class ReservationEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @CreationTimestamp // Tự động điền thời gian đặt bàn
    @Column(name = "set_date_and_time", nullable = false, updatable = false)
    private Timestamp setDateAndTime;

    // Liên kết với ReservationDetailEntity
    @OneToMany(mappedBy = "reservation", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ReservationDetailEntity> reservationDetails = new ArrayList<>(); // Danh sách chi tiết đặt bàn

    // Liên kết với AccountEntity
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false) // Khóa ngoại, không được null
    @NotNull(message = "Tài khoản không được để trống")
    @JsonIgnore
    private AccountEntity account;

    // Liên kết với TableEntity
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_id", nullable = false) // Khóa ngoại, không được null
    @NotNull(message = "Bàn không được để trống")
    @JsonIgnore
    private TableEntity table;
}