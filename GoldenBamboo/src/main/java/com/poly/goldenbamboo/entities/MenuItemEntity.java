package com.poly.goldenbamboo.entities;

import com.poly.goldenbamboo.entities.enums.MenuItemStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.math.BigDecimal; // <-- Import BigDecimal

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

    @PositiveOrZero(message = "Giá phải lớn hơn hoặc bằng 0") // <-- Chuyển validation lên đây
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price; // <-- Dùng BigDecimal

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private MenuItemStatus status;
}