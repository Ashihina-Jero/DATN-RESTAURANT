package com.poly.goldenbamboo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp; // Assuming ReservationEntity has Timestamp fields
import java.time.LocalDate; // Assuming ReservationEntity has LocalDate fields
import java.time.LocalTime; // Assuming ReservationEntity has LocalTime fields
import java.math.BigDecimal; // Assuming ReservationEntity might have BigDecimal for total/prepay

import com.poly.goldenbamboo.entities.enums.ReservationStatus; // Assuming this enum exists
import com.poly.goldenbamboo.entities.enums.PaymentMethod;    // Assuming this enum exists

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResponseDTO {
    // Basic fields from ReservationEntity (assuming these exist based on common reservation patterns)
    private Integer id;
    private Timestamp reservationDate; // Date and time of reservation creation
    private LocalDate desiredDate;     // Desired date for reservation
    private LocalTime desiredTime;     // Desired time for reservation
    private Integer numberOfGuests;
    private String customerName;
    private String customerPhone;
    private String notes;
    private ReservationStatus status;
    private PaymentMethod paymentMethod; // Optional, if reservation itself has a payment method
    private BigDecimal prepayAmount;     // Optional, if reservation itself has a prepay

    // Account (who made the reservation)
    private Integer accountId;
    private String accountName;

    // Branch (where the reservation is made)
    private Integer branchId;
    private String branchName;

    // Tables reserved (list of tables, if direct many-to-many or a join table for tables exists)
    // For simplicity, we might just list table IDs or simple DTOs
    // If you have a ReservationTableEntity for many-to-many between Reservation and Table,
    // you'd include List<ReservationTableResponseDTO> here.
    // Assuming a simple List of table IDs for now if you don't have ReservationTableEntity directly
    // private List<Integer> tableIds; // If direct relationship to tables, not via ReservationTableEntity

    // For simplicity, let's include actual reservation details here.
    private List<ReservationDetailResponseDTO> reservationDetails;
}