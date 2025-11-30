package mx.edu.utez.Estacionamiento.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "fila_espera")
public class VehiculoEnEspera {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "placa", nullable = false, length = 10)
    private String placa;
    
    @Column(name = "hora_entrada", nullable = false)
    private LocalDateTime horaEntrada;
    
    @Column(name = "posicion", nullable = false)
    private Integer posicion;
    
    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;
    
    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }
    
    // Constructores
    public VehiculoEnEspera() {
    }
    
    public VehiculoEnEspera(String placa, LocalDateTime horaEntrada, Integer posicion) {
        this.placa = placa;
        this.horaEntrada = horaEntrada;
        this.posicion = posicion;
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getPlaca() {
        return placa;
    }
    
    public void setPlaca(String placa) {
        this.placa = placa;
    }
    
    public LocalDateTime getHoraEntrada() {
        return horaEntrada;
    }
    
    public void setHoraEntrada(LocalDateTime horaEntrada) {
        this.horaEntrada = horaEntrada;
    }
    
    public Integer getPosicion() {
        return posicion;
    }
    
    public void setPosicion(Integer posicion) {
        this.posicion = posicion;
    }
    
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}

