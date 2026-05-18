package com.reservas.demo_reservas.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class SucursalRequestDTO {

    @NotBlank(message = "El nombre es obligatorio.")
    private String nombre;

    @NotBlank(message = "La direccion es obligatoria.")
    private String direccion;

    @NotNull(message = "Debe seleccionar un gestor.")
    private Long gestorId;
}
