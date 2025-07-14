package com.poly.restaurant.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poly.restaurant.dtos.OrderRequestDTO;
import com.poly.restaurant.dtos.OrderResponseDTO;
import com.poly.restaurant.dtos.PaymentRequestDTO;
import com.poly.restaurant.dtos.PaymentResponseDTO;
import com.poly.restaurant.dtos.UpdateOrderItemQuantityRequestDTO;
import com.poly.restaurant.services.OrderService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

	private final OrderService orderService;

	@Autowired
	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	@GetMapping
	public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
		List<OrderResponseDTO> orders = orderService.getAllOrders();
		return ResponseEntity.ok(orders);
	}

	@GetMapping("/{id}")
	public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long id) {
		OrderResponseDTO order = orderService.getOrderById(id);
		return ResponseEntity.ok(order);
	}

	@PostMapping
	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STAFF')")
	public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody OrderRequestDTO orderRequestDTO) {
		OrderResponseDTO createdOrder = orderService.createOrder(orderRequestDTO);
		return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
	}

	@PatchMapping("/{id}/cancel")
	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STAFF')")
	public ResponseEntity<Void> cancelOrder(@PathVariable Long id) {
		orderService.cancelOrder(id);
		return ResponseEntity.ok().build();
	}

	@PatchMapping("/{id}/pay")
	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STAFF')")
	public ResponseEntity<PaymentResponseDTO> payOrder(@PathVariable Long id,
			@Valid @RequestBody PaymentRequestDTO paymentRequest) {
		PaymentResponseDTO paymentResponse = orderService.markAsPaid(id, paymentRequest);
		return ResponseEntity.ok(paymentResponse);
	}

	@PutMapping("/{orderId}/details/{orderDetailId}")
	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STAFF')")
	public ResponseEntity<OrderResponseDTO> updateOrderItemQuantity(@PathVariable("orderId") Long orderId,
			@PathVariable("orderDetailId") Long orderDetailId, @RequestBody UpdateOrderItemQuantityRequestDTO request) {

		OrderResponseDTO updatedOrder = orderService.updateOrderItemQuantity(orderId, orderDetailId, request);

		if (updatedOrder == null) {
			// Nếu service trả về null, nghĩa là đơn hàng đã bị xóa
			return ResponseEntity.noContent().build(); // Trả về HTTP 204 No Content
		} else {
			// Nếu không, trả về đơn hàng đã được cập nhật
			return ResponseEntity.ok(updatedOrder);
		}
	}
}