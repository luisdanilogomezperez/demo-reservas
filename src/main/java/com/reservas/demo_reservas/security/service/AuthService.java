package com.reservas.demo_reservas.security.service;

import com.reservas.demo_reservas.security.DTO.LoginRequestDTO;
import com.reservas.demo_reservas.dto.response.AuthResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final CustomUserDetailsService userDetailsService;

    public AuthResponseDTO login(LoginRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        UserDetails user = userDetailsService
                .loadUserByUsername(
                        request.getEmail()
                );

        String token = jwtService.generateToken(user);

        return new AuthResponseDTO(token);
    }
}
