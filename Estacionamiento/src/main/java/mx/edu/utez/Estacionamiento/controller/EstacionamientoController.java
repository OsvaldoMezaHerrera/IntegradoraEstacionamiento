package mx.edu.utez.Estacionamiento.controller;

import mx.edu.utez.Estacionamiento.model.Coche;
import mx.edu.utez.Estacionamiento.service.EstacionamientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador REST para la gestión del estacionamiento
 * Expone endpoints API para operaciones de entrada, salida, estadísticas, etc.
 */
@RestController
@RequestMapping("/api/estacionamiento")
public class EstacionamientoController {

    @Autowired
    private EstacionamientoService service;

    // --- Obtener estadísticas del estacionamiento ---
    @GetMapping("/estadisticas")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticas() {
        Map<String, Object> estadisticas = new HashMap<>();
        estadisticas.put("capacidadMaxima", service.getCapacidadMaxima());
        estadisticas.put("lugaresOcupados", service.getLugaresOcupadosSize());
        estadisticas.put("lugaresDisponibles", service.getLugaresDisponibles());
        estadisticas.put("vehiculosEnEspera", service.getFilaEsperaSize());
        return ResponseEntity.ok(estadisticas);
    }

    // --- Obtener lista de coches actuales ---
    @GetMapping("/coches")
    public ResponseEntity<List<Coche>> verCochesActuales() {
        List<Coche> coches = service.getCochesActuales();
        return ResponseEntity.ok(coches);
    }

    // --- Obtener lista de vehículos en espera ---
    @GetMapping("/espera")
    public ResponseEntity<List<Coche>> obtenerVehiculosEnEspera() {
        List<Coche> vehiculos = service.getVehiculosEnEspera();
        return ResponseEntity.ok(vehiculos);
    }

    // --- Registrar entrada de coche ---
    @PostMapping("/entrada")
    public ResponseEntity<Map<String, Object>> registrarEntrada(@RequestBody Map<String, String> request) {
        String placa = request.get("placa");
        String mensaje = service.registrarEntrada(placa);
        
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("mensaje", mensaje);
        respuesta.put("exito", !mensaje.startsWith("ERROR"));
        
        // Incluir estadísticas actualizadas
        Map<String, Object> estadisticas = new HashMap<>();
        estadisticas.put("capacidadMaxima", service.getCapacidadMaxima());
        estadisticas.put("lugaresOcupados", service.getLugaresOcupadosSize());
        estadisticas.put("lugaresDisponibles", service.getLugaresDisponibles());
        estadisticas.put("vehiculosEnEspera", service.getFilaEsperaSize());
        respuesta.put("estadisticas", estadisticas);
        
        return ResponseEntity.ok(respuesta);
    }

    // --- Registrar salida de coche ---
    @PostMapping("/salida")
    public ResponseEntity<Map<String, Object>> registrarSalida(@RequestBody Map<String, String> request) {
        String placa = request.get("placa");
        String mensaje = service.registrarSalida(placa);
        
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("mensaje", mensaje);
        respuesta.put("exito", !mensaje.startsWith("ERROR"));
        
        // Incluir estadísticas actualizadas
        Map<String, Object> estadisticas = new HashMap<>();
        estadisticas.put("capacidadMaxima", service.getCapacidadMaxima());
        estadisticas.put("lugaresOcupados", service.getLugaresOcupadosSize());
        estadisticas.put("lugaresDisponibles", service.getLugaresDisponibles());
        estadisticas.put("vehiculosEnEspera", service.getFilaEsperaSize());
        respuesta.put("estadisticas", estadisticas);
        
        return ResponseEntity.ok(respuesta);
    }

    // --- Obtener información de vehículo para calcular tarifa ---
    @GetMapping("/vehiculo/{placa}")
    public ResponseEntity<Map<String, Object>> obtenerInfoVehiculo(@PathVariable String placa) {
        Map<String, Object> info = service.obtenerInfoVehiculo(placa);
        // Siempre devolver 200 OK, pero con el campo "encontrado" para indicar si existe
        return ResponseEntity.ok(info);
    }

    // --- Obtener historial de salidas ---
    @GetMapping("/historial")
    public ResponseEntity<List<mx.edu.utez.Estacionamiento.model.RegistroEstancia>> obtenerHistorial() {
        List<mx.edu.utez.Estacionamiento.model.RegistroEstancia> historial = service.getHistorialSalidas();
        return ResponseEntity.ok(historial);
    }

    // --- Limpiar historial de salidas ---
    @PostMapping("/historial/limpiar")
    public ResponseEntity<Map<String, Object>> limpiarHistorial() {
        service.limpiarHistorial();
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Historial limpiado correctamente");
        respuesta.put("exito", true);
        return ResponseEntity.ok(respuesta);
    }

    // --- Obtener configuración de tarifas ---
    @GetMapping("/tarifas")
    public ResponseEntity<Map<String, Object>> obtenerTarifas() {
        Map<String, Object> tarifas = service.obtenerTarifas();
        return ResponseEntity.ok(tarifas);
    }

    // --- Actualizar configuración de tarifas ---
    @PostMapping("/tarifas")
    public ResponseEntity<Map<String, Object>> actualizarTarifas(@RequestBody Map<String, Object> nuevasTarifas) {
        Map<String, Object> tarifasActualizadas = service.actualizarTarifas(nuevasTarifas);
        return ResponseEntity.ok(tarifasActualizadas);
    }

    // --- Endpoints legacy (GET) para compatibilidad ---
    @GetMapping("/entrada/{placa}")
    public ResponseEntity<String> registrarEntradaGet(@PathVariable String placa) {
        String mensaje = service.registrarEntrada(placa);
        return ResponseEntity.ok(mensaje);
    }

    @GetMapping("/salida/{placa}")
    public ResponseEntity<String> registrarSalidaGet(@PathVariable String placa) {
        String mensaje = service.registrarSalida(placa);
        return ResponseEntity.ok(mensaje);
    }
}