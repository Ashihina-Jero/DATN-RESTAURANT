package com.poly.restaurant.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "menu_dishes") // Tên bảng theo quy ước (chữ thường, số nhiều)
public class MenuDishEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Sửa lỗi typo
    @Column(name = "id")
    private Integer  id;

    // Liên kết với DishEntity
    @ManyToOne(fetch = FetchType.LAZY) // Tải lười để tối ưu hiệu suất
    @JoinColumn(name = "dish_id", nullable = false) // Khóa ngoại, không được null
    @NotNull(message = "Món ăn không được để trống")
    @JsonIgnore
    private DishEntity dish; // Sửa tên biến từ 'dishe' thành 'dish' cho đúng ngữ nghĩa

    // Liên kết với MenuEntity
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false) // Khóa ngoại, không được null
    @NotNull(message = "Menu không được để trống")
    @JsonIgnore
    private MenuEntity menu; // Sửa tên biến từ 'menus' thành 'menu' cho đúng ngữ nghĩa
    
    @Column(name = "price")
    private BigDecimal price;

}