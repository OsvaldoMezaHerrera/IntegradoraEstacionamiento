package mx.edu.utez.Estacionamiento.service;

import mx.edu.utez.Estacionamiento.model.Coche;
import mx.edu.utez.Estacionamiento.model.RegistroEstancia;
import mx.edu.utez.Estacionamiento.repository.EstacionamientoRepository;
import mx.edu.utez.Estacionamiento.structures.NodoListaSimple;
import mx.edu.utez.Estacionamiento.structures.NodoCola;
import mx.edu.utez.Estacionamiento.structures.NodoPila;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@Service
public class EstacionamientoService {

    @Autowired
    private EstacionamientoRepository repository;

    /**
     * Lógica para registrar la entrada de un coche
     * Usa ListaSimple y Cola, luego sincroniza con BD
     */
    @Transactional
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
        
        // Verificar si ya está estacionado (usando ListaSimple)
        System.out.println("🔍 Verificando si el vehículo ya está estacionado...");
        Coche cocheBusqueda = new Coche(placaNormalizada);
        int indice = repository.lugaresOcupados.buscarIndice(cocheBusqueda);
        if (indice != -1) {
            System.out.println("❌ ERROR: El coche con placa " + placaNormalizada + " ya está dentro (índice: " + indice + ").");
            return "ERROR: El coche con placa " + placaNormalizada + " ya está dentro.";
        }
        System.out.println("✅ Vehículo no encontrado en estacionamiento. Procediendo...");

        Coche nuevoCoche = new Coche(placaNormalizada, new Date());
        System.out.println("📅 Hora de entrada: " + nuevoCoche.getHoraEntrada());

        // Comprobamos si hay lugares limitados (usando ListaSimple)
        int lugaresOcupados = repository.lugaresOcupados.getTamano();
        int capacidadMaxima = repository.CAPACIDAD_MAXIMA;
        System.out.println("📊 Estado actual:");
        System.out.println("   - Lugares ocupados: " + lugaresOcupados);
        System.out.println("   - Capacidad máxima: " + capacidadMaxima);
        System.out.println("   - Lugares disponibles: " + (capacidadMaxima - lugaresOcupados));
        
        if (lugaresOcupados < capacidadMaxima) {
            // Agregar a ListaSimple
            System.out.println("\n📝 Agregando vehículo a ListaSimple (lugares ocupados)...");
            repository.lugaresOcupados.insertarAlFinal(nuevoCoche);
            System.out.println("✅ Vehículo agregado a ListaSimple. Tamaño actual: " + repository.lugaresOcupados.getTamano());
            
            // Sincronizar con BD
            System.out.println("💾 Sincronizando con base de datos...");
            repository.sincronizarVehiculoEstacionado(nuevoCoche, true);
            System.out.println("✅ Sincronización completada.");
            
            int lugaresDisponibles = capacidadMaxima - repository.lugaresOcupados.getTamano();
            System.out.println("✅ PROCESO COMPLETADO: Coche " + placaNormalizada + " estacionado.");
            System.out.println("   Lugares disponibles: " + lugaresDisponibles);
            System.out.println("═══════════════════════════════════════════════════════════\n");
            return "Coche " + placaNormalizada + " estacionado. Lugares disponibles: " + lugaresDisponibles;
        } else {
            // Si está lleno, agregar a la Cola (FIFO)
            System.out.println("\n⚠️  Estacionamiento lleno. Agregando a Cola de espera (FIFO)...");
            int tamanoColaAntes = repository.filaEspera.Tamano();
            repository.filaEspera.Agregar(nuevoCoche);
            System.out.println("✅ Vehículo agregado a Cola. Tamaño de cola: " + tamanoColaAntes + " → " + repository.filaEspera.Tamano());
            
            // Sincronizar cola completa con BD
            System.out.println("💾 Sincronizando cola completa con base de datos...");
            repository.sincronizarFilaEspera();
            System.out.println("✅ Sincronización completada.");
            System.out.println("✅ PROCESO COMPLETADO: Coche " + placaNormalizada + " agregado a la fila de espera.");
            System.out.println("═══════════════════════════════════════════════════════════\n");
            return "Estacionamiento lleno. Coche " + placaNormalizada + " agregado a la fila de espera.";
        }
    }

    /**
     * Lógica para registrar la salida de un coche
     * Usa ListaSimple, Pila y Cola, luego sincroniza con BD
     */
    @Transactional
    public String registrarSalida(String placa) {
        System.out.println("\n═══════════════════════════════════════════════════════════");
        System.out.println("🚪 PROCESO: REGISTRAR SALIDA DE VEHÍCULO");
        System.out.println("═══════════════════════════════════════════════════════════");
        
        // Validar placa
        if (placa == null || placa.trim().isEmpty()) {
            System.out.println("❌ ERROR: La placa no puede estar vacía.");
            return "ERROR: La placa no puede estar vacía.";
        }
        
        final String placaNormalizada = placa.trim().toUpperCase();
        System.out.println("📋 Placa recibida: " + placaNormalizada);

        // 1. Buscar el coche en la ListaSimple (usando nodos)
        System.out.println("🔍 Buscando vehículo en ListaSimple (recorriendo nodos)...");
        Coche cocheBusqueda = new Coche(placaNormalizada);
        Coche cocheEncontrado = null;
        NodoListaSimple<Coche> actual = repository.lugaresOcupados.getHead();
        int posicion = 0;
        while (actual != null) {
            if (actual.getDato().equals(cocheBusqueda)) {
                cocheEncontrado = actual.getDato();
                System.out.println("✅ Vehículo encontrado en posición " + posicion + " de la ListaSimple");
                break;
            }
            actual = actual.getEnlace();
            posicion++;
        }

        if (cocheEncontrado == null) {
            System.out.println("❌ ERROR: El coche con placa " + placaNormalizada + " no se encuentra estacionado.");
            return "ERROR: El coche con placa " + placaNormalizada + " no se encuentra estacionado.";
        }

        System.out.println("📅 Hora de entrada del vehículo: " + cocheEncontrado.getHoraEntrada());

        // 2. Eliminar el coche de la ListaSimple
        System.out.println("\n🗑️  Eliminando vehículo de ListaSimple...");
        int tamanoAntes = repository.lugaresOcupados.getTamano();
        boolean eliminado = repository.lugaresOcupados.eliminarPorValor(cocheEncontrado);
        if (!eliminado) {
            System.out.println("❌ ERROR: No se pudo eliminar el coche " + placaNormalizada);
            return "ERROR: No se pudo eliminar el coche " + placaNormalizada;
        }
        System.out.println("✅ Vehículo eliminado de ListaSimple. Tamaño: " + tamanoAntes + " → " + repository.lugaresOcupados.getTamano());

        // 3. Calcular tarifa usando la tarifa configurada
        System.out.println("\n💰 Calculando tarifa...");
        Date horaSalida = new Date();
        long diffMs = horaSalida.getTime() - cocheEncontrado.getHoraEntrada().getTime();
        long diffMinutos = TimeUnit.MILLISECONDS.toMinutes(diffMs);
        if (diffMinutos < 1) {
            diffMinutos = 1;
        }
        double tarifa = diffMinutos * repository.tarifaPorMinuto;
        System.out.println("   - Tiempo de estancia: " + diffMinutos + " minutos");
        System.out.println("   - Tarifa por minuto: $" + repository.tarifaPorMinuto);
        System.out.println("   - Total a pagar: $" + tarifa);

        // 4. Crear registro y guardarlo en la Pila (LIFO)
        System.out.println("\n📝 Creando registro de salida y agregando a Pila (LIFO)...");
        RegistroEstancia registro = new RegistroEstancia(
            placaNormalizada,
            cocheEncontrado.getHoraEntrada(),
            horaSalida,
            tarifa
        );
        int tamanoPilaAntes = repository.historialSalidas.TamanioPila();
        repository.historialSalidas.Insertar(registro);
        System.out.println("✅ Registro agregado a Pila. Tamaño de pila: " + tamanoPilaAntes + " → " + repository.historialSalidas.TamanioPila());
        
        // Sincronizar con BD
        System.out.println("💾 Sincronizando con base de datos...");
        repository.sincronizarVehiculoEstacionado(cocheEncontrado, false);
        repository.sincronizarHistorial(registro);
        System.out.println("✅ Sincronización completada.");

        String mensajeSalida = "Coche " + placaNormalizada + " salió. Tiempo: " + diffMinutos + " min. Total a pagar: $" + tarifa;

        // 5. Mover a alguien de la Cola de espera (FIFO) usando nodos
        System.out.println("\n🔄 Verificando si hay vehículos en Cola de espera...");
        if (!repository.filaEspera.EstaVacia()) {
            int tamanoColaAntes = repository.filaEspera.Tamano();
            System.out.println("   - Vehículos en espera: " + tamanoColaAntes);
            System.out.println("   - Quitando primer vehículo de la Cola (FIFO)...");
            
            Coche cocheEnEspera = repository.filaEspera.Quitar();
            if (cocheEnEspera != null) {
                System.out.println("✅ Vehículo " + cocheEnEspera.getPlaca() + " quitado de la Cola.");
                // Le asignamos una nueva hora de entrada
                cocheEnEspera.setHoraEntrada(new Date());
                System.out.println("📅 Nueva hora de entrada asignada: " + cocheEnEspera.getHoraEntrada());
                
                // Agregar a ListaSimple
                System.out.println("📝 Agregando vehículo a ListaSimple...");
                repository.lugaresOcupados.insertarAlFinal(cocheEnEspera);
                System.out.println("✅ Vehículo agregado a ListaSimple. Tamaño actual: " + repository.lugaresOcupados.getTamano());
                
                // Sincronizar con BD
                System.out.println("💾 Sincronizando con base de datos...");
                repository.sincronizarVehiculoEstacionado(cocheEnEspera, true);
                repository.sincronizarFilaEspera();
                System.out.println("✅ Sincronización completada.");
                
                mensajeSalida += ". \nCoche " + cocheEnEspera.getPlaca() + " de la fila de espera ha sido estacionado.";
            }
        } else {
            System.out.println("ℹ️  No hay vehículos en la Cola de espera.");
        }

        System.out.println("✅ PROCESO COMPLETADO: " + mensajeSalida);
        System.out.println("═══════════════════════════════════════════════════════════\n");
        return mensajeSalida;
    }

    // Métodos para mostrar (usando estructuras personalizadas)

    public void mostrarCochesActuales() {
        System.out.println("--- COCHES ACTUALMENTE ESTACIONADOS ---");
        repository.lugaresOcupados.mostrar();
    }

    public void mostrarFilaEspera() {
        System.out.println("--- COCHES EN FILA DE ESPERA ---");
        repository.filaEspera.Mostrar();
    }

    public void mostrarHistorialSalidas() {
        System.out.println("--- HISTORIAL DE SALIDAS (LIFO) ---");
        repository.historialSalidas.MostrarPila();
    }

    public int getLugaresOcupadosSize() {
        return repository.lugaresOcupados.getTamano();
    }

    public int getCapacidadMaxima() {
        return repository.CAPACIDAD_MAXIMA;
    }

    public int getLugaresDisponibles() {
        return repository.CAPACIDAD_MAXIMA - repository.lugaresOcupados.getTamano();
    }

    public int getFilaEsperaSize() {
        return repository.filaEspera.Tamano();
    }

    /**
     * Obtiene la lista de vehículos en la fila de espera (usando nodos de Cola)
     */
    public List<Coche> getVehiculosEnEspera() {
        List<Coche> lista = new ArrayList<>();
        if (repository.filaEspera.EstaVacia()) {
            return lista;
        }
        NodoCola<Coche> actual = repository.filaEspera.getInicio();
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
        NodoListaSimple<Coche> actual = repository.lugaresOcupados.getHead();
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
        
        // Buscar el coche en la ListaSimple usando nodos
        System.out.println("🔍 Buscando vehículo en ListaSimple (recorriendo nodos)...");
        Coche cocheEncontrado = null;
        NodoListaSimple<Coche> actual = repository.lugaresOcupados.getHead();
        int posicion = 0;
        while (actual != null) {
            if (actual.getDato().equals(cocheBusqueda)) {
                cocheEncontrado = actual.getDato();
                System.out.println("✅ Vehículo encontrado en posición " + posicion + " de la ListaSimple");
                break;
            }
            actual = actual.getEnlace();
            posicion++;
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
        if (repository.historialSalidas.PilaVacia()) {
            return lista;
        }
        NodoPila<RegistroEstancia> actual = repository.historialSalidas.getCima();
        while (actual != null) {
            lista.add(actual.getValor());
            actual = actual.getSiguiente();
        }
        return lista;
    }

    /**
     * Limpia el historial de salidas
     * Limpia la Pila y sincroniza con BD
     */
    @Transactional
    public void limpiarHistorial() {
        System.out.println("\n═══════════════════════════════════════════════════════════");
        System.out.println("🗑️  PROCESO: LIMPIAR HISTORIAL DE SALIDAS");
        System.out.println("═══════════════════════════════════════════════════════════");
        
        int tamanoPilaAntes = repository.historialSalidas.TamanioPila();
        System.out.println("📊 Tamaño de Pila antes de limpiar: " + tamanoPilaAntes);
        
        System.out.println("🗑️  Limpiando Pila...");
        repository.historialSalidas.LimpiarPila();
        System.out.println("✅ Pila limpiada.");
        
        System.out.println("💾 Limpiando historial en base de datos...");
        repository.limpiarHistorialBD();
        System.out.println("✅ Historial de BD limpiado.");
        
        System.out.println("✅ PROCESO COMPLETADO: Historial limpiado correctamente.");
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
    @Transactional
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
        
        // Sincronizar con BD
        System.out.println("\n💾 Sincronizando tarifas con base de datos...");
        repository.actualizarTarifasEnBD();
        System.out.println("✅ Sincronización completada.");
        
        System.out.println("✅ PROCESO COMPLETADO: Tarifas actualizadas correctamente.");
        System.out.println("═══════════════════════════════════════════════════════════\n");
        
        return obtenerTarifas();
    }

}
