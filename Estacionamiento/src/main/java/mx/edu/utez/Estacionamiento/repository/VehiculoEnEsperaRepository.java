package mx.edu.utez.Estacionamiento.repository;

import mx.edu.utez.Estacionamiento.entity.VehiculoEnEspera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehiculoEnEsperaRepository extends JpaRepository<VehiculoEnEspera, Long> {
    @Query("SELECT v FROM VehiculoEnEspera v ORDER BY v.posicion ASC, v.horaEntrada ASC")
    List<VehiculoEnEspera> findAllOrderByPosicion();
    
    @Query("SELECT MAX(v.posicion) FROM VehiculoEnEspera v")
    Optional<Integer> findMaxPosicion();
    
    @Query("SELECT v FROM VehiculoEnEspera v WHERE v.posicion = (SELECT MIN(v2.posicion) FROM VehiculoEnEspera v2) ORDER BY v.posicion ASC, v.horaEntrada ASC")
    Optional<VehiculoEnEspera> findFirstByOrderByPosicionAsc();
    
    long count();
}

