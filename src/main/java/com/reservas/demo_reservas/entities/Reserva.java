package com.reservas.demo_reservas.entities;

import com.reservas.demo_reservas.enums.EstadoReserva;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservas")
@Getter
@Setter
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String documentoCliente;

    private String nombreCliente;

    private LocalDateTime fechaInicio;

    private LocalDateTime fechaFinEstimada;

    private LocalDateTime fechaCreacion;

    private int asistentes;

    @Enumerated(EnumType.STRING)
    private EstadoReserva estado;

    private String motivoRechazo;

    @ManyToOne
    @JoinColumn(name = "salon_id")
    private Salon salon;

    private BigDecimal CostoTotalEstimado;
}
