//package com.poly.goldenbamboo.controllers;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.poly.goldenbamboo.dtos.TableDTO;
//import com.poly.goldenbamboo.dtos.UpdateStatusDto;
//import com.poly.goldenbamboo.services.TableService;
//
//@RestController
//@RequestMapping("/api/tables")
//public class TableController {
//
//	@Autowired
//	private TableService tableService;
//	
//	@GetMapping("/branch/{branchId}")
//    public List<TableDTO> findAllTableByBranch(@PathVariable("branchId") Integer branchId) {
//        
//        // Controller chỉ gọi đến service để lấy dữ liệu
//        // Mọi logic xử lý nằm ở tầng Service
//        return tableService.getAllTableByBranchId(branchId);
//    }
//	
//	
//	@PutMapping("/{tableId}/status")
//	public ResponseEntity<?> updateTableStatus(
//	        @PathVariable("tableId") Integer tableId,           // <-- Lấy số 5 từ URL
//	        @RequestBody UpdateStatusDto statusDto) {  // <-- Lấy { "status": 1 } từ body
//
//	    // Lấy giá trị status từ DTO
//	    int newStatus = statusDto.getStatus();
//
//	    // Gọi service với đầy đủ 2 thông tin
////	    tableService.updateStatus(tableId, newStatus);
//
//	    return ResponseEntity.ok().body("Cập nhật trạng thái bàn thành công.");
//	}
//}
