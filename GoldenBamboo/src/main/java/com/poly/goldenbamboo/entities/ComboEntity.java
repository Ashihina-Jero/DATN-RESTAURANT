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
@Table(name = "combos")
@PrimaryKeyJoinColumn(name = "id")
public class ComboEntity extends MenuItemEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    // Các trường id, name, description, image, price, status đã được kế thừa.

    // ---------- CÁC MỐI QUAN HỆ RIÊNG CỦA COMBO ----------

    @JsonIgnore
    @OneToMany(mappedBy = "combo", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ComboDishEntity> comboDishes = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "combo", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuComboEntity> menuCombos = new ArrayList<>();
}