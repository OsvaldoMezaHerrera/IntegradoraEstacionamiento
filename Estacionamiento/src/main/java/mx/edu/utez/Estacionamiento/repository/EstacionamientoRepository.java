package mx.edu.utez.Estacionamiento.repository;

import mx.edu.utez.Estacionamiento.model.Coche;
import mx.edu.utez.Estacionamiento.model.RegistroEstancia;
import mx.edu.utez.Estacionamiento.structures.*;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;

/**
 * Repositorio que usa estructuras de datos personalizadas (Lista, Cola, Pila)
 * Trabaja completamente en memoria sin conexión a base de datos
 */
@Repository("estacionamientoRepository")
public class EstacionamientoRepository {

    // ============================================
    // ESTRUCTURAS DE DATOS PRINCIPALES
    // ============================================
    
    // 1. LISTA SIMPLE: Almacenamiento secuencial de vehículos estacionados
    private final ListaSimple<Coche> lugaresOcupados = new ListaSimple<>();
    
    // 2. ÁRBOL BINARIO: Búsqueda rápida O(log n) de vehículos por placa
    private final ArbolBinario<Coche> arbolBusqueda = new ArbolBinario<>();
    
    // 3. COLA: Gestión FIFO de vehículos en espera
    private final Cola<Coche> filaEspera = new Cola<>();
    
    // 4. COLA CIRCULAR: Rotación de espacios disponibles
    private ColaCircular<Coche> espaciosRotativos;
    
    // 5. PILA: Historial de salidas (LIFO - último en salir primero)
    private final Pila<RegistroEstancia> historialSalidas = new Pila<>();
    
    // 6. LISTA DOBLE: Historial con navegación bidireccional
    private final ListaDoble<RegistroEstancia> historialNavegable = new ListaDoble<>();
    
    // 7. ARREGLO DINÁMICO: Estadísticas y reportes temporales
    private final ArregloDinamico<RegistroEstancia> estadisticasTemporales = new ArregloDinamico<>();
    
    /**
     * Getters para acceder a las estructuras (necesarios para proxies CGLIB de Spring)
     */
    public ListaSimple<Coche> getLugaresOcupados() {
        return lugaresOcupados;
    }
    
    public ArbolBinario<Coche> getArbolBusqueda() {
        return arbolBusqueda;
    }
    
    public Cola<Coche> getFilaEspera() {
        return filaEspera;
    }
    
    public ColaCircular<Coche> getEspaciosRotativos() {
        return espaciosRotativos;
    }
    
    public Pila<RegistroEstancia> getHistorialSalidas() {
        return historialSalidas;
    }
    
    public ListaDoble<RegistroEstancia> getHistorialNavegable() {
        return historialNavegable;
    }
    
    public ArregloDinamico<RegistroEstancia> getEstadisticasTemporales() {
        return estadisticasTemporales;
    }
    
    /**
     * Verifica que las estructuras estén inicializadas después de la inyección de dependencias
     */
    @PostConstruct
    public void verificarInicializacion() {
        System.out.println("\n═══════════════════════════════════════════════════════════");
        System.out.println("🔧 VERIFICACIÓN DE ESTRUCTURAS DE DATOS");
        System.out.println("═══════════════════════════════════════════════════════════");
        
        if (lugaresOcupados == null) {
            throw new IllegalStateException("lugaresOcupados (ListaSimple) no está inicializado");
        }
        if (arbolBusqueda == null) {
            throw new IllegalStateException("arbolBusqueda (ArbolBinario) no está inicializado");
        }
        if (filaEspera == null) {
            throw new IllegalStateException("filaEspera (Cola) no está inicializado");
        }
        if (historialSalidas == null) {
            throw new IllegalStateException("historialSalidas (Pila) no está inicializado");
        }
        if (historialNavegable == null) {
            throw new IllegalStateException("historialNavegable (ListaDoble) no está inicializado");
        }
        if (estadisticasTemporales == null) {
            throw new IllegalStateException("estadisticasTemporales (ArregloDinamico) no está inicializado");
        }
        
        // Inicializar ColaCircular con capacidad por defecto
        if (espaciosRotativos == null) {
            espaciosRotativos = new ColaCircular<>(CAPACIDAD_MAXIMA);
            System.out.println("   ✅ ColaCircular inicializada con capacidad: " + CAPACIDAD_MAXIMA);
        }
        
        System.out.println("✅ Estructuras básicas inicializadas correctamente:");
        System.out.println("   1. ListaSimple<Coche> - Lugares ocupados");
        System.out.println("   2. ArbolBinario<Coche> - Búsqueda rápida O(log n)");
        System.out.println("   3. Cola<Coche> - Fila de espera (FIFO)");
        System.out.println("   4. ColaCircular<Coche> - Rotación de espacios");
        System.out.println("   5. Pila<RegistroEstancia> - Historial LIFO");
        System.out.println("   6. ListaDoble<RegistroEstancia> - Historial navegable");
        System.out.println("   7. ArregloDinamico<RegistroEstancia> - Estadísticas temporales");
        System.out.println("═══════════════════════════════════════════════════════════\n");
    }

    // Capacidad máxima del estacionamiento
    public int CAPACIDAD_MAXIMA = 20;

    // Configuración de tarifas (solo tarifa por minuto)
    public double tarifaPorMinuto = 1.5;

    /**
     * Inicializa las estructuras con valores por defecto
     * Los datos se cargan desde localStorage en el frontend
     */
    @PostConstruct
    public void inicializar() {
        System.out.println("\n═══════════════════════════════════════════════════════════");
        System.out.println("📥 PROCESO: INICIALIZACIÓN DE ESTRUCTURAS EN MEMORIA");
        System.out.println("═══════════════════════════════════════════════════════════");
        
        // Limpiar todas las estructuras
        System.out.println("🗑️  Limpiando estructuras en memoria...");
        lugaresOcupados.limpiar();
        arbolBusqueda.limpiar();
        filaEspera.BorrarCola();
        if (espaciosRotativos != null) {
            espaciosRotativos.borrarCola();
        }
        historialSalidas.LimpiarPila();
        historialNavegable.limpiar();
        estadisticasTemporales.limpiar();
        System.out.println("✅ Todas las estructuras limpiadas.");
        
        // Inicializar ColaCircular con la capacidad máxima
        espaciosRotativos = new ColaCircular<>(CAPACIDAD_MAXIMA);
        System.out.println("   ✅ ColaCircular inicializada con capacidad: " + CAPACIDAD_MAXIMA);
        
        System.out.println("\n✅ PROCESO COMPLETADO: Estructuras inicializadas correctamente.");
        System.out.println("   💡 Los datos se gestionan desde localStorage en el frontend.");
        System.out.println("═══════════════════════════════════════════════════════════\n");
    }
}
