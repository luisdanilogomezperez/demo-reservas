package com.reservas.demo_reservas.services;

import com.reservas.demo_reservas.dto.request.FinalizarReservaRequestDTO;
import com.reservas.demo_reservas.dto.request.ReservaRequestDTO;
import com.reservas.demo_reservas.dto.response.FinalizarReservaResponseDTO;
import com.reservas.demo_reservas.dto.response.ReservaCreadaResponseDTO;
import com.reservas.demo_reservas.dto.response.ReservaResponseDTO;

import java.util.List;

public interface ReservaService {

    ReservaCreadaResponseDTO crearReserva(ReservaRequestDTO request);

    ReservaResponseDTO editarReserva(Long id, ReservaRequestDTO request);

    List<ReservaResponseDTO> listarReservas();

    ReservaResponseDTO consultarReserva(Long id);

    ReservaResponseDTO aprobarReserva(Long id);

    ReservaResponseDTO rechazarReserva(Long id, String motivo);

    void eliminarReserva(Long id);

    FinalizarReservaResponseDTO finalizarReserva(FinalizarReservaRequestDTO request);

    List<ReservaResponseDTO> listarReservasActivasPorSalon(Long salonId);

    List<ReservaResponseDTO> listarReservasActivasPorDocumento(String documentoCliente);

    List<ReservaResponseDTO> listarReservasActivasPrimeraVezEnSalon();
}
