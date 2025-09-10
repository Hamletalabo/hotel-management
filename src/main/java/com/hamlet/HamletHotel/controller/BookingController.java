package com.hamlet.HamletHotel.controller;

import com.hamlet.HamletHotel.payload.request.CreateBookingRequest;
import com.hamlet.HamletHotel.payload.request.UpdateBookingRequest;
import com.hamlet.HamletHotel.payload.response.ApiResponse;
import com.hamlet.HamletHotel.payload.response.BookingListResponse;
import com.hamlet.HamletHotel.payload.response.BookingResponse;
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

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<BookingResponse> createBooking(@RequestBody CreateBookingRequest request) {
        BookingResponse response = bookingService.saveBooking(request);
        return ResponseEntity.status(response.getResponseCode()).body(response);
    }

    @PutMapping("/update/{bookingId}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<BookingResponse> updateBooking(@PathVariable Long bookingId,
                                                         @RequestBody UpdateBookingRequest request) {
        BookingResponse response = bookingService.updateBooking(bookingId, request);
        return ResponseEntity.status(response.getResponseCode()).body(response);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<BookingListResponse> getAllBookings() {
        BookingListResponse response = bookingService.getAllBookings();
        return ResponseEntity.status(response.getResponseCode()).body(response);
    }

    @GetMapping("/by-confirmation/{confirmationCode}")
    public ResponseEntity<BookingResponse> getBookingByConfirmationCode(@PathVariable String confirmationCode) {
        BookingResponse response = bookingService.findBookingByConfirmationCode(confirmationCode);
        return ResponseEntity.status(response.getResponseCode()).body(response);
    }

    @DeleteMapping("/cancel/{bookingId}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<ApiResponse> cancelBooking(@PathVariable Long bookingId) {
        ApiResponse response = bookingService.cancelBooking(bookingId);
        return ResponseEntity.status(response.getResponseCode()).body(response);
    }
}
