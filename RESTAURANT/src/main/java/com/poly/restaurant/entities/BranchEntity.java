package com.poly.restaurant.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.poly.restaurant.entities.enums.BranchStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "branches")
public class BranchEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Tên chi nhánh không được để trống")
    private String name;

    @Column(nullable = true)
    @NotBlank(message = "Địa chỉ không được để trống")
    private String address;
    
    @Column(nullable = true)
    private String phone;
    
    @Column(nullable = true)
    private String description;

    // Dùng Enum cho trạng thái, rõ ràng và dễ mở rộng
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BranchStatus status;


    // --- CÁC MỐI QUAN HỆ HAI CHIỀU ---
    // (mappedBy trỏ đến tên trường trong các Entity tương ứng)

    @OneToMany(mappedBy = "branch")
    @JsonIgnore // Dùng JsonIgnore để tránh lỗi lặp vô hạn khi trả về JSON
    private List<AccountEntity> accounts;

    @OneToMany(mappedBy = "branch")
    @JsonIgnore
    private List<OrderEntity> orders;

    @OneToMany(mappedBy = "branch")
    @JsonIgnore
    private List<ReservationEntity> reservations;

    // Giả sử TableEntity và MenuEntity cũng có quan hệ với Branch
    @OneToMany(mappedBy = "branch")
    @JsonIgnore
    private List<TableEntity> tables;

    @OneToMany(mappedBy = "branch")
    @JsonIgnore
    private List<MenuEntity> menus;
    
 // --- THÊM CÁC MỐI QUAN HỆ NÀY ---

    // Một chi nhánh có nhiều món ăn được bán (BranchDish)
    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<BranchDishEntity> branchDishes = new ArrayList<>();

    // Một chi nhánh có nhiều combo được bán (BranchCombo)
    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<BranchComboEntity> branchCombos = new ArrayList<>();
    

}