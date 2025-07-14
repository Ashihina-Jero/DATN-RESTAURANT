package com.poly.restaurant.entities;

import java.math.BigDecimal;

import com.poly.restaurant.entities.enums.MenuItemStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data; 

@Data
@Entity
@Table(name = "menu_items")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class MenuItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Tên không được để trống") // <-- Chuyển validation lên đây
    @Size(max = 100, message = "Tên không được vượt quá 100 ký tự")
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 1000, message = "Mô tả không được vượt quá 1000 ký tự") // <-- Chuyển validation lên đây
    @Column(name = "description")
    private String description;

    @Column(name = "image")
    private String image;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private MenuItemStatus status;
}