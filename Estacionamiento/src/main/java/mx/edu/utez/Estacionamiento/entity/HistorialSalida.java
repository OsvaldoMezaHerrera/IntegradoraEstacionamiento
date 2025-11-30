package mx.edu.utez.Estacionamiento.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "historial_salidas")
public class HistorialSalida {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "placa", nullable = false, length = 10)
    private String placa;
    
    @Column(name = "hora_entrada", nullable = false)
    private LocalDateTime horaEntrada;
    
    @Column(name = "hora_salida", nullable = false)
    private LocalDateTime horaSalida;
    
    @Column(name = "tarifa_pagada", nullable = false, precision = 10, scale = 2)
    private BigDecimal tarifaPagada;
    
    @Column(name = "metodo_pago", length = 50)
    private String metodoPago;
    
    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;
    
    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }
    
    // Constructores
    public HistorialSalida() {
    }
    
    public HistorialSalida(String placa, LocalDateTime horaEntrada, LocalDateTime horaSalida, 
                          BigDecimal tarifaPagada, String metodoPago) {
        this.placa = placa;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
        this.tarifaPagada = tarifaPagada;
        this.metodoPago = metodoPago;
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
    
    public LocalDateTime getHoraSalida() {
        return horaSalida;
    }
    
    public void setHoraSalida(LocalDateTime horaSalida) {
        this.horaSalida = horaSalida;
    }
    
    public BigDecimal getTarifaPagada() {
        return tarifaPagada;
    }
    
    public void setTarifaPagada(BigDecimal tarifaPagada) {
        this.tarifaPagada = tarifaPagada;
    }
    
    public String getMetodoPago() {
        return metodoPago;
    }
    
    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }
    
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}

