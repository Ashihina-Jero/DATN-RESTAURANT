package com.poly.restaurant.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class AddComboToMenuRequestDTO {

    @NotNull(message = "ID của combo không được để trống")
    private Integer comboId;

    // Giá đặc biệt cho combo trong menu này (có thể null)
    private BigDecimal price;
}