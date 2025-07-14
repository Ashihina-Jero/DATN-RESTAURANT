package com.poly.goldenbamboo.dtos;

import com.poly.goldenbamboo.entities.enums.AccountStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountRequestDTO {

    // Không cần id, createAt, updateAt khi tạo mới hoặc cập nhật

    @NotBlank(message = "Tên không được để trống")
    @Size(max = 100, message = "Tên không được vượt quá 100 ký tự")
    private String name;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 8, message = "Mật khẩu phải có ít nhất 8 ký tự")
    private String password; // Mật khẩu sẽ được mã hóa trong Service

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Số điện thoại không hợp lệ")
    private String phone;

    private AccountStatus status; // Cho phép client đặt trạng thái (ví dụ: khi tạo tài khoản mới)

    // Nhận ID của branch và role từ client để thiết lập mối quan hệ
    private Integer branchId; // Sử dụng Integer để có thể null nếu branch không bắt buộc
    private Integer roleId;   // Sử dụng Integer vì role là bắt buộc nhưng vẫn có thể truyền null nếu logic cho phép
}