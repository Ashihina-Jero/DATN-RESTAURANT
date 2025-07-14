package com.poly.goldenbamboo.dtos;

import lombok.Data;

@Data
public class AddDetailRequestDTO {
    private Integer orderId;
    private Integer menuItemId; // ID của món ăn hoặc combo
    private int quantity;
    // không cần isCombo nữa
}