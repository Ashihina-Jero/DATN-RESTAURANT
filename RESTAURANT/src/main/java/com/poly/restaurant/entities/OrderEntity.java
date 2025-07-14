package com.poly.restaurant.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.poly.restaurant.entities.enums.OrderStatus;
import com.poly.restaurant.entities.enums.PaymentMethod;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "orders") // Tên bảng đã đúng theo quy ước (chữ thường, số nhiều)
public class OrderEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @CreationTimestamp 
    @Column(name = "order_date", nullable = false, updatable = false)
    private Timestamp orderDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = true)
    private PaymentMethod paymentMethod;

    @PositiveOrZero(message = "Tiền cọc phải lớn hơn hoặc bằng 0")
    @Column(name = "prepay", nullable = false, precision = 10, scale = 2)
    private BigDecimal prepay;

    @Enumerated(EnumType.STRING) 
    @Column(name = "status", nullable = false, length = 50)
    private OrderStatus status;
    
    @Size(max = 1000, message = "Mô tả không được vượt quá 1000 ký tự")
    @Column(name = "description", nullable = true)
    private String description;

    @PositiveOrZero(message = "Tổng tiền phải lớn hơn hoặc bằng 0")
    @Column(name = "total_amount", nullable = true, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    // Liên kết với OrderDetailEntity
    @ToString.Exclude
    @JsonManagedReference
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<OrderDetailEntity> orderDetails = new ArrayList<>(); // Danh sách chi tiết đơn hàng

    // Liên kết với AccountEntity
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false) // Khóa ngoại, không được null
    @NotNull(message = "Tài khoản không được để trống")
    @JsonIgnore
    private AccountEntity account;

    // Liên kết với BranchEntity
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false) // Khóa ngoại, không được null
    @NotNull(message = "Chi nhánh không được để trống")
    @JsonIgnore
    private BranchEntity branch;

    // Liên kết với TableEntity
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_id", nullable = true) // Có thể để null nếu đơn hàng không gắn với bàn
    @JsonBackReference
    private TableEntity table;
}