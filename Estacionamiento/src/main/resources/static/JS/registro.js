// Funciones específicas para la página de registro (registro.html)

// Función para mostrar mensaje
function mostrarMensaje(mensaje, esError = false) {
  const elemento = document.getElementById("mensaje-registro");
  if (!elemento) return;
  
  elemento.textContent = mensaje || '';
  
  if (mensaje) {
    elemento.className = 'alert ' + (esError ? 'alert-error' : 'alert-success');
    elemento.style.display = 'block';
  } else {
    elemento.style.display = 'none';
  }
}

// Manejar envío del formulario
const formulario = document.getElementById("formulario-registro");
const btnRegistrar = document.getElementById("btn-registrar");

formulario.addEventListener("submit", async (e) => {
  e.preventDefault();

  const matricula = document.getElementById("matricula").value.trim();
  const modelo = document.getElementById("modelo").value.trim();
  const horaEntrada = document.getElementById("hora-entrada").value;

  // Validar que la matrícula esté presente
  if (!matricula) {
    mostrarMensaje("Por favor ingrese la matrícula del vehículo", true);
    return;
  }

  // Nota: El input type="time" ya valida el formato automáticamente
  // No es necesario validar manualmente el formato de hora

  // Deshabilitar botón durante la petición
  btnRegistrar.disabled = true;
  btnRegistrar.textContent = "Registrando...";

  try {
    const response = await fetch(`${API_BASE}/entrada`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ placa: matricula }),
    });

    if (!response.ok) {
      if (response.status === 404) {
        throw new Error('Servidor no encontrado. Asegúrate de que Spring Boot esté ejecutándose en http://localhost:8080');
      }
      throw new Error(`Error del servidor: ${response.status}`);
    }

    const contentType = response.headers.get('content-type');
    if (!contentType || !contentType.includes('application/json')) {
      const text = await response.text();
      throw new Error(`Respuesta no es JSON. ¿Estás accediendo desde Spring Boot?`);
    }

    const data = await response.json();

    if (data.exito) {
      mostrarMensaje(
        `✓ ${data.mensaje}${modelo ? ` (Modelo: ${modelo})` : ""}`,
        false
      );
      // Limpiar formulario después de 2 segundos
      setTimeout(() => {
        formulario.reset();
        mostrarMensaje("");
        // Opcional: redirigir a la página principal
        // window.location.href = "index.html";
      }, 2000);
    } else {
      mostrarMensaje(data.mensaje, true);
    }
  } catch (error) {
    console.error("Error:", error);
    let mensajeError = "Error al conectar con el servidor.";
    if (error.message.includes('Servidor no encontrado') || error.message.includes('no es JSON')) {
      mensajeError = "⚠️ " + error.message + " Accede desde http://localhost:8080";
    }
    mostrarMensaje(mensajeError, true);
  } finally {
    btnRegistrar.disabled = false;
    btnRegistrar.textContent = 'Registrar Entrada';
  }
});

// Establecer hora actual como valor por defecto al cargar la página
document.addEventListener("DOMContentLoaded", () => {
  const horaEntradaInput = document.getElementById("hora-entrada");
  if (horaEntradaInput && !horaEntradaInput.value) {
    const now = new Date();
    const horas = String(now.getHours()).padStart(2, "0");
    const minutos = String(now.getMinutes()).padStart(2, "0");
    horaEntradaInput.value = `${horas}:${minutos}`;
  }
});

