package com.reservas.demo_reservas.services.servicesmpl;

import com.reservas.demo_reservas.dto.request.SucursalRequestDTO;
import com.reservas.demo_reservas.dto.response.SucursalResponseDTO;
import com.reservas.demo_reservas.entities.Sucursal;
import com.reservas.demo_reservas.entities.Usuario;
import com.reservas.demo_reservas.enums.RoleName;
import com.reservas.demo_reservas.exceptions.BadRequestException;
import com.reservas.demo_reservas.repositories.SucursalRepository;
import com.reservas.demo_reservas.repositories.UsuarioRepository;
import com.reservas.demo_reservas.services.SucursalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SucursalServiceImpl implements SucursalService {

    private final UsuarioRepository usuarioRepository;

    private final SucursalRepository sucursalRepository;

    @Override
    public SucursalResponseDTO crearSucursal(SucursalRequestDTO request) {

        boolean existeSucursal = sucursalRepository.existsByNombre(request.getNombre());

        if ( existeSucursal ) {
            throw new BadRequestException(
                    "La sucursal ya existe."
            );
        }

        Usuario gestor = usuarioRepository.findById(request.getGestorId())
                .orElseThrow(() ->
                        new BadRequestException(
                                "Gestor no encontrado."
                        )
                );

        if ( gestor.getRol().getNombre() != RoleName.GESTOR ) {
            throw new BadRequestException(
                    "El usuario asignado no es GESTOR."
            );
        }

        Sucursal sucursal = new Sucursal();

        sucursal.setNombre(request.getNombre());
        sucursal.setDireccion(request.getDireccion());
        sucursal.setGestor(gestor);

        sucursalRepository.save(sucursal);

        return new SucursalResponseDTO(
                sucursal.getId(),
                sucursal.getNombre(),
                sucursal.getDireccion(),
                gestor.getId(),
                gestor.getNombre()
        );
    }

    @Override
    public void eliminarSucursal(Long id) {
        Sucursal sucursal = sucursalRepository
                .findById(id)
                .orElseThrow(() ->
                        new BadRequestException(
                                "Sucursal no encontrada."
                        ));
        sucursalRepository.delete(sucursal);
    }

    @Override
    public SucursalResponseDTO editarSucursal(Long id, SucursalRequestDTO request) {

        Sucursal sucursal = sucursalRepository.findById(id)
                .orElseThrow(() ->
                        new BadRequestException(
                                "Sucursal no encontrada."
                        ));

        boolean existeSucursal = sucursalRepository.existsByNombre(request.getNombre());

        if ( existeSucursal && sucursal.getNombre().equals(request.getNombre()) ) {
            throw new BadRequestException(
                    "La sucursal ya existe."
            );
        }

        Usuario gestor = usuarioRepository.findById(request.getGestorId())
                .orElseThrow(() ->
                        new BadRequestException(
                                "Gestor no encontrado."
                        )
                );

        if ( gestor.getRol().getNombre() != RoleName.GESTOR ) {
            throw new BadRequestException(
                    "El usuario asignado no es GESTOR."
            );
        }

        sucursal.setNombre(request.getNombre());
        sucursal.setDireccion(request.getDireccion());
        sucursal.setGestor(gestor);

        sucursalRepository.save(sucursal);

        return new SucursalResponseDTO(
                sucursal.getId(),
                sucursal.getNombre(),
                sucursal.getDireccion(),
                gestor.getId(),
                gestor.getNombre()
        );
    }

    @Override
    public SucursalResponseDTO consultarSucursal(Long id) {
        Sucursal sucursal = sucursalRepository.findById(id)
                .orElseThrow(() ->
                        new BadRequestException(
                                "Sucursal no encontrada."
                        )
                );

        return new SucursalResponseDTO(
                sucursal.getId(),
                sucursal.getNombre(),
                sucursal.getDireccion(),
                sucursal.getGestor().getId(),
                sucursal.getGestor().getNombre()
        );
    }

    @Override
    public List<SucursalResponseDTO> listarSucursales() {
        return sucursalRepository.findAll()
                .stream()
                .map(sucursal ->
                        new SucursalResponseDTO(
                                sucursal.getId(),
                                sucursal.getNombre(),
                                sucursal.getDireccion(),
                                sucursal.getGestor().getId(),
                                sucursal.getGestor().getNombre()
                        )
                )
                .toList();
    }
}
