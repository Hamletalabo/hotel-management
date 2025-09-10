package com.hamlet.HamletHotel.service.impl;

import com.hamlet.HamletHotel.entity.Booking;
import com.hamlet.HamletHotel.entity.Room;
import com.hamlet.HamletHotel.entity.User;
import com.hamlet.HamletHotel.exception.BadRequestException;
import com.hamlet.HamletHotel.exception.NotFoundException;
import com.hamlet.HamletHotel.payload.request.CreateBookingRequest;
import com.hamlet.HamletHotel.payload.request.UpdateBookingRequest;
import com.hamlet.HamletHotel.payload.response.*;
import com.hamlet.HamletHotel.repository.BookingRepository;
import com.hamlet.HamletHotel.repository.RoomRepository;
import com.hamlet.HamletHotel.repository.UserRepository;
import com.hamlet.HamletHotel.service.BookingService;
import com.hamlet.HamletHotel.service.RoomService;
import com.hamlet.HamletHotel.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {


    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public BookingResponse saveBooking(CreateBookingRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        validateBookingDates(request.getCheckInDate(), request.getCheckOutDate());

        // 3. fetch room
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new NotFoundException("Room not found"));

        // 4. check availability
        boolean isAvailable = roomRepository.findAvailableRoomsByDatesAndTypes(
                request.getCheckInDate(),
                request.getCheckOutDate(),
                room.getRoomType()
        ).stream().anyMatch(r -> r.getId().equals(room.getId()));

        if (!isAvailable) {
            throw new BadRequestException("Room is not available for the selected dates.");
        }

        // 5. build booking
        Booking booking = Booking.builder()
                .checkInDate(request.getCheckInDate())
                .checkOutDate(request.getCheckOutDate())
                .numberOfAdults(request.getNumberOfAdults())
                .numberOfChildren(request.getNumberOfChildren())
                .totalNumberOfGuest(request.getNumberOfAdults() + request.getNumberOfChildren())
                .bookingConfirmationCode(Utils.confirmationCode(6))
                .user(user)
                .room(room)
                .build();

        Booking savedBooking = bookingRepository.save(booking);

        return BookingResponse.builder()
                .responseCode(201)
                .responseMessage("Booking created successfully")
                .bookingInfo(mapToBookingInfo(savedBooking))
                .build();
    }

    @Override
    public BookingResponse findBookingByConfirmationCode(String confirmationCode) {
        Booking booking = bookingRepository.findByBookingConfirmationCode(confirmationCode)
                .orElseThrow(() -> new NotFoundException("Booking not found with confirmation code: " + confirmationCode));

        return BookingResponse.builder()
                .responseCode(200)
                .responseMessage("Booking retrieved successfully")
                .bookingInfo(mapToBookingInfo(booking))
                .build();
    }

    @Override
    public BookingListResponse getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

        if (bookings.isEmpty()) {
            return BookingListResponse.builder()
                    .responseCode(200)
                    .responseMessage("No bookings found")
                    .bookings(List.of())
                    .build();
        }
        List<BookingInfo> bookingInfos = bookings.stream()
                .map(this::mapToBookingInfo)
                .toList();

        return BookingListResponse.builder()
                .responseCode(200)
                .responseMessage("Bookings retrieved successfully")
                .bookings(bookingInfos)
                .build();
    }

    @Override
    @Transactional
    public ApiResponse cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking not found"));

        bookingRepository.delete(booking);

        return ApiResponse.builder()
                .responseCode(200)
                .responseMessage("Booking cancelled successfully")
                .build();
    }


    @Override
    @Transactional
    public BookingResponse updateBooking(Long bookingId, UpdateBookingRequest request) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking not found"));

        validateBookingDates(request.getCheckInDate(), request.getCheckOutDate());

        booking.setCheckInDate(request.getCheckInDate());
        booking.setCheckOutDate(request.getCheckOutDate());
        booking.setNumberOfAdults(request.getNumberOfAdults());
        booking.setNumberOfChildren(request.getNumberOfChildren());
        booking.setTotalNumberOfGuest(request.getNumberOfAdults() + request.getNumberOfChildren());

        Booking updatedBooking = bookingRepository.save(booking);

        return BookingResponse.builder()
                .responseCode(200)
                .responseMessage("Booking updated successfully")
                .bookingInfo(mapToBookingInfo(updatedBooking))
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

    private BookingInfo mapToBookingInfo(Booking booking) {
        return BookingInfo.builder()
                .id(booking.getId())
                .checkInDate(booking.getCheckInDate())
                .checkOutDate(booking.getCheckOutDate())
                .numberOfAdults(booking.getNumberOfAdults())
                .numberOfChildren(booking.getNumberOfChildren())
                .totalNumberOfGuest(booking.getTotalNumberOfGuest())
                .bookingConfirmationCode(booking.getBookingConfirmationCode())
                .userId(booking.getUser().getId())
                .roomId(booking.getRoom().getId())
                .roomType(booking.getRoom().getRoomType())
                .build();
    }
}
