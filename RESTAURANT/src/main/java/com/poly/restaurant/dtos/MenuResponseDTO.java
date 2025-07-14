package com.poly.restaurant.dtos;

import lombok.Data;
import java.util.List;

@Data
public class MenuResponseDTO {
    private Integer id;
    private String name;
    private Boolean isDefault;
    private Integer branchId;
    private String branchName;
    private List<MenuDishResponseDTO> dishes; 
    private List<MenuComboResponseDTO> combos;
}