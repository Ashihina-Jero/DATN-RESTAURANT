//package com.poly.goldenbamboo.controllers;
//
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.CookieValue;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.poly.goldenbamboo.dtos.BranchDTO;
//import com.poly.goldenbamboo.dtos.OrderDTO;
//import com.poly.goldenbamboo.services.AccountService;
//import com.poly.goldenbamboo.services.OrderService;
//
//@RestController
//@RequestMapping("/api/staff")
//public class StaffController {
//	
//	@Autowired
//    private AccountService accountService;
//	
//	@Autowired
//	private OrderService orderService;
//	
//	@GetMapping("/my-branch")
//    public ResponseEntity<BranchDTO> getMyBranch(@CookieValue("userId") Integer userId) {
//        BranchDTO branch = accountService.getBranchByUserId(userId);
//        return ResponseEntity.ok(branch);
//    }
//	
//	// Get table create order
//	@PostMapping("/orders/find-or-create")
//	public ResponseEntity<?> findOrCreateOrderForTable(
//	        @RequestBody Map<String, Integer> payload, 
//	        @CookieValue(name = "userId", required = false) Integer accountId) {
//	    
//	    if (accountId == null) {
//	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Vui lòng đăng nhập.");
//	    }
//	    try {
//	        Integer tableId = payload.get("tableId");
//	        OrderDTO orderDto = orderService.findOrCreateOrderForTable(tableId, accountId);
//	        return ResponseEntity.ok(orderDto);
//	    } catch (RuntimeException e) {
//	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//	    }
//	}
//	
//}
