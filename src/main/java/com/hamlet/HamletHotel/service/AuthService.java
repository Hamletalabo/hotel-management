package com.hamlet.HamletHotel.service;

import com.hamlet.HamletHotel.payload.request.LoginRequest;
import com.hamlet.HamletHotel.payload.request.RegistrationRequest;
import com.hamlet.HamletHotel.payload.request.UserRequest;
import com.hamlet.HamletHotel.payload.response.ApiResponse;
import com.hamlet.HamletHotel.payload.response.LoginResponse;
import com.hamlet.HamletHotel.payload.response.UserRegisterResponse;

public interface AuthService {

    UserRegisterResponse register(RegistrationRequest request);

    LoginResponse login (LoginRequest loginRequest);
}
