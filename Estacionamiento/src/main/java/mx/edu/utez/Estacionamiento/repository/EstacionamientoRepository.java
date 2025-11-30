package mx.edu.utez.Estacionamiento.repository;

import mx.edu.utez.Estacionamiento.entity.*;
import mx.edu.utez.Estacionamiento.model.Coche;
import mx.edu.utez.Estacionamiento.model.RegistroEstancia;
import mx.edu.utez.Estacionamiento.structures.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * Repositorio que usa estructuras de datos personalizadas (Lista, Cola, Pila)
 * y sincroniza con la base de datos MySQL
 */
@Repository
public class EstacionamientoRepository {

    // Estructuras de datos personalizadas
    public final ListaSimple<Coche> lugaresOcupados = new ListaSimple<>();
    public final Cola<Coche> filaEspera = new Cola<>();
    public final Pila<RegistroEstancia> historialSalidas = new Pila<>();

    // Capacidad máxima del estacionamiento
    public final int CAPACIDAD_MAXIMA = 20;

    // Repositorios JPA para sincronización con BD
    @Autowired
    private VehiculoEstacionadoRepository vehiculoEstacionadoRepository;
    
    @Autowired
    private VehiculoEnEsperaRepository vehiculoEnEsperaRepository;
    
    @Autowired
    private HistorialSalidaRepository historialSalidaRepository;
    
    @Autowired
    private ConfiguracionEstacionamientoRepository configuracionRepository;
    
    @Autowired
    private TarifaRepository tarifaRepository;

    // Configuración de tarifas (se carga desde BD)
    public double tarifaPorMinuto = 1.5;
    public double tarifa1Minuto = 1.5;
    public double tarifa0_1Hora = 5.0;
    public double tarifa1_2Horas = 10.0;
    public double tarifa2MasHoras = 15.0;
    public double tarifaMaximaDiaria = 0.0;
    public double tarifaMaximaSemanal = 0.0;
    public double tarifaTicketPerdido = 0.0;

    /**
     * Carga los datos desde la base de datos a las estructuras en memoria
     * Se ejecuta al iniciar la aplicación
     */
    @Transactional
    public void cargarDesdeBaseDatos() {
        System.out.println("\n═══════════════════════════════════════════════════════════");
        System.out.println("📥 PROCESO: CARGAR DATOS DESDE BASE DE DATOS");
        System.out.println("═══════════════════════════════════════════════════════════");
        
        // Limpiar estructuras
        System.out.println("🗑️  Limpiando estructuras en memoria...");
        lugaresOcupados.limpiar();
        filaEspera.BorrarCola();
        historialSalidas.LimpiarPila();
        System.out.println("✅ Estructuras limpiadas.");

        // Cargar vehículos estacionados desde BD a ListaSimple
        System.out.println("\n📋 Cargando vehículos estacionados desde BD a ListaSimple...");
        List<VehiculoEstacionado> vehiculosBD = vehiculoEstacionadoRepository.findAll();
        System.out.println("   - Vehículos encontrados en BD: " + vehiculosBD.size());
        for (VehiculoEstacionado v : vehiculosBD) {
            Coche coche = new Coche(v.getPlaca(), convertToDate(v.getHoraEntrada()));
            lugaresOcupados.insertarAlFinal(coche);
            System.out.println("   ✅ Vehículo " + v.getPlaca() + " agregado a ListaSimple");
        }
        System.out.println("✅ ListaSimple cargada. Tamaño: " + lugaresOcupados.getTamano());

        // Cargar fila de espera desde BD a Cola (ordenados por posición)
        System.out.println("\n📋 Cargando fila de espera desde BD a Cola...");
        List<VehiculoEnEspera> esperaBD = vehiculoEnEsperaRepository.findAllOrderByPosicion();
        System.out.println("   - Vehículos en espera encontrados en BD: " + esperaBD.size());
        for (VehiculoEnEspera v : esperaBD) {
            Coche coche = new Coche(v.getPlaca(), convertToDate(v.getHoraEntrada()));
            filaEspera.Agregar(coche);
            System.out.println("   ✅ Vehículo " + v.getPlaca() + " (posición " + v.getPosicion() + ") agregado a Cola");
        }
        System.out.println("✅ Cola cargada. Tamaño: " + filaEspera.Tamano());

        // Cargar historial desde BD a Pila (orden inverso para mantener LIFO)
        System.out.println("\n📋 Cargando historial desde BD a Pila (LIFO)...");
        List<HistorialSalida> historialBD = historialSalidaRepository.findAllOrderByFechaCreacionDesc();
        System.out.println("   - Registros de historial encontrados en BD: " + historialBD.size());
        for (HistorialSalida h : historialBD) {
            RegistroEstancia registro = new RegistroEstancia(
                h.getPlaca(),
                convertToDate(h.getHoraEntrada()),
                convertToDate(h.getHoraSalida()),
                h.getTarifaPagada().doubleValue()
            );
            historialSalidas.Insertar(registro);
            System.out.println("   ✅ Registro de " + h.getPlaca() + " agregado a Pila");
        }
        System.out.println("✅ Pila cargada. Tamaño: " + historialSalidas.TamanioPila());

        // Cargar configuración de tarifas
        System.out.println("\n💰 Cargando configuración de tarifas...");
        cargarTarifas();
        System.out.println("   ✅ Tarifa por minuto: $" + tarifaPorMinuto);
        System.out.println("   ✅ Tarifa 1 minuto: $" + tarifa1Minuto);
        System.out.println("   ✅ Tarifa 0-1 hora: $" + tarifa0_1Hora);
        System.out.println("   ✅ Tarifa 1-2 horas: $" + tarifa1_2Horas);
        System.out.println("   ✅ Tarifa 2+ horas: $" + tarifa2MasHoras);
        
        System.out.println("\n✅ PROCESO COMPLETADO: Datos cargados desde BD correctamente.");
        System.out.println("═══════════════════════════════════════════════════════════\n");
    }

    /**
     * Sincroniza un vehículo estacionado desde la estructura a la BD
     */
    @Transactional
    public void sincronizarVehiculoEstacionado(Coche coche, boolean agregar) {
        if (agregar) {
            System.out.println("   💾 Guardando vehículo " + coche.getPlaca() + " en BD...");
            VehiculoEstacionado vehiculo = new VehiculoEstacionado(
                coche.getPlaca(),
                convertToLocalDateTime(coche.getHoraEntrada())
            );
            vehiculoEstacionadoRepository.save(vehiculo);
            System.out.println("   ✅ Vehículo guardado en BD.");
        } else {
            System.out.println("   💾 Eliminando vehículo " + coche.getPlaca() + " de BD...");
            vehiculoEstacionadoRepository.findByPlaca(coche.getPlaca())
                .ifPresent(v -> {
                    vehiculoEstacionadoRepository.delete(v);
                    System.out.println("   ✅ Vehículo eliminado de BD.");
                });
        }
    }

    /**
     * Sincroniza la fila de espera completa con la BD
     */
    @Transactional
    public void sincronizarFilaEspera() {
        System.out.println("   💾 Sincronizando Cola completa con BD...");
        // Eliminar todos los registros de espera en BD
        vehiculoEnEsperaRepository.deleteAll();
        System.out.println("   🗑️  Registros anteriores eliminados de BD.");
        
        // Insertar todos los vehículos de la cola en BD
        NodoCola<Coche> actual = filaEspera.getInicio();
        int posicion = 1;
        int contador = 0;
        while (actual != null) {
            VehiculoEnEspera vehiculo = new VehiculoEnEspera(
                actual.getDato().getPlaca(),
                convertToLocalDateTime(actual.getDato().getHoraEntrada()),
                posicion
            );
            vehiculoEnEsperaRepository.save(vehiculo);
            System.out.println("   ✅ Vehículo " + actual.getDato().getPlaca() + " (posición " + posicion + ") guardado en BD");
            actual = actual.getSiguiente();
            posicion++;
            contador++;
        }
        System.out.println("   ✅ Cola sincronizada. Total de vehículos guardados: " + contador);
    }

    /**
     * Sincroniza un registro de historial desde la estructura a la BD
     */
    @Transactional
    public void sincronizarHistorial(RegistroEstancia registro) {
        System.out.println("   💾 Guardando registro de historial en BD...");
        HistorialSalida historial = new HistorialSalida(
            registro.getPlaca(),
            convertToLocalDateTime(registro.getHoraEntrada()),
            convertToLocalDateTime(registro.getHoraSalida()),
            java.math.BigDecimal.valueOf(registro.getTarifaPagada()),
            null
        );
        historialSalidaRepository.save(historial);
        System.out.println("   ✅ Registro de " + registro.getPlaca() + " guardado en BD (Tarifa: $" + registro.getTarifaPagada() + ")");
    }

    /**
     * Limpia el historial en la base de datos
     */
    @Transactional
    public void limpiarHistorialBD() {
        long cantidadAntes = historialSalidaRepository.count();
        System.out.println("   💾 Eliminando " + cantidadAntes + " registros de historial en BD...");
        historialSalidaRepository.deleteAll();
        System.out.println("   ✅ Historial de BD limpiado.");
    }

    /**
     * Carga las tarifas desde la BD
     */
    private void cargarTarifas() {
        tarifaRepository.findFirstByOrderByIdDesc().ifPresent(tarifa -> {
            tarifaPorMinuto = tarifa.getTarifaPorMinuto().doubleValue();
            tarifa1Minuto = tarifa.getTarifa1Minuto() != null ? tarifa.getTarifa1Minuto().doubleValue() : 1.5;
            tarifa0_1Hora = tarifa.getTarifa0_1Hora().doubleValue();
            tarifa1_2Horas = tarifa.getTarifa1_2Horas().doubleValue();
            tarifa2MasHoras = tarifa.getTarifa2MasHoras().doubleValue();
            tarifaMaximaDiaria = tarifa.getTarifaMaximaDiaria().doubleValue();
            tarifaMaximaSemanal = tarifa.getTarifaMaximaSemanal().doubleValue();
            tarifaTicketPerdido = tarifa.getTarifaTicketPerdido().doubleValue();
        });
    }

    /**
     * Actualiza las tarifas en la BD
     */
    @Transactional
    public void actualizarTarifasEnBD() {
        System.out.println("   💾 Obteniendo registro de tarifas de BD...");
        Tarifa tarifa = tarifaRepository.findFirstByOrderByIdDesc().orElse(new Tarifa());
        
        if (tarifa.getId() == null) {
            System.out.println("   ⚠️  No se encontró registro de tarifas. Creando nuevo registro...");
        } else {
            System.out.println("   ✅ Registro de tarifas encontrado (ID: " + tarifa.getId() + "). Actualizando...");
        }
        
        System.out.println("   📝 Estableciendo valores de tarifas:");
        System.out.println("      - tarifaPorMinuto: $" + tarifaPorMinuto);
        System.out.println("      - tarifa1Minuto: $" + tarifa1Minuto);
        System.out.println("      - tarifa0_1Hora: $" + tarifa0_1Hora);
        System.out.println("      - tarifa1_2Horas: $" + tarifa1_2Horas);
        System.out.println("      - tarifa2MasHoras: $" + tarifa2MasHoras);
        System.out.println("      - tarifaMaximaDiaria: $" + tarifaMaximaDiaria);
        System.out.println("      - tarifaMaximaSemanal: $" + tarifaMaximaSemanal);
        System.out.println("      - tarifaTicketPerdido: $" + tarifaTicketPerdido);
        
        tarifa.setTarifaPorMinuto(java.math.BigDecimal.valueOf(tarifaPorMinuto));
        tarifa.setTarifa1Minuto(java.math.BigDecimal.valueOf(tarifa1Minuto));
        tarifa.setTarifa0_1Hora(java.math.BigDecimal.valueOf(tarifa0_1Hora));
        tarifa.setTarifa1_2Horas(java.math.BigDecimal.valueOf(tarifa1_2Horas));
        tarifa.setTarifa2MasHoras(java.math.BigDecimal.valueOf(tarifa2MasHoras));
        tarifa.setTarifaMaximaDiaria(java.math.BigDecimal.valueOf(tarifaMaximaDiaria));
        tarifa.setTarifaMaximaSemanal(java.math.BigDecimal.valueOf(tarifaMaximaSemanal));
        tarifa.setTarifaTicketPerdido(java.math.BigDecimal.valueOf(tarifaTicketPerdido));
        
        System.out.println("   💾 Guardando tarifas en BD...");
        Tarifa tarifaGuardada = tarifaRepository.save(tarifa);
        System.out.println("   ✅ Tarifas guardadas exitosamente (ID: " + tarifaGuardada.getId() + ")");
        
        // Verificar que se guardó correctamente
        tarifaRepository.findFirstByOrderByIdDesc().ifPresent(t -> {
            System.out.println("   ✅ Verificación: Tarifas en BD después de guardar:");
            System.out.println("      - tarifaPorMinuto: $" + t.getTarifaPorMinuto());
            System.out.println("      - tarifa1Minuto: $" + t.getTarifa1Minuto());
            System.out.println("      - tarifa0_1Hora: $" + t.getTarifa0_1Hora());
            System.out.println("      - tarifa1_2Horas: $" + t.getTarifa1_2Horas());
            System.out.println("      - tarifa2MasHoras: $" + t.getTarifa2MasHoras());
        });
    }

    // Métodos de conversión
    private Date convertToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    private LocalDateTime convertToLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}

