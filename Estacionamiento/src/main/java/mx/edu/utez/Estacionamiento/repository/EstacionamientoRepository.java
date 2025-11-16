package mx.edu.utez.Estacionamiento.repository;

import mx.edu.utez.Estacionamiento.model.Coche;
import mx.edu.utez.Estacionamiento.model.RegistroEstancia;
import mx.edu.utez.Estacionamiento.structures.*; // Importa tus estructuras
import org.springframework.stereotype.Repository;

@Repository // Le dice a Spring que esta clase maneja datos
public class EstacionamientoRepository {

    // 1. LISTA para almacenamiento general de coches DENTRO
    // Usamos tu ListaSimple para agregar, eliminar, buscar y mostrar
    public final ListaSimple<Coche> lugaresOcupados = new ListaSimple<>();

    // 2. COLA para gestionar coches en espera (si el parking está lleno)
    // Gestiona elementos en orden de llegada (FIFO)
    public final Cola<Coche> filaEspera = new Cola<>();

    // 3. PILA para el historial de salidas
    // El último en salir es el primero en el historial (LIFO)
    public final Pila<RegistroEstancia> historialSalidas = new Pila<>();

    // Definimos los lugares limitados
    public final int CAPACIDAD_MAXIMA = 20; // Cantidad Default
}