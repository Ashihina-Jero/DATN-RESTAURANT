package com.poly.restaurant.dtos;

import com.poly.restaurant.entities.enums.AccountStatus;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class AccountResponseDTO {
    private Long id;
    private String username;
    private String name;
    private String phone;
    private AccountStatus status;
    private LocalDateTime createAt;
    
    // Thông tin chi nhánh
    private Integer branchId;
    private String branchName;
    
    // THAY ĐỔI Ở ĐÂY: Chứa danh sách tên các vai trò
    private List<String> roles; 
}