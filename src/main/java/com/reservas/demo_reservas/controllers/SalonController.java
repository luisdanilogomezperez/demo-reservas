package com.reservas.demo_reservas.controllers;

import com.reservas.demo_reservas.dto.request.SalonRequestDTO;
import com.reservas.demo_reservas.dto.request.SucursalRequestDTO;
import com.reservas.demo_reservas.dto.response.ReservaResponseDTO;
import com.reservas.demo_reservas.dto.response.SalonResponseDTO;
import com.reservas.demo_reservas.dto.response.SucursalResponseDTO;
import com.reservas.demo_reservas.services.SalonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/salones")
@RequiredArgsConstructor
public class SalonController {

    private final SalonService salonService;

    @PostMapping("/crear")
    public ResponseEntity<SalonResponseDTO> crearSalon(@Valid @RequestBody SalonRequestDTO request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(salonService.crearSalon(request)
        );
    }

    @GetMapping("/consultar/{id}")
    public ResponseEntity<SalonResponseDTO> consultarSalon(@PathVariable Long id) {
        return ResponseEntity.ok(salonService.consultarSalon(id));
    }

    @GetMapping("/listar")
    public ResponseEntity<List<SalonResponseDTO>> listarSalones() {
        return ResponseEntity.ok(salonService.listarSalones());
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<SalonResponseDTO> editarSalon(@PathVariable Long id, @Valid @RequestBody SalonRequestDTO request) {
        return ResponseEntity.ok(salonService.editarSalon(id, request));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarSalon(@PathVariable Long id) {
        salonService.eliminarSalon(id);
        return ResponseEntity.noContent().build();
    }
}
