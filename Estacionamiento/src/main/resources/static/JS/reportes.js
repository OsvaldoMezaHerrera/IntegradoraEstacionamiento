// Funciones específicas para la página de reportes (reportes.html)

// Función para formatear fecha con día y hora
function formatearFecha(fechaString) {
  const fecha = new Date(fechaString);
  const dia = String(fecha.getDate()).padStart(2, "0");
  const mes = String(fecha.getMonth() + 1).padStart(2, "0");
  const año = fecha.getFullYear();
  const horas = String(fecha.getHours()).padStart(2, "0");
  const minutos = String(fecha.getMinutes()).padStart(2, "0");
  const ampm = fecha.getHours() >= 12 ? "PM" : "AM";
  const horas12 = fecha.getHours() % 12 || 12;
  return `${dia}/${mes}/${año} ${horas12}:${minutos} ${ampm}`;
}

// Función para calcular duración (mejorada para mostrar días si es necesario)
function calcularDuracion(horaEntrada, horaSalida) {
  const entrada = new Date(horaEntrada);
  const salida = new Date(horaSalida);
  const diffMs = salida.getTime() - entrada.getTime();
  const diffMinutos = Math.floor(diffMs / 60000);
  const horas = Math.floor(diffMinutos / 60);
  const minutos = diffMinutos % 60;
  const dias = Math.floor(horas / 24);
  const horasRestantes = horas % 24;

  // Si hay días, mostrarlos
  if (dias > 0) {
    if (horasRestantes === 0 && minutos === 0) {
      return `${dias} día${dias > 1 ? 's' : ''}`;
    } else if (horasRestantes === 0) {
      return `${dias} día${dias > 1 ? 's' : ''} ${minutos}m`;
    } else if (minutos === 0) {
      return `${dias} día${dias > 1 ? 's' : ''} ${horasRestantes}h`;
    } else {
      return `${dias} día${dias > 1 ? 's' : ''} ${horasRestantes}h ${minutos}m`;
    }
  }

  // Si no hay días, mostrar horas y minutos
  if (horas === 0) {
    return `${minutos} min`;
  } else if (minutos === 0) {
    return `${horas} hora${horas > 1 ? 's' : ''}`;
  } else {
    return `${horas} hora${horas > 1 ? 's' : ''} ${minutos} min`;
  }
}

// Función para calcular tiempo transcurrido desde una fecha hasta ahora
function calcularTiempoTranscurrido(horaEntrada) {
  const entrada = new Date(horaEntrada);
  const ahora = new Date();
  const diffMs = ahora.getTime() - entrada.getTime();
  const diffMinutos = Math.floor(diffMs / 60000);
  const horas = Math.floor(diffMinutos / 60);
  const minutos = diffMinutos % 60;
  const dias = Math.floor(horas / 24);
  const horasRestantes = horas % 24;

  // Si hay días, mostrarlos
  if (dias > 0) {
    if (horasRestantes === 0 && minutos === 0) {
      return `${dias} día${dias > 1 ? 's' : ''}`;
    } else if (horasRestantes === 0) {
      return `${dias} día${dias > 1 ? 's' : ''} ${minutos} min`;
    } else if (minutos === 0) {
      return `${dias} día${dias > 1 ? 's' : ''} ${horasRestantes} hora${horasRestantes > 1 ? 's' : ''}`;
    } else {
      return `${dias} día${dias > 1 ? 's' : ''} ${horasRestantes} hora${horasRestantes > 1 ? 's' : ''} ${minutos} min`;
    }
  }

  // Si no hay días, mostrar horas y minutos
  if (horas === 0) {
    return `${minutos} min`;
  } else if (minutos === 0) {
    return `${horas} hora${horas > 1 ? 's' : ''}`;
  } else {
    return `${horas} hora${horas > 1 ? 's' : ''} ${minutos} min`;
  }
}

// Función para cargar historial
async function cargarHistorial() {
  const mensajeCarga = document.getElementById("mensaje-carga");
  const tablaContenedor = document.getElementById("tabla-contenedor");
  const tablaBody = document.getElementById("tabla-body");
  const mensajeVacio = document.getElementById("mensaje-vacio");

  try {
    const apiBase = ParkSmart.Config ? ParkSmart.Config.getApiBase() : API_BASE;
    const response = await fetch(`${apiBase}/historial`);
    
    if (!response.ok) {
      throw new Error(`Error del servidor: ${response.status}`);
    }

    const contentType = response.headers.get('content-type');
    if (!contentType || !contentType.includes('application/json')) {
      throw new Error('Respuesta no es JSON');
    }

    const historial = await response.json();

    mensajeCarga.classList.add("util-hidden");

    if (historial.length === 0) {
      mensajeVacio.classList.remove("util-hidden");
      tablaContenedor.classList.add("util-hidden");
      return;
    }

    mensajeVacio.classList.add("util-hidden");
    tablaContenedor.classList.remove("util-hidden");

    // Renderizar historial
    renderizarHistorial(historial);
  } catch (error) {
    console.error("Error al cargar historial:", error);
    let mensajeError = "Error al cargar el historial. Por favor recargue la página.";
    if (error.message.includes('Servidor no encontrado') || error.message.includes('no es JSON')) {
      mensajeError = "⚠️ " + error.message + " Accede desde http://localhost:8080";
    }
    mensajeCarga.textContent = mensajeError;
    mensajeCarga.classList.remove("util-hidden");
    tablaContenedor.classList.add("util-hidden");
    mensajeVacio.classList.add("util-hidden");
  }
}

// Función para renderizar el historial en la tabla
function renderizarHistorial(historial) {
  const tablaBody = document.getElementById("tabla-body");
  if (!tablaBody) return;
  
  tablaBody.innerHTML = "";

  // Ordenar historial por fecha de salida (más reciente primero)
  const historialOrdenado = [...historial].sort((a, b) => {
    const fechaA = new Date(a.horaSalida);
    const fechaB = new Date(b.horaSalida);
    return fechaB - fechaA; // Orden descendente (más reciente primero)
  });

  historialOrdenado.forEach((registro) => {
    const fila = document.createElement("tr");
    fila.setAttribute("role", "row");

    // Columna: Placa del vehículo
    const placa = document.createElement("td");
    placa.setAttribute("role", "cell");
    placa.textContent = registro.placa || '-';
    placa.style.fontWeight = '600'; // Hacer la placa más visible

    // Columna: Hora de entrada
    const entrada = document.createElement("td");
    entrada.setAttribute("role", "cell");
    entrada.textContent = formatearFecha(registro.horaEntrada);

    // Columna: Hora de salida
    const salida = document.createElement("td");
    salida.setAttribute("role", "cell");
    salida.textContent = formatearFecha(registro.horaSalida);

    // Columna: Duración (tiempo que se quedó)
    const duracion = document.createElement("td");
    duracion.setAttribute("role", "cell");
    const tiempoEstancia = calcularDuracion(
      registro.horaEntrada,
      registro.horaSalida
    );
    duracion.textContent = tiempoEstancia;
    duracion.style.color = 'var(--color-primary, #2563eb)'; // Resaltar el tiempo

    // Columna: Tarifa pagada (monto)
    const tarifa = document.createElement("td");
    tarifa.setAttribute("role", "cell");
    const montoPagado = registro.tarifaPagada || 0;
    tarifa.textContent = formatearMoneda(montoPagado);
    tarifa.style.fontWeight = '600';
    tarifa.style.color = 'var(--color-success, #10b981)'; // Resaltar el monto en verde

    fila.appendChild(placa);
    fila.appendChild(entrada);
    fila.appendChild(salida);
    fila.appendChild(duracion);
    fila.appendChild(tarifa);

    tablaBody.appendChild(fila);
  });
}


// Función para cargar vehículos estacionados actualmente
async function cargarVehiculosEstacionados() {
  const mensajeCarga = document.getElementById("mensaje-carga-estacionados");
  const tablaContenedor = document.getElementById("tabla-estacionados-contenedor");
  const tablaBody = document.getElementById("tabla-estacionados-body");
  const mensajeVacio = document.getElementById("mensaje-vacio-estacionados");

  try {
    const apiBase = ParkSmart.Config ? ParkSmart.Config.getApiBase() : API_BASE;
    const response = await fetch(`${apiBase}/coches`);
    
    if (!response.ok) {
      throw new Error(`Error del servidor: ${response.status}`);
    }

    const contentType = response.headers.get('content-type');
    if (!contentType || !contentType.includes('application/json')) {
      throw new Error('Respuesta no es JSON');
    }

    const vehiculos = await response.json();

    mensajeCarga.classList.add("util-hidden");

    if (vehiculos.length === 0) {
      mensajeVacio.classList.remove("util-hidden");
      tablaContenedor.classList.add("util-hidden");
      return;
    }

    mensajeVacio.classList.add("util-hidden");
    tablaContenedor.classList.remove("util-hidden");

    // Renderizar vehículos estacionados
    renderizarVehiculosEstacionados(vehiculos);
  } catch (error) {
    console.error("Error al cargar vehículos estacionados:", error);
    let mensajeError = "Error al cargar los vehículos estacionados. Por favor recargue la página.";
    if (error.message.includes('Servidor no encontrado') || error.message.includes('no es JSON')) {
      mensajeError = "⚠️ " + error.message + " Accede desde http://localhost:8080";
    }
    mensajeCarga.textContent = mensajeError;
    mensajeCarga.classList.remove("util-hidden");
    tablaContenedor.classList.add("util-hidden");
    mensajeVacio.classList.add("util-hidden");
  }
}

// Función para renderizar vehículos estacionados en la tabla
function renderizarVehiculosEstacionados(vehiculos) {
  const tablaBody = document.getElementById("tabla-estacionados-body");
  if (!tablaBody) return;
  
  tablaBody.innerHTML = "";

  vehiculos.forEach((vehiculo) => {
    const fila = document.createElement("tr");
    fila.setAttribute("role", "row");

    // Columna: Placa del vehículo
    const placa = document.createElement("td");
    placa.setAttribute("role", "cell");
    placa.textContent = vehiculo.placa || '-';
    placa.style.fontWeight = '600'; // Hacer la placa más visible

    // Columna: Hora de entrada
    const entrada = document.createElement("td");
    entrada.setAttribute("role", "cell");
    entrada.textContent = formatearFecha(vehiculo.horaEntrada);

    // Columna: Tiempo estacionado (desde entrada hasta ahora)
    const tiempoEstacionado = document.createElement("td");
    tiempoEstacionado.setAttribute("role", "cell");
    const tiempo = calcularTiempoTranscurrido(vehiculo.horaEntrada);
    tiempoEstacionado.textContent = tiempo;
    tiempoEstacionado.style.color = 'var(--color-primary, #2563eb)'; // Resaltar el tiempo
    tiempoEstacionado.style.fontWeight = '500';

    fila.appendChild(placa);
    fila.appendChild(entrada);
    fila.appendChild(tiempoEstacionado);

    tablaBody.appendChild(fila);
  });
}

// Función para actualizar el tiempo transcurrido de los vehículos estacionados
function actualizarTiemposEstacionados() {
  const tablaBody = document.getElementById("tabla-estacionados-body");
  if (!tablaBody) return;

  const filas = tablaBody.querySelectorAll("tr");
  filas.forEach((fila) => {
    const celdas = fila.querySelectorAll("td");
    if (celdas.length >= 3) {
      const entradaCell = celdas[1];
      const tiempoCell = celdas[2];
      
      // Obtener la fecha de entrada del texto
      const textoEntrada = entradaCell.textContent;
      // Convertir el texto de fecha a Date (formato: DD/MM/YYYY HH:MM AM/PM)
      const partes = textoEntrada.split(' ');
      if (partes.length >= 2) {
        const fechaParte = partes[0].split('/');
        const horaParte = partes[1].split(':');
        const ampm = partes[2];
        
        if (fechaParte.length === 3 && horaParte.length === 2) {
          const dia = parseInt(fechaParte[0]);
          const mes = parseInt(fechaParte[1]) - 1; // Mes es 0-indexed
          const año = parseInt(fechaParte[2]);
          let horas = parseInt(horaParte[0]);
          const minutos = parseInt(horaParte[1]);
          
          // Convertir a formato 24 horas
          if (ampm === 'PM' && horas !== 12) {
            horas += 12;
          } else if (ampm === 'AM' && horas === 12) {
            horas = 0;
          }
          
          const fechaEntrada = new Date(año, mes, dia, horas, minutos);
          const tiempo = calcularTiempoTranscurrido(fechaEntrada);
          tiempoCell.textContent = tiempo;
        }
      }
    }
  });
}

// Cargar datos al cargar la página
document.addEventListener("DOMContentLoaded", () => {
  // Cargar vehículos estacionados inmediatamente
  cargarVehiculosEstacionados();
  
  // Cargar historial inmediatamente
  cargarHistorial();
  
  // Actualizar vehículos estacionados cada 5 segundos (para actualizar tiempos)
  setInterval(() => {
    cargarVehiculosEstacionados();
  }, 5000);
  
  // Actualizar historial cada 10 segundos
  setInterval(cargarHistorial, 10000);
});

