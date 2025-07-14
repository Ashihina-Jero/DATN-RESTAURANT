package com.poly.restaurant.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDTO {
    // Có thể là username hoặc số điện thoại
    @NotBlank
    private String loginIdentifier;

    @NotBlank
    private String password;
}