//package com.poly.goldenbamboo.authcontroller;
//
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.poly.goldenbamboo.dtos.RegisterDTO;
//import com.poly.goldenbamboo.entities.AccountEntity;
//import com.poly.goldenbamboo.services.AccountService;
//
//import jakarta.validation.Valid;
//
//@RestController
//public class RegisterController {
//
//	@Autowired
//	private AccountService accountService;
//
//	@PostMapping("/Register")
//	public ResponseEntity<?> register(@ModelAttribute RegisterDTO registerDTO, BindingResult result) {
//
//		if (result.hasErrors()) {
//			String errorMessage = result.getFieldErrors().stream().map(error -> error.getDefaultMessage()).findFirst()
//					.orElse("Dữ liệu không hợp lệ");
//
//			return ResponseEntity.badRequest().body(Map.of("success", false, "message", errorMessage));
//		}
//
//		try {
//			AccountEntity user = accountService.register(registerDTO);
//			return ResponseEntity.ok(Map.of("success", true, "message", "Đăng ký thành công", "data",
//					Map.of("id", user.getId(), "phone", user.getPhone(), "name", user.getName())));
//		} catch (IllegalArgumentException e) {
//			return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
//		}
//	}
//
//}
