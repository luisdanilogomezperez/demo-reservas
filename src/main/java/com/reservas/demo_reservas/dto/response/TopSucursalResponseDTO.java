package com.reservas.demo_reservas.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class TopSucursalResponseDTO {

    private String nombreSucursal;

    private BigDecimal totalFacturado;

}
