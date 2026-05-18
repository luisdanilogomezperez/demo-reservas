package com.reservas.demo_reservas.services.servicesmpl;

import com.reservas.demo_reservas.dto.response.TopClienteResponseDTO;
import com.reservas.demo_reservas.dto.response.TopSucursalResponseDTO;
import com.reservas.demo_reservas.entities.Reserva;
import com.reservas.demo_reservas.entities.ReservaHistorico;
import com.reservas.demo_reservas.enums.EstadoReserva;
import com.reservas.demo_reservas.repositories.ReservaHistoricoRepository;
import com.reservas.demo_reservas.services.ReservaHistoricoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservaHistoricoServiceImpl implements ReservaHistoricoService {

    private final ReservaHistoricoRepository reservaHistoricoRepository;

    private record DateRange(LocalDateTime inicio, LocalDateTime fin) {}

    @Override
    public void guardarReservaHistorico(Reserva reserva, LocalDateTime fechaFinReal, BigDecimal totalCobrado) {
        ReservaHistorico reservaHistorico = new ReservaHistorico();

        reservaHistorico.setReservaId(reserva.getId());
        reservaHistorico.setDocumentoCliente(reserva.getDocumentoCliente());
        reservaHistorico.setNombreCliente(reserva.getNombreCliente());
        reservaHistorico.setFechaInicio(reserva.getFechaInicio());
        reservaHistorico.setFechaFinEstimada(reserva.getFechaFinEstimada());
        reservaHistorico.setFechaFinReal(fechaFinReal);
        reservaHistorico.setFechaCreacion(reserva.getFechaCreacion());
        reservaHistorico.setAsistentes(reserva.getAsistentes());
        reservaHistorico.setCostoEstimado(reserva.getCostoTotalEstimado());
        reservaHistorico.setTotalCobrado(totalCobrado);
        reservaHistorico.setMotivoRechazo(reserva.getMotivoRechazo());
        reservaHistorico.setEstado(EstadoReserva.FINALIZADA);
        reservaHistorico.setSalon(reserva.getSalon());

        reservaHistoricoRepository.save(reservaHistorico);
    }

    @Override
    public List<TopClienteResponseDTO> topClientes() {
        Pageable top10 = PageRequest.of(0, 10);

        return reservaHistoricoRepository.topClientes(top10);
    }

    @Override
    public List<TopClienteResponseDTO> topClientesPorSalon(Long salonId) {
        Pageable top10 = PageRequest.of(0, 10);
        return reservaHistoricoRepository.topClientesPorSalon(salonId, top10);
    }

    @Override
    public BigDecimal totalGananciasDia(Long salonId) {
        DateRange range = rangoHoy();
        return reservaHistoricoRepository.totalGanancias(salonId, range.inicio, range.fin);
    }

    @Override
    public BigDecimal totalGananciasSemana(Long salonId) {
        DateRange range = rangoSemana();
        return reservaHistoricoRepository.totalGanancias(salonId, range.inicio, range.fin);
    }

    @Override
    public BigDecimal totalGananciasMes(Long salonId) {
        DateRange range = rangoMes();
        return reservaHistoricoRepository.totalGanancias(salonId,range.inicio, range.fin);
    }

    @Override
    public BigDecimal totalGananciasAnio(Long salonId) {
        DateRange range = rangoAnio();
        return reservaHistoricoRepository.totalGanancias(salonId, range.inicio, range.fin);
    }

    @Override
    public List<TopSucursalResponseDTO> topSucursales() {
        DateRange rangeAnio = rangoAnio();
        DateRange rangeMes = rangoMes();
        Pageable top3 = PageRequest.of(0, 3);
        return reservaHistoricoRepository
                .topSucursales(
                        top3, rangeMes.inicio, rangeMes.fin,
                        rangeAnio.inicio, rangeAnio.fin
                );
    }

    private DateRange rangoHoy() {
        LocalDate hoy = LocalDate.now();
        return new DateRange(hoy.atStartOfDay(), hoy.atTime(LocalTime.MAX));
    }

    private DateRange rangoSemana() {
        LocalDate hoy = LocalDate.now();
        return new DateRange(
                hoy.with(DayOfWeek.MONDAY).atStartOfDay(),
                hoy.with(DayOfWeek.SUNDAY).atTime(LocalTime.MAX)
        );
    }

    private DateRange rangoMes() {
        LocalDate hoy = LocalDate.now();
        return new DateRange(
                hoy.withDayOfMonth(1).atStartOfDay(),
                hoy.withDayOfMonth(hoy.lengthOfMonth()).atTime(LocalTime.MAX)
        );
    }

    private DateRange rangoAnio() {
        LocalDate hoy = LocalDate.now();
        return new DateRange(
                hoy.withDayOfYear(1).atStartOfDay(),
                hoy.withDayOfYear(hoy.lengthOfYear()).atTime(LocalTime.MAX)
        );
    }

}
