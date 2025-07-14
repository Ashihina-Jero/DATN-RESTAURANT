package com.poly.restaurant.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(name = "combos")
@PrimaryKeyJoinColumn(name = "id")
public class ComboEntity extends MenuItemEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    // Các trường id, name, description, image, price, status đã được kế thừa.

    // ---------- CÁC MỐI QUAN HỆ RIÊNG CỦA COMBO ----------
    
    @ToString.Exclude
    @JsonManagedReference
    @OneToMany(mappedBy = "combo", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ComboDishEntity> comboDishes = new ArrayList<>();
    
}