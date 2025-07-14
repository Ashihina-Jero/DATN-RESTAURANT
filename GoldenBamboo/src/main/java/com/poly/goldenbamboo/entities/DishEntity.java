package com.poly.goldenbamboo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "dishes")
@PrimaryKeyJoinColumn(name = "id")
public class DishEntity extends MenuItemEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    // KHÔNG CẦN CÁC TRƯỜNG id, name, description, image, price, status nữa.
    // Chúng đã được kế thừa từ MenuItemEntity.

    // ---------- CÁC MỐI QUAN HỆ RIÊNG CỦA DISH ----------

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;

    // Giữ lại các mối quan hệ OneToMany nếu bạn cần chúng ở Entity này
    @JsonIgnore
    @OneToMany(mappedBy = "dish", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ComboDishEntity> comboDishes = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "dish", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuDishEntity> menuDishes = new ArrayList<>();
}