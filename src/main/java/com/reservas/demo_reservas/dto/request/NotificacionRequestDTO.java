package com.reservas.demo_reservas.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificacionRequestDTO {

    private String email;

    private String documento;

    private String mensaje;

    private Long salonId;
}
