package com.reservas.demo_reservas.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificacionExternaRequestDTO {

    private String email;

    private String documento;

    private String mensaje;

    private String salonNombre;
}
