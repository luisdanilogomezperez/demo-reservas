package com.reservas.demo_reservas.controllers;

import com.reservas.demo_reservas.dto.request.SucursalRequestDTO;
import com.reservas.demo_reservas.dto.response.SucursalResponseDTO;
import com.reservas.demo_reservas.services.SucursalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sucursales")
@RequiredArgsConstructor
public class SucursalController {

    private final SucursalService sucursalService;

    @PostMapping("/crear")
    public ResponseEntity<SucursalResponseDTO> crearSucursal(@Valid @RequestBody SucursalRequestDTO request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(sucursalService.crearSucursal(request));
    }

    @GetMapping("/consultar/{id}")
    public ResponseEntity<SucursalResponseDTO> consultarSucursal(@PathVariable Long id) {
        return ResponseEntity.ok(sucursalService.consultarSucursal(id));
    }

    @GetMapping("/listar")
    public ResponseEntity<List<SucursalResponseDTO>> listarSucursales() {
        return ResponseEntity.ok(sucursalService.listarSucursales());
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<SucursalResponseDTO> editarSucursal(@PathVariable Long id, @Valid @RequestBody SucursalRequestDTO request) {
        return ResponseEntity.ok(sucursalService.editarSucursal(id, request));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarSucursal(@PathVariable Long id) {
        sucursalService.eliminarSucursal(id);
        return ResponseEntity.noContent().build();
    }
}
