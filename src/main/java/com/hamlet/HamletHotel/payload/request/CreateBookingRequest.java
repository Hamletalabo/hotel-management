package com.hamlet.HamletHotel.payload.request;

import lombok.Data;
import java.time.LocalDate;

@Data
public class CreateBookingRequest {
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private int numberOfAdults;
    private int numberOfChildren;
    private Long userId;
    private Long roomId;
}
