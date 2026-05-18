package com.reservas.demo_reservas.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FinalizarReservaRequestDTO {

    @NotNull(message = "La fecha de Fin Real de la reserva es obligatoria.")
    private LocalDateTime fechaFinReal;

    @NotBlank(message = "El documento es obligatorio.")
    @Pattern(
            regexp = "^\\d{6,12}$",
            message = "El documento debe contener entre 6 y 12 dígitos."
    )
    private String documentoCliente;

    @NotNull(message = "Debe seleccionar un salon.")
    private Long salonId;
}
