package com.reservas.demo_reservas.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TopClienteResponseDTO {

    private String documentoCliente;

    private String nombreCliente;

    private Long totalReservas;

}
