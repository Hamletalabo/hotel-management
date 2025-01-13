package com.hamlet.HamletHotel.config;

import com.hamlet.HamletHotel.exception.ExpiredJwtTokenException;
import com.hamlet.HamletHotel.repository.JwtTokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final JwtTokenRepository jwtTokenRepository;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        final String username;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            jwt = authHeader.substring(7);
            logger.info("Processing JWT: {}");
            // extract the email from the jwt service

            userEmail = jwtService.extractUsername(jwt);
            logger.info("Extracted username from JWT: {}");
            if (userEmail != null &&
                    SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails =
                        this.userDetailsService.loadUserByUsername(userEmail);
                var isTokenValid = jwtTokenRepository.findByToken(jwt)
                        .map(t -> !t.isExpired() && !t.isRevoked())
                        .orElse(false);

                if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken
                                    (userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }else {
                    logger.warn("JWT token is invalid or revoked for user: {}");
                }

            }
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtTokenException e) {
            logger.error("JWT token expired", e);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT token expired");
        } catch (IOException | ServletException e) {
            logger.error("JWT token is not valid", e);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT token is not valid");
        }
    }
}
