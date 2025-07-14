//package com.poly.goldenbamboo.controllers;
//
//import org.springframework.dao.DataIntegrityViolationException;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.http.ResponseEntity.BodyBuilder;
//import org.springframework.web.bind.annotation.*;
//
//import com.poly.goldenbamboo.entities.DiscountComboEntity;
//import com.poly.goldenbamboo.services.DiscountComboService;
//
//@RestController
//@RequestMapping("/api/discount-combos")
//public class DiscountComboController {
//
//    private final DiscountComboService discountComboService;
//
//    public DiscountComboController(DiscountComboService discountComboService) {
//        this.discountComboService = discountComboService;
//    }
//
//    @GetMapping
//    public ResponseEntity<?> getAll() {
//        try {
//            return ResponseEntity.ok(discountComboService.getAll());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Lỗi khi lấy danh sách discount combo");
//        }
//    }
//
//    @PostMapping
//    public ResponseEntity<?> create(@RequestBody DiscountCombodtos dto) {
//        try {
//            DiscountComboEntity created = discountComboService.create(dto);
//            return ResponseEntity.status(HttpStatus.CREATED).body(created);
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body("Lỗi khi tạo discount combo");
//        }
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<?> update(
//            @PathVariable Integer id,
//            @RequestBody DiscountCombodtos dto) {
//        try {
//            DiscountComboEntity updated = discountComboService.update(id, dto);
//            if (updated != null) {
//                return ResponseEntity.ok(updated);
//            }
//            return ResponseEntity.notFound().build();
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body("Lỗi khi cập nhật discount combo");
//        }
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> delete(@PathVariable Integer id) {
//        try {
//            if (discountComboService.delete(id)) {
//                return ResponseEntity.noContent().build();
//            }
//            return ((BodyBuilder) ResponseEntity.notFound()).body("Discount combo không tồn tại");
//        } catch (DataIntegrityViolationException e) {
//            return ResponseEntity.status(HttpStatus.CONFLICT)
//                    .body("Không thể xóa do tồn tại ràng buộc dữ liệu");
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body("Lỗi khi xóa discount combo");
//        }
//    }
//
//}