package com.hamlet.HamletHotel.service;

import com.hamlet.HamletHotel.entity.User;
import com.hamlet.HamletHotel.payload.request.EditUserRequest;
import com.hamlet.HamletHotel.payload.request.LoginRequest;
import com.hamlet.HamletHotel.payload.response.*;

public interface UserService {

    EditUserResponse editUser(Long userId, EditUserRequest userRequest);
    UserListResponse getAllUsers();
    UserBookingHistoryResponse getUserBookingsHistory(Long userId);
    ApiResponse deleteUser(Long userId);
    UserResponse getUserById(Long userId);
    UserResponse getUserInfo(String email);
}
