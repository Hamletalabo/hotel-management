package com.hamlet.HamletHotel.controller;

import com.hamlet.HamletHotel.payload.response.ApiResponse;
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

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse> getAllUsers(){
        ApiResponse response = userService.getAllUsers();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-by-id/{userId}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable("userId") String userId){
        ApiResponse response = userService.getUserById(userId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable("userId") String userId){
        ApiResponse response = userService.deleteUser(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/profile-info")
    public ResponseEntity<ApiResponse> userProfile(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        ApiResponse response = userService.getUserInfo(email);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-user-bookings/{userId}")
    public ResponseEntity<ApiResponse> userBookingHistory(@PathVariable("userId") String userId){
        ApiResponse response = userService.getUserBookingsHistory(userId);
        return ResponseEntity.ok(response);
    }
}
