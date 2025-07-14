package com.poly.restaurant.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MenuRequestDTO {
    @NotBlank
    private String name;

    @NotNull
    private Boolean isDefault;

    // ID của chi nhánh mà menu này thuộc về (có thể null)
    private Integer branchId;
}