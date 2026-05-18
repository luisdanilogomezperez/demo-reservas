package com.reservas.demo_reservas.services.servicesmpl;

import com.reservas.demo_reservas.dto.request.SalonRequestDTO;
import com.reservas.demo_reservas.dto.response.SalonResponseDTO;
import com.reservas.demo_reservas.entities.Salon;
import com.reservas.demo_reservas.entities.Sucursal;
import com.reservas.demo_reservas.entities.Usuario;
import com.reservas.demo_reservas.enums.RoleName;
import com.reservas.demo_reservas.exceptions.BadRequestException;
import com.reservas.demo_reservas.repositories.SalonRepository;
import com.reservas.demo_reservas.repositories.SucursalRepository;
import com.reservas.demo_reservas.repositories.UsuarioRepository;
import com.reservas.demo_reservas.services.SalonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SalonServiceImpl implements SalonService {

    private final UsuarioRepository usuarioRepository;

    private final SucursalRepository sucursalRepository;

    private final SalonRepository salonRepository;

    @Override
    public SalonResponseDTO crearSalon(SalonRequestDTO request) {

        boolean existeSalon = salonRepository.existsByNombre(request.getNombre());

        if ( existeSalon ) {
            throw new BadRequestException(
                    "El salón ya existe."
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

        Sucursal sucursal = sucursalRepository.findById(request.getSucursalId())
                .orElseThrow(() ->
                        new BadRequestException(
                                "Sucursal no encontrada."
                        ));

        Salon salon = new Salon();

        salon.setNombre(request.getNombre());
        salon.setCapacidadMaxima(request.getCapacidadMaxima());
        salon.setCostoHora(request.getCostoHora());
        salon.setSucursal(sucursal);
        salon.setGestor(gestor);

        salonRepository.save(salon);

        return new SalonResponseDTO(
                salon.getId(),
                salon.getNombre(),
                salon.getCapacidadMaxima(),
                salon.getCostoHora(),
                gestor.getId(),
                gestor.getNombre(),
                sucursal.getId(),
                sucursal.getNombre()
        );
    }

    @Override
    public void eliminarSalon(Long id) {
        Salon salon = salonRepository
                .findById(id)
                .orElseThrow(() ->
                        new BadRequestException(
                                "Salón no encontrado."
                        ));
        salonRepository.delete(salon);
    }

    @Override
    public SalonResponseDTO editarSalon(Long id, SalonRequestDTO request) {

        Salon salon = salonRepository
                .findById(id)
                .orElseThrow(() ->
                        new BadRequestException(
                                "Salón no encontrado."
                        ));

        boolean existeSalon = salonRepository.existsByNombre(request.getNombre());

        if ( existeSalon ) {
            throw new BadRequestException(
                    "El salón ya existe."
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

        Sucursal sucursal = sucursalRepository.findById(request.getSucursalId())
                .orElseThrow(() ->
                        new BadRequestException(
                                "Sucursal no encontrada."
                        ));

        salon.setNombre(request.getNombre());
        salon.setCapacidadMaxima(request.getCapacidadMaxima());
        salon.setCostoHora(request.getCostoHora());
        salon.setSucursal(sucursal);
        salon.setGestor(gestor);

        salonRepository.save(salon);

        return new SalonResponseDTO(
                salon.getId(),
                salon.getNombre(),
                salon.getCapacidadMaxima(),
                salon.getCostoHora(),
                gestor.getId(),
                gestor.getNombre(),
                sucursal.getId(),
                sucursal.getNombre()
        );
    }

    @Override
    public SalonResponseDTO consultarSalon(Long id) {

        Salon salon = salonRepository
                .findById(id)
                .orElseThrow(() ->
                        new BadRequestException(
                                "Salón no encontrado."
                        ));

        Sucursal sucursal = sucursalRepository.findById(salon.getSucursal().getId())
                .orElseThrow(() ->
                        new BadRequestException(
                                "Sucursal no encontrada."
                        ));

        Usuario gestor = usuarioRepository.findById(salon.getGestor().getId())
                .orElseThrow(() ->
                        new BadRequestException(
                                "Gestor no encontrado."
                        ));

        return new SalonResponseDTO(
                salon.getId(),
                salon.getNombre(),
                salon.getCapacidadMaxima(),
                salon.getCostoHora(),
                gestor.getId(),
                gestor.getNombre(),
                sucursal.getId(),
                sucursal.getNombre()
        );
    }

    @Override
    public List<SalonResponseDTO> listarSalones() {

        return salonRepository.findAll()
                .stream()
                .map(salon ->
                        new SalonResponseDTO(
                                salon.getId(),
                                salon.getNombre(),
                                salon.getCapacidadMaxima(),
                                salon.getCostoHora(),
                                salon.getGestor().getId(),
                                salon.getGestor().getNombre(),
                                salon.getSucursal().getId(),
                                salon.getSucursal().getNombre()
                        )
                )
                .toList();
    }
}
