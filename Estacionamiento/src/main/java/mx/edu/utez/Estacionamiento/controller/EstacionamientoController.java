package mx.edu.utez.Estacionamiento.controller;

import mx.edu.utez.Estacionamiento.service.EstacionamientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController // ¡Importante! Esto lo hace un controlador web
@RequestMapping("/api/estacionamiento") // La URL base
public class EstacionamientoController {

    @Autowired
    private EstacionamientoService service;

    // --- PRUEBA DE LISTA (Mostrar) ---
    @GetMapping("/coches")
    public ResponseEntity<String> verCochesActuales() {
        // Esto no mostrará la lista en la web, pero es un inicio.
        // Para mostrarla bien necesitaríamos un método que devuelva la lista.
        System.out.println("--- Petición Web: Ver Coches ---");
        service.mostrarCochesActuales();
        return ResponseEntity.ok("Petición recibida. Revisa la consola del servidor.");
    }

    // --- PRUEBA DE LISTA (Agregar) Y COLA ---
    @GetMapping("/entrada/{placa}")
    public ResponseEntity<String> registrarEntrada(@PathVariable String placa) {
        String mensaje = service.registrarEntrada(placa);
        return ResponseEntity.ok(mensaje);
    }

    // --- PRUEBA DE LISTA (Eliminar) Y PILA ---
    @GetMapping("/salida/{placa}")
    public ResponseEntity<String> registrarSalida(@PathVariable String placa) {
        String mensaje = service.registrarSalida(placa);
        return ResponseEntity.ok(mensaje);
    }

    // --- PRUEBA DE PILA (Mostrar) ---
    @GetMapping("/historial")
    public ResponseEntity<String> verHistorial() {
        System.out.println("--- Petición Web: Ver Historial ---");
        service.mostrarHistorialSalidas();
        return ResponseEntity.ok("Petición recibida. Revisa la consola del servidor.");
    }
}