package com.poly.restaurant.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "order_details")
public class OrderDetailEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // --- THAY THẾ CHO `menuItemId` và `isCombo` ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_item_id", nullable = false)
    private MenuItemEntity menuItem; // <-- Mối quan hệ trực tiếp tới MenuItem!

    // --- CÁC TRƯỜNG LƯU TRỮ TẠI THỜI ĐIỂM ĐẶT HÀNG (GIỮ NGUYÊN) ---

    @Column(name = "quantity", nullable = false)
    private int quantity;

    // Lưu lại giá tại thời điểm đặt hàng
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    // Lưu lại tên tại thời điểm đặt hàng
    @Column(name = "name")
    private String name;

    // Lưu lại % giảm giá tại thời điểm đặt hàng
    @Column(name = "discount_percentage")
    private Integer discountPercentage;

    // --- MỐI QUAN HỆ VỚI ORDER (GIỮ NGUYÊN) ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private OrderEntity order;
}