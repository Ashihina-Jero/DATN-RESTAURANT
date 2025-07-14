package com.poly.goldenbamboo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "reservation_details")
public class ReservationDetailEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "quantity", nullable = false)
    @Positive(message = "Số lượng phải lớn hơn 0")
    private int quantity;

    // --- THAY ĐỔI LỚN NHẤT NẰM Ở ĐÂY ---
    // Xóa dishOrComboId và type, thay bằng mối quan hệ trực tiếp
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_item_id", nullable = false)
    private MenuItemEntity menuItem;

    // Liên kết với ReservationEntity (giữ nguyên)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", nullable = false)
    @NotNull(message = "Đặt bàn không được để trống")
    @JsonIgnore
    private ReservationEntity reservation;

    // Không cần Enum ItemType ở đây nữa
}