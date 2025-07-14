package com.poly.restaurant.entities;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "combo_dishes") // Tên bảng theo quy ước (chữ thường, số nhiều)
public class ComboDishEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    // Liên kết với ComboEntity
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY) // Tải lười để tối ưu hiệu suất
    @JoinColumn(name = "combo_id", nullable = false) // Khóa ngoại, không được null
    @NotNull(message = "Combo không được để trống")
    @JsonBackReference
    private ComboEntity combo;

    // Liên kết với DishEntity
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dish_id", nullable = false) // Khóa ngoại, không được null
    @NotNull(message = "Món ăn không được để trống")
    private DishEntity dish; // Sửa tên biến từ 'dishe' thành 'dish' cho đúng ngữ nghĩa
    
    @Column(nullable = false)
    private int quantity; 
}