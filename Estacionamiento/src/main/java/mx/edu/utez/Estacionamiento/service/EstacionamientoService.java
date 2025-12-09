package mx.edu.utez.Estacionamiento.service;

import mx.edu.utez.Estacionamiento.model.Coche;
import mx.edu.utez.Estacionamiento.model.RegistroEstancia;
import mx.edu.utez.Estacionamiento.repository.EstacionamientoRepository;
import mx.edu.utez.Estacionamiento.structures.NodoListaSimple;
import mx.edu.utez.Estacionamiento.structures.NodoCola;
import mx.edu.utez.Estacionamiento.structures.NodoPila;
import mx.edu.utez.Estacionamiento.structures.ListaSimple;
import mx.edu.utez.Estacionamiento.structures.Cola;
import mx.edu.utez.Estacionamiento.structures.Pila;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@Service
@org.springframework.context.annotation.DependsOn("estacionamientoRepository")
public class EstacionamientoService {

    private final EstacionamientoRepository repository;

    // Inyección por constructor para asegurar inicialización
    @Autowired
    public EstacionamientoService(EstacionamientoRepository repository) {
        System.out.println("🔧 EstacionamientoService: Iniciando construcción del servicio...");
        if (repository == null) {
            System.err.println("❌ ERROR: EstacionamientoRepository es null");
            throw new IllegalStateException("EstacionamientoRepository no puede ser null");
        }
        System.out.println("   ✅ Repositorio recibido: " + repository.getClass().getName());
        
        // Usar getters en lugar de acceso directo a campos para evitar problemas con proxies CGLIB
        ListaSimple<Coche> lugaresOcupados = repository.getLugaresOcupados();
        if (lugaresOcupados == null) {
            System.err.println("❌ ERROR: lugaresOcupados es null");
            System.err.println("   Tipo del repositorio: " + repository.getClass().getName());
            throw new IllegalStateException("lugaresOcupados no puede ser null en EstacionamientoRepository");
        }
        System.out.println("   ✅ lugaresOcupados inicializado: " + lugaresOcupados.getClass().getName());
        
        Cola<Coche> filaEspera = repository.getFilaEspera();
        if (filaEspera == null) {
            System.err.println("❌ ERROR: filaEspera es null");
            throw new IllegalStateException("filaEspera no puede ser null en EstacionamientoRepository");
        }
        System.out.println("   ✅ filaEspera inicializado: " + filaEspera.getClass().getName());
        
        Pila<RegistroEstancia> historialSalidas = repository.getHistorialSalidas();
        if (historialSalidas == null) {
            System.err.println("❌ ERROR: historialSalidas es null");
            throw new IllegalStateException("historialSalidas no puede ser null en EstacionamientoRepository");
        }
        System.out.println("   ✅ historialSalidas inicializado: " + historialSalidas.getClass().getName());
        
        this.repository = repository;
        System.out.println("✅ EstacionamientoService: Repositorio inicializado correctamente");
    }

    /**
     * Verifica que el repositorio y sus estructuras estén inicializados
     */
    private void verificarInicializacion() {
        if (repository == null) {
            System.err.println("❌ ERROR CRÍTICO: El repositorio no está inicializado.");
            throw new IllegalStateException("El repositorio no está inicializado. Verifique la conexión a la base de datos.");
        }
        // Usar getters para evitar problemas con proxies CGLIB
        if (repository.getLugaresOcupados() == null) {
            System.err.println("❌ ERROR CRÍTICO: La estructura lugaresOcupados es null.");
            System.err.println("   Tipo del repositorio: " + repository.getClass().getName());
            throw new IllegalStateException("La estructura lugaresOcupados no está inicializada.");
        }
        if (repository.getFilaEspera() == null) {
            System.err.println("❌ ERROR CRÍTICO: La estructura filaEspera es null.");
            throw new IllegalStateException("La estructura filaEspera no está inicializada.");
        }
        if (repository.getHistorialSalidas() == null) {
            System.err.println("❌ ERROR CRÍTICO: La estructura historialSalidas es null.");
            throw new IllegalStateException("La estructura historialSalidas no está inicializada.");
        }
    }

    /**
     * Lógica para registrar la entrada de un coche
     * Usa ListaSimple y Cola, luego sincroniza con BD
     */
    public String registrarEntrada(String placa) {
        System.out.println("\n═══════════════════════════════════════════════════════════");
        System.out.println("🚗 PROCESO: REGISTRAR ENTRADA DE VEHÍCULO");
        System.out.println("═══════════════════════════════════════════════════════════");
        
        // Validar placa
        if (placa == null || placa.trim().isEmpty()) {
            System.out.println("❌ ERROR: La placa no puede estar vacía.");
            return "ERROR: La placa no puede estar vacía.";
        }
        
        final String placaNormalizada = placa.trim().toUpperCase();
        System.out.println("📋 Placa recibida: " + placaNormalizada);
        
        // Verificar inicialización
        verificarInicializacion();
        
        // Verificar si ya está estacionado (usando ListaSimple)
        System.out.println("🔍 Verificando si el vehículo ya está estacionado...");
        Coche cocheBusqueda = new Coche(placaNormalizada);
        int indice = repository.getLugaresOcupados().buscarIndice(cocheBusqueda);
        if (indice != -1) {
            System.out.println("❌ ERROR: El coche con placa " + placaNormalizada + " ya está dentro (índice: " + indice + ").");
            return "ERROR: El coche con placa " + placaNormalizada + " ya está dentro.";
        }
        System.out.println("✅ Vehículo no encontrado en estacionamiento. Procediendo...");

        Coche nuevoCoche = new Coche(placaNormalizada, new Date());
        System.out.println("📅 Hora de entrada: " + nuevoCoche.getHoraEntrada());

        // Comprobamos si hay lugares limitados (usando ListaSimple)
        int lugaresOcupados = repository.getLugaresOcupados().getTamano();
        int capacidadMaxima = repository.CAPACIDAD_MAXIMA;
        
        // Validar capacidad máxima
        if (capacidadMaxima <= 0) {
            System.err.println("⚠️  ADVERTENCIA: CAPACIDAD_MAXIMA es inválida (" + capacidadMaxima + "). Usando valor por defecto: 20");
            capacidadMaxima = 20;
            repository.CAPACIDAD_MAXIMA = 20;
        }
        
        System.out.println("📊 Estado actual:");
        System.out.println("   - Lugares ocupados: " + lugaresOcupados);
        System.out.println("   - Capacidad máxima: " + capacidadMaxima);
        int disponibles = capacidadMaxima - lugaresOcupados;
        System.out.println("   - Lugares disponibles: " + (disponibles >= 0 ? disponibles : 0));
        
        if (lugaresOcupados < capacidadMaxima) {
            // Agregar a múltiples estructuras de datos
            System.out.println("\n📝 Agregando vehículo a estructuras de datos...");
            
            // 1. ListaSimple (almacenamiento secuencial)
            repository.getLugaresOcupados().insertarAlFinal(nuevoCoche);
            System.out.println("   ✅ Agregado a ListaSimple. Tamaño: " + repository.getLugaresOcupados().getTamano());
            
            // 2. Árbol Binario (búsqueda rápida O(log n))
            repository.getArbolBusqueda().insertar(nuevoCoche);
            System.out.println("   ✅ Agregado a ÁrbolBinario. Tamaño: " + repository.getArbolBusqueda().getTamano());
            
            // 3. Cola Circular (rotación de espacios)
            if (repository.getEspaciosRotativos() != null && !repository.getEspaciosRotativos().colaLlena()) {
                repository.getEspaciosRotativos().insertar(nuevoCoche);
                System.out.println("   ✅ Agregado a ColaCircular. Tamaño: " + repository.getEspaciosRotativos().obtenerTamano());
            }
            
            int lugaresDisponibles = Math.max(0, capacidadMaxima - repository.getLugaresOcupados().getTamano());
            System.out.println("✅ PROCESO COMPLETADO: Coche " + placaNormalizada + " estacionado.");
            System.out.println("   Lugares disponibles: " + lugaresDisponibles);
            System.out.println("═══════════════════════════════════════════════════════════\n");
            return "Coche " + placaNormalizada + " estacionado. Lugares disponibles: " + lugaresDisponibles;
        } else {
            // Si está lleno, agregar a la Cola (FIFO)
            System.out.println("\n⚠️  Estacionamiento lleno. Agregando a Cola de espera (FIFO)...");
            int tamanoColaAntes = repository.getFilaEspera().Tamano();
            repository.getFilaEspera().Agregar(nuevoCoche);
            System.out.println("✅ Vehículo agregado a Cola. Tamaño de cola: " + tamanoColaAntes + " → " + repository.getFilaEspera().Tamano());
            
            System.out.println("✅ PROCESO COMPLETADO: Coche " + placaNormalizada + " agregado a la fila de espera.");
            System.out.println("═══════════════════════════════════════════════════════════\n");
            return "Estacionamiento lleno. Coche " + placaNormalizada + " agregado a la fila de espera.";
        }
    }

    /**
     * Lógica para registrar la salida de un coche
     * Usa ListaSimple, Pila y Cola, luego sincroniza con BD
     * Retorna un Map con información detallada de la salida
     */
    public Map<String, Object> registrarSalida(String placa) {
        Map<String, Object> resultado = new HashMap<>();
        System.out.println("\n═══════════════════════════════════════════════════════════");
        System.out.println("🚪 PROCESO: REGISTRAR SALIDA DE VEHÍCULO");
        System.out.println("═══════════════════════════════════════════════════════════");
        
        // Validar placa
        if (placa == null || placa.trim().isEmpty()) {
            System.out.println("❌ ERROR: La placa no puede estar vacía.");
            resultado.put("exito", false);
            resultado.put("mensaje", "ERROR: La placa no puede estar vacía.");
            return resultado;
        }
        
        final String placaNormalizada = placa.trim().toUpperCase();
        System.out.println("📋 Placa recibida: " + placaNormalizada);

        // 1. Buscar el coche usando Árbol Binario (búsqueda rápida O(log n))
        System.out.println("🔍 Buscando vehículo usando Árbol Binario (búsqueda O(log n))...");
        Coche cocheBusqueda = new Coche(placaNormalizada);
        Coche cocheEncontrado = repository.getArbolBusqueda().buscar(cocheBusqueda);
        
        if (cocheEncontrado != null) {
            System.out.println("✅ Vehículo encontrado en Árbol Binario: " + cocheEncontrado.getPlaca());
            // También buscar en ListaSimple para obtener la posición
            int posicion = repository.getLugaresOcupados().buscarIndice(cocheEncontrado);
            if (posicion != -1) {
                System.out.println("   - Posición en ListaSimple: " + posicion);
            }
        }

        if (cocheEncontrado == null) {
            System.out.println("❌ ERROR: El coche con placa " + placaNormalizada + " no se encuentra estacionado.");
            resultado.put("exito", false);
            resultado.put("mensaje", "ERROR: El coche con placa " + placaNormalizada + " no se encuentra estacionado.");
            return resultado;
        }

        System.out.println("📅 Hora de entrada del vehículo: " + cocheEncontrado.getHoraEntrada());

        // 2. Eliminar el coche de todas las estructuras
        System.out.println("\n🗑️  Eliminando vehículo de estructuras de datos...");
        int tamanoAntes = repository.getLugaresOcupados().getTamano();
        
        // Eliminar de ListaSimple
        boolean eliminadoLista = repository.getLugaresOcupados().eliminarPorValor(cocheEncontrado);
        System.out.println("   - ListaSimple: " + (eliminadoLista ? "✅ Eliminado" : "❌ No encontrado"));
        
        // Eliminar de Árbol Binario
        boolean eliminadoArbol = repository.getArbolBusqueda().eliminar(cocheEncontrado);
        System.out.println("   - ÁrbolBinario: " + (eliminadoArbol ? "✅ Eliminado" : "❌ No encontrado"));
        
        if (!eliminadoLista && !eliminadoArbol) {
            System.out.println("❌ ERROR: No se pudo eliminar el coche " + placaNormalizada + " de ninguna estructura");
            resultado.put("exito", false);
            resultado.put("mensaje", "ERROR: No se pudo eliminar el coche " + placaNormalizada);
            return resultado;
        }
        System.out.println("✅ Vehículo eliminado de estructuras. ListaSimple: " + tamanoAntes + " → " + repository.getLugaresOcupados().getTamano());

        // 3. Calcular tarifa usando la tarifa configurada
        System.out.println("\n💰 Calculando tarifa...");
        Date horaSalida = new Date();
        Date horaEntrada = cocheEncontrado.getHoraEntrada();
        System.out.println("   - Hora de entrada: " + horaEntrada);
        System.out.println("   - Hora de salida: " + horaSalida);
        
        long diffMs = horaSalida.getTime() - horaEntrada.getTime();
        System.out.println("   - Diferencia en milisegundos: " + diffMs);
        
        long diffMinutos = TimeUnit.MILLISECONDS.toMinutes(diffMs);
        System.out.println("   - Diferencia en minutos (antes de validación): " + diffMinutos);
        
        if (diffMinutos < 1) {
            diffMinutos = 1;
        }
        long diffHoras = diffMinutos / 60;
        long minutosRestantes = diffMinutos % 60;
        
        // Validar que tarifaPorMinuto tenga un valor válido
        double tarifaPorMinutoUsar = repository.tarifaPorMinuto;
        if (tarifaPorMinutoUsar <= 0) {
            System.err.println("   ⚠️ ADVERTENCIA: tarifaPorMinuto es " + tarifaPorMinutoUsar + ". Usando valor por defecto: $1.5");
            tarifaPorMinutoUsar = 1.5;
            System.out.println("   ⚠️  Usando valor por defecto: $1.5");
        }
        
        System.out.println("   - Tarifa por minuto configurada: $" + tarifaPorMinutoUsar);
        System.out.println("   - Tipo de tarifaPorMinuto: " + tarifaPorMinutoUsar);
        
        double tarifaPorHora = tarifaPorMinutoUsar * 60;
        double tarifa = diffMinutos * tarifaPorMinutoUsar;
        
        System.out.println("   - Tiempo de estancia: " + diffMinutos + " minutos (" + diffHoras + " horas y " + minutosRestantes + " minutos)");
        System.out.println("   - Tarifa por minuto: $" + tarifaPorMinutoUsar);
        System.out.println("   - Tarifa por hora: $" + tarifaPorHora);
        System.out.println("   - Cálculo: " + diffMinutos + " minutos × $" + tarifaPorMinutoUsar + " = $" + tarifa);
        System.out.println("   - Total a pagar: $" + tarifa);
        
        // Validar que la tarifa no sea 0 o negativa
        if (tarifa <= 0) {
            System.err.println("   ⚠️ ADVERTENCIA: La tarifa calculada es " + tarifa + ". Verificando valores...");
            System.err.println("      - diffMinutos: " + diffMinutos);
            System.err.println("      - tarifaPorMinutoUsar: " + tarifaPorMinutoUsar);
            System.err.println("      - Resultado del cálculo: " + (diffMinutos * tarifaPorMinutoUsar));
            // Forzar un valor mínimo si la tarifa es 0 o negativa
            if (tarifa <= 0) {
                tarifa = diffMinutos * tarifaPorMinutoUsar;
                if (tarifa <= 0) {
                    tarifa = tarifaPorMinutoUsar; // Al menos cobrar 1 minuto
                    System.err.println("      ⚠️  Tarifa forzada a mínimo: $" + tarifa);
                }
            }
        }

        // 4. Crear registro y guardarlo en múltiples estructuras
        System.out.println("\n📝 Creando registro de salida y agregando a estructuras...");
        RegistroEstancia registro = new RegistroEstancia(
            placaNormalizada,
            cocheEncontrado.getHoraEntrada(),
            horaSalida,
            tarifa
        );
        
        // Agregar a Pila (LIFO - último en salir primero)
        int tamanoPilaAntes = repository.getHistorialSalidas().TamanioPila();
        repository.getHistorialSalidas().Insertar(registro);
        System.out.println("   ✅ Agregado a Pila. Tamaño: " + tamanoPilaAntes + " → " + repository.getHistorialSalidas().TamanioPila());
        
        // Agregar a ListaDoble (navegación bidireccional)
        repository.getHistorialNavegable().insertarAlFinal(registro);
        System.out.println("   ✅ Agregado a ListaDoble. Tamaño: " + repository.getHistorialNavegable().getTamano());
        
        // Agregar a ArregloDinamico (estadísticas y reportes)
        repository.getEstadisticasTemporales().agregar(registro);
        System.out.println("   ✅ Agregado a ArregloDinamico. Tamaño: " + repository.getEstadisticasTemporales().getTamano());
        
        String mensajeSalida = "Coche " + placaNormalizada + " salió. Tiempo: " + diffMinutos + " min. Total a pagar: $" + tarifa;
        
        // Agregar información detallada al resultado
        resultado.put("exito", true);
        resultado.put("mensaje", mensajeSalida);
        resultado.put("placa", placaNormalizada);
        resultado.put("horaEntrada", cocheEncontrado.getHoraEntrada());
        resultado.put("horaSalida", horaSalida);
        resultado.put("tiempoMinutos", diffMinutos);
        resultado.put("tiempoHoras", diffHoras);
        resultado.put("tiempoMinutosRestantes", minutosRestantes);
        
        // Asegurar que los valores numéricos se serialicen correctamente como Double
        resultado.put("tarifaPorMinuto", Double.valueOf(tarifaPorMinutoUsar));
        resultado.put("tarifaPorHora", Double.valueOf(tarifaPorHora));
        resultado.put("tarifaTotal", Double.valueOf(tarifa));
        
        // Verificar que los valores se agregaron correctamente al Map
        System.out.println("   - Verificación de valores en Map:");
        System.out.println("      - tarifaPorMinuto en Map: " + resultado.get("tarifaPorMinuto"));
        System.out.println("      - tarifaPorHora en Map: " + resultado.get("tarifaPorHora"));
        System.out.println("      - tarifaTotal en Map: " + resultado.get("tarifaTotal"));
        
        // Log para depuración
        System.out.println("📤 Enviando respuesta al frontend:");
        System.out.println("   - Tiempo: " + diffHoras + " horas y " + minutosRestantes + " minutos (" + diffMinutos + " minutos total)");
        System.out.println("   - Tarifa por minuto: $" + tarifaPorMinutoUsar);
        System.out.println("   - Tarifa por hora: $" + tarifaPorHora);
        System.out.println("   - Tarifa total: $" + tarifa);
        System.out.println("   - Tipo de tarifaTotal: double (primitivo)");
        System.out.println("   - Valor numérico de tarifaTotal: " + tarifa);

        // 5. Mover a alguien de la Cola de espera (FIFO) usando nodos
        System.out.println("\n🔄 Verificando si hay vehículos en Cola de espera...");
        if (!repository.getFilaEspera().EstaVacia()) {
            int tamanoColaAntes = repository.getFilaEspera().Tamano();
            System.out.println("   - Vehículos en espera: " + tamanoColaAntes);
            System.out.println("   - Quitando primer vehículo de la Cola (FIFO)...");
            
            Coche cocheEnEspera = repository.getFilaEspera().Quitar();
            if (cocheEnEspera != null) {
                System.out.println("✅ Vehículo " + cocheEnEspera.getPlaca() + " quitado de la Cola.");
                // Le asignamos una nueva hora de entrada
                cocheEnEspera.setHoraEntrada(new Date());
                System.out.println("📅 Nueva hora de entrada asignada: " + cocheEnEspera.getHoraEntrada());
                
                // Agregar a múltiples estructuras
                System.out.println("📝 Agregando vehículo a estructuras de datos...");
                
                // 1. ListaSimple
                repository.getLugaresOcupados().insertarAlFinal(cocheEnEspera);
                System.out.println("   ✅ Agregado a ListaSimple. Tamaño: " + repository.getLugaresOcupados().getTamano());
                
                // 2. Árbol Binario
                repository.getArbolBusqueda().insertar(cocheEnEspera);
                System.out.println("   ✅ Agregado a ÁrbolBinario. Tamaño: " + repository.getArbolBusqueda().getTamano());
                
                // 3. Cola Circular
                if (repository.getEspaciosRotativos() != null && !repository.getEspaciosRotativos().colaLlena()) {
                    repository.getEspaciosRotativos().insertar(cocheEnEspera);
                    System.out.println("   ✅ Agregado a ColaCircular. Tamaño: " + repository.getEspaciosRotativos().obtenerTamano());
                }
                
                mensajeSalida += ". \nCoche " + cocheEnEspera.getPlaca() + " de la fila de espera ha sido estacionado.";
            }
        } else {
            System.out.println("ℹ️  No hay vehículos en la Cola de espera.");
        }

        // Actualizar el mensaje en el resultado si cambió
        resultado.put("mensaje", mensajeSalida);
        
        System.out.println("✅ PROCESO COMPLETADO: " + mensajeSalida);
        System.out.println("═══════════════════════════════════════════════════════════\n");
        return resultado;
    }

    // Métodos para mostrar (usando estructuras personalizadas)

    public void mostrarCochesActuales() {
        System.out.println("--- COCHES ACTUALMENTE ESTACIONADOS ---");
        repository.getLugaresOcupados().mostrar();
    }

    public void mostrarFilaEspera() {
        System.out.println("--- COCHES EN FILA DE ESPERA ---");
        repository.getFilaEspera().Mostrar();
    }

    public void mostrarHistorialSalidas() {
        System.out.println("--- HISTORIAL DE SALIDAS (LIFO) ---");
        repository.getHistorialSalidas().MostrarPila();
    }

    public int getLugaresOcupadosSize() {
        try {
            verificarInicializacion();
            return repository.getLugaresOcupados().getTamano();
        } catch (Exception e) {
            System.err.println("❌ ERROR en getLugaresOcupadosSize: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    public int getCapacidadMaxima() {
        try {
            verificarInicializacion();
            int capacidad = repository.CAPACIDAD_MAXIMA;
            // Validar que la capacidad sea válida
            if (capacidad <= 0) {
                System.err.println("⚠️  ADVERTENCIA: CAPACIDAD_MAXIMA es inválida (" + capacidad + "). Usando valor por defecto: 20");
                capacidad = 20;
                repository.CAPACIDAD_MAXIMA = 20;
            }
            return capacidad;
        } catch (Exception e) {
            System.err.println("❌ ERROR en getCapacidadMaxima: " + e.getMessage());
            return 20; // Valor por defecto
        }
    }

    public int getLugaresDisponibles() {
        try {
            verificarInicializacion();
            int capacidadMaxima = repository.CAPACIDAD_MAXIMA;
            int lugaresOcupados = repository.getLugaresOcupados().getTamano();
            
            // Validar que la capacidad máxima sea válida
            if (capacidadMaxima <= 0) {
                System.err.println("⚠️  ADVERTENCIA: CAPACIDAD_MAXIMA es inválida (" + capacidadMaxima + "). Usando valor por defecto: 20");
                capacidadMaxima = 20;
                repository.CAPACIDAD_MAXIMA = 20;
            }
            
            int disponibles = capacidadMaxima - lugaresOcupados;
            
            // Asegurar que nunca sea negativo
            if (disponibles < 0) {
                System.err.println("⚠️  ADVERTENCIA: Lugares disponibles negativo (" + disponibles + "). Capacidad: " + capacidadMaxima + ", Ocupados: " + lugaresOcupados);
                return 0; // Retornar 0 en lugar de negativo
            }
            
            return disponibles;
        } catch (Exception e) {
            System.err.println("❌ ERROR en getLugaresDisponibles: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    public int getFilaEsperaSize() {
        try {
            verificarInicializacion();
            return repository.getFilaEspera().Tamano();
        } catch (Exception e) {
            System.err.println("❌ ERROR en getFilaEsperaSize: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Obtiene la lista de vehículos en la fila de espera (usando nodos de Cola)
     */
    public List<Coche> getVehiculosEnEspera() {
        List<Coche> lista = new ArrayList<>();
        if (repository == null || repository.getFilaEspera() == null) {
            System.err.println("⚠️  ADVERTENCIA: repository o filaEspera es null");
            return lista;
        }
        if (repository.getFilaEspera().EstaVacia()) {
            return lista;
        }
        NodoCola<Coche> actual = repository.getFilaEspera().getInicio();
        while (actual != null) {
            lista.add(actual.getDato());
            actual = actual.getSiguiente();
        }
        return lista;
    }

    /**
     * Obtiene la lista de coches actuales (usando nodos de ListaSimple)
     */
    public List<Coche> getCochesActuales() {
        List<Coche> lista = new ArrayList<>();
        if (repository == null || repository.getLugaresOcupados() == null) {
            System.err.println("⚠️  ADVERTENCIA: repository o lugaresOcupados es null");
            return lista;
        }
        NodoListaSimple<Coche> actual = repository.getLugaresOcupados().getHead();
        while (actual != null) {
            lista.add(actual.getDato());
            actual = actual.getEnlace();
        }
        return lista;
    }

    /**
     * Obtiene información de un vehículo para calcular tarifa antes de procesar salida
     * Usa nodos de ListaSimple para buscar
     */
    public Map<String, Object> obtenerInfoVehiculo(String placa) {
        System.out.println("\n═══════════════════════════════════════════════════════════");
        System.out.println("🔍 PROCESO: OBTENER INFORMACIÓN DE VEHÍCULO");
        System.out.println("═══════════════════════════════════════════════════════════");
        
        Map<String, Object> info = new HashMap<>();
        
        // Validar placa
        if (placa == null || placa.trim().isEmpty()) {
            System.out.println("❌ ERROR: La placa no puede estar vacía.");
            info.put("encontrado", false);
            info.put("mensaje", "La placa no puede estar vacía.");
            return info;
        }
        
        final String placaNormalizada = placa.trim().toUpperCase();
        System.out.println("📋 Placa recibida: " + placaNormalizada);
        Coche cocheBusqueda = new Coche(placaNormalizada);
        
        // Verificar inicialización
        verificarInicializacion();
        
        // Verificar inicialización
        verificarInicializacion();
        
        // Buscar el coche usando Árbol Binario (búsqueda rápida O(log n))
        System.out.println("🔍 Buscando vehículo usando Árbol Binario (búsqueda O(log n))...");
        Coche cocheEncontrado = repository.getArbolBusqueda().buscar(cocheBusqueda);
        
        if (cocheEncontrado != null) {
            System.out.println("✅ Vehículo encontrado en Árbol Binario: " + cocheEncontrado.getPlaca());
            // También obtener posición en ListaSimple para referencia
            int posicion = repository.getLugaresOcupados().buscarIndice(cocheEncontrado);
            if (posicion != -1) {
                System.out.println("   - Posición en ListaSimple: " + posicion);
            }
        }

        if (cocheEncontrado == null) {
            System.out.println("❌ ERROR: El vehículo con placa " + placaNormalizada + " no se encuentra estacionado.");
            info.put("encontrado", false);
            info.put("mensaje", "El vehículo con placa " + placaNormalizada + " no se encuentra estacionado.");
            return info;
        }

        // Calcular tiempo de estancia y tarifa
        System.out.println("💰 Calculando tiempo de estancia y tarifa...");
        Date horaActual = new Date();
        long diffMs = horaActual.getTime() - cocheEncontrado.getHoraEntrada().getTime();
        long diffMinutos = TimeUnit.MILLISECONDS.toMinutes(diffMs);
        if (diffMinutos < 0) {
            diffMinutos = 0;
        }
        long diffHoras = diffMinutos / 60;
        long minutosRestantes = diffMinutos % 60;
        
        // Tarifa: usar la tarifa configurada
        double tarifaPorHora = repository.tarifaPorMinuto * 60;
        double tarifaTotal = diffMinutos * repository.tarifaPorMinuto;
        
        System.out.println("   - Hora de entrada: " + cocheEncontrado.getHoraEntrada());
        System.out.println("   - Hora actual: " + horaActual);
        System.out.println("   - Tiempo de estancia: " + diffHoras + " horas y " + minutosRestantes + " minutos (" + diffMinutos + " minutos total)");
        System.out.println("   - Tarifa por hora: $" + tarifaPorHora);
        System.out.println("   - Tarifa total: $" + tarifaTotal);
        
        info.put("encontrado", true);
        info.put("placa", cocheEncontrado.getPlaca());
        info.put("horaEntrada", cocheEncontrado.getHoraEntrada());
        info.put("tiempoMinutos", diffMinutos);
        info.put("tiempoHoras", diffHoras);
        info.put("tiempoMinutosRestantes", minutosRestantes);
        info.put("tarifaPorHora", tarifaPorHora);
        info.put("tarifaTotal", tarifaTotal);
        
        System.out.println("✅ PROCESO COMPLETADO: Información obtenida correctamente.");
        System.out.println("═══════════════════════════════════════════════════════════\n");
        return info;
    }

    /**
     * Obtiene el historial completo de salidas (LIFO - último en salir primero)
     * Usa nodos de Pila
     */
    public List<RegistroEstancia> getHistorialSalidas() {
        List<RegistroEstancia> lista = new ArrayList<>();
        if (repository == null || repository.getHistorialSalidas() == null) {
            System.err.println("⚠️  ADVERTENCIA: repository o historialSalidas es null");
            return lista;
        }
        if (repository.getHistorialSalidas().PilaVacia()) {
            return lista;
        }
        NodoPila<RegistroEstancia> actual = repository.getHistorialSalidas().getCima();
        while (actual != null) {
            lista.add(actual.getValor());
            actual = actual.getSiguiente();
        }
        return lista;
    }

    /**
     * Limpia el historial de salidas
     * Limpia todas las estructuras de historial y sincroniza con BD
     */
    public void limpiarHistorial() {
        System.out.println("\n═══════════════════════════════════════════════════════════");
        System.out.println("🗑️  PROCESO: LIMPIAR HISTORIAL DE SALIDAS");
        System.out.println("═══════════════════════════════════════════════════════════");
        
        int tamanoPilaAntes = repository.getHistorialSalidas().TamanioPila();
        int tamanoListaDobleAntes = repository.getHistorialNavegable().getTamano();
        int tamanoArregloAntes = repository.getEstadisticasTemporales().getTamano();
        
        System.out.println("📊 Tamaños antes de limpiar:");
        System.out.println("   - Pila: " + tamanoPilaAntes);
        System.out.println("   - ListaDoble: " + tamanoListaDobleAntes);
        System.out.println("   - ArregloDinamico: " + tamanoArregloAntes);
        
        System.out.println("\n🗑️  Limpiando estructuras de historial...");
        repository.getHistorialSalidas().LimpiarPila();
        System.out.println("   ✅ Pila limpiada.");
        repository.getHistorialNavegable().limpiar();
        System.out.println("   ✅ ListaDoble limpiada.");
        repository.getEstadisticasTemporales().limpiar();
        System.out.println("   ✅ ArregloDinamico limpiado.");
        
        System.out.println("✅ PROCESO COMPLETADO: Todas las estructuras de historial limpiadas correctamente.");
        System.out.println("═══════════════════════════════════════════════════════════\n");
    }

    /**
     * Obtiene la configuración actual de tarifas
     */
    public Map<String, Object> obtenerTarifas() {
        Map<String, Object> tarifas = new HashMap<>();
        tarifas.put("tarifaPorMinuto", repository.tarifaPorMinuto);
        tarifas.put("tarifaPorHora", repository.tarifaPorMinuto * 60);
        tarifas.put("tarifa1Minuto", repository.tarifa1Minuto);
        tarifas.put("tarifa0_1Hora", repository.tarifa0_1Hora);
        tarifas.put("tarifa1_2Horas", repository.tarifa1_2Horas);
        tarifas.put("tarifa2MasHoras", repository.tarifa2MasHoras);
        tarifas.put("tarifaMaximaDiaria", repository.tarifaMaximaDiaria);
        tarifas.put("tarifaMaximaSemanal", repository.tarifaMaximaSemanal);
        tarifas.put("tarifaTicketPerdido", repository.tarifaTicketPerdido);
        return tarifas;
    }

    /**
     * Actualiza la configuración de tarifas
     * Actualiza en memoria y sincroniza con BD
     */
    public Map<String, Object> actualizarTarifas(Map<String, Object> nuevasTarifas) {
        System.out.println("\n═══════════════════════════════════════════════════════════");
        System.out.println("💰 PROCESO: ACTUALIZAR TARIFAS");
        System.out.println("═══════════════════════════════════════════════════════════");
        
        System.out.println("📋 Tarifas recibidas para actualizar:");
        nuevasTarifas.forEach((key, value) -> System.out.println("   - " + key + ": " + value));
        
        System.out.println("\n📝 Actualizando tarifas en memoria...");
        if (nuevasTarifas.containsKey("tarifaPorMinuto")) {
            repository.tarifaPorMinuto = ((Number) nuevasTarifas.get("tarifaPorMinuto")).doubleValue();
            System.out.println("   ✅ tarifaPorMinuto: $" + repository.tarifaPorMinuto);
        }
        if (nuevasTarifas.containsKey("tarifa1Minuto")) {
            repository.tarifa1Minuto = ((Number) nuevasTarifas.get("tarifa1Minuto")).doubleValue();
            System.out.println("   ✅ tarifa1Minuto: $" + repository.tarifa1Minuto);
        }
        if (nuevasTarifas.containsKey("tarifa0_1Hora")) {
            repository.tarifa0_1Hora = ((Number) nuevasTarifas.get("tarifa0_1Hora")).doubleValue();
            System.out.println("   ✅ tarifa0_1Hora: $" + repository.tarifa0_1Hora);
        }
        if (nuevasTarifas.containsKey("tarifa1_2Horas")) {
            repository.tarifa1_2Horas = ((Number) nuevasTarifas.get("tarifa1_2Horas")).doubleValue();
            System.out.println("   ✅ tarifa1_2Horas: $" + repository.tarifa1_2Horas);
        }
        if (nuevasTarifas.containsKey("tarifa2MasHoras")) {
            repository.tarifa2MasHoras = ((Number) nuevasTarifas.get("tarifa2MasHoras")).doubleValue();
            System.out.println("   ✅ tarifa2MasHoras: $" + repository.tarifa2MasHoras);
        }
        if (nuevasTarifas.containsKey("tarifaMaximaDiaria")) {
            repository.tarifaMaximaDiaria = ((Number) nuevasTarifas.get("tarifaMaximaDiaria")).doubleValue();
            System.out.println("   ✅ tarifaMaximaDiaria: $" + repository.tarifaMaximaDiaria);
        }
        if (nuevasTarifas.containsKey("tarifaMaximaSemanal")) {
            repository.tarifaMaximaSemanal = ((Number) nuevasTarifas.get("tarifaMaximaSemanal")).doubleValue();
            System.out.println("   ✅ tarifaMaximaSemanal: $" + repository.tarifaMaximaSemanal);
        }
        if (nuevasTarifas.containsKey("tarifaTicketPerdido")) {
            repository.tarifaTicketPerdido = ((Number) nuevasTarifas.get("tarifaTicketPerdido")).doubleValue();
            System.out.println("   ✅ tarifaTicketPerdido: $" + repository.tarifaTicketPerdido);
        }
        
        System.out.println("✅ PROCESO COMPLETADO: Tarifas actualizadas correctamente.");
        System.out.println("═══════════════════════════════════════════════════════════\n");
        
        return obtenerTarifas();
    }

}
