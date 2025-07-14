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
@Table(name = "combo_dishes") // Tên bảng theo quy ước (chữ thường, số nhiều)
public class ComboDishEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    // Liên kết với ComboEntity
    @ManyToOne(fetch = FetchType.LAZY) // Tải lười để tối ưu hiệu suất
    @JoinColumn(name = "combo_id", nullable = false) // Khóa ngoại, không được null
    @NotNull(message = "Combo không được để trống")
    @JsonIgnore
    private ComboEntity combo;

    // Liên kết với DishEntity
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dish_id", nullable = false) // Khóa ngoại, không được null
    @NotNull(message = "Món ăn không được để trống")
    @JsonIgnore
    private DishEntity dish; // Sửa tên biến từ 'dishe' thành 'dish' cho đúng ngữ nghĩa
}