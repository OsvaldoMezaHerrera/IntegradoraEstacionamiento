# 🚗 ParkSmart - Sistema de Gestión de Estacionamiento

Sistema web completo para la gestión de estacionamientos desarrollado con Spring Boot y tecnologías web modernas, utilizando estructuras de datos personalizadas y persistencia en `localStorage`.

## 📋 Descripción

ParkSmart es una aplicación web que permite gestionar de manera eficiente un estacionamiento, incluyendo:
- Registro de entrada y salida de vehículos
- Cálculo automático de tarifas (por minuto)
- Historial completo de estancias
- Gestión de tarifas configurable
- Dashboard en tiempo real
- Búsqueda eficiente con Árbol Binario (BST)
- Persistencia de datos en `localStorage` del navegador

## 🛠️ Tecnologías Utilizadas

### Backend
- **Spring Boot 3.5.7** - Framework Java
- **Java 21** - Lenguaje de programación
- **Maven** - Gestión de dependencias
- **RESTful API** - Comunicación con el frontend

### Frontend
- **HTML5** - Estructura semántica
- **CSS3** - Estilos modernos con variables CSS
- **JavaScript (ES6+)** - Lógica del cliente
- **localStorage** - Persistencia de datos en el navegador
- **RESTful API** - Comunicación frontend-backend

### Estructuras de Datos Personalizadas

El proyecto implementa las siguientes estructuras de datos desde cero:

#### Estructuras Lineales
- **ListaSimple** - Almacenamiento de vehículos estacionados (lista enlazada simple)
- **ListaDoble** - Historial navegable bidireccional (lista doblemente enlazada)
- **ListaCircular** - Lista circular para rotación de espacios
- **Cola** - Gestión de vehículos en espera (FIFO - First In First Out)
- **ColaCircular** - Cola circular para rotación de espacios del estacionamiento
- **Pila** - Historial de salidas (LIFO - Last In First Out)
- **ArregloDinamico** - Estadísticas temporales con redimensionamiento automático

#### Estructuras No Lineales
- **ArbolBinario** - Árbol Binario de Búsqueda (BST) para búsqueda eficiente O(log n)
  - Implementa métodos recursivos para:
    - Recorrido inorden, preorden y postorden
    - Cálculo de altura y profundidad
    - Conteo de hojas y nodos internos
    - Búsqueda de mínimo y máximo
    - Validación de BST
    - Todas las operaciones utilizan recursividad

## 📁 Estructura del Proyecto

```
Estacionamiento/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── mx/edu/utez/Estacionamiento/
│   │   │       ├── config/          # Configuraciones (CORS, Web)
│   │   │       ├── controller/      # Controladores REST
│   │   │       ├── model/           # Modelos de datos (Coche, RegistroEstancia)
│   │   │       ├── repository/      # Repositorio de datos en memoria
│   │   │       ├── service/         # Lógica de negocio
│   │   │       ├── structures/      # Estructuras de datos personalizadas
│   │   │       │   ├── ArbolBinario.java      # BST con recursividad
│   │   │       │   ├── ListaSimple.java       # Lista enlazada simple
│   │   │       │   ├── ListaDoble.java        # Lista doblemente enlazada
│   │   │       │   ├── Cola.java              # Cola FIFO
│   │   │       │   ├── ColaCircular.java      # Cola circular
│   │   │       │   ├── Pila.java              # Pila LIFO
│   │   │       │   └── ArregloDinamico.java   # Arreglo dinámico
│   │   │       └── exception/       # Manejo de excepciones
│   │   └── resources/
│   │       ├── static/              # Recursos estáticos
│   │       │   ├── CSS/            # Hojas de estilo
│   │       │   ├── JS/             # Scripts JavaScript
│   │       │   │   ├── storage.js  # Gestión de localStorage
│   │       │   │   └── *.js        # Otros scripts
│   │       │   └── *.html         # Páginas HTML
│   │       └── application.properties
│   └── test/                       # Pruebas unitarias
├── pom.xml                         # Configuración Maven
└── README.md                       # Este archivo
```

## 🚀 Instalación y Configuración

### Requisitos Previos

- **Java 21** o superior
- **Maven 3.6+** o superior
- **Navegador web moderno** (Chrome, Firefox, Edge, Safari) con soporte para `localStorage`

### Pasos de Instalación

1. **Clonar o descargar el proyecto**
   ```bash
   git clone <url-del-repositorio>
   cd Estacionamiento
   ```

2. **Compilar el proyecto**
   ```bash
   mvn clean install
   ```

3. **Ejecutar la aplicación**
   ```bash
   mvn spring-boot:run
   ```
   
   O ejecutar directamente el JAR:
   ```bash
   java -jar target/Estacionamiento-0.0.1-SNAPSHOT.jar
   ```

4. **Acceder a la aplicación**
   - Abrir el navegador en: `http://localhost:8080`
   - La página principal se cargará automáticamente
   - Los datos se guardarán automáticamente en `localStorage` del navegador

### ⚠️ Nota sobre Persistencia

Este proyecto utiliza `localStorage` del navegador para persistir datos. Esto significa que:
- Los datos se guardan localmente en el navegador
- Cada navegador tiene su propio almacenamiento independiente
- Los datos persisten entre sesiones del mismo navegador
- Si se limpia el caché del navegador, se perderán los datos

## 🌐 Uso de la Aplicación

### Páginas Disponibles

1. **Dashboard (`/` o `/index.html`)**
   - Vista general del estacionamiento
   - Estadísticas en tiempo real
   - Acciones rápidas
   - Visualización de vehículos estacionados

2. **Registro de Entrada (`/registro.html`)**
   - Registrar nuevos vehículos
   - Captura de matrícula
   - Validación de capacidad disponible
   - Gestión automática de fila de espera

3. **Procesamiento de Salida (`/salida.html`)**
   - Buscar vehículo por matrícula (usando Árbol Binario)
   - Calcular tarifa automáticamente
   - Mostrar tiempo de estancia
   - Procesar salida

4. **Reportes (`/reportes.html`)**
   - Historial completo de estancias
   - Filtros y búsqueda
   - Visualización de estadísticas

5. **Gestión de Tarifas (`/tarifas.html`)**
   - Configurar tarifa por minuto
   - Visualización de tarifa por hora (calculada automáticamente)
   - Actualización en tiempo real

### API REST

La aplicación expone los siguientes endpoints:

#### Estadísticas
- `GET /api/estacionamiento/estadisticas` - Obtener estadísticas del estacionamiento
  - Retorna: capacidad máxima, lugares ocupados, lugares disponibles, vehículos en espera

#### Gestión de Vehículos
- `POST /api/estacionamiento/entrada` - Registrar entrada de vehículo
  - Body: `{"placa": "ABC123"}`
  - Retorna: mensaje de confirmación y estadísticas actualizadas

- `POST /api/estacionamiento/salida` - Registrar salida de vehículo
  - Body: `{"placa": "ABC123"}`
  - Retorna: información de tarifa, tiempo de estancia y estadísticas

- `GET /api/estacionamiento/vehiculo/{placa}` - Obtener información de vehículo
  - Retorna: información del vehículo si está estacionado

- `GET /api/estacionamiento/coches` - Obtener lista de vehículos estacionados
  - Retorna: lista completa de vehículos actualmente estacionados

- `GET /api/estacionamiento/espera` - Obtener vehículos en fila de espera
  - Retorna: lista de vehículos en espera

#### Historial
- `GET /api/estacionamiento/historial` - Obtener historial de salidas
  - Retorna: lista completa del historial de estancias

- `POST /api/estacionamiento/historial/limpiar` - Limpiar historial
  - Limpia todas las estructuras de historial (Pila, ListaDoble, ArregloDinamico)

#### Tarifas
- `GET /api/estacionamiento/tarifas` - Obtener configuración de tarifas
  - Retorna: `{"tarifaPorMinuto": 1.5, "tarifaPorHora": 90.0}`

- `POST /api/estacionamiento/tarifas` - Actualizar configuración de tarifas
  - Body: `{"tarifaPorMinuto": 2.0}`
  - Retorna: tarifas actualizadas

#### Árbol Binario (Recursividad)
- `GET /api/estacionamiento/arbol/estadisticas` - Obtener estadísticas del Árbol Binario
  - Retorna: tamaño, altura, número de hojas, nodos internos, mínimo, máximo, validación BST
  - **Demuestra el uso de recursividad** para calcular propiedades del árbol

- `GET /api/estacionamiento/arbol/vehiculos-ordenados` - Obtener vehículos ordenados (inorden)
  - Retorna: lista de vehículos ordenados por placa
  - **Demuestra el uso de recursividad** para recorrer el árbol

#### Endpoints Legacy (Compatibilidad)
- `GET /api/estacionamiento/entrada/{placa}` - Registrar entrada (método GET)
- `GET /api/estacionamiento/salida/{placa}` - Registrar salida (método GET)

## ⚙️ Configuración

### Persistencia con localStorage

El proyecto utiliza `localStorage` del navegador para guardar:
- Vehículos estacionados
- Fila de espera
- Historial de salidas
- Configuración de tarifas
- Configuración del estacionamiento

El módulo `storage.js` gestiona automáticamente la carga y guardado de datos.

### Variables de Entorno

Puedes configurar el puerto del servidor usando una variable de entorno:

```bash
export PORT=8080
java -jar target/Estacionamiento-0.0.1-SNAPSHOT.jar
```

O editar `src/main/resources/application.properties`:

```properties
server.port=8080
```

### Configuración de CORS

Para producción, edita `src/main/java/mx/edu/utez/Estacionamiento/config/WebConfig.java` y cambia:

```java
.allowedOrigins("*")  // Cambiar a dominios específicos
```

Por ejemplo:
```java
.allowedOrigins("https://tudominio.com", "https://www.tudominio.com")
```

## 📦 Despliegue en Producción

### Opción 1: JAR Ejecutable

1. **Generar JAR**
   ```bash
   mvn clean package
   ```

2. **Ejecutar JAR**
   ```bash
   java -jar target/Estacionamiento-0.0.1-SNAPSHOT.jar
   ```

### Opción 2: Servidor de Aplicaciones

1. Generar WAR (requiere configuración adicional en pom.xml)
2. Desplegar en Tomcat, Jetty u otro servidor compatible

### Opción 3: Docker (Opcional)

```dockerfile
FROM openjdk:21-jdk-slim
COPY target/Estacionamiento-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

## 🧪 Pruebas

Ejecutar pruebas unitarias:

```bash
mvn test
```

## 📝 Características

### Funcionalidades Principales
- ✅ Interfaz web moderna y responsive
- ✅ API RESTful completa
- ✅ Cálculo automático de tarifas (por minuto)
- ✅ Historial persistente de estancias
- ✅ Gestión configurable de tarifas
- ✅ Actualización en tiempo real
- ✅ CORS configurado para desarrollo
- ✅ Persistencia con `localStorage`

### Estructuras de Datos Implementadas
- ✅ **ListaSimple** - Lista enlazada simple para vehículos estacionados
- ✅ **ListaDoble** - Lista doblemente enlazada para historial navegable
- ✅ **ListaCircular** - Lista circular para rotación
- ✅ **Cola** - Cola FIFO para fila de espera
- ✅ **ColaCircular** - Cola circular para rotación de espacios
- ✅ **Pila** - Pila LIFO para historial de salidas
- ✅ **ArregloDinamico** - Arreglo dinámico para estadísticas
- ✅ **ArbolBinario** - Árbol Binario de Búsqueda (BST) con:
  - Búsqueda eficiente O(log n)
  - Métodos recursivos para recorrido (inorden, preorden, postorden)
  - Cálculo recursivo de altura y profundidad
  - Conteo recursivo de hojas y nodos internos
  - Búsqueda recursiva de mínimo y máximo
  - Validación recursiva de BST

### Algoritmos y Complejidad
- **Búsqueda en Árbol Binario**: O(log n) en promedio, O(n) en peor caso
- **Inserción en Árbol Binario**: O(log n) en promedio, O(n) en peor caso
- **Eliminación en Árbol Binario**: O(log n) en promedio, O(n) en peor caso
- **Recorrido del árbol**: O(n) - visita todos los nodos
- **Operaciones en ListaSimple**: O(n) para búsqueda, O(1) para inserción al final
- **Operaciones en Cola/Pila**: O(1) para inserción y eliminación

## 🔧 Solución de Problemas

### El servidor no inicia
- Verificar que el puerto 8080 no esté en uso
- Verificar que Java 21 esté instalado correctamente
- Revisar los logs de Spring Boot para errores

### Los archivos estáticos no se cargan
- Verificar que los archivos estén en `src/main/resources/static/`
- Limpiar y recompilar: `mvn clean install`
- Verificar que el servidor esté ejecutándose en `http://localhost:8080`

### Error de CORS
- Verificar la configuración en `WebConfig.java`
- Asegurarse de que el frontend esté en el mismo dominio o configurar CORS correctamente

### Los datos no persisten
- Verificar que el navegador tenga habilitado `localStorage`
- Verificar la consola del navegador para errores de JavaScript
- Asegurarse de que `storage.js` esté cargado correctamente

### El árbol binario no funciona correctamente
- Verificar que los vehículos tengan placas válidas (no null)
- Revisar los logs del servidor para errores
- Verificar que `Coche` implemente `Comparable<Coche>` correctamente

## 📊 Arquitectura del Sistema

### Flujo de Datos

1. **Entrada de Vehículo**:
   - Frontend envía placa → Backend
   - Backend inserta en `ListaSimple`, `ArbolBinario` y `ColaCircular`
   - Backend retorna confirmación → Frontend
   - Frontend guarda en `localStorage`

2. **Salida de Vehículo**:
   - Frontend envía placa → Backend
   - Backend busca en `ArbolBinario` (O(log n))
   - Backend calcula tarifa
   - Backend elimina de `ListaSimple` y `ArbolBinario`
   - Backend agrega a `Pila`, `ListaDoble` y `ArregloDinamico`
   - Backend retorna información → Frontend
   - Frontend actualiza `localStorage`

3. **Persistencia**:
   - Al cargar la página, `storage.js` carga datos de `localStorage`
   - Al realizar operaciones, `storage.js` guarda en `localStorage`
   - Backend mantiene datos en memoria durante la sesión

## 📄 Licencia

Este proyecto es parte de una integradora académica.

## 👥 Autores

Desarrollado como proyecto integrador de Estructura de Datos y Aplicaciones Web.

## 🔄 Versión

**Versión actual:** 0.0.1-SNAPSHOT

---

## 🎓 Notas Académicas

Este proyecto demuestra la implementación práctica de:

1. **Estructuras de Datos Lineales**:
   - Listas enlazadas (simple, doble, circular)
   - Colas (FIFO, circular)
   - Pilas (LIFO)
   - Arreglos dinámicos

2. **Estructuras de Datos No Lineales**:
   - Árbol Binario de Búsqueda (BST)
   - Operaciones recursivas en árboles

3. **Algoritmos**:
   - Búsqueda binaria en árboles
   - Recursividad para recorrido y cálculo de propiedades
   - Gestión de memoria dinámica

4. **Arquitectura de Software**:
   - Patrón MVC (Model-View-Controller)
   - API RESTful
   - Separación de responsabilidades
   - Inyección de dependencias

5. **Persistencia**:
   - Almacenamiento en memoria (backend)
   - Persistencia en cliente (`localStorage`)

