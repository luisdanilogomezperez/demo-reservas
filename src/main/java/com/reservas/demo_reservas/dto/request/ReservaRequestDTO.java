package com.reservas.demo_reservas.dto.request;

import com.reservas.demo_reservas.enums.EstadoReserva;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Setter
@Getter
public class ReservaRequestDTO {

    @NotBlank(message = "El documento es obligatorio.")
    @Pattern(
            regexp = "^\\d{6,12}$",
            message = "El documento debe contener entre 6 y 12 dígitos."
    )
    private String documentoCliente;

    @NotBlank(message = "El nombre del cliente es obligatorio.")
    private String nombreCliente;

    @NotNull(message = "La fecha de inicio es obligatoria.")
    private LocalDateTime fechaInicio;

    @NotNull(message = "La fecha de Fin es obligatoria.")
    private LocalDateTime fechaFinEstimada;

    @Positive(message = "La cantidad de asistentes debe ser mayor a cero.")
    private int asistentes;

    @NotNull(message = "Debe seleccionar un salon.")
    private Long salonId;

}
