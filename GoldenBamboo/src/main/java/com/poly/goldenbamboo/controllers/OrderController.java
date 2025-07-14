//package com.poly.goldenbamboo.controllers;
//import org.springframework.http.HttpStatus;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.poly.goldenbamboo.dtos.AddDetailRequestDTO;
//import com.poly.goldenbamboo.dtos.UpdateQuantityRequestDTO;
//import com.poly.goldenbamboo.entities.OrderDetailEntity;
//import com.poly.goldenbamboo.mappers.OrderDetailMapper;
//import com.poly.goldenbamboo.services.OrderDetailService;
//import com.poly.goldenbamboo.services.OrderService;
//
//@RestController
//@RequestMapping("/api/orders")
//public class OrderController {
//
//	@Autowired
//	private OrderService orderService;
//	
//	@Autowired
//	private OrderDetailService orderDetailService;
//	
//	@Autowired
//	private OrderDetailMapper orderDetailMapper;
//
//	@GetMapping("/{orderId}")
//	public ResponseEntity<OrderDTO> getOrderById(@PathVariable("orderId") Integer orderId) {
//		OrderDTO order = orderService.getOrderById(orderId); // Giả sử phương thức này trả về DTO
//		return ResponseEntity.ok(order);
//	}
//
//	@GetMapping("/active-for-table/{tableId}")
//	public ResponseEntity<OrderDTO> getActiveOrderForTable(@PathVariable("tableId") Integer tableId) {
//		OrderDTO order = orderService.findActiveOrderByTableId(tableId); // Giả sử có phương thức này
//		return ResponseEntity.ok(order);
//	}
//	
//	/**
//     * Thêm một món mới vào đơn hàng.
//     */
//    // POST /api/orders/{orderId}/details
//	@PostMapping("/{orderId}/details")
//	public ResponseEntity<OrderDetailDTO> addDetailToOrder(@PathVariable("orderId") Integer orderId,
//			@RequestBody AddDetailRequestDTO request) { // Cần tạo DTO này
//
//		request.setOrderId(orderId);
//		OrderDetailEntity savedEntity = orderDetailService.addOrUpdateDetail(request); // Giả sử service dùng DTO này
//		OrderDetailDTO responseDto = orderDetailMapper.toDTO(savedEntity);
//		return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
//	}
//	
//	/**
//     * Cập nhật số lượng của một món trong đơn hàng.
//     */
//    // PUT /api/orders/{orderId}/details/{detailId}
//	@PutMapping("/{orderId}/details/{detailId}")
//	public ResponseEntity<OrderDetailDTO> updateDetailQuantity(@PathVariable("orderId") Integer orderId,
//			@PathVariable Integer detailId, @RequestBody UpdateQuantityRequestDTO request) { // Cần tạo DTO này
//
//		OrderDetailDTO updatedDto = orderDetailService.updateQuantity(orderId, detailId, request.getQuantity());
//		return ResponseEntity.ok(updatedDto);
//	}
//}
