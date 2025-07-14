package com.poly.restaurant.entities;

import com.poly.restaurant.entities.enums.BranchComboStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "branch_combos")
public class BranchComboEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id")
    private BranchEntity branch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "combo_id")
    private ComboEntity combo;
    
    @PositiveOrZero(message = "Giá phải lớn hơn hoặc bằng 0") 
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    // Trạng thái của combo tại chi nhánh
    @Enumerated(EnumType.STRING)
    private BranchComboStatus status;
}