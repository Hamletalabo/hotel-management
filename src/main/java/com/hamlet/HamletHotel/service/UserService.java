package com.hamlet.HamletHotel.service;

import com.hamlet.HamletHotel.entity.User;
import com.hamlet.HamletHotel.payload.request.LoginRequest;
import com.hamlet.HamletHotel.payload.request.UserRequest;
import com.hamlet.HamletHotel.payload.response.ApiResponse;
import com.hamlet.HamletHotel.payload.response.LoginResponse;
import com.hamlet.HamletHotel.payload.response.UserRegisterResponse;

public interface UserService {

    UserRegisterResponse register(User user);

    LoginResponse login(LoginRequest loginRequest);

    ApiResponse getAllUsers();

    ApiResponse getUserBookingsHistory(String userId);

    ApiResponse deleteUser(String userId);

    ApiResponse getUserById(String userId);

    ApiResponse getUserInfo(String email);
}
