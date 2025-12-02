# 🚀 Sistema de Gestión de Solicitudes (Aprobaciones)

Este proyecto implementa breve y sencillo sistema de gestión de flujo de trabajo donde los usuarios pueden crear 
solicitudes que deben ser revisadas y aprobadas o rechazadas por un usuario designado (Aprobador). 
La estructuras  sistema se divide en un **Backend** (Spring Boot) y un **Frontend** (HTML/JavaScript puro).

-----

## 🧱 Estructura del Proyecto

El proyecto sigue una estructura de desarrollo estándar:

```
/Proyecto
├── /backend/                  # Código Java (Spring Boot)
│   ├── src/main/java/...
│   └── ...
└── /frontend/                 # Código Web (HTML, CSS, JS)
    ├── /html/
    │   ├── bandeja.html          # Bandeja de Aprobación
    │   ├── crear_solicitud.html  # Formulario de Creación
    │   └── mis_solicitudes.html  # Historial de Solicitudes del Solicitante
    ├── /js/
    │   ├── user.js             # Función global de usuario activo
    │   ├── crear_solicitud.js    # Lógica de envío del formulario
    │   └── mis_solicitudes.js    # Lógica para mostrar solicitudes
    └── /css/
        └── styles.css
```

-----

## ⚙️ Backend (Spring Boot API)

El Backend maneja la lógica de negocio, la persistencia y la exposición de los **endpoints API** a través del framework **SpringBoot**.

### 🎯 Componentes Clave

  * **`SolicitudController`**: Gestiona las peticiones **`POST`** (Creación) y **`GET`** (Consulta).
  * **`UsuarioRepository`**: Se encarga de buscar y validar los IDs de los usuarios (Solicitante y Aprobador) antes de crear una solicitud.

-----

## 🖥️ Frontend (HTML/CSS/JavaScript)

El Frontend maneja la interfaz y garantiza que los datos enviados a la API sean correctos.

### 🔑 Archivos y Funcionalidades

| Archivo | Funcionalidad Principal |
| :--- | :--- |
| **`mis_solicitudes.html`** | Visualización de solicitudes creadas. |
| **`crear_solicitud.js`** | Envío de datos al endpoint `/api/solicitudes`. |
| **`mis_solicitudes.js`** | Renderización de la lista de solicitudes. |

-----


## 🏃 Guía de Ejecución (Usando Docker)

Para poner en marcha el sistema de solicitudes, sigue estos pasos utilizando Docker Compose.

### 1\. ⚙️ Requisitos Previos

Asegúrate de tener instalado:

  * **Docker**
  * **Docker Compose**

### 2\. 🚀 Puesta en Marcha

Navega al directorio raíz del proyecto (donde se encuentra el archivo `docker-compose.yml`) y ejecuta el siguiente comando:

```bash
docker compose up --build -d
```

  * `--build`: Fuerza la reconstrucción de las imágenes (útil si hiciste cambios recientes en el código).
  * `-d`: Ejecuta los contenedores en segundo plano (`detached` mode).

### 3\. ✅ Acceder a la Aplicación

Una vez que los contenedores estén levantados:

  * **Backend (Spring Boot)**: La API REST estará disponible internamente en el contenedor `http://localhost:8080/api/solicitudes`.
  * **Frontend (Web)**: La interfaz de usuario estará disponible en tu navegador en **http://localhost/**

### 4\. 📝 Flujo de Prueba

1.  Abre la aplicación en el navegador (http://localhost/).
2.  Navega a **Crear Solicitud**.
3.  Completa los campos, asegurando que el **ID del Aprobador** sea un usuario válido (ej. **`u001`**).
4.  Navega a **Mis Solicitudes** para verificar que la nueva solicitud se muestre correctamente, incluyendo la **Descripción detallada**.

### 5\. 🛑 Detener y Limpiar

Para detener y eliminar los contenedores y las redes definidas:

```bash
docker compose down
```
