package com.poly.goldenbamboo.dtos;

import com.poly.goldenbamboo.entities.enums.PaymentMethod;
import com.poly.goldenbamboo.entities.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDTO {

    // Không cần id và orderDate khi tạo mới

    private PaymentMethod paymentMethod; // Có thể null
    
    @PositiveOrZero(message = "Tiền cọc phải lớn hơn hoặc bằng 0")
    private BigDecimal prepay;

    @NotNull(message = "Trạng thái đơn hàng không được để trống")
    private OrderStatus status;

    @Size(max = 1000, message = "Mô tả không được vượt quá 1000 ký tự")
    private String description;

    @PositiveOrZero(message = "Tổng tiền phải lớn hơn hoặc bằng 0")
    private BigDecimal totalAmount; // Có thể được tính toán ở backend

    // Nhận ID của Account
    @NotNull(message = "ID Tài khoản không được để trống")
    private Integer accountId;

    // Nhận ID của Branch
    @NotNull(message = "ID Chi nhánh không được để trống")
    private Integer branchId;

    // Nhận ID của Table (có thể null)
    private Integer tableId;

    // Danh sách các chi tiết đơn hàng (có thể là List<OrderDetailRequestDTO>)
    // Khi tạo/cập nhật Order, bạn sẽ gửi kèm danh sách các món/combo
    private List<OrderDetailRequestDTO> orderDetails;
}