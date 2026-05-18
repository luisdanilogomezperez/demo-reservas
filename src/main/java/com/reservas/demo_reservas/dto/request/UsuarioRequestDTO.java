package com.reservas.demo_reservas.dto.request;

import com.reservas.demo_reservas.enums.RoleName;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class UsuarioRequestDTO {

    @NotBlank(message = "El nombre es obligatorio.")
    private String nombre;

    @NotBlank(message = "El correo es obligatorio.")
    @Email(message = "Debes ingresar un correo válido.")
    @Size(
            min = 6,
            message = "La contraseña debe tener mínimo 6 caracteres.")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria.")
    private String password;

    private boolean activo;

    @NotNull(message = "Debe seleccionar un Rol.")
    private RoleName rol;
}
