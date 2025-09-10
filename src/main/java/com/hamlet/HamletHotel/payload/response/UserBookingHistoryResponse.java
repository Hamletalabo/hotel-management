package com.hamlet.HamletHotel.payload.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserBookingHistoryResponse {
    private int responseCode;
    private String responseMessage;
    private List<BookingInfo> bookings;
}
