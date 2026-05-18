package com.reservas.demo_reservas.services.servicesmpl;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.reservas.demo_reservas.dto.request.FinalizarReservaRequestDTO;
import com.reservas.demo_reservas.dto.request.NotificacionRequestDTO;
import com.reservas.demo_reservas.dto.request.ReservaRequestDTO;
import com.reservas.demo_reservas.dto.response.FinalizarReservaResponseDTO;
import com.reservas.demo_reservas.dto.response.NotificacionResponseDTO;
import com.reservas.demo_reservas.dto.response.ReservaCreadaResponseDTO;
import com.reservas.demo_reservas.dto.response.ReservaResponseDTO;
import com.reservas.demo_reservas.entities.Reserva;
import com.reservas.demo_reservas.entities.Salon;
import com.reservas.demo_reservas.enums.EstadoReserva;
import com.reservas.demo_reservas.exceptions.BadRequestException;
import com.reservas.demo_reservas.repositories.ReservaRepository;
import com.reservas.demo_reservas.repositories.SalonRepository;
import com.reservas.demo_reservas.services.ReservaHistoricoService;
import com.reservas.demo_reservas.services.ReservaService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReservaServiceImpl implements ReservaService {

    private final ReservaRepository reservaRepository;

    private final SalonRepository salonRepository;

    private final ReservaHistoricoService reservaHistoricoService;

    private static final BigDecimal UMBRAL_RESERVA_PREMIUM = BigDecimal.valueOf(500_000);

    private final NotificacionService notificacionService;

    @Override
    @Transactional
    public ReservaCreadaResponseDTO crearReserva(ReservaRequestDTO request) {
        log.info("Iniciando creación de reserva para documento: {}", request.getDocumentoCliente());

        Salon salon = salonRepository.findById(request.getSalonId())
                .orElseThrow(() -> new BadRequestException("Salón no encontrado."));

        validarFechas(request.getFechaInicio(), request.getFechaFinEstimada());
        validarCapacidad(salon, request.getAsistentes(), "crear");

        if (reservaRepository.existeReservaSolapada(salon.getId(),
                        request.getFechaInicio(), request.getFechaFinEstimada())) {
            log.warn("Reserva solapada. SalonId: {}", salon.getId());
            throw new BadRequestException("El salón ya se encuentra reservado en ese horario.");
        }

        if (reservaRepository.existsByDocumentoClienteAndEstado(
                request.getDocumentoCliente(), EstadoReserva.ACTIVA)) {
            log.warn("Cliente con reserva activa. Documento: {}", request.getDocumentoCliente());
            throw new BadRequestException(
                    "Ya existe una reserva activa para este documento en este u otro salón.");
        }

        Duration duracion = Duration.between(
                request.getFechaInicio(),
                request.getFechaFinEstimada()
        );

        BigDecimal costoTotal = calcularCostoReserva(request.getFechaInicio(),
                request.getFechaFinEstimada(), salon.getCostoHora());

        EstadoReserva estadoInicial = determinarEstadoInicialReserva(costoTotal);

        if (estadoInicial == EstadoReserva.PENDIENTE_APROBACION) {
            log.info("Reserva premium. Documento: {}, Costo: {}", request.getDocumentoCliente(), costoTotal);
        }

        Reserva reserva = new Reserva();

        reserva.setDocumentoCliente(request.getDocumentoCliente());
        reserva.setNombreCliente(request.getNombreCliente());
        reserva.setFechaInicio(request.getFechaInicio());
        reserva.setFechaFinEstimada(request.getFechaFinEstimada());
        reserva.setFechaCreacion(LocalDateTime.now());
        reserva.setAsistentes(request.getAsistentes());
        reserva.setEstado(estadoInicial);
        reserva.setSalon(salon);
        reserva.setCostoTotalEstimado(costoTotal);

        reserva = reservaRepository.save(reserva);

        log.info(
                "Reserva creada exitosamente con id: {}",
                reserva.getId()
        );

        return new ReservaCreadaResponseDTO(
                reserva.getId()
        );
    }

    @Override
    @Transactional
    public ReservaResponseDTO editarReserva(Long id, ReservaRequestDTO request) {
        log.info("Iniciando edición de reserva con id: {}", id);

        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Reserva no encontrada."));

        if (reserva.getEstado() == EstadoReserva.RECHAZADA ||
                reserva.getEstado() == EstadoReserva.EXPIRADA ||
                reserva.getEstado() == EstadoReserva.FINALIZADA) {

            log.warn("Intento de edición de reserva no editable. Id: {}, Estado: {}",
                    reserva.getId(), reserva.getEstado());

            throw new BadRequestException("El estado de la reserva no puede ser modificado.");
        }

        Salon salon = salonRepository.findById(request.getSalonId())
                .orElseThrow(() -> new BadRequestException("Salón no encontrado."));

        validarFechas(request.getFechaInicio(), request.getFechaFinEstimada());
        validarCapacidad(salon, request.getAsistentes(), "editar");

        if (reservaRepository.existeReservaSolapadaEditar(
                salon.getId(), id, request.getFechaInicio(), request.getFechaFinEstimada()))
            throw new BadRequestException("El salón ya se encuentra reservado en ese horario.");

        if (reservaRepository.existsByDocumentoClienteAndEstadoAndIdNot(
                request.getDocumentoCliente(), EstadoReserva.ACTIVA, id))
            throw new BadRequestException(
                    "Ya existe una reserva activa para este documento en este u otro salón.");

        BigDecimal costoTotal = calcularCostoReserva(request.getFechaInicio(),
                request.getFechaFinEstimada(), salon.getCostoHora());

        EstadoReserva estadoInicial = determinarEstadoInicialReserva(costoTotal);

        reserva.setDocumentoCliente(request.getDocumentoCliente());
        reserva.setNombreCliente(request.getNombreCliente());
        reserva.setFechaInicio(request.getFechaInicio());
        reserva.setFechaFinEstimada(request.getFechaFinEstimada());
        reserva.setAsistentes(request.getAsistentes());
        reserva.setEstado(estadoInicial);
        reserva.setSalon(salon);
        reserva.setCostoTotalEstimado(costoTotal);
        reserva = reservaRepository.save(reserva);

        log.info("Reserva editada correctamente. Id: {}", reserva.getId());
        return mapToResponse(reserva);
    }

    @Override
    @Transactional
    public ReservaResponseDTO aprobarReserva(Long id) {

        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Reserva no encontrada."));

        marcarExpiradaSiCorresponde(reserva);

        if (reserva.getEstado() != EstadoReserva.PENDIENTE_APROBACION) {
            throw new BadRequestException(
                    "Solo se pueden aprobar reservas pendientes. Estado actual: "
                            + reserva.getEstado()
            );
        }

        if (reservaRepository.existeReservaSolapada(reserva.getSalon().getId(),
                reserva.getFechaInicio(), reserva.getFechaFinEstimada()))
            throw new BadRequestException("El salón ya se encuentra reservado en ese horario.");

        if (reservaRepository.existsByDocumentoClienteAndEstado(
                reserva.getDocumentoCliente(), EstadoReserva.ACTIVA)) {
            log.warn("Cliente con reserva activa al aprobar. Documento: {}", reserva.getDocumentoCliente());
            throw new BadRequestException(
                    "Ya existe una reserva activa para este documento en este u otro salón.");
        }

        reserva.setEstado(EstadoReserva.ACTIVA);
        reservaRepository.save(reserva);

        NotificacionRequestDTO notificacion = new NotificacionRequestDTO();
        notificacion.setEmail(
                reserva.getSalon()
                        .getSucursal()
                        .getGestor()
                        .getEmail()
        );
        notificacion.setDocumento(reserva.getDocumentoCliente());
        notificacion.setMensaje("La reserva premium para el documento " +
                reserva.getDocumentoCliente() + " fue aprobada.");
        notificacion.setSalonId(reserva.getSalon().getId());

        reservaRepository.findByDocumentoClienteAndSalonIdAndEstado(
                reserva.getDocumentoCliente(),
                reserva.getSalon().getId(),
                EstadoReserva.ACTIVA
        ).orElseThrow(() ->
                new BadRequestException(
                        "No existe una reserva activa para este documento en el salón"
                )
        );
        notificacionService.enviarNotificacion(notificacion);

        log.info("Reserva aprobada correctamente. Id: {}", reserva.getId());

        return mapToResponse(reserva);
    }

    @Override
    @Transactional
    public ReservaResponseDTO rechazarReserva(Long id, String motivo) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Reserva no encontrada."));

        marcarExpiradaSiCorresponde(reserva);

        if (reserva.getEstado() != EstadoReserva.PENDIENTE_APROBACION) {
            throw new BadRequestException(
                    "Solo se pueden aprobar rechazar pendientes. Estado actual: "
                            + reserva.getEstado()
            );

        }
        reserva.setEstado(EstadoReserva.RECHAZADA);
        reserva.setMotivoRechazo(motivo);

        reservaRepository.save(reserva);

        log.info("Reserva rechazada correctamente. Id: {}, Motivo: {}",
                reserva.getId(), motivo);

        return mapToResponse(reserva);
    }

    @Override
    @Transactional
    public FinalizarReservaResponseDTO finalizarReserva(FinalizarReservaRequestDTO request) {

        Reserva reserva = reservaRepository.findByDocumentoClienteAndSalonIdAndEstado(
                        request.getDocumentoCliente(),
                        request.getSalonId(),
                        EstadoReserva.ACTIVA
                )
                .orElseThrow(() -> new BadRequestException(
                        "No se puede Finalizar Reserva, no existe una reserva activa para este documento en el salón"));

        if (request.getFechaFinReal().isBefore(reserva.getFechaInicio())) {
            throw new BadRequestException(
                    "La fecha final real no puede ser menor a la fecha de inicio."
            );
        }

        BigDecimal totalCobrado = calcularCostoReserva(reserva.getFechaInicio(),
                request.getFechaFinReal(), reserva.getSalon().getCostoHora());

        reservaHistoricoService.guardarReservaHistorico(
                reserva, request.getFechaFinReal(), totalCobrado);

        reservaRepository.delete(reserva);

        log.info("Reserva finalizada. Documento: {}, Salon: {}, Total: {}",
                request.getDocumentoCliente(), request.getSalonId(), totalCobrado);

        return new FinalizarReservaResponseDTO(
                "Reserva finalizada",
                totalCobrado
        );
    }

    @Override
    public void eliminarReserva(Long id) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() ->
                        new BadRequestException(
                                "Reserva no encontrada."
                        )
                );

        reservaRepository.delete(reserva);
        log.info(
                "Reserva eliminada correctamente. Id: {}",
                reserva.getId()
        );
    }

    @Override
    public List<ReservaResponseDTO> listarReservas() {

        return reservaRepository.findAll()
                .stream()
                .map(this::mapToResponse
                ).toList();
    }

    @Override
    public ReservaResponseDTO consultarReserva(Long id) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() ->
                        new BadRequestException(
                                "Reserva no encontrada."
                        )
                );

        return mapToResponse(reserva);
    }

    @Override
    public List<ReservaResponseDTO> listarReservasActivasPorSalon(Long salonId) {

        log.info(
                "Consultando reservas activas para el salon: SalonId: {}",
                salonId
        );

        salonRepository.findById(salonId)
                .orElseThrow(()->
                        new BadRequestException(
                                "Salón no encontrado."
                        )
                );
        List<Reserva> reservas = reservaRepository.findBySalonIdAndEstadoOrderByFechaInicioAsc(
                salonId,
                EstadoReserva.ACTIVA
        );

        return reservas.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<ReservaResponseDTO> listarReservasActivasPorDocumento(String documentoCliente) {

        log.info(
                "Consultando reservas activas por documento de cliente: DocumentoCliente: {}",
                documentoCliente
        );
        if (documentoCliente == null || documentoCliente.isBlank()) {
            throw new BadRequestException(
                    "Debe ingresar un documento para realizar la búsqueda."
            );
        }
        List<Reserva> reservas = reservaRepository.buscarReservasActivasPorDocumento(
                documentoCliente,
                EstadoReserva.ACTIVA
        );

        return reservas.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<ReservaResponseDTO> listarReservasActivasPrimeraVezEnSalon() {
        return reservaRepository.reservasActivasPrimeraVezEnSalon().stream()
                .map(this::mapToResponse).toList();
    }

    private ReservaResponseDTO mapToResponse(Reserva reserva) {
        return new ReservaResponseDTO(
                reserva.getId(),
                reserva.getDocumentoCliente(),
                reserva.getNombreCliente(),
                reserva.getFechaInicio(),
                reserva.getFechaFinEstimada(),
                reserva.getFechaCreacion(),
                reserva.getAsistentes(),
                reserva.getEstado(),
                reserva.getSalon().getId(),
                reserva.getSalon().getNombre(),
                reserva.getCostoTotalEstimado()
        );
    }
    private void marcarExpiradaSiCorresponde(Reserva reserva) {
        if (LocalDateTime.now().isAfter(reserva.getFechaCreacion().plusHours(48))) {
            reserva.setEstado(EstadoReserva.EXPIRADA);
            reservaRepository.save(reserva);
            log.warn("Reserva expirada automáticamente. Id: {}", reserva.getId());
            throw new BadRequestException(
                    "La reserva ha expirado y no puede ser procesada.");
        }
    }

    private BigDecimal calcularCostoReserva(LocalDateTime inicio, LocalDateTime fin, BigDecimal costoHora) {
        long minutos = Duration.between(inicio, fin).toMinutes();
        BigDecimal horas = BigDecimal.valueOf(Math.ceil(minutos / 60.0));
        return horas.multiply(costoHora);
    }

    private EstadoReserva determinarEstadoInicialReserva(BigDecimal costo) {
        return costo.compareTo(UMBRAL_RESERVA_PREMIUM) > 0
                ? EstadoReserva.PENDIENTE_APROBACION
                : EstadoReserva.ACTIVA;
    }

    private void validarFechas(LocalDateTime inicio, LocalDateTime fin) {
        if (!fin.isAfter(inicio))
            throw new BadRequestException("La fecha fin debe ser mayor a la fecha inicio.");
        if (inicio.isBefore(LocalDateTime.now()))
            throw new BadRequestException("La reserva no puede ser creada en fechas pasadas.");
    }

    private void validarCapacidad(Salon salon, int asistentes, String accion) {
        if (asistentes > salon.getCapacidadMaxima()) {
            log.warn("Capacidad excedida. Salon: {}, Max: {}, Solicitados: {}",
                    salon.getNombre(), salon.getCapacidadMaxima(), asistentes);
            throw new BadRequestException(
                    "No se puede " + accion + " la reserva, capacidad insuficiente en el salón.");
        }
    }
}
