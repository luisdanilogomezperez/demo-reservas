package com.reservas.demo_reservas.controllers;

import com.reservas.demo_reservas.dto.request.FinalizarReservaRequestDTO;
import com.reservas.demo_reservas.dto.request.RechazoReservaRequestDTO;
import com.reservas.demo_reservas.dto.request.ReservaRequestDTO;
import com.reservas.demo_reservas.dto.response.FinalizarReservaResponseDTO;
import com.reservas.demo_reservas.dto.response.ReservaCreadaResponseDTO;
import com.reservas.demo_reservas.dto.response.ReservaHistoricoResponseDTO;
import com.reservas.demo_reservas.dto.response.ReservaResponseDTO;
import com.reservas.demo_reservas.services.ReservaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservas")
@RequiredArgsConstructor
public class ReservaController {

    private final ReservaService reservaService;

    @PostMapping("/crear")
    public ResponseEntity<ReservaCreadaResponseDTO> crearReserva(@Valid @RequestBody ReservaRequestDTO request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(reservaService.crearReserva(request));
    }

    @GetMapping("/listar")
    public ResponseEntity<List<ReservaResponseDTO>> listarReserva() {
        return ResponseEntity.ok(reservaService.listarReservas());
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<ReservaResponseDTO> editarReserva(@PathVariable Long id, @Valid @RequestBody ReservaRequestDTO request) {
        return ResponseEntity.ok(reservaService.editarReserva(id, request));
    }

    @GetMapping("/consultar/{id}")
    public ResponseEntity<ReservaResponseDTO> consultarReserva(@PathVariable Long id) {
        return ResponseEntity.ok(reservaService.consultarReserva(id));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarReserva(@PathVariable Long id) {
        reservaService.eliminarReserva(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/aprobar")
    public ResponseEntity<ReservaResponseDTO> aprobarReserva(@PathVariable Long id) {
        return ResponseEntity.ok(reservaService.aprobarReserva(id));
    }

    @PostMapping("/{id}/rechazar")
    public ResponseEntity<ReservaResponseDTO> rechazarReserva(@PathVariable Long id, @Valid @RequestBody RechazoReservaRequestDTO request) {
        return ResponseEntity.ok(reservaService.rechazarReserva(id, request.getMotivoRechazo()));
    }

    @PostMapping("/finalizar")
    public ResponseEntity<FinalizarReservaResponseDTO> finalizarReserva(@Valid @RequestBody FinalizarReservaRequestDTO request) {
        return ResponseEntity.ok(reservaService.finalizarReserva(request));
    }

    @GetMapping("/listar/{salonId}/activas")
    public ResponseEntity<List<ReservaResponseDTO>> listarReservasActivasPorSalon(@PathVariable Long salonId) {
        return ResponseEntity.ok(reservaService.listarReservasActivasPorSalon(salonId));
    }
    @GetMapping("/buscar-documento")
    public ResponseEntity<List<ReservaResponseDTO>> listarReservasPorDocumento(@RequestParam String documentoCliente) {
        return ResponseEntity.ok(reservaService.listarReservasActivasPorDocumento(documentoCliente));
    }
}
