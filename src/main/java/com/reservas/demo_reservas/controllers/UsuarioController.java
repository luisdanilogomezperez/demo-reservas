package com.reservas.demo_reservas.controllers;

import com.reservas.demo_reservas.dto.request.UsuarioRequestDTO;
import com.reservas.demo_reservas.dto.response.SucursalResponseDTO;
import com.reservas.demo_reservas.dto.response.UsuarioResponseDTO;
import com.reservas.demo_reservas.services.UsuarioSrvice;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioSrvice usuarioSrvice;

    @PostMapping("/crear")
    public ResponseEntity<UsuarioResponseDTO> crearUsuario(@Valid @RequestBody UsuarioRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(usuarioSrvice.crearUsuario(request));
    }

    @GetMapping("/listar")
    public ResponseEntity<List<UsuarioResponseDTO>> listarUsuarios() {
        return ResponseEntity.ok(usuarioSrvice.listarUsuarios());
    }

    @GetMapping("/consultar/{id}")
    public ResponseEntity<UsuarioResponseDTO> consultarUsuario(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioSrvice.consultarUsuario(id));
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<UsuarioResponseDTO> editarUsuario(@PathVariable Long id, @Valid  @RequestBody UsuarioRequestDTO request) {
        return ResponseEntity.ok(usuarioSrvice.editarUsuario(id, request));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        usuarioSrvice.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}
