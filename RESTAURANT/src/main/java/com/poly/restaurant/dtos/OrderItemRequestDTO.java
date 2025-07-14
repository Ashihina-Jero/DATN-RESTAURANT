package com.poly.restaurant.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderItemRequestDTO {
    @NotNull
    private Integer menuItemId;

    @Min(1)
    private int quantity;
}