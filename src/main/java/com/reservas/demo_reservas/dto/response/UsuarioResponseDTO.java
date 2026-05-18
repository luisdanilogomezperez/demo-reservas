package com.reservas.demo_reservas.dto.response;

import com.reservas.demo_reservas.enums.RoleName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UsuarioResponseDTO {

    private Long id;

    private String nombre;

    private String email;

    private boolean activo;

    private RoleName rol;
}
