/**
 * ============================================
 * FUNCIONES COMUNES - ParkSmart
 * Utilidades compartidas entre todas las páginas
 * ============================================
 */

// Asegurar que el namespace existe
window.ParkSmart = window.ParkSmart || {};

// Obtener API base desde la configuración
const API_BASE = ParkSmart.Config ? ParkSmart.Config.getApiBase() : '/api/estacionamiento';

/**
 * Utilidades de formateo
 */
ParkSmart.Utils = ParkSmart.Utils || {};

/**
 * Formatea una cantidad como moneda
 * @param {number} cantidad - Cantidad a formatear
 * @param {string} simbolo - Símbolo de moneda (default: '$')
 * @param {number} decimales - Número de decimales (default: 2)
 * @returns {string} Cantidad formateada
 */
ParkSmart.Utils.formatearMoneda = function(cantidad, simbolo = '$', decimales = 2) {
  if (typeof cantidad !== 'number' || isNaN(cantidad)) {
    return simbolo + '0.00';
  }
  return simbolo + cantidad.toFixed(decimales);
};

/**
 * Formatea una fecha a formato legible
 * @param {string|Date} fecha - Fecha a formatear
 * @param {string} formato - Formato deseado ('short' | 'long' | 'time')
 * @returns {string} Fecha formateada
 */
ParkSmart.Utils.formatearFecha = function(fecha, formato = 'short') {
  if (!fecha) return '-';
  
  const date = new Date(fecha);
  if (isNaN(date.getTime())) return '-';
  
  const horas = String(date.getHours()).padStart(2, '0');
  const minutos = String(date.getMinutes()).padStart(2, '0');
  const ampm = date.getHours() >= 12 ? 'PM' : 'AM';
  const horas12 = date.getHours() % 12 || 12;
  
  if (formato === 'time') {
    return `${horas12}:${minutos} ${ampm}`;
  }
  
  const dia = String(date.getDate()).padStart(2, '0');
  const mes = String(date.getMonth() + 1).padStart(2, '0');
  const año = date.getFullYear();
  
  if (formato === 'long') {
    return `${dia}/${mes}/${año} ${horas12}:${minutos} ${ampm}`;
  }
  
  return `${horas12}:${minutos} ${ampm}`;
};

/**
 * Calcula la duración entre dos fechas
 * @param {string|Date} fechaInicio - Fecha de inicio
 * @param {string|Date} fechaFin - Fecha de fin
 * @returns {string} Duración formateada
 */
ParkSmart.Utils.calcularDuracion = function(fechaInicio, fechaFin) {
  if (!fechaInicio || !fechaFin) return '-';
  
  const inicio = new Date(fechaInicio);
  const fin = new Date(fechaFin);
  
  if (isNaN(inicio.getTime()) || isNaN(fin.getTime())) return '-';
  
  const diffMs = fin.getTime() - inicio.getTime();
  const diffMinutos = Math.floor(diffMs / 60000);
  const horas = Math.floor(diffMinutos / 60);
  const minutos = diffMinutos % 60;
  
  if (horas === 0) {
    return `${minutos}m`;
  } else if (minutos === 0) {
    return `${horas}h`;
  } else {
    return `${horas}h ${minutos}m`;
  }
};

/**
 * Utilidades de mensajes
 */
ParkSmart.Messages = ParkSmart.Messages || {};

/**
 * Muestra un mensaje en un elemento del DOM
 * @param {string} elementId - ID del elemento donde mostrar el mensaje
 * @param {string} mensaje - Texto del mensaje
 * @param {boolean} esError - Si es true, muestra como error
 * @param {number} tiempoVisible - Tiempo en ms que el mensaje será visible (0 = permanente)
 */
ParkSmart.Messages.mostrar = function(elementId, mensaje, esError = false, tiempoVisible = 0) {
  const elemento = document.getElementById(elementId);
  if (!elemento) {
    console.warn('Elemento con ID "' + elementId + '" no encontrado');
    return;
  }
  
  elemento.textContent = mensaje || '';
  
  // Aplicar clases según el tipo de mensaje
  if (mensaje) {
    elemento.className = 'alert ' + (esError ? 'alert-error' : 'alert-success');
    elemento.style.display = 'block';
  } else {
    elemento.style.display = 'none';
  }
  
  // Ocultar automáticamente si se especifica tiempo
  if (tiempoVisible > 0 && mensaje) {
    setTimeout(function() {
      elemento.textContent = '';
      elemento.style.display = 'none';
    }, tiempoVisible);
  }
};

/**
 * Valida una placa de vehículo
 * @param {string} placa - Placa a validar
 * @returns {object} { valida: boolean, mensaje: string }
 */
ParkSmart.Utils.validarPlaca = function(placa) {
  if (!placa || typeof placa !== 'string') {
    return {
      valida: false,
      mensaje: 'La placa es requerida'
    };
  }
  
  const placaTrimmed = placa.trim();
  
  if (placaTrimmed.length < ParkSmart.Config.VALIDATION.MIN_PLATE_LENGTH) {
    return {
      valida: false,
      mensaje: 'La placa debe tener al menos ' + ParkSmart.Config.VALIDATION.MIN_PLATE_LENGTH + ' carácter'
    };
  }
  
  if (placaTrimmed.length > ParkSmart.Config.VALIDATION.MAX_PLATE_LENGTH) {
    return {
      valida: false,
      mensaje: 'La placa no puede tener más de ' + ParkSmart.Config.VALIDATION.MAX_PLATE_LENGTH + ' caracteres'
    };
  }
  
  if (!ParkSmart.Config.VALIDATION.PLATE_PATTERN.test(placaTrimmed)) {
    return {
      valida: false,
      mensaje: 'La placa contiene caracteres inválidos'
    };
  }
  
  return {
    valida: true,
    mensaje: ''
  };
};

/**
 * Valida un formato de hora (HH:MM)
 * @param {string} hora - Hora a validar
 * @returns {object} { valida: boolean, mensaje: string }
 */
ParkSmart.Utils.validarHora = function(hora) {
  if (!hora || hora.trim() === '') {
    return { valida: true, mensaje: '' }; // Hora opcional
  }
  
  if (!ParkSmart.Config.VALIDATION.TIME_PATTERN.test(hora.trim())) {
    return {
      valida: false,
      mensaje: 'El formato de hora debe ser HH:MM (ejemplo: 14:30)'
    };
  }
  
  return { valida: true, mensaje: '' };
};

// Funciones de compatibilidad (mantener para código existente)
function formatearMoneda(cantidad) {
  return ParkSmart.Utils.formatearMoneda(cantidad);
}

function mostrarMensaje(elementId, mensaje, esError = false) {
  ParkSmart.Messages.mostrar(elementId, mensaje, esError);
}

