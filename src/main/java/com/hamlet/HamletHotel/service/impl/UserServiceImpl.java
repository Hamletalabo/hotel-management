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
import com.hamlet.HamletHotel.payload.response.Response;
import com.hamlet.HamletHotel.payload.response.LoginResponse;
import com.hamlet.HamletHotel.payload.response.UserRegisterResponse;
import com.hamlet.HamletHotel.repository.JwtTokenRepository;
import com.hamlet.HamletHotel.repository.UserRepository;
import com.hamlet.HamletHotel.service.UserService;
import com.hamlet.HamletHotel.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenRepository jwtTokenRepository;

//    @Override
//    public UserRegisterResponse register(User user) {
//        if (user.getRoles() == null) {
//            user.setRoles(Roles.USER);
//        }
//        if (userRepository.existsByEmail(user.getEmail())) { throw new AlreadyExistsException(user.getEmail()
//                + " already exists");
//        }
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        User savedUser = userRepository.save(user);
//        return UserRegisterResponse.builder()
//                .user(Utils.mapUserEntityToUserRequest(savedUser))
//                .response(Response.builder()
//                        .responseCode(200)
//                        .responseMessage("User registered successfully")
//                        .build())
//                .build(); }
//
//    @Override
//    public LoginResponse login(LoginRequest loginRequest) {
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        loginRequest.getEmail(),
//                        loginRequest.getPassword()
//                )
//        );
//
//        User user = userRepository.findByEmail(loginRequest.getEmail())
//                .orElseThrow(() -> new NotFoundException("User not found"));
//
//        String jwtToken = jwtService.generateToken(user);
//        revokeAllUserTokens(user);
//        saveUserToken(user, jwtToken);
//
//        return LoginResponse.builder()
//                .responseCode("200")
//                .responseMessage("You have been logged in successfully")
//                .token(jwtToken)
//                .build();
//    }

    @Override
    public Response getAllUsers() {
        List<User> userList = userRepository.findAll();
        List<UserRequest> userRequests = Utils.mapUserListEntityToUserListRequest(userList);

        return Response.builder()
                .responseCode(200)
                .responseMessage("Successfully retrieved users")
                .userLists(userRequests)
                .build();
    }

    @Override
    public Response getUserBookingsHistory(String userId) {
        User user = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new NotFoundException("User not found"));

        UserRequest userRequest = Utils.mapUserEntityToUserRequestPlusUserBookingsAndRoom(user);

        return Response.builder()
                .responseCode(200)
                .responseMessage("Successfully retrieved user booking history")
                .user(userRequest)
                .build();
    }

    @Override
    public Response deleteUser(String userId) {
        User user = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new NotFoundException("User not found"));

        userRepository.delete(user);

        return Response.builder()
                .responseCode(200)
                .responseMessage("User deleted successfully")
                .build();
    }

    @Override
    public Response getUserById(String userId) {
        User user = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new NotFoundException("User not found"));

        return Response.builder()
                .responseCode(200)
                .responseMessage("Successfully retrieved user")
                .user(Utils.mapUserEntityToUserRequest(user))
                .build();
    }

    @Override
    public Response getUserInfo(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        return Response.builder()
                .responseCode(200)
                .responseMessage("Successfully retrieved user information")
                .user(Utils.mapUserEntityToUserRequest(user))
                .build();
    }

//    private void saveUserToken(User user, String jwtToken) {
//        JwtToken token = JwtToken.builder()
//                .user(user)
//                .token(jwtToken)
//                .tokenType(TokenType.BEARER)
//                .expired(false)
//                .revoked(false)
//                .build();
//        jwtTokenRepository.save(token);
//    }
//
//    private void revokeAllUserTokens(User user) {
//        var validUserTokens = jwtTokenRepository.findAllValidTokenByUser(user.getId());
//        if (validUserTokens.isEmpty()) return;
//
//        validUserTokens.forEach(token -> {
//            token.setExpired(true);
//            token.setRevoked(true);
//        });
//
//        jwtTokenRepository.saveAll(validUserTokens);
//    }
}
