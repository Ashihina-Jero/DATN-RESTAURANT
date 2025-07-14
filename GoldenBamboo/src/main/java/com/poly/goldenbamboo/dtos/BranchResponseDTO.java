package com.poly.goldenbamboo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BranchResponseDTO {
    private int id;
    private String address;
    private String description;
    private String name;
    private boolean parentId;
    private boolean status;
}