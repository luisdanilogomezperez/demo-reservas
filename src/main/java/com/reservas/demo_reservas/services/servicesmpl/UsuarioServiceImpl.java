package com.reservas.demo_reservas.services.servicesmpl;

import com.reservas.demo_reservas.dto.request.UsuarioRequestDTO;
import com.reservas.demo_reservas.dto.response.UsuarioResponseDTO;
import com.reservas.demo_reservas.entities.Rol;
import com.reservas.demo_reservas.entities.Usuario;
import com.reservas.demo_reservas.repositories.RolRepository;
import com.reservas.demo_reservas.repositories.UsuarioRepository;
import com.reservas.demo_reservas.services.UsuarioSrvice;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.reservas.demo_reservas.exceptions.BadRequestException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioSrvice {

    private final UsuarioRepository usuarioRepository;

    private final RolRepository rolRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UsuarioResponseDTO crearUsuario(UsuarioRequestDTO request) {

        boolean existe = usuarioRepository.existsByEmail(request.getEmail());

        if (existe) {
            throw new BadRequestException(
                    "El mail ya existe."
            );
        }

        if (request.getRol() == null) {
            throw new BadRequestException(
                    "El campo Rol es obligatorio."
            );
        }

        Rol rol = rolRepository
                .findByNombre(request.getRol())
                .orElseThrow();

        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setEmail(request.getEmail());
        usuario.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );
        usuario.setRol(rol);
        usuario.setActivo(true);
        usuario = usuarioRepository.save(usuario);

        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.isActivo(),
                usuario.getRol().getNombre()
        );
    }

    @Override
    public UsuarioResponseDTO editarUsuario(Long id, UsuarioRequestDTO request) {

        Usuario usuario = usuarioRepository
                .findById(id)
                .orElseThrow(() ->
                        new BadRequestException(
                                "Usuario no encontrado."
                        )
                );

        boolean existeMail = usuarioRepository.existsByEmail(request.getEmail());

        if ( existeMail && !usuario.getEmail().equals(request.getEmail()) ) {
            throw new BadRequestException(
                    "El mail ya existe."
            );
        }

        Rol rol = rolRepository
                .findByNombre(request.getRol())
                .orElseThrow();

        usuario.setNombre(request.getNombre());
        usuario.setEmail(request.getEmail());
        usuario.setActivo(request.isActivo());
        usuario.setRol(rol);

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            usuario.setPassword(
                    passwordEncoder.encode(
                            request.getPassword()
                    )
            );
        }
        usuarioRepository.save(usuario);

        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.isActivo(),
                usuario.getRol().getNombre()
        );
    }

    @Override
    public UsuarioResponseDTO consultarUsuario(Long id) {

        Usuario usuario = usuarioRepository
                .findById(id)
                .orElseThrow(() ->
                        new BadRequestException(
                                "Usuario no encontrado."
                        ));

        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.isActivo(),
                usuario.getRol().getNombre()
        );
    }

    @Override
    public void eliminarUsuario(Long id) {
        Usuario usuario = usuarioRepository
                .findById(id)
                .orElseThrow(() ->
                        new BadRequestException(
                                "Usuario no encontrado."
                        ));
        usuarioRepository.delete(usuario);
    }

    @Override
    public List<UsuarioResponseDTO> listarUsuarios() {
        List<Usuario> listadoUsuarios = usuarioRepository.findAll();

        return listadoUsuarios.stream()
                .map(usuario ->
                    new UsuarioResponseDTO(
                            usuario.getId(),
                            usuario.getNombre(),
                            usuario.getEmail(),
                            usuario.isActivo(),
                            usuario.getRol().getNombre()
                    )
                )
                .toList();
    }
}
