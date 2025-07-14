package com.poly.restaurant.dtos;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class OrderRequestDTO {

    // ID của các đối tượng liên quan
    @NotNull
    private Integer tableId;

    @NotNull
    private Long accountId;

    @NotNull
    private Integer branchId;

    // Danh sách các món hàng và số lượng
    @NotEmpty(message = "Đơn hàng phải có ít nhất một món")
    private List<OrderItemRequestDTO> items;
    
    private String description;
    
    @PositiveOrZero(message = "Tiền trả trước phải là số không âm")
    private BigDecimal prepay;
}