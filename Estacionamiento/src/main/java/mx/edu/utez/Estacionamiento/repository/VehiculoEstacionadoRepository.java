package mx.edu.utez.Estacionamiento.repository;

import mx.edu.utez.Estacionamiento.entity.VehiculoEstacionado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehiculoEstacionadoRepository extends JpaRepository<VehiculoEstacionado, Long> {
    Optional<VehiculoEstacionado> findByPlaca(String placa);
    boolean existsByPlaca(String placa);
    long count();
}

