package mx.edu.utez.Estacionamiento.repository;

import mx.edu.utez.Estacionamiento.entity.Tarifa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TarifaRepository extends JpaRepository<Tarifa, Long> {
    Optional<Tarifa> findFirstByOrderByIdDesc();
}

