package com.reservas.demo_reservas.repositories;

import com.reservas.demo_reservas.entities.Reserva;
import com.reservas.demo_reservas.enums.EstadoReserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    @Query("""
                SELECT COUNT(r) > 0
                FROM Reserva r
                WHERE r.salon.id = :salonId
                AND r.estado = 'ACTIVA'
                AND (
                    :fechaInicio < r.fechaFinEstimada
                    AND
                    :fechaFin > r.fechaInicio
                )
            """)
    boolean existeReservaSolapada(
            @Param("salonId") Long salonId,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin
    );

    @Query("""
                SELECT COUNT(r) > 0
                FROM Reserva r
                WHERE r.salon.id = :salonId
                AND r.id = :reservaId
                AND r.estado = 'ACTIVA'
                AND (
                    :fechaInicio < r.fechaFinEstimada
                    AND
                    :fechaFin > r.fechaInicio
                )
            """)
    boolean existeReservaSolapadaEditar(
            @Param("salonId") Long salonId,
            @Param("reservaId") Long reservaId,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin
    );

    boolean existsByDocumentoClienteAndEstado(
            String documentoCliente,
            EstadoReserva estado
    );

    boolean existsByDocumentoClienteAndEstadoAndIdNot(
            String documentoCliente,
            EstadoReserva estado,
            Long id
    );

    Optional<Reserva> findByDocumentoClienteAndSalonIdAndEstado(
            String documentoCliente,
            Long salonId,
            EstadoReserva estado
    );

    List<Reserva> findBySalonIdAndEstadoOrderByFechaInicioAsc(Long salonId, EstadoReserva estado);

    @Query("""
                SELECT r
                FROM Reserva r
                WHERE r.estado = :estado
                AND r.documentoCliente LIKE %:documentoCliente%
            """)
    List<Reserva> buscarReservasActivasPorDocumento(
            @Param("documentoCliente") String documentoCliente,
            @Param("estado") EstadoReserva estado
    );

    @Query("""
                SELECT r
                FROM Reserva r
                WHERE r.estado = 'ACTIVA'
                  AND NOT EXISTS (
                      SELECT 1 FROM ReservaHistorico h
                      WHERE h.documentoCliente = r.documentoCliente
                        AND h.salon.id = r.salon.id
                  )
            """)
    List<Reserva> reservasActivasPrimeraVezEnSalon();
}
