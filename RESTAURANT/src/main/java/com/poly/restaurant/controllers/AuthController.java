package com.poly.restaurant.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poly.restaurant.dtos.AuthResponseDTO;
import com.poly.restaurant.dtos.LoginRequestDTO;
import com.poly.restaurant.dtos.StaffRegistrationRequestDTO;
import com.poly.restaurant.dtos.UserRegistrationRequestDTO;
import com.poly.restaurant.entities.AccountEntity;
import com.poly.restaurant.services.AccountService;
import com.poly.restaurant.services.JwtService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AccountService accountService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    
    @PostMapping("/register-user")
    public ResponseEntity<AuthResponseDTO> registerUser(@Valid @RequestBody UserRegistrationRequestDTO request) {
        AuthResponseDTO authResponse = accountService.registerUser(request);
        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }

    // Endpoint để admin tạo tài khoản cho nhân viên/quản lý
    // Lưu ý: Endpoint này sau này cần được bảo vệ, chỉ cho ADMIN truy cập
    @PostMapping("/register-staff")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AuthResponseDTO> registerStaff(@Valid @RequestBody StaffRegistrationRequestDTO request) {
        AuthResponseDTO authResponse = accountService.registerStaff(request);
        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }

    // Endpoint để đăng nhập
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        // 1. Xác thực từ username/phone và password
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getLoginIdentifier(),
                        request.getPassword()
                )
        );

        // 2. Nếu xác thực thành công, lấy thông tin user
        var user = (AccountEntity) authentication.getPrincipal();

        // 3. Tạo JWT token
        String token = jwtService.generateToken(user);

        // 4. Trả về token cho client
        AuthResponseDTO response = new AuthResponseDTO(token, user.getUsername());
        return ResponseEntity.ok(response);
    }
}