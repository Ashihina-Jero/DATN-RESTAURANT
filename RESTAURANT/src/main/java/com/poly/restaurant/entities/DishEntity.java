package com.poly.restaurant.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@Entity
@Table(name = "dishes")
@PrimaryKeyJoinColumn(name = "id")
public class DishEntity extends MenuItemEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    // KHÔNG CẦN CÁC TRƯỜNG id, name, description, image, price, status nữa.
    // Chúng đã được kế thừa từ MenuItemEntity.

    // ---------- CÁC MỐI QUAN HỆ RIÊNG CỦA DISH ----------

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "category_id", nullable = true)
    private CategoryEntity category;

    // Giữ lại các mối quan hệ OneToMany nếu bạn cần chúng ở Entity này
    @ToString.Exclude
    @JsonManagedReference
    @OneToMany(mappedBy = "dish", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ComboDishEntity> comboDishes = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "dish", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuDishEntity> menuDishes = new ArrayList<>();
}