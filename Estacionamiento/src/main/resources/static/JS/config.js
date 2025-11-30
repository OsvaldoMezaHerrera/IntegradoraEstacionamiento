/**
 * ============================================
 * CONFIGURACIÓN GLOBAL - ParkSmart
 * Configuración centralizada de la aplicación
 * ============================================
 */

// Namespace principal para evitar conflictos globales
window.ParkSmart = window.ParkSmart || {};

// Configuración de la aplicación
ParkSmart.Config = {
  // URL base de la API
  API_BASE_URL: '/api/estacionamiento',
  
  // Puerto por defecto del servidor
  DEFAULT_PORT: 8080,
  
  // Intervalos de actualización (en milisegundos)
  UPDATE_INTERVALS: {
    STATISTICS: 5000,    // 5 segundos
    HISTORY: 10000,      // 10 segundos
    TARIFFS: 30000       // 30 segundos
  },
  
  // Mensajes de error predefinidos
  MESSAGES: {
    SERVER_NOT_FOUND: 'Servidor no encontrado. Asegúrate de que Spring Boot esté ejecutándose en http://localhost:8080',
    INVALID_RESPONSE: 'Respuesta no es JSON. ¿Estás accediendo desde Spring Boot?',
    CONNECTION_ERROR: 'Error al conectar con el servidor',
    LOAD_ERROR: 'Error al cargar los datos',
    SAVE_ERROR: 'Error al guardar los datos',
    NETWORK_ERROR: 'Error de red. Verifica tu conexión a internet.'
  },
  
  // Configuración de validación
  VALIDATION: {
    PLATE_PATTERN: /^[A-Z0-9]{1,10}$/i,
    TIME_PATTERN: /^([0-1][0-9]|2[0-3]):[0-5][0-9]$/,
    MIN_PLATE_LENGTH: 1,
    MAX_PLATE_LENGTH: 10
  },
  
  // Configuración de UI
  UI: {
    ANIMATION_DURATION: 300,
    MODAL_CLOSE_DELAY: 2000,
    MESSAGE_DISPLAY_TIME: 3000,
    LOADING_TEXT: 'Cargando...',
    SAVING_TEXT: 'Guardando...',
    PROCESSING_TEXT: 'Procesando...'
  }
};

// Función helper para obtener la URL base de la API
ParkSmart.Config.getApiBase = function() {
  const hostname = window.location.hostname;
  const port = window.location.port;
  const protocol = window.location.protocol;
  
  // Si estamos en file:// o en un puerto diferente al por defecto, usar localhost:8080
  if (protocol === 'file:' || (port && port !== String(ParkSmart.Config.DEFAULT_PORT) && port !== '')) {
    console.warn('⚠️ Detectado acceso directo al archivo. Usando http://localhost:' + ParkSmart.Config.DEFAULT_PORT + ' para la API.');
    return 'http://localhost:' + ParkSmart.Config.DEFAULT_PORT + ParkSmart.Config.API_BASE_URL;
  }
  
  // Si no hay puerto especificado y estamos en localhost, asumir puerto por defecto
  if (hostname === 'localhost' && !port) {
    return 'http://localhost:' + ParkSmart.Config.DEFAULT_PORT + ParkSmart.Config.API_BASE_URL;
  }
  
  // Caso normal: usar ruta relativa
  return ParkSmart.Config.API_BASE_URL;
};

// Exportar para uso en otros módulos
if (typeof module !== 'undefined' && module.exports) {
  module.exports = ParkSmart.Config;
}

