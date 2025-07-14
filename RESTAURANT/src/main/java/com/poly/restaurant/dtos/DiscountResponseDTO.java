package com.poly.restaurant.dtos;

import com.poly.restaurant.entities.enums.DiscountStatus;
import com.poly.restaurant.entities.enums.DiscountType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class DiscountResponseDTO {
    private Integer id;
    private String name;
    private DiscountType type;
    private BigDecimal value;
    private LocalDate startDate;
    private LocalDate endDate;
    private DiscountStatus status;
    private List<String> branchNames;
    private List<String> dishNames;
    private List<String> comboNames;
}