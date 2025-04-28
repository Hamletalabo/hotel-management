package com.hamlet.HamletHotel.service.impl;

import com.hamlet.HamletHotel.config.JwtService;
import com.hamlet.HamletHotel.entity.User;
import com.hamlet.HamletHotel.enums.Roles;
import com.hamlet.HamletHotel.exception.AlreadyExistsException;
import com.hamlet.HamletHotel.payload.request.LoginRequest;
import com.hamlet.HamletHotel.payload.request.UserRequest;
import com.hamlet.HamletHotel.payload.response.ApiResponse;
import com.hamlet.HamletHotel.payload.response.UserRegisterResponse;
import com.hamlet.HamletHotel.repository.UserRepository;
import com.hamlet.HamletHotel.service.UserService;
import com.hamlet.HamletHotel.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtService jwtService;
    private AuthenticationManager authenticationManager;

    @Override
    public UserRegisterResponse register(User user) {

        UserRegisterResponse response = new UserRegisterResponse();
        try {
            if (user.getRoles() == null){
                user.setRoles(Roles.USER);
            }
            if (userRepository.existsByEmail(user.getEmail())){
                throw new AlreadyExistsException(user.getEmail() + "Already exists");
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            User savedUser = userRepository.save(user);
            UserRequest userRequest = Utils.mapUserEntityToUserRequest(savedUser);
            response.setUser(userRequest);
            response.setResponse(ApiResponse.builder()
                            .responseCode("200")
                    .build());

        } catch (AlreadyExistsException e) {
            response.setResponse(ApiResponse.builder()
                            .responseCode("400")
                            .responseMessage("User already exists")
                    .build());
        }
        return response;
    }

    @Override
    public UserRegisterResponse login(LoginRequest registration) {
        return null;
    }

    @Override
    public ApiResponse getUserBookingsHistory(String userId) {
        return null;
    }

    @Override
    public ApiResponse deleteUser(String userId) {
        return null;
    }

    @Override
    public ApiResponse getUserById(String userId) {
        return null;
    }

    @Override
    public ApiResponse getUserDetails(String userId) {
        return null;
    }
}
