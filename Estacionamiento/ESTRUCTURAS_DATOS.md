# 📊 Estructuras de Datos del Proyecto ParkSmart

## Resumen de la Reestructuración

El proyecto ha sido completamente reestructurado para utilizar **7 estructuras de datos personalizadas** que trabajan en conjunto para optimizar el rendimiento y demostrar el uso de diferentes tipos de estructuras de datos.

---

## 🏗️ Estructuras Implementadas

### 1. **ListaSimple** (Lista Enlazada Simple)
- **Uso**: Almacenamiento secuencial de vehículos estacionados
- **Complejidad**: 
  - Búsqueda: O(n)
  - Inserción: O(1) al final, O(1) al inicio
  - Eliminación: O(n)
- **Ventaja**: Fácil de recorrer y mantener orden secuencial
- **Ubicación**: `lugaresOcupados` en `EstacionamientoRepository`

### 2. **ArbolBinario** (Árbol Binario de Búsqueda - BST)
- **Uso**: Búsqueda rápida de vehículos por placa
- **Complejidad**: 
  - Búsqueda: O(log n) promedio, O(n) peor caso
  - Inserción: O(log n) promedio
  - Eliminación: O(log n) promedio
- **Ventaja**: Búsqueda eficiente cuando hay muchos vehículos
- **Ubicación**: `arbolBusqueda` en `EstacionamientoRepository`

### 3. **Cola** (FIFO - First In First Out)
- **Uso**: Gestión de vehículos en espera
- **Complejidad**: 
  - Encolar: O(1)
  - Desencolar: O(1)
- **Ventaja**: Mantiene el orden de llegada (FIFO)
- **Ubicación**: `filaEspera` en `EstacionamientoRepository`

### 4. **ColaCircular** (Arreglo Circular)
- **Uso**: Rotación de espacios disponibles
- **Complejidad**: 
  - Insertar: O(1)
  - Quitar: O(1)
- **Ventaja**: Uso eficiente de memoria con tamaño fijo
- **Ubicación**: `espaciosRotativos` en `EstacionamientoRepository`

### 5. **Pila** (LIFO - Last In First Out)
- **Uso**: Historial de salidas (último en salir primero)
- **Complejidad**: 
  - Push: O(1)
  - Pop: O(1)
- **Ventaja**: Acceso rápido al registro más reciente
- **Ubicación**: `historialSalidas` en `EstacionamientoRepository`

### 6. **ListaDoble** (Lista Doblemente Enlazada)
- **Uso**: Historial con navegación bidireccional
- **Complejidad**: 
  - Búsqueda: O(n)
  - Inserción: O(1) al inicio o final
  - Eliminación: O(n)
- **Ventaja**: Navegación hacia adelante y atrás
- **Ubicación**: `historialNavegable` en `EstacionamientoRepository`

### 7. **ArregloDinamico** (Arreglo Dinámico)
- **Uso**: Estadísticas y reportes temporales
- **Complejidad**: 
  - Acceso por índice: O(1)
  - Inserción: O(1) amortizado
  - Redimensionamiento: O(n) cuando se llena
- **Ventaja**: Acceso aleatorio rápido y redimensionamiento automático
- **Ubicación**: `estadisticasTemporales` en `EstacionamientoRepository`

---

## 🔄 Flujo de Operaciones

### **Registro de Entrada**
1. **Verificación**: Usa `ArbolBinario` para buscar si el vehículo ya está estacionado (O(log n))
2. **Almacenamiento**: 
   - Agrega a `ListaSimple` (almacenamiento secuencial)
   - Agrega a `ArbolBinario` (búsqueda rápida)
   - Agrega a `ColaCircular` (rotación de espacios)
3. **Si está lleno**: Agrega a `Cola` (fila de espera FIFO)

### **Registro de Salida**
1. **Búsqueda**: Usa `ArbolBinario` para encontrar el vehículo (O(log n))
2. **Eliminación**: 
   - Elimina de `ListaSimple`
   - Elimina de `ArbolBinario`
3. **Historial**: 
   - Agrega a `Pila` (LIFO)
   - Agrega a `ListaDoble` (navegación bidireccional)
   - Agrega a `ArregloDinamico` (estadísticas)
4. **Cola de Espera**: Si hay vehículos en espera, mueve el primero de `Cola` a las estructuras principales

### **Búsqueda de Vehículo**
- Usa `ArbolBinario` para búsqueda rápida O(log n)
- También puede consultar `ListaSimple` para obtener posición

### **Reportes y Estadísticas**
- Usa `ArregloDinamico` para acceso rápido a registros
- Usa `ListaDoble` para navegación bidireccional en historial
- Usa `Pila` para obtener los registros más recientes

---

## 📈 Ventajas de la Reestructuración

1. **Rendimiento Mejorado**: 
   - Búsqueda de vehículos pasa de O(n) a O(log n) usando Árbol Binario
   - Acceso rápido a estadísticas con ArregloDinamico

2. **Flexibilidad**:
   - Múltiples estructuras para diferentes casos de uso
   - Navegación bidireccional en historial con ListaDoble

3. **Demostración de Conceptos**:
   - Uso práctico de todas las estructuras de datos fundamentales
   - Cada estructura se usa donde es más eficiente

4. **Sincronización con BD**:
   - Todas las estructuras se sincronizan automáticamente con MySQL
   - Los datos persisten entre reinicios de la aplicación

---

## 🗂️ Archivos de Estructuras

Todas las estructuras están en: `src/main/java/mx/edu/utez/Estacionamiento/structures/`

- `ListaSimple.java` - Lista enlazada simple
- `ListaDoble.java` - Lista doblemente enlazada
- `ListaCircular.java` - Lista circular
- `Cola.java` - Cola FIFO
- `ColaCircular.java` - Cola circular con arreglo
- `Pila.java` - Pila LIFO
- `ArbolBinario.java` - Árbol binario de búsqueda (NUEVO)
- `ArregloDinamico.java` - Arreglo dinámico (NUEVO)
- `Nodo*.java` - Nodos para cada estructura

---

## 🔍 Ejemplos de Uso

### Búsqueda con Árbol Binario
```java
Coche cocheBusqueda = new Coche("ABC123");
Coche encontrado = repository.getArbolBusqueda().buscar(cocheBusqueda);
```

### Agregar a múltiples estructuras
```java
// Al registrar entrada
repository.getLugaresOcupados().insertarAlFinal(coche);
repository.getArbolBusqueda().insertar(coche);
repository.getEspaciosRotativos().insertar(coche);
```

### Historial en múltiples formatos
```java
// Pila (LIFO - más reciente primero)
Pila<RegistroEstancia> historialLIFO = repository.getHistorialSalidas();

// ListaDoble (navegación bidireccional)
ListaDoble<RegistroEstancia> historialNavegable = repository.getHistorialNavegable();

// ArregloDinamico (acceso por índice)
ArregloDinamico<RegistroEstancia> estadisticas = repository.getEstadisticasTemporales();
```

---

## ✅ Estado de Implementación

- ✅ ListaSimple - Implementada y en uso
- ✅ ArbolBinario - Implementada y en uso (NUEVO)
- ✅ Cola - Implementada y en uso
- ✅ ColaCircular - Implementada y en uso
- ✅ Pila - Implementada y en uso
- ✅ ListaDoble - Implementada y en uso
- ✅ ArregloDinamico - Implementada y en uso (NUEVO)

Todas las estructuras están completamente integradas y sincronizadas con la base de datos MySQL.

