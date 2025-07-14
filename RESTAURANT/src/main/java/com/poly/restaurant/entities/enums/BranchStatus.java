package com.poly.restaurant.entities.enums;

public enum BranchStatus {
    ACTIVE,     // Đang hoạt động
    /**
     * Vẫn là chi nhánh hợp lệ nhưng tạm thời không nhận đơn.
     * (Ví dụ: hết giờ làm việc, nghỉ lễ, tạm hết hàng).
     */
    TEMPORARILY_CLOSED,
    /**
     * Chi nhánh không còn hoạt động, ẩn khỏi hệ thống.
     */
    INACTIVE
}