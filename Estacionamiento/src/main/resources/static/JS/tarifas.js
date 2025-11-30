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

    document.getElementById("tarifa-1-minuto").value = formatearMonedaNumero(
      tarifas.tarifa1Minuto || 1.50
    );
    document.getElementById("tarifa-0-1").value = formatearMonedaNumero(
      tarifas.tarifa0_1Hora
    );
    document.getElementById("tarifa-1-2").value = formatearMonedaNumero(
      tarifas.tarifa1_2Horas
    );
    document.getElementById("tarifa-2-plus").value = formatearMonedaNumero(
      tarifas.tarifa2MasHoras
    );
    document.getElementById("maximo-diario").value =
      tarifas.tarifaMaximaDiaria > 0
        ? formatearMonedaNumero(tarifas.tarifaMaximaDiaria)
        : "";
    document.getElementById("maximo-semanal").value =
      tarifas.tarifaMaximaSemanal > 0
        ? formatearMonedaNumero(tarifas.tarifaMaximaSemanal)
        : "";
    document.getElementById("ticket-perdido").value =
      tarifas.tarifaTicketPerdido > 0
        ? formatearMonedaNumero(tarifas.tarifaTicketPerdido)
        : "";
    
  } catch (error) {
    console.error("Error al cargar tarifas:", error);
    let mensajeError = "Error al cargar las tarifas actuales";
    if (error.message.includes('Servidor no encontrado') || error.message.includes('no es JSON')) {
      mensajeError = "⚠️ " + error.message + " Accede desde http://localhost:8080";
    }
    mostrarMensaje(mensajeError, true);
  }
}


// Guardar tarifas
const btnGuardar = document.getElementById("btn-guardar");

btnGuardar.addEventListener("click", async () => {
  const nuevasTarifas = {
    tarifa1Minuto: parseFloat(
      document.getElementById("tarifa-1-minuto").value
    ),
    tarifa0_1Hora: parseFloat(
      document.getElementById("tarifa-0-1").value
    ),
    tarifa1_2Horas: parseFloat(
      document.getElementById("tarifa-1-2").value
    ),
    tarifa2MasHoras: parseFloat(
      document.getElementById("tarifa-2-plus").value
    ),
    tarifaMaximaDiaria: parseFloat(
      document.getElementById("maximo-diario").value || "0"
    ),
    tarifaMaximaSemanal: parseFloat(
      document.getElementById("maximo-semanal").value || "0"
    ),
    tarifaTicketPerdido: parseFloat(
      document.getElementById("ticket-perdido").value || "0"
    ),
  };

  // Validar que las tarifas por tiempo sean válidas
  if (
    isNaN(nuevasTarifas.tarifa1Minuto) ||
    isNaN(nuevasTarifas.tarifa0_1Hora) ||
    isNaN(nuevasTarifas.tarifa1_2Horas) ||
    isNaN(nuevasTarifas.tarifa2MasHoras)
  ) {
    mostrarMensaje(
      "Por favor ingrese valores válidos para todas las tarifas",
      true
    );
    return;
  }

  if (
    nuevasTarifas.tarifa1Minuto < 0 ||
    nuevasTarifas.tarifa0_1Hora < 0 ||
    nuevasTarifas.tarifa1_2Horas < 0 ||
    nuevasTarifas.tarifa2MasHoras < 0
  ) {
    mostrarMensaje("Las tarifas no pueden ser negativas", true);
    return;
  }

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

