package com.reservas.demo_reservas.controllers;

import com.reservas.demo_reservas.dto.response.GananciasResponseDTO;
import com.reservas.demo_reservas.dto.response.ReservaResponseDTO;
import com.reservas.demo_reservas.dto.response.TopClienteResponseDTO;
import com.reservas.demo_reservas.dto.response.TopSucursalResponseDTO;
import com.reservas.demo_reservas.exceptions.BadRequestException;
import com.reservas.demo_reservas.services.ReservaHistoricoService;
import com.reservas.demo_reservas.services.ReservaService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/indicadores")
@RequiredArgsConstructor
public class IndicadoresController {

    private final ReservaHistoricoService reservaHistoricoService;

    private final ReservaService reservaService;

    @GetMapping("/top-clientes")
    public ResponseEntity<List<TopClienteResponseDTO>> topClientes() {
        return ResponseEntity.ok(reservaHistoricoService.topClientes());
    }

    @GetMapping("/top-clientes/salon/{salonId}")
    public ResponseEntity<List<TopClienteResponseDTO>> topClientesPorSalon(@PathVariable Long salonId) {
        return ResponseEntity.ok(reservaHistoricoService.topClientesPorSalon(salonId));
    }
    @GetMapping("/ganancias/{salonId}")
    public ResponseEntity<GananciasResponseDTO> totalGanancias(
            @PathVariable @Positive Long salonId,
            @RequestParam @NotBlank String periodo) {

        BigDecimal total = switch (periodo.toLowerCase()) {
            case "dia"    -> reservaHistoricoService.totalGananciasDia(salonId);
            case "semana" -> reservaHistoricoService.totalGananciasSemana(salonId);
            case "mes"    -> reservaHistoricoService.totalGananciasMes(salonId);
            case "anio"   -> reservaHistoricoService.totalGananciasAnio(salonId);
            default -> throw new BadRequestException(
                    "Período inválido. Valores aceptados: dia, semana, mes, anio.");
        };

        return ResponseEntity.ok(new GananciasResponseDTO(salonId, periodo, total));
    }
    @GetMapping("/reservas-activas/{salonId}")
    public ResponseEntity<List<ReservaResponseDTO>> listarReservasActivasPorSalon(@PathVariable Long salonId) {
        return ResponseEntity.ok(reservaService.listarReservasActivasPorSalon(salonId));
    }

    @GetMapping("/clientes-primera-vez")
    public ResponseEntity<List<ReservaResponseDTO>> listarReservasActivasPrimeraVezEnSalon() {
        return ResponseEntity.ok(reservaService.listarReservasActivasPrimeraVezEnSalon());
    }

    @GetMapping("/top-sucursales")
    public ResponseEntity<List<TopSucursalResponseDTO>> listarTopSucursales() {
        return ResponseEntity.ok(reservaHistoricoService.topSucursales());
    }
}
