package mx.edu.utez.Estacionamiento; // Paquete principal

import mx.edu.utez.Estacionamiento.service.EstacionamientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component // Le dice a Spring que gestione esta clase
public class PruebasDeConsolaRunner implements CommandLineRunner {

    // Inyectamos el servicio con toda la lógica
    @Autowired
    private EstacionamientoService service;

    /**
     * Este método se ejecutará automáticamente cuando corras tu proyecto,
     * como si fuera tu "main" de pruebas.
     */
    @Override
    public void run(String... args) throws Exception {
        System.out.println("******************************************");
        System.out.println("INICIANDO PRUEBAS DE CONSOLA DEL ESTACIONAMIENTO");
        System.out.println("******************************************");

        // --- PRUEBA 1: INGRESAR COCHES ---
        System.out.println("\n--- Prueba 1: Ingresando coches ---");
        System.out.println(service.registrarEntrada("ABC-123"));
        Thread.sleep(1000); // Pequeña pausa para simular el tiempo
        System.out.println(service.registrarEntrada("XYZ-789"));
        Thread.sleep(1000);
        System.out.println(service.registrarEntrada("UTE-001"));

        // Mostrar estado actual [cite: 13]
        service.mostrarCochesActuales();
        service.mostrarFilaEspera();

        // --- PRUEBA 2: INGRESAR COCHE REPETIDO ---
        System.out.println("\n--- Prueba 2: Ingresando coche repetido ---");
        System.out.println(service.registrarEntrada("ABC-123")); // Debería dar error

        // --- PRUEBA 3: SACAR UN COCHE ---
        System.out.println("\n--- Prueba 3: Sacando un coche ---");
        Thread.sleep(2000); // Simular 2 segundos de estancia
        System.out.println(service.registrarSalida("ABC-123")); // Sale el primero

        // Mostrar estado después de la salida
        System.out.println("\n--- Estado después de la salida ---");
        service.mostrarCochesActuales();
        service.mostrarHistorialSalidas(); // [cite: 14]

        // --- PRUEBA 4: LLENAR EL ESTACIONAMIENTO ---
        // (Asumiendo capacidad de 20 en el Repository)
        System.out.println("\n--- Prueba 4: Llenando el estacionamiento (simulación) ---");
        for (int i = 1; i <= 18; i++) { // Ya hay 2 (XYZ y UTE), metemos 18 más
            service.registrarEntrada("CAR-" + i);
        }
        System.out.println("--- Estacionamiento Lleno ---");
        service.mostrarCochesActuales();
        System.out.println("Tamaño actual: " + service.getLugaresOcupadosSize());
        // --- PRUEBA 5: COCHES A LA FILA DE ESPERA (FIFO) ---
        System.out.println("\n--- Prueba 5: Enviando coches a la fila de espera ---");
        System.out.println(service.registrarEntrada("ESP-001")); // Va a la cola
        System.out.println(service.registrarEntrada("ESP-002")); // Va a la cola
        service.mostrarFilaEspera();

        // --- PRUEBA 6: SACAR COCHE PARA QUE ENTRE ALGUIEN DE LA COLA ---
        System.out.println("\n--- Prueba 6: Liberando un lugar ---");
        System.out.println(service.registrarSalida("XYZ-789")); // Sale XYZ

        System.out.println("\n--- Estado final ---");
        service.mostrarCochesActuales(); // Debería incluir a "ESP-001"
        service.mostrarFilaEspera(); // Debería tener solo a "ESP-002"
        service.mostrarHistorialSalidas(); // Debería tener a ABC y XYZ

        System.out.println("******************************************");
        System.out.println("PRUEBAS DE CONSOLA FINALIZADAS");
        System.out.println("******************************************");
    }
}