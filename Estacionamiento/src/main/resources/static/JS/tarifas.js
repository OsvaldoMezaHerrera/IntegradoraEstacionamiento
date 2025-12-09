// Funciones específicas para la página de tarifas (tarifas.html)

// Función para mostrar mensaje
function mostrarMensaje(mensaje, esError = false) {
  const elemento = document.getElementById("mensaje-tarifas");
  if (!elemento) return;
  
  elemento.textContent = mensaje || '';
  
  if (mensaje) {
    elemento.className = 'alert ' + (esError ? 'alert-error' : 'alert-success');
    elemento.style.display = 'block';
  } else {
    elemento.style.display = 'none';
  }
}

// Función para formatear moneda (solo número)
function formatearMonedaNumero(cantidad) {
  return cantidad.toFixed(2);
}

// Función para formatear moneda con símbolo
function formatearMoneda(cantidad) {
  if (typeof cantidad !== 'number' || isNaN(cantidad)) {
    return '$0.00';
  }
  return '$' + cantidad.toFixed(2);
}

// Cargar tarifas actuales
async function cargarTarifas() {
  try {
    const apiBase = ParkSmart.Config ? ParkSmart.Config.getApiBase() : API_BASE;
    console.log("Cargando tarifas desde:", `${apiBase}/tarifas`);
    const response = await fetch(`${apiBase}/tarifas`);
    
    if (!response.ok) {
      throw new Error(`Error del servidor: ${response.status}`);
    }

    const contentType = response.headers.get('content-type');
    if (!contentType || !contentType.includes('application/json')) {
      throw new Error('Respuesta no es JSON');
    }

    const tarifas = await response.json();
    console.log("Tarifas recibidas del servidor:", tarifas);

    // Cargar tarifa por minuto
    const tarifaPorMinuto = tarifas.tarifaPorMinuto || 1.50;
    document.getElementById("tarifa-por-minuto").value = formatearMonedaNumero(tarifaPorMinuto);
    
    // Actualizar tarifa por hora calculada
    actualizarTarifaPorHora(tarifaPorMinuto);
    
  } catch (error) {
    console.error("Error al cargar tarifas:", error);
    let mensajeError = "Error al cargar las tarifas actuales";
    if (error.message.includes('Servidor no encontrado') || error.message.includes('no es JSON')) {
      mensajeError = "⚠️ " + error.message + " Accede desde http://localhost:8080";
    }
    mostrarMensaje(mensajeError, true);
  }
}

// Actualizar tarifa por hora calculada
function actualizarTarifaPorHora(tarifaPorMinuto) {
  const tarifaPorHora = tarifaPorMinuto * 60;
  const displayElement = document.getElementById("tarifa-por-hora-display");
  if (displayElement) {
    displayElement.textContent = formatearMoneda(tarifaPorHora);
  }
}


// Guardar tarifas
const btnGuardar = document.getElementById("btn-guardar");

// Actualizar tarifa por hora cuando cambia la tarifa por minuto
document.getElementById("tarifa-por-minuto").addEventListener("input", function() {
  const tarifaPorMinuto = parseFloat(this.value) || 0;
  actualizarTarifaPorHora(tarifaPorMinuto);
});

btnGuardar.addEventListener("click", async () => {
  const tarifaPorMinutoInput = document.getElementById("tarifa-por-minuto").value;
  const tarifaPorMinuto = parseFloat(tarifaPorMinutoInput);

  // Validar que la tarifa sea válida
  if (isNaN(tarifaPorMinuto)) {
    mostrarMensaje("Por favor ingrese un valor válido para la tarifa por minuto", true);
    return;
  }

  if (tarifaPorMinuto < 0) {
    mostrarMensaje("La tarifa no puede ser negativa", true);
    return;
  }

  const nuevasTarifas = {
    tarifaPorMinuto: tarifaPorMinuto
  };

  btnGuardar.disabled = true;
  btnGuardar.innerHTML = '<span class="truncate">Guardando...</span>';

  try {
    const apiBase = ParkSmart.Config ? ParkSmart.Config.getApiBase() : API_BASE;
    console.log("Enviando tarifas al servidor:", nuevasTarifas);
    console.log("URL:", `${apiBase}/tarifas`);
    
    const response = await fetch(`${apiBase}/tarifas`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(nuevasTarifas),
    });

    if (!response.ok) {
      if (response.status === 404) {
        throw new Error('Servidor no encontrado. Asegúrate de que Spring Boot esté ejecutándose.');
      }
      throw new Error(`Error del servidor: ${response.status}`);
    }

    const contentType = response.headers.get('content-type');
    if (!contentType || !contentType.includes('application/json')) {
      throw new Error(`Respuesta no es JSON. ¿Estás accediendo desde Spring Boot?`);
    }

    const tarifasActualizadas = await response.json();

    // Verificar que las tarifas se guardaron correctamente
    console.log("Tarifas actualizadas recibidas del servidor:", tarifasActualizadas);
    
    mostrarMensaje("✓ Tarifas actualizadas correctamente", false);
    
    // Recargar las tarifas para asegurar que se muestren los valores correctos
    setTimeout(() => {
      cargarTarifas();
    }, 500);
  } catch (error) {
    console.error("Error:", error);
    let mensajeError = "Error al guardar las tarifas. Por favor intente nuevamente.";
    if (error.message.includes('Servidor no encontrado') || error.message.includes('no es JSON')) {
      mensajeError = "⚠️ " + error.message + " Accede desde http://localhost:8080";
    }
    mostrarMensaje(mensajeError, true);
  } finally {
    btnGuardar.disabled = false;
    btnGuardar.innerHTML = '<span class="truncate">Guardar Cambios</span>';
  }
});

// Cargar tarifas al iniciar
document.addEventListener("DOMContentLoaded", () => {
  cargarTarifas();
});

