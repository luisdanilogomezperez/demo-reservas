package com.reservas.demo_reservas.services;

import com.reservas.demo_reservas.dto.response.TopClienteResponseDTO;
import com.reservas.demo_reservas.dto.response.TopSucursalResponseDTO;
import com.reservas.demo_reservas.entities.Reserva;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface ReservaHistoricoService {

    void guardarReservaHistorico(Reserva reserva, LocalDateTime fechaFinReal, BigDecimal totalCobrado);

    List<TopClienteResponseDTO> topClientes();

    List<TopClienteResponseDTO> topClientesPorSalon(Long salonId);

    BigDecimal totalGananciasDia(Long salonId);

    BigDecimal totalGananciasSemana(Long salonId);

    BigDecimal totalGananciasMes(Long salonId);

    BigDecimal totalGananciasAnio(Long salonId);

    List<TopSucursalResponseDTO> topSucursales();
}
