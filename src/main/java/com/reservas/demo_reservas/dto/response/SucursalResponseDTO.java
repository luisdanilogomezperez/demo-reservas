package com.reservas.demo_reservas.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class SucursalResponseDTO {

    private Long id;

    private String nombre;

    private String direccion;

    private Long gestorId;

    private String nombreGestor;
}
