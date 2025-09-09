package com.hamlet.HamletHotel.service.impl;

import com.hamlet.HamletHotel.config.JwtService;
import com.hamlet.HamletHotel.entity.JwtToken;
import com.hamlet.HamletHotel.entity.User;
import com.hamlet.HamletHotel.enums.Roles;
import com.hamlet.HamletHotel.enums.TokenType;
import com.hamlet.HamletHotel.exception.AlreadyExistsException;
import com.hamlet.HamletHotel.exception.NotFoundException;
import com.hamlet.HamletHotel.payload.request.LoginRequest;
import com.hamlet.HamletHotel.payload.request.RegistrationRequest;
import com.hamlet.HamletHotel.payload.response.ApiResponse;
import com.hamlet.HamletHotel.payload.response.LoginResponse;
import com.hamlet.HamletHotel.payload.response.UserRegisterResponse;
import com.hamlet.HamletHotel.repository.JwtTokenRepository;
import com.hamlet.HamletHotel.repository.UserRepository;
import com.hamlet.HamletHotel.service.AuthService;
import com.hamlet.HamletHotel.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final JwtTokenRepository jwtTokenRepository;


    @Override
    public UserRegisterResponse register(RegistrationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AlreadyExistsException(request.getEmail() + " already exists");
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        Roles assignedRole = (request.getRole() != null) ? request.getRole() : Roles.USER;

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(assignedRole)
                .enabled(true)
                .build();

        User savedUser = userRepository.save(user);

        return UserRegisterResponse.builder()
                .name(savedUser.getName())
                .phoneNumber(savedUser.getPhoneNumber())
                .email(savedUser.getEmail())
                .response(ApiResponse.builder()
                                .responseCode(200)
                                .responseMessage("User registered successfully")
                                .build()
                ).build();
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (!user.isEnabled()) {
            throw new RuntimeException("User account is not enabled.");
        }

        String jwtToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);

        return LoginResponse.builder()
                .apiResponse(ApiResponse.builder()
                        .responseCode(200)
                        .responseMessage("Login successful")
                        .build())
                .token(jwtToken)
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        JwtToken token = JwtToken.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        jwtTokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = jwtTokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty()) return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        jwtTokenRepository.saveAll(validUserTokens);
    }

    // Local mapping method to replace Utils
    private RegistrationRequest mapUserEntityToDto(User user) {
        return RegistrationRequest.builder()
                .name(user.getName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRoles())
                .build();
    }
}
