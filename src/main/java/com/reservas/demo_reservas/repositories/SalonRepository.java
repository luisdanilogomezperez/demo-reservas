package com.reservas.demo_reservas.repositories;

import com.reservas.demo_reservas.entities.Salon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalonRepository extends JpaRepository<Salon, Long> {

    boolean existsByNombre (String nombre);

}
