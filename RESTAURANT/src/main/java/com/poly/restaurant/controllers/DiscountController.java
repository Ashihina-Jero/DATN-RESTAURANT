package com.poly.restaurant.controllers;

import com.poly.restaurant.dtos.DiscountRequestDTO;
import com.poly.restaurant.dtos.DiscountResponseDTO;
import com.poly.restaurant.services.DiscountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/discounts")
@RequiredArgsConstructor
public class DiscountController {

    private final DiscountService discountService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DiscountResponseDTO> createDiscount(@Valid @RequestBody DiscountRequestDTO request) {
        DiscountResponseDTO createdDiscount = discountService.createDiscount(request);
        return new ResponseEntity<>(createdDiscount, HttpStatus.CREATED);
    }
}