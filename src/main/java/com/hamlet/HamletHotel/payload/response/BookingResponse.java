package com.hamlet.HamletHotel.payload.response;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class BookingResponse {
    private Long id;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private int numberOfAdults;
    private int numberOfChildren;
    private int totalNumberOfGuest;
    private String bookingConfirmationCode;
    private Long userId;
    private Long roomId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
