package com.poly.restaurant.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonIgnore;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "menus") 
public class MenuEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotBlank(message = "Tên menu không được để trống")
    @Size(max = 100, message = "Tên menu không được vượt quá 100 ký tự")
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "is_default", nullable = false)
    private Boolean isDefault; // Cho biết đây có phải là menu mặc định không

    // Liên kết với MenuComboEntity
    @OneToMany(mappedBy = "menu", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<MenuComboEntity> menuCombos = new ArrayList<>(); // Danh sách combo trong menu

    // Liên kết với MenuDishEntity
    @OneToMany(mappedBy = "menu", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<MenuDishEntity> menuDishes = new ArrayList<>(); // Danh sách món ăn trong menu

    // Liên kết với BranchEntity
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = true) // True để khi null là dùng cho toàn bộ hệ thống
    @JsonIgnore
    private BranchEntity branch; // Chi nhánh sở hữu menu
}