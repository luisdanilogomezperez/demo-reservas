package com.reservas.demo_reservas.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@Setter
@Getter
public class SalonResponseDTO {

    private Long id;

    private String nombre;

    private int capacidadMaxima;

    private BigDecimal costoHora;

    private Long gestorId;

    private String nombreGestor;

    private Long sucursalId;

    private String NombreSucursal;
}
