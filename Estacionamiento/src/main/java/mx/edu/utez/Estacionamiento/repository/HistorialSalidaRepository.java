package mx.edu.utez.Estacionamiento.repository;

import mx.edu.utez.Estacionamiento.entity.HistorialSalida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistorialSalidaRepository extends JpaRepository<HistorialSalida, Long> {
    @Query("SELECT h FROM HistorialSalida h ORDER BY h.fechaCreacion DESC, h.horaSalida DESC")
    List<HistorialSalida> findAllOrderByFechaCreacionDesc();
    
    List<HistorialSalida> findByPlacaOrderByHoraSalidaDesc(String placa);
}

