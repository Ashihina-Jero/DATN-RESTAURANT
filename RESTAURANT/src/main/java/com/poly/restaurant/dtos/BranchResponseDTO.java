package com.poly.restaurant.dtos;

import com.poly.restaurant.entities.enums.BranchStatus;
import lombok.Data;

@Data
public class BranchResponseDTO {
    private Integer id;
    private String name;
    private String address;
    private String phone;
    private String description;
    private BranchStatus status;
    
}