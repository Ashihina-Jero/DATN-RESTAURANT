package com.poly.restaurant.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDetails {
    private LocalDateTime timestamp;
    private String message;
    private String details;
    private String errorCode; // Mã lỗi tùy chỉnh (ví dụ: NOT_FOUND, VALIDATION_ERROR)
}