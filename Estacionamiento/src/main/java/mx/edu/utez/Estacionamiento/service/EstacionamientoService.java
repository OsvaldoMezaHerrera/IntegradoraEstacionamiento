package mx.edu.utez.Estacionamiento.service;

import mx.edu.utez.Estacionamiento.model.Coche;
import mx.edu.utez.Estacionamiento.model.RegistroEstancia;
import mx.edu.utez.Estacionamiento.repository.EstacionamientoRepository;
import mx.edu.utez.Estacionamiento.structures.NodoListaSimple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service // Le dice a Spring que esta clase es de Lógica de Negocio
public class EstacionamientoService {

    // Inyectamos nuestro "DAO" en memoria
    @Autowired
    private EstacionamientoRepository repository;

    /**
     * Lógica para registrar la entrada de un coche
     */
    public String registrarEntrada(String placa) {
        // Verificar si ya está adentro (usando método buscarIndice)
        Coche cocheBusqueda = new Coche(placa);
        if (repository.lugaresOcupados.buscarIndice(cocheBusqueda) != -1) {
            return "ERROR: El coche con placa " + placa + " ya está dentro.";
        }

        Coche nuevoCoche = new Coche(placa, new Date()); // Hora de entrada actual

        // Comprobamos si hay lugares limitados
        if (repository.lugaresOcupados.getTamano() < repository.CAPACIDAD_MAXIMA) {
            repository.lugaresOcupados.insertarAlFinal(nuevoCoche);
            return "Coche " + placa + " estacionado. Lugares disponibles: " +
                    (repository.CAPACIDAD_MAXIMA - repository.lugaresOcupados.getTamano());
        } else {
            // Si está lleno, a la cola (FIFO)
            repository.filaEspera.Agregar(nuevoCoche);
            return "Estacionamiento lleno. Coche " + placa + " agregado a la fila de espera.";
        }
    }

    /**
     * Lógica para registrar la salida de un coche
     */
    public String registrarSalida(String placa) {
        Coche cocheBusqueda = new Coche(placa);

        // 1. Buscar el coche en la lista (Necesitamos el objeto completo)
        Coche cocheEncontrado = null;
        NodoListaSimple<Coche> actual = repository.lugaresOcupados.getHead();
        while (actual != null) {
            if (actual.getDato().equals(cocheBusqueda)) {
                cocheEncontrado = actual.getDato();
                break;
            }
            actual = actual.getEnlace();
        }

        if (cocheEncontrado == null) {
            return "ERROR: El coche con placa " + placa + " no se encuentra estacionado.";
        }

        // 2. Eliminar el coche de la lista
        boolean eliminado = repository.lugaresOcupados.eliminarPorValor(cocheEncontrado);

        if (!eliminado) {
            return "ERROR: No se pudo eliminar el coche " + placa; // No debería pasar si lo encontramos
        }

        // 3. Calcular tarifa (simulada)
        Date horaSalida = new Date();
        long diffMs = horaSalida.getTime() - cocheEncontrado.getHoraEntrada().getTime();
        long diffMinutos = TimeUnit.MILLISECONDS.toMinutes(diffMs);
        double tarifa = diffMinutos * 1.5; // Tarifa simulada: $1.5 por minuto

        // 4. Crear registro y guardarlo en la Pila (LIFO)
        RegistroEstancia registro = new RegistroEstancia(placa, cocheEncontrado.getHoraEntrada(), horaSalida, tarifa);
        repository.historialSalidas.Insertar(registro);

        String mensajeSalida = "Coche " + placa + " salió. Tiempo: " + diffMinutos + " min. Total a pagar: $" + tarifa;

        // 5. Mover a alguien de la cola de espera (FIFO)
        if (!repository.filaEspera.EstaVacia()) {
            Coche cocheEnEspera = repository.filaEspera.Quitar();
            if (cocheEnEspera != null) {
                // Le asignamos una nueva hora de entrada
                cocheEnEspera.setHoraEntrada(new Date());
                repository.lugaresOcupados.insertarAlFinal(cocheEnEspera);
                mensajeSalida += ". \nCoche " + cocheEnEspera.getPlaca() + " de la fila de espera ha sido estacionado.";
            }
        }

        return mensajeSalida;
    }

    // Métodos para mostrar (como pide tu clase ListaSimple.mostrar)

    public void mostrarCochesActuales() {
        System.out.println("--- COCHES ACTUALMENTE ESTACIONADOS ---");
        repository.lugaresOcupados.mostrar(); // Método ListaSimple
    }

    public void mostrarFilaEspera() {
        System.out.println("--- COCHES EN FILA DE ESPERA ---");
        repository.filaEspera.Mostrar(); // Método cola
    }

    public void mostrarHistorialSalidas() {
        System.out.println("--- HISTORIAL DE SALIDAS (LIFO) ---");
        repository.historialSalidas.MostrarPila(); // Método pila
    }

    public int getLugaresOcupadosSize() {
        return repository.lugaresOcupados.getTamano();
    }
}