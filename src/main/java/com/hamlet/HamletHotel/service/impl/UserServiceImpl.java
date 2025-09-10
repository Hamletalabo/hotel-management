package com.hamlet.HamletHotel.service.impl;

import com.hamlet.HamletHotel.entity.Booking;
import com.hamlet.HamletHotel.entity.User;
import com.hamlet.HamletHotel.exception.NotFoundException;
import com.hamlet.HamletHotel.payload.request.UserRequest;
import com.hamlet.HamletHotel.payload.response.*;
import com.hamlet.HamletHotel.repository.UserRepository;
import com.hamlet.HamletHotel.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserListResponse getAllUsers() {
        List<UserInfo> users = userRepository.findAll()
                .stream()
                .map(this::mapToUserInfo)
                .collect(Collectors.toList());

        return UserListResponse.builder()
                .responseCode(200)
                .responseMessage("All users retrieved successfully")
                .users(users)
                .build();
    }

    @Override
    public UserBookingHistoryResponse getUserBookingsHistory(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + userId));

        List<BookingInfo> bookingInfos = user.getBookings().stream()
                .map(this::mapToBookingInfo)
                .collect(Collectors.toList());

        return UserBookingHistoryResponse.builder()
                .responseCode(200)
                .responseMessage("Booking history retrieved successfully")
                .bookings(bookingInfos)
                .build();
    }

    @Override
    public ApiResponse deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + userId));

        userRepository.delete(user);

        return ApiResponse.builder()
                .responseCode(200)
                .responseMessage("User deleted successfully")
                .build();
    }

    @Override
    public UserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + userId));

        return UserResponse.builder()
                .responseCode(200)
                .responseMessage("User retrieved successfully")
                .userInfo(mapToUserInfo(user))
                .build();
    }

    @Override
    public UserResponse getUserInfo(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + email));

        return UserResponse.builder()
                .responseCode(200)
                .responseMessage("User info retrieved successfully")
                .userInfo(mapToUserInfo(user))
                .build();
    }

    // ---------------- Mapping Helpers ----------------
    private UserInfo mapToUserInfo(User user) {
        return UserInfo.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRoles().name())
                .build();
    }

    private BookingInfo mapToBookingInfo(Booking booking) {
        return BookingInfo.builder()
                .id(booking.getId())
                .checkInDate(booking.getCheckInDate())
                .checkOutDate(booking.getCheckOutDate())
                .roomId(booking.getRoom().getId())
                .roomType(booking.getRoom().getRoomType())
                .build();
    }

}
