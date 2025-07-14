package com.poly.goldenbamboo.entities;

import java.io.Serializable;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonIgnore;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "discount_dishes") // Tên bảng theo quy ước (chữ thường, số nhiều)
public class DiscountDishEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    // Liên kết với DiscountEntity
    @ManyToOne(fetch = FetchType.LAZY) // Tải lười để tối ưu hiệu suất
    @JoinColumn(name = "discount_id", nullable = false) // Khóa ngoại, không được null
    @NotNull(message = "Mã giảm giá không được để trống")
    @JsonIgnore
    private DiscountEntity discount;

    // Liên kết với DishEntity
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dish_id", nullable = false) // Khóa ngoại, không được null
    @NotNull(message = "Món ăn không được để trống")
    @JsonIgnore
    private MenuDishEntity dish; // Sửa tên biến từ 'dishe' thành 'dish' cho đúng ngữ nghĩa
}