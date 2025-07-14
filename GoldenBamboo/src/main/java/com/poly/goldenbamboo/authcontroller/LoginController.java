//package com.poly.goldenbamboo.authcontroller;
//
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.poly.goldenbamboo.dtos.LoginDTO;
//import com.poly.goldenbamboo.entities.AccountEntity;
//import com.poly.goldenbamboo.services.AccountService;
//
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletResponse;
//
//@RestController
//public class LoginController {
//    @Autowired
//    private AccountService accountService;
//
//    @PostMapping("/Login")
//    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO, HttpServletResponse response) {
//        try {
//            // Gọi dịch vụ đăng nhập và lấy người dùng
//            AccountEntity user = accountService.login(loginDTO);
//            
//            // Tạo và thêm cookie cho userId
//            Cookie cookie = new Cookie("userId", String.valueOf(user.getId()));
//            cookie.setPath("/");
//            cookie.setHttpOnly(true);
//            cookie.setMaxAge(7 * 24 * 60 * 60); // Thời gian sống của cookie (7 ngày)
//            response.addCookie(cookie);
//            
//            // Trả về thông báo thành công
//            return ResponseEntity.ok().body(Map.of(
//                "success", true,
//                "message", "Đăng nhập thành công"
//            ));
//
//        } catch (IllegalArgumentException e) {
//            // Trả về thông báo lỗi nếu đăng nhập không thành công
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
//                "success", false,
//                "message", e.getMessage() // Trả về thông báo lỗi từ exception
//            ));
//        }
//    }
//}
