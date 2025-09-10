package com.hamlet.HamletHotel.service;

import com.hamlet.HamletHotel.payload.request.CreateBookingRequest;
import com.hamlet.HamletHotel.payload.request.UpdateBookingRequest;
import com.hamlet.HamletHotel.payload.response.ApiResponse;
import com.hamlet.HamletHotel.payload.response.BookingListResponse;
import com.hamlet.HamletHotel.payload.response.BookingResponse;

public interface BookingService {
    BookingResponse saveBooking(CreateBookingRequest request);
    BookingResponse findBookingByConfirmationCode(String confirmationCode);
    BookingListResponse getAllBookings();
    ApiResponse cancelBooking(Long bookingId);
    BookingResponse updateBooking(Long bookingId, UpdateBookingRequest request);
}
