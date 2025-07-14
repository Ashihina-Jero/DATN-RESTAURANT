package com.poly.goldenbamboo.entities;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonIgnore;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "discount_combos") // Tên bảng theo quy ước (chữ thường, số nhiều)
public class DiscountComboEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @PositiveOrZero(message = "Phần trăm giảm giá phải lớn hơn hoặc bằng 0")
    @Column(name = "discount_percentage", nullable = false, precision = 5, scale = 2) // Ví dụ: 15.75%
    private BigDecimal discountPercentage;

    // Liên kết với ComboEntity
    @ManyToOne(fetch = FetchType.LAZY) // Tải lười để tối ưu hiệu suất
    @JoinColumn(name = "combo_id", nullable = false) // Khóa ngoại, không được null
    @NotNull(message = "Combo không được để trống")
    @JsonIgnore
    private MenuComboEntity combo;

    // Liên kết với DiscountEntity
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discount_id", nullable = false) // Khóa ngoại, không được null
    @NotNull(message = "Mã giảm giá không được để trống")
    @JsonIgnore
    private DiscountEntity discount;
}