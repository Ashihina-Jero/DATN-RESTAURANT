package com.poly.restaurant.dtos;

import java.math.BigDecimal;

import com.poly.restaurant.entities.enums.PaymentMethod;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class PaymentRequestDTO {

    @NotNull(message = "Phương thức thanh toán không được để trống")
    private PaymentMethod paymentMethod;
    
    @PositiveOrZero(message = "Tiền khách đưa phải là số dương")
    private BigDecimal cashReceived;
    
}