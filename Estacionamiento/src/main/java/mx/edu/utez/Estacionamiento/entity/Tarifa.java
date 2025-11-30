package mx.edu.utez.Estacionamiento.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tarifas")
public class Tarifa {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "tarifa_por_minuto", nullable = false, precision = 10, scale = 2)
    private BigDecimal tarifaPorMinuto;
    
    @Column(name = "tarifa_1_minuto", nullable = false, precision = 10, scale = 2)
    private BigDecimal tarifa1Minuto;
    
    @Column(name = "tarifa_0_1_hora", nullable = false, precision = 10, scale = 2)
    private BigDecimal tarifa0_1Hora;
    
    @Column(name = "tarifa_1_2_horas", nullable = false, precision = 10, scale = 2)
    private BigDecimal tarifa1_2Horas;
    
    @Column(name = "tarifa_2_mas_horas", nullable = false, precision = 10, scale = 2)
    private BigDecimal tarifa2MasHoras;
    
    @Column(name = "tarifa_maxima_diaria", nullable = false, precision = 10, scale = 2)
    private BigDecimal tarifaMaximaDiaria;
    
    @Column(name = "tarifa_maxima_semanal", nullable = false, precision = 10, scale = 2)
    private BigDecimal tarifaMaximaSemanal;
    
    @Column(name = "tarifa_ticket_perdido", nullable = false, precision = 10, scale = 2)
    private BigDecimal tarifaTicketPerdido;
    
    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;
    
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
    
    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        fechaActualizacion = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }
    
    // Constructores
    public Tarifa() {
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public BigDecimal getTarifaPorMinuto() {
        return tarifaPorMinuto;
    }
    
    public void setTarifaPorMinuto(BigDecimal tarifaPorMinuto) {
        this.tarifaPorMinuto = tarifaPorMinuto;
    }
    
    public BigDecimal getTarifa1Minuto() {
        return tarifa1Minuto;
    }
    
    public void setTarifa1Minuto(BigDecimal tarifa1Minuto) {
        this.tarifa1Minuto = tarifa1Minuto;
    }
    
    public BigDecimal getTarifa0_1Hora() {
        return tarifa0_1Hora;
    }
    
    public void setTarifa0_1Hora(BigDecimal tarifa0_1Hora) {
        this.tarifa0_1Hora = tarifa0_1Hora;
    }
    
    public BigDecimal getTarifa1_2Horas() {
        return tarifa1_2Horas;
    }
    
    public void setTarifa1_2Horas(BigDecimal tarifa1_2Horas) {
        this.tarifa1_2Horas = tarifa1_2Horas;
    }
    
    public BigDecimal getTarifa2MasHoras() {
        return tarifa2MasHoras;
    }
    
    public void setTarifa2MasHoras(BigDecimal tarifa2MasHoras) {
        this.tarifa2MasHoras = tarifa2MasHoras;
    }
    
    public BigDecimal getTarifaMaximaDiaria() {
        return tarifaMaximaDiaria;
    }
    
    public void setTarifaMaximaDiaria(BigDecimal tarifaMaximaDiaria) {
        this.tarifaMaximaDiaria = tarifaMaximaDiaria;
    }
    
    public BigDecimal getTarifaMaximaSemanal() {
        return tarifaMaximaSemanal;
    }
    
    public void setTarifaMaximaSemanal(BigDecimal tarifaMaximaSemanal) {
        this.tarifaMaximaSemanal = tarifaMaximaSemanal;
    }
    
    public BigDecimal getTarifaTicketPerdido() {
        return tarifaTicketPerdido;
    }
    
    public void setTarifaTicketPerdido(BigDecimal tarifaTicketPerdido) {
        this.tarifaTicketPerdido = tarifaTicketPerdido;
    }
    
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }
    
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
}

