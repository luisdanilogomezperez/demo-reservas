package com.reservas.demo_reservas.controllers;

import com.reservas.demo_reservas.dto.request.NotificacionRequestDTO;
import com.reservas.demo_reservas.dto.response.NotificacionResponseDTO;
import com.reservas.demo_reservas.services.servicesmpl.NotificacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notificaciones")
@RequiredArgsConstructor
@Validated
public class NotificacionController {

    private final NotificacionService notificacionService;

    @PostMapping("/enviar")
    public ResponseEntity<NotificacionResponseDTO> enviarNotificacion(
            @RequestBody NotificacionRequestDTO request) {
        return ResponseEntity.ok(
                notificacionService.enviarNotificacion(request)
        );
    }
}
