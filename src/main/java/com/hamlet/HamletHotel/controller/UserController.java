package com.hamlet.HamletHotel.controller;

import com.hamlet.HamletHotel.payload.request.EditUserRequest;
import com.hamlet.HamletHotel.payload.response.*;
import com.hamlet.HamletHotel.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PutMapping("/update/{userId}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<EditUserResponse> updateUser(
            @PathVariable Long userId,
            @RequestBody EditUserRequest request) {

        EditUserResponse response = userService.editUser(userId, request);

        return ResponseEntity.status(response.getResponseCode()).body(response);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserListResponse> getAllUsers() {
        UserListResponse response = userService.getAllUsers();
        return ResponseEntity.status(response.getResponseCode()).body(response);
    }

    @GetMapping("/get-by-id/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable("userId") Long userId) {
        UserResponse response = userService.getUserById(userId);
        return ResponseEntity.status(response.getResponseCode()).body(response);
    }

    @DeleteMapping("/delete/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable("userId") Long userId) {
        ApiResponse response = userService.deleteUser(userId);
        return ResponseEntity.status(response.getResponseCode()).body(response);
    }

    @GetMapping("/profile-info")
    public ResponseEntity<UserResponse> userProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        UserResponse response = userService.getUserInfo(email);
        return ResponseEntity.status(response.getResponseCode()).body(response);
    }

    @GetMapping("/get-user-bookings/{userId}")
    public ResponseEntity<UserBookingHistoryResponse> userBookingHistory(@PathVariable("userId") Long userId) {
        UserBookingHistoryResponse response = userService.getUserBookingsHistory(userId);
        return ResponseEntity.status(response.getResponseCode()).body(response);
    }
}