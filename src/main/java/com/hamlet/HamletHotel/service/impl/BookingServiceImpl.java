package com.hamlet.HamletHotel.service.impl;

import com.hamlet.HamletHotel.entity.Booking;
import com.hamlet.HamletHotel.entity.Room;
import com.hamlet.HamletHotel.entity.User;
import com.hamlet.HamletHotel.exception.BadRequestException;
import com.hamlet.HamletHotel.exception.NotFoundException;
import com.hamlet.HamletHotel.payload.response.ApiResponse;
import com.hamlet.HamletHotel.repository.BookingRepository;
import com.hamlet.HamletHotel.repository.RoomRepository;
import com.hamlet.HamletHotel.repository.UserRepository;
import com.hamlet.HamletHotel.service.BookingService;
import com.hamlet.HamletHotel.service.RoomService;
import com.hamlet.HamletHotel.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final RoomService roomService;
    private final UserRepository userRepository;


    @Transactional
    @Override
    public ApiResponse saveBooking(Long roomId, Long userId, Booking bookingRequest) {

        // 1. Fetch user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        // 2. Validate dates
        validateBookingDates(bookingRequest.getCheckInDate(), bookingRequest.getCheckOutDate());

        // 3. Fetch requested room
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundException("Room not found"));

        // 4. Check availability for this room
        boolean isAvailable = roomRepository.findAvailableRoomsByDatesAndTypes(
                bookingRequest.getCheckInDate(),
                bookingRequest.getCheckOutDate(),
                room.getRoomType()
        ).stream().anyMatch(r -> r.getId().equals(roomId));

        if (!isAvailable) {
            throw new NotFoundException("Selected room is not available for the given dates.");
        }
        Booking booking = Booking.builder()
                .checkInDate(bookingRequest.getCheckInDate())
                .checkOutDate(bookingRequest.getCheckOutDate())
                .numberOfAdults(bookingRequest.getNumberOfAdults())
                .numberOfChildren(bookingRequest.getNumberOfChildren())
                .totalNumberOfGuest(bookingRequest.getTotalNumberOfGuest())
                .bookingConfirmationCode(Utils.confirmationCode(6))
                .user(user)
                .room(room)
                .build();

        bookingRepository.save(booking);

        return ApiResponse.builder()
                .responseCode(200)
                .responseMessage("Booking successful. Confirmation code: " + bookingRequest.getBookingConfirmationCode())
                .build();
    }

    private void validateBookingDates(LocalDate checkIn, LocalDate checkOut) {
        LocalDate today = LocalDate.now();
        if (checkIn.isBefore(today)) {
            throw new BadRequestException("Check-in date cannot be in the past.");
        }
        if (!checkOut.isAfter(checkIn)) {
            throw new BadRequestException("Check-out date must be after check-in date.");
        }
    }


    @Override
    public ApiResponse findBookingByConfirmationCode(String confirmationCode) {
        Booking booking = bookingRepository.findByBookingConfirmationCode(confirmationCode)
                .orElseThrow(() -> new NotFoundException("Booking not found with confirmation code: " + confirmationCode));

        return ApiResponse.builder()
                .responseCode(200)
                .responseMessage("Booking retrieved successfully")
                .booking(Utils.mapBookingsEntityBookingRequest(booking))
                .build();
    }

    @Override
    public ApiResponse getAllBooking() {
        List<Booking> bookings = bookingRepository.findAll();

        if (bookings.isEmpty()) {
            return ApiResponse.builder()
                    .responseCode(200)
                    .responseMessage("No bookings found")
                    .build();
        }

        return ApiResponse.builder()
                .responseCode(200)
                .responseMessage("Bookings retrieved successfully")
                .bookingList(Utils.mapBookingListEntityToBookingListRequest(bookings))
                .build();
    }

    @Override
    public ApiResponse cancelBookings(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking not found"));

        bookingRepository.delete(booking);

        return ApiResponse.builder()
                .responseCode(200)
                .responseMessage("Booking cancelled successfully")
                .build();
    }
}
