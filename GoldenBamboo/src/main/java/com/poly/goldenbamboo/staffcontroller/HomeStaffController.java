//package com.poly.goldenbamboo.staffcontroller;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.CookieValue;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.poly.goldenbamboo.controllers.ReservationController;
//import com.poly.goldenbamboo.dtos.BranchDTO;
//import com.poly.goldenbamboo.dtos.OrderDTO;
//import com.poly.goldenbamboo.dtos.OrderDetailDTO;
//import com.poly.goldenbamboo.dtos.TableDTO;
//import com.poly.goldenbamboo.dtos.UpdateQuantityDTO;
//import com.poly.goldenbamboo.entities.CategoryEntity;
//import com.poly.goldenbamboo.entities.ComboEntity;
//import com.poly.goldenbamboo.entities.DishEntity;
//import com.poly.goldenbamboo.entities.OrderDetailEntity;
//import com.poly.goldenbamboo.entities.OrderEntity;
//import com.poly.goldenbamboo.mappers.OrderDetailMapper;
//import com.poly.goldenbamboo.services.AccountService;
//import com.poly.goldenbamboo.services.BranchService;
//import com.poly.goldenbamboo.services.CategoryService;
//import com.poly.goldenbamboo.services.ComboService;
//import com.poly.goldenbamboo.services.DishService;
//import com.poly.goldenbamboo.services.OrderDetailService;
//import com.poly.goldenbamboo.services.OrderService;
//import com.poly.goldenbamboo.services.TableService;
//
//import jakarta.persistence.EntityNotFoundException;
//
//@RestController
//public class HomeStaffController {
//
//	private final ReservationController reservationController;
//
//	@Autowired
//	private OrderService orderService;
//
//	@Autowired
//	private AccountService accountService;
//
//	@Autowired
//	private DishService dishService;
//
//	@Autowired
//	private TableService tableService;
//
//	@Autowired
//	private CategoryService categoryService;
//
//	@Autowired
//	private OrderDetailService orderDetailService;
//
//	@Autowired
//	private BranchService branchService;
//
//	@Autowired
//	private ComboService comboService;
//	
//	  @Autowired
//	    private OrderDetailMapper orderDetailMapper;
//
//	HomeStaffController(ReservationController reservationController) {
//		this.reservationController = reservationController;
//	}
//
//	@GetMapping("/Staff")
//	public BranchDTO findBranchByUserIdCookie(@CookieValue("userId") Integer userId) {
//		return accountService.getBranchByUserId(userId);
//	}
//
//
//	@PostMapping("/Staff/CreateOrder")
//	public OrderDTO createOrder(@RequestBody OrderDTO dto, @CookieValue("userId") Integer userId) {
//		dto.setAccountId(userId);
//		return orderService.createOrder(dto);
//	}
//
//
//	// Trong file OrderController.java
//
//	
//	
//	// Trong file OrderController.java
//
//	@PostMapping("/api/orders/force-create")
//	public ResponseEntity<?> forceCreateNewOrder(@RequestBody Map<String, Integer> payload,
//	                                             @CookieValue(name = "userId", required = false) Integer accountId) {
//	    if (accountId == null) {
//	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Vui lòng đăng nhập.");
//	    }
//	    Integer tableId = payload.get("tableId");
//	    if (tableId == null) {
//	        return ResponseEntity.badRequest().body("Thiếu tableId.");
//	    }
//
//	    try {
//	        OrderEntity newOrder = orderService.forceCreateNewOrderForTable(tableId, accountId);
//	        return ResponseEntity.ok(newOrder);
//	    } catch (RuntimeException e) {
//	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//	    }
//	}
//	
//	@GetMapping("/Staff/Order/{orderId}")
//    public ResponseEntity<OrderDTO> findOrderDTO(@PathVariable("orderId") Integer orderId) {
//        // Gọi Service để lấy OrderDTO
//        OrderDTO orderDTO = orderService.getOrderById(orderId);
//
//        // Trả về OrderDTO trong một ResponseEntity với trạng thái HTTP 200 OK
//        return ResponseEntity.ok(orderDTO);
//    }
//
//	// Thực hiện hiển thị giao diện chọn món
//	@GetMapping("/Staff/Branch/{branchId}/Table/{tableId}/Order/{orderId}/Category/{categoryId}")
//	public Map<String, Object> Order(@PathVariable("branchId") Integer branchId,
//			@PathVariable("tableId") Integer tableId, @PathVariable("categoryId") Integer categoryId,
//			@PathVariable("orderId") Integer orderId) {
//
//		Map<String, Object> res = new HashMap<>();
//
//		// 1. Lấy thông tin bàn
//		List<TableDTO> tables = tableService.getAllTableByBranchId(branchId);
//
//		// 2. Lấy danh mục
//		List<CategoryEntity> categories = categoryService.getAllCategory();
//
//		// 3. Lấy chi tiết đơn hàng
//		List<OrderDetailDTO> orderDetails = orderDetailService.getOrderDetailsByOrderId(orderId);
//		for (OrderDetailDTO detail : orderDetails) {
//			if (detail.isType()) {
//				ComboEntity combo = comboService.getComboById(detail.getDishOrComboId());
//				if (combo != null) {
//					detail.setName(combo.getName());
//					detail.setImage(combo.getImage());
//					detail.setDescription(combo.getDescription());
//				}
//			} else {
//				DishEntity dish = dishService.getDishById(detail.getDishOrComboId());
//				if (dish != null) {
//					detail.setName(dish.getName());
//					detail.setImage(dish.getImage());
//					detail.setDescription(dish.getDescription());
//				}
//			}
//		}
//
//		// 4. Lấy combo mặc định theo chi nhánh
//		List<ComboEntity> combos = comboService.getDefaultCombosByBranch(branchId);
//
//		// 5. Lấy danh sách món ăn theo category
//		List<DishEntity> foods = new ArrayList<>();
//		if (categoryId != -1) {
//			foods = dishService.getDefaultMenuDishesByBranchAndCategory(branchId, categoryId);
//		}
//		
//		OrderDTO order = orderService.getOrderById(orderId);
//		
//
//		// 6. Trả dữ liệu về
//		res.put("orderDetails", orderDetails);
//		res.put("categories", categories);
//		res.put("foods", foods);
//		res.put("combos", combos);
//		res.put("tables", tables);
//		res.put("order", order);
//		
//
//		return res;
//	}
//	
//	@PutMapping("/api/orders/{id}")
//	public ResponseEntity<OrderDTO> updateOrder(
//	        @PathVariable("id") Integer id, 
//	        @RequestBody OrderDTO orderDTO) {
//	    try {
//	        orderDTO.setId(id); // Đảm bảo ID được set đúng
//	        OrderDTO updatedOrder = orderService.updateOrder(orderDTO);
//	        return ResponseEntity.ok(updatedOrder);
//	    } catch (RuntimeException e) {
//	        return ResponseEntity.notFound().build();
//	    }
//	}
//	
//	
//	// Đường dẫn mới, gọn hơn và chỉ cần orderId
////	@GetMapping("/Staff/Order/{orderId}/Details")
////	public Map<String, Object> getOrderDetailsOnly(@PathVariable("orderId") Integer orderId) {
////
////	    Map<String, Object> res = new HashMap<>();
////
////	    // 1. Chỉ lấy chi tiết đơn hàng
////	    List<OrderDetailDTO> orderDetails = orderDetailService.getOrderDetailsByOrderId(orderId);
////
////	    // 2. Lấy thông tin tên, ảnh cho từng chi tiết
////	    for (OrderDetailDTO detail : orderDetails) {
////	        if (detail.isType()) { // Là Combo
////	            ComboEntity combo = comboService.getComboById(detail.getDishOrComboId());
////	            if (combo != null) {
////	                detail.setName(combo.getName());
////	                detail.setImage(combo.getImage());
////	            }
////	        } else { // Là Món ăn
////	            DishEntity dish = dishService.getDishById(detail.getDishOrComboId());
////	            if (dish != null) {
////	                detail.setName(dish.getName());
////	                detail.setImage(dish.getImage());
////	            }
////	        }
////	    }
////
////	    // 3. Chỉ trả về danh sách chi tiết
////	    res.put("orderDetails", orderDetails);
////	    return res;
////	}
//	
//
//
//
//	@PostMapping("/Order/AddFoodToOrder")
//	public ResponseEntity<OrderDetailDTO> addDishToOrder(@RequestBody OrderDetailDTO dto) {
//	    try {
//	        // 1. Service xử lý và trả về Entity đã được lưu
//	        OrderDetailEntity savedEntity = orderDetailService.addOrUpdateDetail(dto);
//
//	        // 2. Mapper chuyển Entity thành DTO để trả về cho client
//	        OrderDetailDTO responseDto = orderDetailMapper.toDTO(savedEntity);
//
//	        // 3. Trả về DTO trong một ResponseEntity với status 201 Created
//	        // Đây là status code phù hợp cho việc tạo mới tài nguyên thành công
//	        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
//
//	    } catch (Exception e) {
//	        // Có thể bắt lỗi và trả về response phù hợp
//	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//	    }
//	}
//
//
//
//	// Thêm {orderId} vào URL và dùng @PathVariable
//	@PutMapping("/Order/{orderId}/UpdateQuantity")
//	public ResponseEntity<?> updateQuantity(@PathVariable("orderId") Integer orderId, @RequestBody UpdateQuantityDTO dto) {
//	    try {
//	        Integer orderDetailId = dto.getOrderDetailId();
//	        Integer quantity = dto.getQuantity();
//
//	        if (orderDetailId == null || quantity == null) {
//	            return ResponseEntity.badRequest().body("Yêu cầu thiếu orderDetailId hoặc quantity.");
//	        }
//
//	        // Gọi service với đủ 3 tham số
//	        OrderDetailDTO updatedDetail = orderDetailService.updateQuantity(orderId, orderDetailId, quantity);
//	        return ResponseEntity.ok(updatedDetail);
//
//	    } catch (RuntimeException e) { // Bắt lỗi chung từ service
//	        // Có thể phân loại lỗi rõ hơn nếu service trả về các exception cụ thể
//	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//	    } catch (Exception e) {
//	        // Nên dùng logger thay cho printStackTrace
//	        // log.error("Lỗi khi cập nhật số lượng:", e);
//	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi máy chủ khi cập nhật số lượng.");
//	    }
//	}
//	    
//	 // Giả sử bạn đặt trong OrderController.java
//	 // OrderController.java
//	    @DeleteMapping("/api/staff/orders/details/{detailId}")
//	    public ResponseEntity<?> removeFood(@PathVariable("detailId") Integer detailId) {
//	        try {
//	            orderDetailService.deleteDetailById(detailId);
//	            return ResponseEntity.ok("Đã xóa món ăn và cập nhật lại đơn hàng thành công.");
//	        } catch (RuntimeException e) {
//	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//	        }
//	    
//	    
//	}
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	@PostMapping("/orders/{orderId}/checkout")
//    public ResponseEntity<?> checkoutOrder(@PathVariable("orderId") Integer orderId) {
//        try {
//            // Gọi phương thức checkout từ service đã được tối ưu
//            OrderEntity paidOrder = orderService.checkoutOrder(orderId);
//
//            // Tạo phản hồi thành công
//            Map<String, Object> response = new HashMap<>();
//            response.put("message", "Thanh toán thành công!");
//            response.put("finalAmount", paidOrder.getTotalAmount());
//            response.put("orderStatus", paidOrder.getStatus());
//            response.put("orderId", paidOrder.getId());
//
//            return ResponseEntity.ok(response);
//
//        } catch (RuntimeException e) {
//            // Bắt lỗi nếu không tìm thấy đơn hàng (từ findOrderById)
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//        } catch (Exception e) {
//            // Bắt các lỗi không mong muốn khác
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                                 .body("Lỗi máy chủ trong quá trình thanh toán: " + e.getMessage());
//        }
//    }
//}
