package com.hamlet.HamletHotel.service;

import com.hamlet.HamletHotel.entity.User;
import com.hamlet.HamletHotel.payload.request.LoginRequest;
import com.hamlet.HamletHotel.payload.response.*;

public interface UserService {

//    UserRegisterResponse register(User user);
//
//    LoginResponse login(LoginRequest loginRequest);

//    Response getAllUsers();
//
//    Response getUserBookingsHistory(String userId);
//
//    Response deleteUser(String userId);
//
//    Response getUserById(String userId);
//
//    Response getUserInfo(String email);

    UserListResponse getAllUsers();
    UserBookingHistoryResponse getUserBookingsHistory(Long userId);
    ApiResponse deleteUser(Long userId);
    UserResponse getUserById(Long userId);
    UserResponse getUserInfo(String email);
}
