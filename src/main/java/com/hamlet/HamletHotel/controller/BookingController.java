package com.hamlet.HamletHotel.controller;

import com.hamlet.HamletHotel.entity.Booking;
import com.hamlet.HamletHotel.payload.response.Response;
import com.hamlet.HamletHotel.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping("/booking-room/{roomId}/{userId}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<Response> saveBookings(@PathVariable Long roomId, @PathVariable Long userId,
                                                 @RequestBody Booking bookingRequest){
        Response response = bookingService.saveBooking( roomId, userId, bookingRequest);
        return ResponseEntity.status(response.getResponseCode()).body(response);
    }

    @GetMapping("/all-bookings")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<Response> getAllBookings(){
        Response response = bookingService.getAllBooking();
        return ResponseEntity.status(response.getResponseCode()).body(response);
    }

    @GetMapping("/get-booking-by-confirmation-code/{confirmationCode}")
    public ResponseEntity<Response> getBookingByConfirmationCode(@PathVariable String confirmationCode){
        Response response = bookingService.findBookingByConfirmationCode(confirmationCode);
        return ResponseEntity.status(response.getResponseCode()).body(response);
    }

    @DeleteMapping("/cancel-bookings/{bookingId}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<Response> cancelBooking(@PathVariable Long bookingId){
        Response response = bookingService.cancelBookings(bookingId);
        return ResponseEntity.status(response.getResponseCode()).body(response);
    }
}
