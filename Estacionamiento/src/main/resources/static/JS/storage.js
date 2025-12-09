/**
 * Módulo de gestión de localStorage para el sistema de estacionamiento
 * Reemplaza la funcionalidad de base de datos
 */

const StorageManager = {
    // Claves para localStorage
    KEYS: {
        VEHICULOS_ESTACIONADOS: 'parking_vehiculos_estacionados',
        FILA_ESPERA: 'parking_fila_espera',
        HISTORIAL_SALIDAS: 'parking_historial_salidas',
        TARIFAS: 'parking_tarifas',
        CONFIGURACION: 'parking_configuracion'
    },

    /**
     * Inicializa los datos por defecto si no existen
     */
    inicializar() {
        console.log('🔧 Inicializando localStorage...');
        
        // Inicializar configuración
        if (!this.getConfiguracion()) {
            this.setConfiguracion({
                capacidadMaxima: 20
            });
            console.log('✅ Configuración inicializada');
        }

        // Inicializar tarifas
        if (!this.getTarifas()) {
            this.setTarifas({
                tarifaPorMinuto: 1.5,
                tarifa1Minuto: 1.5,
                tarifa0_1Hora: 5.0,
                tarifa1_2Horas: 10.0,
                tarifa2MasHoras: 15.0,
                tarifaMaximaDiaria: 0.0,
                tarifaMaximaSemanal: 0.0,
                tarifaTicketPerdido: 0.0
            });
            console.log('✅ Tarifas inicializadas');
        }

        // Inicializar arrays vacíos si no existen
        if (!this.getVehiculosEstacionados()) {
            this.setVehiculosEstacionados([]);
        }
        if (!this.getFilaEspera()) {
            this.setFilaEspera([]);
        }
        if (!this.getHistorialSalidas()) {
            this.setHistorialSalidas([]);
        }

        console.log('✅ localStorage inicializado correctamente');
    },

    // ============================================
    // VEHÍCULOS ESTACIONADOS
    // ============================================

    getVehiculosEstacionados() {
        try {
            const data = localStorage.getItem(this.KEYS.VEHICULOS_ESTACIONADOS);
            return data ? JSON.parse(data) : [];
        } catch (e) {
            console.error('Error al leer vehículos estacionados:', e);
            return [];
        }
    },

    setVehiculosEstacionados(vehiculos) {
        try {
            localStorage.setItem(this.KEYS.VEHICULOS_ESTACIONADOS, JSON.stringify(vehiculos));
            return true;
        } catch (e) {
            console.error('Error al guardar vehículos estacionados:', e);
            return false;
        }
    },

    agregarVehiculoEstacionado(vehiculo) {
        const vehiculos = this.getVehiculosEstacionados();
        vehiculos.push({
            placa: vehiculo.placa,
            horaEntrada: vehiculo.horaEntrada || new Date().toISOString(),
            id: vehiculo.id || Date.now()
        });
        return this.setVehiculosEstacionados(vehiculos);
    },

    eliminarVehiculoEstacionado(placa) {
        const vehiculos = this.getVehiculosEstacionados();
        const filtrados = vehiculos.filter(v => v.placa.toUpperCase() !== placa.toUpperCase());
        return this.setVehiculosEstacionados(filtrados);
    },

    buscarVehiculoEstacionado(placa) {
        const vehiculos = this.getVehiculosEstacionados();
        return vehiculos.find(v => v.placa.toUpperCase() === placa.toUpperCase()) || null;
    },

    // ============================================
    // FILA DE ESPERA
    // ============================================

    getFilaEspera() {
        try {
            const data = localStorage.getItem(this.KEYS.FILA_ESPERA);
            return data ? JSON.parse(data) : [];
        } catch (e) {
            console.error('Error al leer fila de espera:', e);
            return [];
        }
    },

    setFilaEspera(fila) {
        try {
            localStorage.setItem(this.KEYS.FILA_ESPERA, JSON.stringify(fila));
            return true;
        } catch (e) {
            console.error('Error al guardar fila de espera:', e);
            return false;
        }
    },

    agregarAFilaEspera(vehiculo) {
        const fila = this.getFilaEspera();
        fila.push({
            placa: vehiculo.placa,
            horaEntrada: vehiculo.horaEntrada || new Date().toISOString(),
            posicion: fila.length + 1,
            id: vehiculo.id || Date.now()
        });
        return this.setFilaEspera(fila);
    },

    eliminarDeFilaEspera(placa) {
        const fila = this.getFilaEspera();
        const filtrados = fila.filter(v => v.placa.toUpperCase() !== placa.toUpperCase());
        // Reordenar posiciones
        filtrados.forEach((v, index) => {
            v.posicion = index + 1;
        });
        return this.setFilaEspera(filtrados);
    },

    obtenerPrimeroEnFila() {
        const fila = this.getFilaEspera();
        return fila.length > 0 ? fila[0] : null;
    },

    // ============================================
    // HISTORIAL DE SALIDAS
    // ============================================

    getHistorialSalidas() {
        try {
            const data = localStorage.getItem(this.KEYS.HISTORIAL_SALIDAS);
            return data ? JSON.parse(data) : [];
        } catch (e) {
            console.error('Error al leer historial de salidas:', e);
            return [];
        }
    },

    setHistorialSalidas(historial) {
        try {
            localStorage.setItem(this.KEYS.HISTORIAL_SALIDAS, JSON.stringify(historial));
            return true;
        } catch (e) {
            console.error('Error al guardar historial de salidas:', e);
            return false;
        }
    },

    agregarHistorialSalida(registro) {
        const historial = this.getHistorialSalidas();
        historial.unshift({ // Agregar al inicio (LIFO)
            placa: registro.placa,
            horaEntrada: registro.horaEntrada,
            horaSalida: registro.horaSalida || new Date().toISOString(),
            tarifaPagada: registro.tarifaPagada || 0,
            metodoPago: registro.metodoPago || null,
            id: registro.id || Date.now()
        });
        // Mantener solo los últimos 1000 registros
        if (historial.length > 1000) {
            historial.splice(1000);
        }
        return this.setHistorialSalidas(historial);
    },

    limpiarHistorial() {
        return this.setHistorialSalidas([]);
    },

    // ============================================
    // TARIFAS
    // ============================================

    getTarifas() {
        try {
            const data = localStorage.getItem(this.KEYS.TARIFAS);
            return data ? JSON.parse(data) : null;
        } catch (e) {
            console.error('Error al leer tarifas:', e);
            return null;
        }
    },

    setTarifas(tarifas) {
        try {
            localStorage.setItem(this.KEYS.TARIFAS, JSON.stringify(tarifas));
            return true;
        } catch (e) {
            console.error('Error al guardar tarifas:', e);
            return false;
        }
    },

    // ============================================
    // CONFIGURACIÓN
    // ============================================

    getConfiguracion() {
        try {
            const data = localStorage.getItem(this.KEYS.CONFIGURACION);
            return data ? JSON.parse(data) : null;
        } catch (e) {
            console.error('Error al leer configuración:', e);
            return null;
        }
    },

    setConfiguracion(config) {
        try {
            localStorage.setItem(this.KEYS.CONFIGURACION, JSON.stringify(config));
            return true;
        } catch (e) {
            console.error('Error al guardar configuración:', e);
            return false;
        }
    },

    // ============================================
    // UTILIDADES
    // ============================================

    limpiarTodo() {
        try {
            Object.values(this.KEYS).forEach(key => {
                localStorage.removeItem(key);
            });
            console.log('✅ localStorage limpiado completamente');
            return true;
        } catch (e) {
            console.error('Error al limpiar localStorage:', e);
            return false;
        }
    },

    exportarDatos() {
        return {
            vehiculosEstacionados: this.getVehiculosEstacionados(),
            filaEspera: this.getFilaEspera(),
            historialSalidas: this.getHistorialSalidas(),
            tarifas: this.getTarifas(),
            configuracion: this.getConfiguracion()
        };
    },

    importarDatos(datos) {
        try {
            if (datos.vehiculosEstacionados) this.setVehiculosEstacionados(datos.vehiculosEstacionados);
            if (datos.filaEspera) this.setFilaEspera(datos.filaEspera);
            if (datos.historialSalidas) this.setHistorialSalidas(datos.historialSalidas);
            if (datos.tarifas) this.setTarifas(datos.tarifas);
            if (datos.configuracion) this.setConfiguracion(datos.configuracion);
            console.log('✅ Datos importados correctamente');
            return true;
        } catch (e) {
            console.error('Error al importar datos:', e);
            return false;
        }
    }
};

// Inicializar al cargar el script
if (typeof window !== 'undefined') {
    StorageManager.inicializar();
}

