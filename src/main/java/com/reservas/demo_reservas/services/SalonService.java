package com.reservas.demo_reservas.services;

import com.reservas.demo_reservas.dto.request.SalonRequestDTO;
import com.reservas.demo_reservas.dto.response.SalonResponseDTO;

import java.util.List;

public interface SalonService {

    SalonResponseDTO crearSalon(SalonRequestDTO request);

    void eliminarSalon(Long id);

    SalonResponseDTO editarSalon(Long id, SalonRequestDTO request);

    SalonResponseDTO consultarSalon(Long id);

    List<SalonResponseDTO> listarSalones();
}
