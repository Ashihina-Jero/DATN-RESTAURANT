// package com.poly.goldenbamboo.entities.enums;
// ReservationStatus.java
package com.poly.goldenbamboo.entities.enums;

public enum ReservationStatus {
    PENDING,     // Đặt chỗ đang chờ xác nhận
    CONFIRMED,   // Đặt chỗ đã được xác nhận
    CHECKED_IN,  // Khách đã đến và nhận bàn
    COMPLETED,   // Đặt chỗ đã hoàn thành (khách đã rời đi)
    CANCELED,    // Đặt chỗ đã bị hủy
    NO_SHOW,     // Khách không đến và không hủy
    EXPIRED      // Đặt chỗ đã quá thời gian mà khách không đến và không check-in
}