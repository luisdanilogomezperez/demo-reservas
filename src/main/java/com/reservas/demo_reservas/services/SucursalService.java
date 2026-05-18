package com.reservas.demo_reservas.services;

import com.reservas.demo_reservas.dto.request.SucursalRequestDTO;
import com.reservas.demo_reservas.dto.response.SucursalResponseDTO;

import java.util.List;

public interface SucursalService {

    SucursalResponseDTO crearSucursal(SucursalRequestDTO request);

    void eliminarSucursal(Long id);

    SucursalResponseDTO editarSucursal(Long id, SucursalRequestDTO request);

    SucursalResponseDTO consultarSucursal(Long id);

    List<SucursalResponseDTO> listarSucursales();
}
