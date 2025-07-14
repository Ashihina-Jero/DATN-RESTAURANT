package com.poly.restaurant.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class PaymentResponseDTO {
    private OrderResponseDTO order;
    private BigDecimal changeDue; // Tiền thừa trả lại khách
}