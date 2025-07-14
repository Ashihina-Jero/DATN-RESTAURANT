//package com.poly.goldenbamboo.clientcontroller;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import com.poly.goldenbamboo.dtos.BranchDTO;
//import com.poly.goldenbamboo.entities.CategoryEntity;
//import com.poly.goldenbamboo.entities.DishEntity;
//import com.poly.goldenbamboo.entities.OrderDetailEntity;
//import com.poly.goldenbamboo.services.AccountService;
//import com.poly.goldenbamboo.services.BranchService;
//import com.poly.goldenbamboo.services.CategoryService;
//import com.poly.goldenbamboo.services.DishService;
//
//import com.poly.goldenbamboo.services.TableService;
//import com.poly.goldenbamboo.repositories.MenuDishJPA;
//import com.poly.goldenbamboo.repositories.MenuJPA;
//import com.poly.goldenbamboo.repositories.TableJPA;
//
//@RestController
//public class HomeClientController {
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
//	@GetMapping("/Client/Branch/{branchId}")
//	public Map<String, Object> orderDeitalMap(@PathVariable("branchId") Integer branchId,
//			@PathVariable("orderId") Integer orderId, @PathVariable("categoryId") Integer categoryId) {
//		Map<String, Object> response = new HashMap<>();
//
//		BranchDTO branch = branchService.getBranchById(branchId);
//		List<DishEntity> foods = dishService.getDefaultMenuDishesByBranchAndCategory(branchId, categoryId);
//		List<CategoryEntity> categories = categoryService.getAllCategory();
//		List<OrderDetailEntity> orderDetails = orderDetailService.getOrderDetailsByOrderId(orderId);
//
//		response.put("orderDetails", orderDetails);
//		response.put("branch", branch);
//		response.put("categories", categories);
//		response.put("foods", foods);
//		return response;
//	}
//
//    @GetMapping("/Branch/{id}/DefaultMenu")
//    public ResponseEntity<List<DishEntity>> getDishesFromDefaultMenu(@PathVariable("id") Integer id) {
//        List<DishEntity> dishes = dishService.getDishesFromDefaultMenu(id);
//        return ResponseEntity.ok(dishes);
//    }
//
//    @GetMapping("/Branch/{id}/DefaultMenu-Table")
//    public ResponseEntity<Map<String, Object>> getAllBranchData(@PathVariable("id") Integer branchId) {
//        Map<String, Object> response = new HashMap<>();
//
//        response.put("dishes", dishService.getDishesFromDefaultMenu(branchId));
//        response.put("tables", tableJPA.findByBranchId(branchId));
//
//
//        return ResponseEntity.ok(response);
//    }
//}
