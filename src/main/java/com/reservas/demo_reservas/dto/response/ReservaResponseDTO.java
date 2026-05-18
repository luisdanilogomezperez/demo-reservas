package com.reservas.demo_reservas.dto.response;

import com.reservas.demo_reservas.enums.EstadoReserva;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@Setter
@Getter
public class ReservaResponseDTO {

    private Long id;

    private String documentoCliente;

    private String nombreCliente;

    private LocalDateTime fechaInicio;

    private LocalDateTime fechaFinEstimada;

    private LocalDateTime fechaCreacion;

    private int asistentes;

    private EstadoReserva estado;

    private Long salonId;

    private String nombreSalon;

    BigDecimal CostoTotalEstimado;

}
