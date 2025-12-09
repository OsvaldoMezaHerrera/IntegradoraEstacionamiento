package mx.edu.utez.Estacionamiento.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Manejador global de excepciones para la aplicación
 * Captura y maneja errores de manera centralizada
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja errores de base de datos (SQL)
     */
    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Map<String, Object>> handleSQLException(SQLException ex) {
        System.err.println("❌ ERROR DE BASE DE DATOS (SQLException):");
        System.err.println("   Mensaje: " + ex.getMessage());
        System.err.println("   Estado SQL: " + ex.getSQLState());
        System.err.println("   Código de error: " + ex.getErrorCode());
        ex.printStackTrace();
        
        Map<String, Object> error = new HashMap<>();
        error.put("error", "Error de conexión a la base de datos");
        error.put("mensaje", ex.getMessage());
        error.put("tipo", "SQLException");
        error.put("sqlState", ex.getSQLState());
        error.put("errorCode", ex.getErrorCode());
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }


    /**
     * Maneja errores de NullPointerException
     */
    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Map<String, Object>> handleNullPointerException(NullPointerException ex) {
        System.err.println("❌ ERROR: NullPointerException");
        System.err.println("   Mensaje: " + ex.getMessage());
        ex.printStackTrace();
        
        Map<String, Object> error = new HashMap<>();
        error.put("error", "Error interno del servidor");
        error.put("mensaje", "Se produjo un error al procesar la solicitud. Verifique los logs del servidor.");
        error.put("tipo", "NullPointerException");
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    /**
     * Maneja errores de IllegalArgumentException
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        System.err.println("❌ ERROR: Argumento inválido");
        System.err.println("   Mensaje: " + ex.getMessage());
        
        Map<String, Object> error = new HashMap<>();
        error.put("error", "Argumento inválido");
        error.put("mensaje", ex.getMessage());
        error.put("tipo", "IllegalArgumentException");
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Maneja cualquier otra excepción no especificada
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        System.err.println("❌ ERROR GENÉRICO:");
        System.err.println("   Tipo: " + ex.getClass().getName());
        System.err.println("   Mensaje: " + ex.getMessage());
        ex.printStackTrace();
        
        Map<String, Object> error = new HashMap<>();
        error.put("error", "Error interno del servidor");
        error.put("mensaje", ex.getMessage());
        error.put("tipo", ex.getClass().getSimpleName());
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}

