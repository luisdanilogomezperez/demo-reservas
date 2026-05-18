package com.reservas.demo_reservas.security.controller;

import com.reservas.demo_reservas.dto.response.AuthResponseDTO;
import com.reservas.demo_reservas.security.DTO.LoginRequestDTO;
import com.reservas.demo_reservas.security.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(
            @RequestBody LoginRequestDTO request) {

        return ResponseEntity.ok(authService.login(request));
    }
}
