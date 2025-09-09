package com.hamlet.HamletHotel.controller;

import com.hamlet.HamletHotel.entity.User;
import com.hamlet.HamletHotel.payload.request.LoginRequest;
import com.hamlet.HamletHotel.payload.request.RegistrationRequest;
import com.hamlet.HamletHotel.payload.response.ApiResponse;
import com.hamlet.HamletHotel.payload.response.LoginResponse;
import com.hamlet.HamletHotel.payload.response.UserRegisterResponse;
import com.hamlet.HamletHotel.service.AuthService;
import com.hamlet.HamletHotel.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponse> register(@RequestBody RegistrationRequest request){
        UserRegisterResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest){
        LoginResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }
}
