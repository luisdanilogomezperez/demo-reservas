package com.reservas.demo_reservas.repositories;

import com.reservas.demo_reservas.dto.response.TopClienteResponseDTO;
import com.reservas.demo_reservas.dto.response.TopSucursalResponseDTO;
import com.reservas.demo_reservas.entities.Reserva;
import com.reservas.demo_reservas.entities.ReservaHistorico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface ReservaHistoricoRepository extends JpaRepository<ReservaHistorico, Long> {

    // ADMIN y GESTOR: top 10 clientes con más reservas en todo el sistema
    @Query("""
                SELECT new com.reservas.demo_reservas.dto.response.TopClienteResponseDTO(
                    r.documentoCliente,
                    r.nombreCliente,
                    COUNT(r)
                )
                FROM ReservaHistorico r
                GROUP BY r.documentoCliente, r.nombreCliente
                ORDER BY COUNT(r) DESC
            """)
    List<TopClienteResponseDTO> topClientes(Pageable pageable);

    // ADMIN y GESTOR: top 10 clientes con más reservas en un salón específico
    @Query("""
                SELECT new com.reservas.demo_reservas.dto.response.TopClienteResponseDTO(
                    r.documentoCliente,
                    r.nombreCliente,
                    COUNT(r)
                )
                FROM ReservaHistorico r
                WHERE r.salon.id = :salonId
                GROUP BY r.documentoCliente, r.nombreCliente
                ORDER BY COUNT(r) DESC
            """)
    List<TopClienteResponseDTO> topClientesPorSalon(
            @Param("salonId") Long salonId,
            Pageable pageable);

    // GESTOR y ADMIN: ganancias en un salón para cualquier rango de fecha
    @Query("""
                SELECT COALESCE(SUM(r.totalCobrado), 0)
                FROM ReservaHistorico r
                WHERE r.salon.id = :salonId
                AND r.fechaFinReal BETWEEN :inicio AND :fin
            """)
    BigDecimal totalGanancias(
            @Param("salonId") Long salonId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin
    );

    // ADMIN: top 3 sucursales con mayor facturación en el mes actual
    @Query("""
                SELECT new com.reservas.demo_reservas.dto.response.TopSucursalResponseDTO(
                    r.salon.sucursal.nombre,
                    SUM(r.totalCobrado)
                )
                FROM ReservaHistorico r
                WHERE r.fechaFinReal BETWEEN :inicioMes AND :finMes
                GROUP BY r.salon.sucursal.nombre
                ORDER BY SUM(r.totalCobrado) DESC
            """)
    List<TopSucursalResponseDTO> topSucursales(
            Pageable pageable,
            @Param("inicioMes") LocalDateTime inicioMes,
            @Param("finMes") LocalDateTime finMes,
            @Param("inicioAnio") LocalDateTime inicioAnio,
            @Param("finAnio") LocalDateTime finAnio
    );

}
