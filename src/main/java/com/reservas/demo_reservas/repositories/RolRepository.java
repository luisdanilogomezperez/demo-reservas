package com.reservas.demo_reservas.repositories;

import com.reservas.demo_reservas.entities.Rol;
import com.reservas.demo_reservas.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {

    Optional<Rol> findByNombre(RoleName nombre);
}
