package com.reservas.demo_reservas.services;

import com.reservas.demo_reservas.dto.request.UsuarioRequestDTO;
import com.reservas.demo_reservas.dto.response.UsuarioResponseDTO;

import java.util.List;

public interface UsuarioSrvice {

    UsuarioResponseDTO crearUsuario(UsuarioRequestDTO request);

    void eliminarUsuario(Long id);

    UsuarioResponseDTO editarUsuario(Long id, UsuarioRequestDTO request);

    UsuarioResponseDTO consultarUsuario(Long id);

    List<UsuarioResponseDTO> listarUsuarios();
}
