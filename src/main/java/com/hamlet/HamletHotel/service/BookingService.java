package com.hamlet.HamletHotel.service;

import com.hamlet.HamletHotel.entity.Booking;
import com.hamlet.HamletHotel.payload.response.ApiResponse;

public interface BookingService {
    ApiResponse saveBooking(Long roomId, Long userId, Booking booking);
    ApiResponse findBookingByConfirmationCode(String confirmationCode);
    ApiResponse getAllBooking();
    ApiResponse cancelBookings(Long bookingId);
}
