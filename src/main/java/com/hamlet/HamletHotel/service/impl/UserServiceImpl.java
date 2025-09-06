package com.hamlet.HamletHotel.service.impl;

import com.hamlet.HamletHotel.config.JwtService;
import com.hamlet.HamletHotel.entity.JwtToken;
import com.hamlet.HamletHotel.entity.User;
import com.hamlet.HamletHotel.enums.Roles;
import com.hamlet.HamletHotel.enums.TokenType;
import com.hamlet.HamletHotel.exception.AlreadyExistsException;
import com.hamlet.HamletHotel.exception.NotFoundException;
import com.hamlet.HamletHotel.payload.request.LoginRequest;
import com.hamlet.HamletHotel.payload.request.UserRequest;
import com.hamlet.HamletHotel.payload.response.ApiResponse;
import com.hamlet.HamletHotel.payload.response.LoginResponse;
import com.hamlet.HamletHotel.payload.response.UserRegisterResponse;
import com.hamlet.HamletHotel.repository.JwtTokenRepository;
import com.hamlet.HamletHotel.repository.UserRepository;
import com.hamlet.HamletHotel.service.UserService;
import com.hamlet.HamletHotel.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtService jwtService;
    private AuthenticationManager authenticationManager;
    private JwtTokenRepository jwtTokenRepository;

    @Override
    public UserRegisterResponse register(User user) {

        UserRegisterResponse response = new UserRegisterResponse();
        try {
            if (user.getRoles() == null ){
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
    public LoginResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        User person = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(()-> new NotFoundException("User is not found"));

        var jwtToken = jwtService.generateToken(person);
        revokeAllUserTokens(person);
        saveUserToken(person, jwtToken);


        return LoginResponse.builder()
                .responseCode("002")
                .responseMessage("Your have been logged in successfully")
                .token(jwtToken)
                .build();
    }

    @Override
    public ApiResponse getAllUsers() {
        ApiResponse response = new ApiResponse();
        try {
            List<User> userList = userRepository.findAll();
            List<UserRequest> userRequests = Utils.mapUserListEntityToUserListRequest(userList);
            response.setResponseCode("200");
            response.setResponseMessage("successful");
            response.setUserLists(userRequests);

        } catch (Exception e) {
            response.setResponseCode("500");
            response.setResponseMessage("Failed to retrieve users: " + e.getMessage());
        }
        return response;
    }

    private void saveUserToken(User user, String jwtToken){
        var token = JwtToken.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        jwtTokenRepository.save(token);
    }
    private void revokeAllUserTokens(User user){
        var validUserTokens = jwtTokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        jwtTokenRepository.saveAll(validUserTokens);
    }


    @Override
    public ApiResponse getUserBookingsHistory(String userId) {

        ApiResponse response = new ApiResponse();
        try {
            User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(()-> new  NotFoundException(
                    "User not found"
            ));
            UserRequest userRequest = Utils.mapUserEntityToUserRequestPlusUserBookingsAndRoom(user);
            response.setResponseCode("200");
            response.setResponseMessage("successful");
            response.setUser(userRequest);


        }  catch (Exception e) {
            response.setResponseCode("404");
            response.setResponseMessage("Error retrieving all users: " + e.getMessage());
        }
        return response;
    }

    @Override
    public ApiResponse deleteUser(String userId) {

        ApiResponse response = new ApiResponse();
        try {

            userRepository.findById(Long.valueOf(userId)).orElseThrow(()-> new  NotFoundException(
                    "User not found"
            ));
            userRepository.deleteById(Long.valueOf(userId));
            response.setResponseCode("200");
            response.setResponseMessage("User deleted successfully");


        }  catch (Exception e) {
            response.setResponseCode("404");
            response.setResponseMessage("Error deleting a users: " + e.getMessage());
        }

        return null;
    }

    @Override
    public ApiResponse getUserById(String userId) {

        ApiResponse response = new ApiResponse();
        try {
            User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(()-> new  NotFoundException(
                    "User not found"
            ));
            UserRequest userRequest = Utils.mapUserEntityToUserRequest(user);
            response.setResponseCode("200");
            response.setResponseMessage("successful");
            response.setUser(userRequest);


        }  catch (Exception e) {
            response.setResponseCode("404");
            response.setResponseMessage("Error getting a user: " + e.getMessage());
        }

        return response;
    }

    @Override
    public ApiResponse getUserInfo(String email) {

        ApiResponse response = new ApiResponse();
        try {
            User user = userRepository.findByEmail(email).orElseThrow(()-> new  NotFoundException(
                    "User not found"
            ));
            UserRequest userRequest = Utils.mapUserEntityToUserRequest(user);
            response.setResponseCode("200");
            response.setResponseMessage("successful");
            response.setUser(userRequest);


        }  catch (Exception e) {
            response.setResponseCode("404");
            response.setResponseMessage("Error retrieving user's information: " + e.getMessage());
        }
        return null;
    }
}
