package com.poly.restaurant.entities;

import com.poly.restaurant.entities.enums.DiscountStatus;
import com.poly.restaurant.entities.enums.DiscountType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "discounts")
public class DiscountEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private LocalDate startDate;
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private DiscountStatus status;

    @Enumerated(EnumType.STRING)
    private DiscountType type;

    private BigDecimal value;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "discount_branches",
            joinColumns = @JoinColumn(name = "discount_id"),
            inverseJoinColumns = @JoinColumn(name = "branch_id")
    )
    private Set<BranchEntity> branches = new HashSet<>();

    @OneToMany(mappedBy = "discount", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DiscountDishEntity> discountDishes = new ArrayList<>();

    @OneToMany(mappedBy = "discount", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DiscountComboEntity> discountCombos = new ArrayList<>();
}