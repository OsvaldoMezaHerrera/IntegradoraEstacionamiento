/**
 * ============================================
 * PÁGINA PRINCIPAL - ParkSmart
 * Funcionalidad específica de index.html
 * ============================================
 */

// Asegurar que el namespace existe
window.ParkSmart = window.ParkSmart || {};

// Módulo de la página principal
ParkSmart.Index = ParkSmart.Index || {};

/**
 * Actualiza las estadísticas del estacionamiento
 */
ParkSmart.Index.actualizarEstadisticas = async function() {
  try {
    const apiBase = ParkSmart.Config ? ParkSmart.Config.getApiBase() : API_BASE;
    
    // Intentar obtener del servidor
    const response = await fetch(`${apiBase}/estadisticas`);
    
    if (!response.ok) {
      throw new Error(`Error del servidor: ${response.status} ${response.statusText}`);
    }

    // Verificar si la respuesta es JSON
    const contentType = response.headers.get('content-type');
    if (!contentType || !contentType.includes('application/json')) {
      throw new Error('Respuesta no es JSON');
    }

    // Obtener datos del servidor
    const data = await response.json();
    
    // Actualizar la interfaz
    const espaciosDisponiblesEl = document.getElementById("espacios-disponibles");
    const espaciosOcupadosEl = document.getElementById("espacios-ocupados");
    const vehiculosEsperaEl = document.getElementById("vehiculos-espera");
    
    if (espaciosDisponiblesEl) {
      espaciosDisponiblesEl.textContent = data.lugaresDisponibles || 20;
    }
    if (espaciosOcupadosEl) {
      espaciosOcupadosEl.textContent = data.lugaresOcupados || 0;
    }
    if (vehiculosEsperaEl) {
      vehiculosEsperaEl.textContent = data.vehiculosEnEspera || 0;
    }
    
    ParkSmart.Index.ocultarIndicadorCache();
    
  } catch (error) {
    console.error("Error al actualizar estadísticas:", error);
    ParkSmart.Index.mostrarErrorConexion(error);
  }
};

/**
 * Actualiza la lista de vehículos en espera
 */
ParkSmart.Index.actualizarFilaEspera = async function() {
  try {
    const apiBase = ParkSmart.Config ? ParkSmart.Config.getApiBase() : API_BASE;
    const response = await fetch(`${apiBase}/espera`);
    
    if (!response.ok) {
      throw new Error(`Error del servidor: ${response.status}`);
    }

    const contentType = response.headers.get('content-type');
    if (!contentType || !contentType.includes('application/json')) {
      throw new Error('Respuesta no es JSON');
    }

    const vehiculos = await response.json();
    const listaEspera = document.getElementById("lista-espera");
    const mensajeEsperaVacia = document.getElementById("mensaje-espera-vacia");
    
    if (!listaEspera) return;

    // Limpiar lista
    listaEspera.innerHTML = "";

    if (vehiculos.length === 0) {
      if (mensajeEsperaVacia) {
        mensajeEsperaVacia.classList.remove("util-hidden");
      }
    } else {
      if (mensajeEsperaVacia) {
        mensajeEsperaVacia.classList.add("util-hidden");
      }

      // Agregar cada vehículo a la lista
      vehiculos.forEach((vehiculo, index) => {
        const item = document.createElement("div");
        item.className = "card";
        item.style.cssText = "padding: var(--spacing-sm) var(--spacing-md); margin-bottom: var(--spacing-xs); display: flex; align-items: center; justify-content: space-between;";
        
        const info = document.createElement("div");
        info.className = "app-flex app-flex-col app-gap-xs";
        
        const placa = document.createElement("p");
        placa.style.cssText = "font-size: var(--font-size-sm); font-weight: var(--font-weight-bold); color: var(--color-text-primary); margin: 0;";
        placa.textContent = vehiculo.placa || "Sin placa";
        
        const posicion = document.createElement("p");
        posicion.style.cssText = "font-size: var(--font-size-xs); color: var(--color-text-secondary); margin: 0;";
        posicion.textContent = `Posición ${index + 1}`;
        
        if (vehiculo.horaEntrada) {
          const hora = document.createElement("p");
          hora.style.cssText = "font-size: var(--font-size-xs); color: var(--color-text-secondary); margin: 0;";
          hora.textContent = `Desde: ${ParkSmart.Utils.formatearFecha(vehiculo.horaEntrada, 'short')}`;
          info.appendChild(hora);
        }
        
        info.appendChild(placa);
        info.appendChild(posicion);
        
        const badge = document.createElement("span");
        badge.className = "btn btn-secondary";
        badge.style.cssText = "min-width: auto; min-height: auto; padding: var(--spacing-xs) var(--spacing-sm); font-size: var(--font-size-xs); height: auto;";
        badge.textContent = `#${index + 1}`;
        
        item.appendChild(info);
        item.appendChild(badge);
        listaEspera.appendChild(item);
      });
    }
  } catch (error) {
    console.error("Error al actualizar fila de espera:", error);
    // No mostrar error al usuario, solo loguear
  }
};

/**
 * Oculta el indicador de cache (mantenido para compatibilidad)
 */
ParkSmart.Index.ocultarIndicadorCache = function() {
  // Función vacía, ya no se usa localStorage
};

/**
 * Muestra mensaje de error de conexión
 */
ParkSmart.Index.mostrarErrorConexion = function(error) {
  const errorMsg = document.getElementById("error-servidor");
  if (errorMsg) {
    errorMsg.textContent = `⚠️ ${error.message}`;
    errorMsg.style.display = 'block';
    errorMsg.className = 'alert alert-warning';
  } else {
    // Crear elemento de error si no existe
    const errorDiv = document.createElement('div');
    errorDiv.id = 'error-servidor';
    errorDiv.className = 'alert alert-warning';
    errorDiv.innerHTML = `
      <p style="font-weight: var(--font-weight-bold);">⚠️ Error de Conexión</p>
      <p>${error.message}</p>
      <p style="margin-top: var(--spacing-sm); font-size: var(--font-size-sm);">
        Asegúrate de ejecutar: <code style="background-color: var(--color-bg-tertiary); padding: var(--spacing-xs) var(--spacing-sm); border-radius: var(--border-radius-sm);">mvn spring-boot:run</code>
      </p>
      <p style="font-size: var(--font-size-sm);">
        Luego accede a: <a href="http://localhost:8080" style="color: var(--color-primary); text-decoration: underline;">http://localhost:8080</a>
      </p>
    `;
    const main = document.querySelector('.app-main');
    if (main) {
      const content = main.querySelector('.app-content');
      if (content) {
        content.insertBefore(errorDiv, content.firstChild);
      }
    }
  }
};

/**
 * Inicializa los modales de entrada y salida
 */
ParkSmart.Index.inicializarModales = function() {
  // Modal de entrada
  const btnEntrada = document.getElementById("btn-entrada");
  const modalEntrada = document.getElementById("modal-entrada");
  const confirmarEntrada = document.getElementById("confirmar-entrada");
  const cancelarEntrada = document.getElementById("cancelar-entrada");
  const placaEntrada = document.getElementById("placa-entrada");
  const mensajeEntrada = document.getElementById("mensaje-entrada");

  if (btnEntrada && modalEntrada) {
    btnEntrada.addEventListener("click", function() {
      modalEntrada.classList.remove("util-hidden");
      if (placaEntrada) placaEntrada.value = "";
      if (mensajeEntrada) {
        mensajeEntrada.textContent = "";
        mensajeEntrada.style.display = 'none';
      }
    });
  }

  if (cancelarEntrada && modalEntrada) {
    cancelarEntrada.addEventListener("click", function() {
      modalEntrada.classList.add("util-hidden");
    });
  }

  if (confirmarEntrada) {
    confirmarEntrada.addEventListener("click", async function() {
      if (!placaEntrada) return;
      
      const placa = placaEntrada.value.trim();
      
      // Validar placa
      const validacion = ParkSmart.Utils.validarPlaca(placa);
      if (!validacion.valida) {
        ParkSmart.Messages.mostrar("mensaje-entrada", validacion.mensaje, true);
        return;
      }

      if (confirmarEntrada) confirmarEntrada.disabled = true;
      if (confirmarEntrada) confirmarEntrada.textContent = "Registrando...";

      try {
        const apiBase = ParkSmart.Config ? ParkSmart.Config.getApiBase() : API_BASE;
        const response = await fetch(`${apiBase}/entrada`, {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({ placa }),
        });

        if (!response.ok) {
          throw new Error(`Error del servidor: ${response.status}`);
        }

        const contentType = response.headers.get('content-type');
        if (!contentType || !contentType.includes('application/json')) {
          throw new Error('Respuesta no es JSON');
        }

        const data = await response.json();
        
        ParkSmart.Messages.mostrar("mensaje-entrada", data.mensaje, !data.exito);

        if (data.exito) {
          await ParkSmart.Index.actualizarEstadisticas();
          await ParkSmart.Index.actualizarFilaEspera();
          setTimeout(function() {
            if (modalEntrada) modalEntrada.classList.add("util-hidden");
          }, ParkSmart.Config ? ParkSmart.Config.UI.MODAL_CLOSE_DELAY : 2000);
        }
      } catch (error) {
        console.error("Error:", error);
        ParkSmart.Messages.mostrar("mensaje-entrada", "Error al conectar con el servidor", true);
      } finally {
        if (confirmarEntrada) {
          confirmarEntrada.disabled = false;
          confirmarEntrada.textContent = "Confirmar";
        }
      }
    });
  }

  // Cerrar modal al hacer clic fuera
  if (modalEntrada) {
    modalEntrada.addEventListener("click", function(e) {
      if (e.target === modalEntrada) {
        modalEntrada.classList.add("util-hidden");
      }
    });
  }

  // Modal de salida
  const btnSalida = document.getElementById("btn-salida");
  const modalSalida = document.getElementById("modal-salida");
  const confirmarSalida = document.getElementById("confirmar-salida");
  const cancelarSalida = document.getElementById("cancelar-salida");
  const placaSalida = document.getElementById("placa-salida");
  const mensajeSalida = document.getElementById("mensaje-salida");

  if (btnSalida && modalSalida) {
    btnSalida.addEventListener("click", function() {
      modalSalida.classList.remove("util-hidden");
      if (placaSalida) placaSalida.value = "";
      if (mensajeSalida) {
        mensajeSalida.textContent = "";
        mensajeSalida.style.display = 'none';
      }
    });
  }

  if (cancelarSalida && modalSalida) {
    cancelarSalida.addEventListener("click", function() {
      modalSalida.classList.add("util-hidden");
    });
  }

  if (confirmarSalida) {
    confirmarSalida.addEventListener("click", async function() {
      if (!placaSalida) return;
      
      const placa = placaSalida.value.trim();
      
      // Validar placa
      const validacion = ParkSmart.Utils.validarPlaca(placa);
      if (!validacion.valida) {
        ParkSmart.Messages.mostrar("mensaje-salida", validacion.mensaje, true);
        return;
      }

      if (confirmarSalida) confirmarSalida.disabled = true;
      if (confirmarSalida) confirmarSalida.textContent = "Procesando...";

      try {
        const apiBase = ParkSmart.Config ? ParkSmart.Config.getApiBase() : API_BASE;
        const response = await fetch(`${apiBase}/salida`, {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({ placa }),
        });

        if (!response.ok) {
          throw new Error(`Error del servidor: ${response.status}`);
        }

        const contentType = response.headers.get('content-type');
        if (!contentType || !contentType.includes('application/json')) {
          throw new Error('Respuesta no es JSON');
        }

        const data = await response.json();
        
        ParkSmart.Messages.mostrar("mensaje-salida", data.mensaje, !data.exito);

        if (data.exito) {
          await ParkSmart.Index.actualizarEstadisticas();
          await ParkSmart.Index.actualizarFilaEspera();
          setTimeout(function() {
            if (modalSalida) modalSalida.classList.add("util-hidden");
          }, ParkSmart.Config ? ParkSmart.Config.UI.MODAL_CLOSE_DELAY : 2000);
        }
      } catch (error) {
        console.error("Error:", error);
        ParkSmart.Messages.mostrar("mensaje-salida", "Error al conectar con el servidor", true);
      } finally {
        if (confirmarSalida) {
          confirmarSalida.disabled = false;
          confirmarSalida.textContent = "Confirmar";
        }
      }
    });
  }

  // Cerrar modal al hacer clic fuera
  if (modalSalida) {
    modalSalida.addEventListener("click", function(e) {
      if (e.target === modalSalida) {
        modalSalida.classList.add("util-hidden");
      }
    });
  }
};

/**
 * Inicializa la página principal
 */
ParkSmart.Index.inicializar = function() {
  // Inicializar modales
  ParkSmart.Index.inicializarModales();
  
  // Actualizar estadísticas desde el servidor
  ParkSmart.Index.actualizarEstadisticas();
  
  // Actualizar fila de espera
  ParkSmart.Index.actualizarFilaEspera();
  
  // Actualizar estadísticas cada 5 segundos
  const intervalo = ParkSmart.Config ? ParkSmart.Config.UPDATE_INTERVALS.STATISTICS : 5000;
  setInterval(ParkSmart.Index.actualizarEstadisticas, intervalo);
  setInterval(ParkSmart.Index.actualizarFilaEspera, intervalo);
};

// Inicializar cuando el DOM esté listo
if (document.readyState === 'loading') {
  document.addEventListener('DOMContentLoaded', ParkSmart.Index.inicializar);
} else {
  ParkSmart.Index.inicializar();
}

// Funciones de compatibilidad (mantener para código existente)
function actualizarEstadisticas() {
  ParkSmart.Index.actualizarEstadisticas();
}
