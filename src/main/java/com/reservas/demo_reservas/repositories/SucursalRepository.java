package com.reservas.demo_reservas.repositories;

import com.reservas.demo_reservas.entities.Sucursal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SucursalRepository extends JpaRepository<Sucursal, Long> {

    boolean existsByNombre (String nombre);
}
