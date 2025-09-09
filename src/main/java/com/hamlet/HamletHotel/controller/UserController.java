package com.hamlet.HamletHotel.controller;

import com.hamlet.HamletHotel.payload.response.Response;
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
    public ResponseEntity<Response> getAllUsers(){
        Response response = userService.getAllUsers();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-by-id/{userId}")
    public ResponseEntity<Response> getUserById(@PathVariable("userId") String userId){
        Response response = userService.getUserById(userId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<Response> deleteUser(@PathVariable("userId") String userId){
        Response response = userService.deleteUser(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/profile-info")
    public ResponseEntity<Response> userProfile(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Response response = userService.getUserInfo(email);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-user-bookings/{userId}")
    public ResponseEntity<Response> userBookingHistory(@PathVariable("userId") String userId){
        Response response = userService.getUserBookingsHistory(userId);
        return ResponseEntity.ok(response);
    }
}
