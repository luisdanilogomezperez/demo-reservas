package com.reservas.demo_reservas.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class FinalizarReservaResponseDTO {

    private String mensaje;

    private BigDecimal totalCobrado;
}
