package com.poly.restaurant.dtos;

import com.poly.restaurant.entities.enums.DiscountType;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class DiscountRequestDTO {
	
    @NotBlank
    private String name;
    @NotNull
    private DiscountType type;
    @NotNull
    private BigDecimal value;
    @NotNull
    @FutureOrPresent
    private LocalDate startDate;
    @NotNull
    @FutureOrPresent
    private LocalDate endDate;

    private List<Integer> branchIds;
    private List<Integer> dishIds;
    private List<Integer> comboIds;
}