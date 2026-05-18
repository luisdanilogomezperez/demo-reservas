package com.reservas.demo_reservas.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@Setter
@Getter
public class SalonRequestDTO {

    private String nombre;

    private int capacidadMaxima;

    private BigDecimal costoHora;

    private Long gestorId;

    private Long sucursalId;

}
