package mx.edu.utez.Estacionamiento.config;

import mx.edu.utez.Estacionamiento.entity.ConfiguracionEstacionamiento;
import mx.edu.utez.Estacionamiento.entity.Tarifa;
import mx.edu.utez.Estacionamiento.repository.ConfiguracionEstacionamientoRepository;
import mx.edu.utez.Estacionamiento.repository.EstacionamientoRepository;
import mx.edu.utez.Estacionamiento.repository.TarifaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Inicializador de datos por defecto
 * Se ejecuta al iniciar la aplicación y crea los datos iniciales si no existen
 * También carga los datos desde BD a las estructuras personalizadas
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private ConfiguracionEstacionamientoRepository configuracionRepository;
    
    @Autowired
    private TarifaRepository tarifaRepository;
    
    @Autowired
    private EstacionamientoRepository estacionamientoRepository;

    @Override
    public void run(String... args) throws Exception {
        inicializarConfiguracion();
        inicializarTarifas();
        // Cargar datos desde BD a las estructuras personalizadas
        estacionamientoRepository.cargarDesdeBaseDatos();
        System.out.println("✅ Estructuras de datos cargadas desde la base de datos");
    }

    /**
     * Inicializa la configuración del estacionamiento si no existe
     */
    private void inicializarConfiguracion() {
        if (!configuracionRepository.findByClave("capacidad_maxima").isPresent()) {
            ConfiguracionEstacionamiento config = new ConfiguracionEstacionamiento(
                "capacidad_maxima",
                "20",
                "Capacidad máxima de vehículos que pueden estar estacionados simultáneamente"
            );
            configuracionRepository.save(config);
            System.out.println("✅ Configuración inicial creada: capacidad_maxima = 20");
        }
    }

    /**
     * Inicializa las tarifas por defecto si no existen
     */
    private void inicializarTarifas() {
        if (tarifaRepository.count() == 0) {
            Tarifa tarifa = new Tarifa();
            tarifa.setTarifaPorMinuto(BigDecimal.valueOf(1.50));
            tarifa.setTarifa1Minuto(BigDecimal.valueOf(1.50));
            tarifa.setTarifa0_1Hora(BigDecimal.valueOf(5.00));
            tarifa.setTarifa1_2Horas(BigDecimal.valueOf(10.00));
            tarifa.setTarifa2MasHoras(BigDecimal.valueOf(15.00));
            tarifa.setTarifaMaximaDiaria(BigDecimal.ZERO);
            tarifa.setTarifaMaximaSemanal(BigDecimal.ZERO);
            tarifa.setTarifaTicketPerdido(BigDecimal.ZERO);
            
            tarifaRepository.save(tarifa);
            System.out.println("✅ Tarifas iniciales creadas");
        }
    }
}

