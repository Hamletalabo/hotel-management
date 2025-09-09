package com.hamlet.HamletHotel.payload.request;

import lombok.Data;
import java.time.LocalDate;

@Data
public class UpdateBookingRequest {
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private int numberOfAdults;
    private int numberOfChildren;
}
