package mx.edu.utez.Estacionamiento.model;

import java.util.Date;
import java.util.Objects;

public class Coche {
    private String placa;
    private Date horaEntrada;

    // Constructor para cuando entra
    public Coche(String placa, Date horaEntrada) {
        this.placa = placa;
        this.horaEntrada = horaEntrada;
    }

    // Constructor simple solo con placa (útil para buscar y eliminar)
    public Coche(String placa) {
        this.placa = placa;
        this.horaEntrada = null;
    }

    // Getters y Setters
    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }
    public Date getHoraEntrada() { return horaEntrada; }
    public void setHoraEntrada(Date horaEntrada) { this.horaEntrada = horaEntrada; }

    /**
     * Esto es para que tu método ListaSimple.eliminarPorValor() [cite: 180]
     * funcione correctamente al buscar un coche solo por su placa.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coche coche = (Coche) o;
        return Objects.equals(placa, coche.placa);
    }

    @Override
    public int hashCode() {
        return Objects.hash(placa);
    }

    @Override
    public String toString() {
        return "Coche{" +
                "placa='" + placa + '\'' +
                ", horaEntrada=" + horaEntrada +
                '}';
    }
}