package com.poly.goldenbamboo.dtos;

import com.poly.goldenbamboo.entities.enums.OrderStatus;
import com.poly.goldenbamboo.entities.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List; // Để bao gồm OrderDetailResponseDTOs

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {
    private Integer id;
    private Timestamp orderDate;
    private PaymentMethod paymentMethod;
    private BigDecimal prepay;
    private OrderStatus status;
    private String description;
    private BigDecimal totalAmount;

    // Chỉ trả về ID và tên của Account liên kết
    private Integer accountId;
    private String accountName; // Giả định AccountEntity có trường 'name'

    // Chỉ trả về ID và tên của Branch liên kết
    private Integer branchId;
    private String branchName; // Giả định BranchEntity có trường 'name'

    // Chỉ trả về ID của Table liên kết (có thể null)
    private Integer tableId;
    private String tableName; // Giả định TableEntity có trường 'name'

    // Bao gồm danh sách OrderDetailResponseDTOs
    private List<OrderDetailResponseDTO> orderDetails;
}