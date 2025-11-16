package mx.edu.utez.Estacionamiento.model;

import java.util.Date;

public class RegistroEstancia {
    private String placa;
    private Date horaEntrada;
    private Date horaSalida;
    private double tarifaPagada;

    public RegistroEstancia(String placa, Date horaEntrada, Date horaSalida, double tarifaPagada) {
        this.placa = placa;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
        this.tarifaPagada = tarifaPagada;
    }

    // Getters, Setters y toString()

    public Date getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(Date horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public double getTarifaPagada() {
        return tarifaPagada;
    }

    public void setTarifaPagada(double tarifaPagada) {
        this.tarifaPagada = tarifaPagada;
    }

    public Date getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(Date horaSalida) {
        this.horaSalida = horaSalida;
    }

    @Override
    public String toString() {
        return "RegistroEstancia{" +
                "placa='" + placa + '\'' +
                ", horaEntrada=" + horaEntrada +
                ", horaSalida=" + horaSalida +
                ", tarifaPagada=" + tarifaPagada +
                '}';
    }
}