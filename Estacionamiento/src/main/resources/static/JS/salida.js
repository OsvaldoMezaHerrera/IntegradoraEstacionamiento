// Funciones específicas para la página de salida (salida.html)

let infoVehiculo = null;

// Función para mostrar mensaje (compatibilidad)
function mostrarMensaje(elementId, mensaje, esError = false) {
  if (ParkSmart && ParkSmart.Messages) {
    ParkSmart.Messages.mostrar(elementId, mensaje, esError);
  } else {
    const elemento = document.getElementById(elementId);
    if (elemento) {
      elemento.textContent = mensaje || '';
      if (mensaje) {
        elemento.className = 'alert ' + (esError ? 'alert-error' : 'alert-success');
        elemento.style.display = 'block';
      } else {
        elemento.style.display = 'none';
      }
    }
  }
}

// Función para formatear tiempo
function formatearTiempo(horas, minutos) {
  if (horas === 0) {
    return `${minutos} minutos`;
  } else if (minutos === 0) {
    return `${horas} hora${horas > 1 ? "s" : ""}`;
  } else {
    return `${horas} hora${horas > 1 ? "s" : ""} ${minutos} minuto${
      minutos > 1 ? "s" : ""
    }`;
  }
}

// Buscar vehículo
const btnBuscar = document.getElementById("btn-buscar");
const matriculaInput = document.getElementById("matricula-salida");
const detallesEstancia = document.getElementById("detalles-estancia");

btnBuscar.addEventListener("click", async () => {
  const placa = matriculaInput.value.trim();
  if (!placa) {
    mostrarMensaje("mensaje-busqueda", "Por favor ingrese una matrícula", true);
    return;
  }

  btnBuscar.disabled = true;
  btnBuscar.textContent = "Buscando...";

  try {
    const apiBase = ParkSmart.Config ? ParkSmart.Config.getApiBase() : API_BASE;
    const response = await fetch(`${apiBase}/vehiculo/${encodeURIComponent(placa)}`);
    
    // Verificar el tipo de contenido primero
    const contentType = response.headers.get('content-type');
    if (!contentType || !contentType.includes('application/json')) {
      if (response.status === 404) {
        throw new Error('Servidor no encontrado. Asegúrate de que Spring Boot esté ejecutándose en http://localhost:8080');
      }
      throw new Error(`Respuesta no es JSON. ¿Estás accediendo desde Spring Boot?`);
    }

    // Leer el JSON independientemente del status code
    const data = await response.json();

    // Si el servidor devuelve 400 pero con JSON, es porque el vehículo no se encontró
    if (!response.ok) {
      if (response.status === 400 && data.mensaje) {
        // Vehículo no encontrado - mostrar mensaje del servidor
        ParkSmart.Messages.mostrar("mensaje-busqueda", data.mensaje, true);
        detallesEstancia.classList.add("util-hidden");
        infoVehiculo = null;
        return;
      }
      if (response.status === 404) {
        throw new Error('Servidor no encontrado. Asegúrate de que Spring Boot esté ejecutándose en http://localhost:8080');
      }
      throw new Error(`Error del servidor: ${response.status} - ${data.mensaje || 'Error desconocido'}`);
    }

    if (data.encontrado) {
      infoVehiculo = data;
      ParkSmart.Messages.mostrar("mensaje-busqueda", "Vehículo encontrado", false);

      // Mostrar detalles
      const horas = data.tiempoHoras;
      const minutos = data.tiempoMinutosRestantes;
      document.getElementById("tiempo-estancia").textContent =
        formatearTiempo(horas, minutos);
      document.getElementById("tarifa-hora").textContent = ParkSmart.Utils.formatearMoneda(
        data.tarifaPorHora
      );
      document.getElementById("tarifa-total").textContent = ParkSmart.Utils.formatearMoneda(
        data.tarifaTotal
      );

      detallesEstancia.classList.remove("util-hidden");
    } else {
      ParkSmart.Messages.mostrar("mensaje-busqueda", data.mensaje || "Vehículo no encontrado", true);
      detallesEstancia.classList.add("util-hidden");
      infoVehiculo = null;
    }
  } catch (error) {
    console.error("Error:", error);
    let mensajeError = "Error al conectar con el servidor";
    if (error.message.includes('Servidor no encontrado') || error.message.includes('no es JSON')) {
      mensajeError = "⚠️ " + error.message;
    } else if (error.message) {
      mensajeError = error.message;
    }
    ParkSmart.Messages.mostrar("mensaje-busqueda", mensajeError, true);
    detallesEstancia.classList.add("util-hidden");
    infoVehiculo = null;
  } finally {
    btnBuscar.disabled = false;
    btnBuscar.textContent = "Buscar";
  }
});

// Permitir buscar con Enter
matriculaInput.addEventListener("keypress", (e) => {
  if (e.key === "Enter") {
    btnBuscar.click();
  }
});

// Procesar pago y salida
const btnProcesar = document.getElementById("btn-procesar");

btnProcesar.addEventListener("click", async () => {
  if (!infoVehiculo) {
    mostrarMensaje(
      "mensaje-procesamiento",
      "Por favor busque un vehículo primero",
      true
    );
    return;
  }


  btnProcesar.disabled = true;
  btnProcesar.innerHTML =
    '<span class="truncate">Procesando...</span>';

        try {
          const apiBase = ParkSmart.Config ? ParkSmart.Config.getApiBase() : API_BASE;
          const response = await fetch(`${apiBase}/salida`, {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
            },
            body: JSON.stringify({ placa: infoVehiculo.placa }),
          });

          // Verificar el tipo de contenido primero
          const contentType = response.headers.get('content-type');
          if (!contentType || !contentType.includes('application/json')) {
            if (response.status === 404) {
              throw new Error('Servidor no encontrado. Asegúrate de que Spring Boot esté ejecutándose en http://localhost:8080');
            }
            throw new Error(`Respuesta no es JSON. ¿Estás accediendo desde Spring Boot?`);
          }

          const data = await response.json();

          if (!response.ok) {
            throw new Error(data.mensaje || `Error del servidor: ${response.status}`);
          }

          if (data.exito) {
            // Actualizar los detalles de la tarifa con la información final
            console.log("=== DATOS RECIBIDOS DEL SERVIDOR ===");
            console.log("Objeto completo:", data);
            console.log("tarifaTotal (raw):", data.tarifaTotal);
            console.log("tarifaTotal (type):", typeof data.tarifaTotal);
            console.log("tarifaPorHora (raw):", data.tarifaPorHora);
            console.log("tarifaPorMinuto (raw):", data.tarifaPorMinuto);
            console.log("tiempoMinutos:", data.tiempoMinutos);
            console.log("tiempoHoras:", data.tiempoHoras);
            
            // Asegurar que la sección de detalles esté visible
            detallesEstancia.classList.remove("util-hidden");
            
            // Actualizar tiempo de estancia
            const horas = data.tiempoHoras != null ? Number(data.tiempoHoras) : 0;
            const minutos = data.tiempoMinutosRestantes != null ? Number(data.tiempoMinutosRestantes) : 0;
            document.getElementById("tiempo-estancia").textContent =
              formatearTiempo(horas, minutos);
            
            // Actualizar tarifa por hora
            const tarifaPorHora = data.tarifaPorHora != null ? Number(data.tarifaPorHora) : 0;
            console.log("tarifaPorHora convertida:", tarifaPorHora);
            document.getElementById("tarifa-hora").textContent = ParkSmart.Utils.formatearMoneda(tarifaPorHora);
            
            // Actualizar tarifa total (siempre mostrar, incluso si es 0)
            let tarifaTotal = 0;
            if (data.tarifaTotal != null && data.tarifaTotal !== undefined) {
              tarifaTotal = Number(data.tarifaTotal);
              console.log("tarifaTotal después de Number():", tarifaTotal);
              console.log("Es NaN?", isNaN(tarifaTotal));
            } else {
              console.warn("⚠️ tarifaTotal es null o undefined");
            }
            
            if (isNaN(tarifaTotal)) {
              console.error("⚠️ ERROR: tarifaTotal no es un número válido:", data.tarifaTotal);
              console.error("   Intentando parseFloat...");
              tarifaTotal = parseFloat(data.tarifaTotal) || 0;
              console.error("   Resultado de parseFloat:", tarifaTotal);
            }
            
            console.log("Valor final de tarifaTotal que se mostrará:", tarifaTotal);
            document.getElementById("tarifa-total").textContent = ParkSmart.Utils.formatearMoneda(tarifaTotal);
            console.log("Texto mostrado en pantalla:", document.getElementById("tarifa-total").textContent);
            
            ParkSmart.Messages.mostrar(
              "mensaje-procesamiento",
              `✓ ${data.mensaje}`,
              false
            );
            // Limpiar formulario después de 8 segundos (más tiempo para ver la tarifa)
            setTimeout(() => {
              matriculaInput.value = "";
              detallesEstancia.classList.add("util-hidden");
              infoVehiculo = null;
              ParkSmart.Messages.mostrar("mensaje-busqueda", "");
              ParkSmart.Messages.mostrar("mensaje-procesamiento", "");
            }, 8000);
          } else {
            ParkSmart.Messages.mostrar("mensaje-procesamiento", data.mensaje || "Error al procesar la salida", true);
          }
        } catch (error) {
          console.error("Error:", error);
          let mensajeError = "Error al conectar con el servidor";
          if (error.message) {
            mensajeError = error.message;
            if (!error.message.includes('Servidor no encontrado') && !error.message.includes('no es JSON')) {
              mensajeError = "⚠️ " + mensajeError;
            }
          }
          ParkSmart.Messages.mostrar("mensaje-procesamiento", mensajeError, true);
  } finally {
    btnProcesar.disabled = false;
    btnProcesar.textContent = "Procesar Pago y Salida";
  }
});

