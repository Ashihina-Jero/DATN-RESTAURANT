package com.poly.goldenbamboo.dtos;

import com.poly.goldenbamboo.entities.enums.TableStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Không cần import List vì các mối quan hệ OneToMany sẽ bị bỏ qua trong ResponseDTO này
// private List<OrderEntity> orders;
// private List<ReservationEntity> reservations;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableResponseDTO {
    private int id;
    private String number;
    private TableStatus status;

    // Chỉ trả về ID và tên của Branch liên kết
    private Integer branchId;
    private String branchName; // Giả định BranchEntity có trường 'name'
}