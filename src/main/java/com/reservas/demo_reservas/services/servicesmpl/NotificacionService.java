package com.reservas.demo_reservas.services.servicesmpl;

import com.reservas.demo_reservas.dto.request.NotificacionExternaRequestDTO;
import com.reservas.demo_reservas.dto.request.NotificacionRequestDTO;
import com.reservas.demo_reservas.dto.response.NotificacionResponseDTO;
import com.reservas.demo_reservas.entities.Reserva;
import com.reservas.demo_reservas.entities.Salon;
import com.reservas.demo_reservas.enums.EstadoReserva;
import com.reservas.demo_reservas.exceptions.BadRequestException;
import com.reservas.demo_reservas.repositories.ReservaRepository;
import com.reservas.demo_reservas.repositories.SalonRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@AllArgsConstructor
public class NotificacionService {

    private final ReservaRepository reservaRepository;

    private final SalonRepository salonRepository;

    private final RestTemplate restTemplate;

    private final String URL_NOTIFICACIONES =
            "http://localhost:8081/notificaciones/enviar";

    public NotificacionResponseDTO enviarNotificacion(
            NotificacionRequestDTO request
    ) {
        log.info("enviando notificacion al Gestor.");
        Salon salon = salonRepository.findById(
                request.getSalonId()
        ).orElseThrow(() ->
                new BadRequestException(
                        "Salón no encontrado."
                )
        );

        reservaRepository
                .findByDocumentoClienteAndSalonIdAndEstado(
                        request.getDocumento(),
                        request.getSalonId(),
                        EstadoReserva.ACTIVA
                ).orElseThrow(() ->
                        new BadRequestException(
                                "No existe una reserva activa para este documento en el salón "
                        )
                );

        NotificacionExternaRequestDTO externa =
                new NotificacionExternaRequestDTO();

        externa.setEmail(request.getEmail());
        externa.setDocumento(request.getDocumento());
        externa.setMensaje(request.getMensaje());
        externa.setSalonNombre(salon.getNombre());

        log.info("Enviando notificación al microservicio.");

        return restTemplate.postForObject(
                URL_NOTIFICACIONES,
                externa,
                NotificacionResponseDTO.class
        );
    }
}
