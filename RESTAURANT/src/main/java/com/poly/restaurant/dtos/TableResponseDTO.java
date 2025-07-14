package com.poly.restaurant.dtos;

import com.poly.restaurant.entities.enums.TableStatus;
import lombok.Data;

@Data
public class TableResponseDTO {

    private Integer id;
    private String name;
    private TableStatus status;
    private Integer branchId;
    private String branchName;
}