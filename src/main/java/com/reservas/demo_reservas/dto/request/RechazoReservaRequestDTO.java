package com.reservas.demo_reservas.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RechazoReservaRequestDTO {

    @NotBlank(message = "El motivo del rechazo es obligatorio.")
    private String motivoRechazo;

}
