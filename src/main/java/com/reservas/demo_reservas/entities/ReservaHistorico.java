package com.reservas.demo_reservas.entities;

import com.reservas.demo_reservas.enums.EstadoReserva;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "historico_reservas")
@Getter
@Setter
public class ReservaHistorico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long reservaId;

    private String documentoCliente;

    private String nombreCliente;

    private LocalDateTime fechaInicio;

    private LocalDateTime fechaFinEstimada;

    private LocalDateTime fechaFinReal;

    private LocalDateTime fechaCreacion;

    private int asistentes;

    private BigDecimal costoEstimado;

    private BigDecimal totalCobrado;

    private String motivoRechazo;

    @Enumerated(EnumType.STRING)
    private EstadoReserva estado;

    @ManyToOne
    @JoinColumn(name = "salon_id")
    private Salon salon;
}
