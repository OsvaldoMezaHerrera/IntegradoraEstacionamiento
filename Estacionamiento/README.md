# 🚗 ParkSmart - Sistema de Gestión de Estacionamiento

Sistema web completo para la gestión de estacionamientos desarrollado con Spring Boot y tecnologías web modernas.

## 📋 Descripción

ParkSmart es una aplicación web que permite gestionar de manera eficiente un estacionamiento, incluyendo:
- Registro de entrada y salida de vehículos
- Cálculo automático de tarifas
- Historial completo de estancias
- Gestión de tarifas configurable
- Dashboard en tiempo real

## 🛠️ Tecnologías Utilizadas

### Backend
- **Spring Boot 3.5.7** - Framework Java
- **Java 21** - Lenguaje de programación
- **Maven** - Gestión de dependencias
- **MySQL/MariaDB** - Base de datos (XAMPP)
- **Spring Data JPA** - Acceso a datos

### Frontend
- **HTML5** - Estructura semántica
- **CSS3** - Estilos modernos con Tailwind CSS
- **JavaScript (ES6+)** - Lógica del cliente
- **RESTful API** - Comunicación frontend-backend

### Base de Datos
- **MySQL/MariaDB** - Base de datos relacional
- **XAMPP** - Servidor local de desarrollo
- **5 Tablas principales**: vehiculos_estacionados, fila_espera, historial_salidas, configuracion_estacionamiento, tarifas

### Estructuras de Datos
- **ListaSimple** - Almacenamiento de vehículos estacionados
- **Cola** - Gestión de vehículos en espera
- **Pila** - Historial de salidas (LIFO)

## 📁 Estructura del Proyecto

```
Estacionamiento/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── mx/edu/utez/Estacionamiento/
│   │   │       ├── config/          # Configuraciones (CORS, Web)
│   │   │       ├── controller/      # Controladores REST
│   │   │       ├── model/           # Modelos de datos
│   │   │       ├── repository/      # Repositorio de datos
│   │   │       ├── service/         # Lógica de negocio
│   │   │       └── structures/      # Estructuras de datos personalizadas
│   │   └── resources/
│   │       ├── static/              # Recursos estáticos
│   │       │   ├── CSS/            # Hojas de estilo
│   │       │   ├── JS/             # Scripts JavaScript
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
- **XAMPP** (para MySQL/MariaDB)
- **Navegador web moderno** (Chrome, Firefox, Edge, Safari)

### Pasos de Instalación

1. **Clonar o descargar el proyecto**
   ```bash
   git clone <url-del-repositorio>
   cd Estacionamiento
   ```

2. **Configurar la base de datos (XAMPP)**
   - Iniciar XAMPP y activar MySQL
   - Abrir phpMyAdmin: `http://localhost/phpmyadmin`
   - Ejecutar el script: `database/00_install_all.sql`
   - Ver instrucciones detalladas en: `database/INSTRUCCIONES_INSTALACION.md`

3. **Configurar Spring Boot para usar la base de datos**
   - Editar `src/main/resources/application.properties`
   - Descomentar las líneas de configuración de base de datos
   - Ajustar usuario y contraseña si es necesario

4. **Compilar el proyecto**
   ```bash
   mvn clean install
   ```

5. **Ejecutar la aplicación**
   ```bash
   mvn spring-boot:run
   ```
   
   O ejecutar directamente el JAR:
   ```bash
   java -jar target/Estacionamiento-0.0.1-SNAPSHOT.jar
   ```

6. **Acceder a la aplicación**
   - Abrir el navegador en: `http://localhost:8080`
   - La página principal se cargará automáticamente

## 🌐 Uso de la Aplicación

### Páginas Disponibles

1. **Dashboard (`/` o `/index.html`)**
   - Vista general del estacionamiento
   - Estadísticas en tiempo real
   - Acciones rápidas

2. **Registro de Entrada (`/registro.html`)**
   - Registrar nuevos vehículos
   - Captura de matrícula y modelo

3. **Procesamiento de Salida (`/salida.html`)**
   - Buscar vehículo por matrícula
   - Calcular tarifa
   - Procesar pago y salida

4. **Reportes (`/reportes.html`)**
   - Historial completo de estancias
   - Filtros y búsqueda

5. **Gestión de Tarifas (`/tarifas.html`)**
   - Configurar tarifas por tiempo
   - Establecer límites diarios/semanales
   - Tarifa por ticket perdido

### API REST

La aplicación expone los siguientes endpoints:

#### Estadísticas
- `GET /api/estacionamiento/estadisticas` - Obtener estadísticas del estacionamiento

#### Gestión de Vehículos
- `POST /api/estacionamiento/entrada` - Registrar entrada de vehículo
- `POST /api/estacionamiento/salida` - Registrar salida de vehículo
- `GET /api/estacionamiento/vehiculo/{placa}` - Obtener información de vehículo

#### Historial
- `GET /api/estacionamiento/historial` - Obtener historial de salidas

#### Tarifas
- `GET /api/estacionamiento/tarifas` - Obtener configuración de tarifas
- `POST /api/estacionamiento/tarifas` - Actualizar configuración de tarifas

## ⚙️ Configuración

### Base de Datos

La aplicación utiliza MySQL/MariaDB a través de XAMPP. Para configurar:

1. **Crear la base de datos**: Ejecuta los scripts en `database/`
2. **Configurar conexión**: Edita `src/main/resources/application.properties`
3. **Verificar conexión**: Revisa los logs al iniciar la aplicación

Para más detalles, consulta: `database/README.md` y `database/INSTRUCCIONES_INSTALACION.md`

### Variables de Entorno

Puedes configurar el puerto del servidor usando una variable de entorno:

```bash
export PORT=8080
java -jar target/Estacionamiento-0.0.1-SNAPSHOT.jar
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

- ✅ Interfaz web moderna y responsive
- ✅ API RESTful completa
- ✅ Cálculo automático de tarifas
- ✅ Historial persistente de estancias
- ✅ Gestión configurable de tarifas
- ✅ Actualización en tiempo real
- ✅ CORS configurado para desarrollo
- ✅ Estructuras de datos personalizadas (Lista, Cola, Pila)

## 🔧 Solución de Problemas

### El servidor no inicia
- Verificar que el puerto 8080 no esté en uso
- Verificar que Java 21 esté instalado correctamente

### Los archivos estáticos no se cargan
- Verificar que los archivos estén en `src/main/resources/static/`
- Limpiar y recompilar: `mvn clean install`

### Error de CORS
- Verificar la configuración en `WebConfig.java`
- Asegurarse de que el frontend esté en el mismo dominio o configurar CORS correctamente

## 📄 Licencia

Este proyecto es parte de una integradora académica.

## 👥 Autores

Desarrollado como proyecto integrador de Estructura de Datos y Aplicaciones Web.

## 🔄 Versión

**Versión actual:** 0.0.1-SNAPSHOT

---

**Nota:** Este es un proyecto académico. Para uso en producción, considerar:
- ✅ Base de datos persistente (MySQL/MariaDB implementada)
- Agregar autenticación y autorización
- Implementar logging más robusto
- Configurar HTTPS
- Agregar pruebas automatizadas más completas
- Configurar respaldos automáticos de la base de datos

