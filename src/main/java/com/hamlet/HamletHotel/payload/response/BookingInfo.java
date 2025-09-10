package com.hamlet.HamletHotel.payload.response;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingInfo {
    private Long id;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private int numberOfAdults;
    private int numberOfChildren;
    private int totalNumberOfGuest;
    private String bookingConfirmationCode;
    private Long userId;
    private Long roomId;
    private String roomType;
}
