package com.poly.restaurant.exceptions;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus; // <-- THAY THẾ org.apache.http.HttpStatus BẰNG DÒNG NÀY
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors; // <-- Cần import này cho MethodArgumentNotValidException handler

// Nếu bạn sử dụng ErrorNotificationService, hãy đảm bảo import nó
// import com.poly.restaurant.services.ErrorNotificationService;
// import org.springframework.beans.factory.annotation.Autowired;


@ControllerAdvice
public class GlobalExceptionHandler {

    // Nếu bạn sử dụng ErrorNotificationService, hãy bỏ comment dòng này
    // @Autowired
    // private ErrorNotificationService errorNotificationService;

    // Đảm bảo import ErrorDetails
    // import com.poly.restaurant.exceptions.ErrorDetails; // <-- THÊM DÒNG NÀY NẾU CHƯA CÓ

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        // Nếu dùng ErrorNotificationService, gọi nó ở đây
        // errorNotificationService.sendErrorNotification(ex, request.getDescription(false), "user_id_if_available", null);

        ErrorDetails errorDetails = new ErrorDetails(
            LocalDateTime.now(),
            ex.getMessage(),
            request.getDescription(false),
            "NOT_FOUND"
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND); // <-- Đã sửa HttpStatus
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // Nếu dùng ErrorNotificationService, gọi nó ở đây
        // errorNotificationService.sendErrorNotification(
        //     ex,
        //     "Validation Error",
        //     "user_id_if_available",
        //     ex.getBindingResult().getFieldErrors().stream()
        //         .collect(Collectors.toMap(e -> e.getField(), e -> e.getDefaultMessage()))
        // );

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage()));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST); // <-- Đã sửa HttpStatus
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorDetails> handleDataIntegrityViolation(DataIntegrityViolationException ex, WebRequest request) {
        // Nếu dùng ErrorNotificationService, gọi nó ở đây
        // errorNotificationService.sendErrorNotification(ex, request.getDescription(false), "user_id_if_available", Map.of("errorCause", ex.getMostSpecificCause().getMessage()));

        String errorMessage = "Data integrity violation: " + ex.getMostSpecificCause().getMessage();
        ErrorDetails errorDetails = new ErrorDetails(
            LocalDateTime.now(),
            errorMessage,
            request.getDescription(false),
            "CONFLICT"
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT); // <-- Đã sửa HttpStatus (thường dùng CONFLICT cho lỗi ràng buộc)
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorDetails> handleIllegalStateException(IllegalStateException ex, WebRequest request) {
        // Nếu dùng ErrorNotificationService, gọi nó ở đây
        // errorNotificationService.sendErrorNotification(ex, request.getDescription(false), "user_id_if_available", null);

        ErrorDetails errorDetails = new ErrorDetails(
            LocalDateTime.now(),
            ex.getMessage(),
            request.getDescription(false),
            "BAD_REQUEST"
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST); // <-- Đã sửa HttpStatus
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGlobalException(Exception ex, WebRequest request) {
        // Nếu dùng ErrorNotificationService, gọi nó ở đây
        // errorNotificationService.sendErrorNotification(ex, request.getDescription(false), "user_id_if_available", null);

        ErrorDetails errorDetails = new ErrorDetails(
            LocalDateTime.now(),
            ex.getMessage(),
            request.getDescription(false),
            "INTERNAL_SERVER_ERROR"
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR); // <-- Đã sửa HttpStatus
    }
}