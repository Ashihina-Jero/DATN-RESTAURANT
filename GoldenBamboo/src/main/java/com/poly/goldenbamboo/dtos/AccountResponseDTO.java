package com.poly.goldenbamboo.dtos;

import com.poly.goldenbamboo.entities.enums.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponseDTO {
    private int id;
    private Timestamp createAt;
    private String name;
    private String phone;
    private AccountStatus status;
    private Timestamp updateAt;

    // Chỉ trả về ID và tên của Branch và Role để tránh vòng lặp và lộ thông tin quá nhiều
    private Integer branchId;
    private String branchName; // Thêm trường này để dễ hiển thị hơn
    private Integer roleId;
    private String roleName; // Thêm trường này để dễ hiển thị hơn
}