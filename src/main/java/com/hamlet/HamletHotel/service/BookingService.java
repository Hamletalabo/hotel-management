package com.hamlet.HamletHotel.service;

import com.hamlet.HamletHotel.entity.Booking;
import com.hamlet.HamletHotel.payload.response.Response;

public interface BookingService {
    Response saveBooking(Long roomId, Long userId, Booking booking);
    Response findBookingByConfirmationCode(String confirmationCode);
    Response getAllBooking();
    Response cancelBookings(Long bookingId);
}
