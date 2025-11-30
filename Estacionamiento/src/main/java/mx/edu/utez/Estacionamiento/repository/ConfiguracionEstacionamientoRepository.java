package mx.edu.utez.Estacionamiento.repository;

import mx.edu.utez.Estacionamiento.entity.ConfiguracionEstacionamiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfiguracionEstacionamientoRepository extends JpaRepository<ConfiguracionEstacionamiento, Long> {
    Optional<ConfiguracionEstacionamiento> findByClave(String clave);
}

