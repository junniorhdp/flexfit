# FlexFit - Sistema de Gestión de Rutinas de Ejercicios

<div align="center">
  <img src="https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" alt="Spring Boot">
  <img src="https://img.shields.io/badge/Java-17+-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java">
  <img src="https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white" alt="MySQL">
  <img src="https://img.shields.io/badge/Thymeleaf-3.x-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white" alt="Thymeleaf">
  <img src="https://img.shields.io/badge/TailwindCSS-3.x-06B6D4?style=for-the-badge&logo=tailwindcss&logoColor=white" alt="Tailwind CSS">
</div>

<div align="center">
  <h3>Aplicación web para la gestión personalizada de rutinas de entrenamiento</h3>
  <p><strong>SENA - Ficha 3197833 | Tecnólogo en Programación de Software</strong></p>
  <p>Fecha: 09/12/2025</p>
  <p>JUNIOR ALIRIO GONZALEZ OCHOA</p>
  <p>ABEL FELIPE AGUILERA</p>
</div>

---

## Descripción

**FlexFit** es una aplicación web desarrollada con Spring Boot que permite a los usuarios crear, gestionar y personalizar sus rutinas de entrenamiento de manera intuitiva y eficiente. El sistema cuenta con dos roles principales: administrador y usuario, cada uno con funcionalidades específicas adaptadas a sus necesidades.

---

## Problema a Resolver

En la actualidad, muchas personas que desean comenzar o mantener una rutina de ejercicios enfrentan diversos obstáculos:

- **Desorganización**: Dificultad para planificar y recordar qué ejercicios realizar cada día.
- **Falta de personalización**: Las aplicaciones genéricas no se adaptan a las necesidades individuales de cada usuario.
- **Información dispersa**: Los ejercicios y sus instrucciones están distribuidos en múltiples fuentes (videos, imágenes, notas).
- **Sin seguimiento**: No existe un registro centralizado del progreso y las rutinas realizadas.
- **Complejidad**: Muchas aplicaciones son difíciles de usar para personas sin conocimientos técnicos.

---

## Solución Propuesta

FlexFit ofrece una solución integral que aborda cada uno de estos problemas:

| Problema | Solución FlexFit |
|----------|------------------|
| Desorganización | Dashboard personalizado con calendario y acceso rápido a rutinas |
| Falta de personalización | Creación de rutinas propias con ejercicios del catálogo |
| Información dispersa | Catálogo centralizado de ejercicios con descripciones y videos |
| Sin seguimiento | Sistema de gestión de rutinas por usuario |
| Complejidad | Interfaz intuitiva y moderna con diseño responsive |

---

## Funcionalidades

### Para Administradores
- Gestión completa de ejercicios (CRUD)
- Visualización de estadísticas generales del sistema
- Administración de usuarios
- Control del catálogo de ejercicios disponibles

### Para Usuarios
- Registro e inicio de sesión seguro
- Dashboard personalizado con estadísticas propias
- Creación y gestión de rutinas personales
- Agregar ejercicios del catálogo a sus rutinas
- Visualización de ejercicios con detalles y videos
- Calendario interactivo

---

## Tecnologías Utilizadas

### Backend
- **Java 17+** - Lenguaje de programación principal
- **Spring Boot 3.x** - Framework de desarrollo
- **Spring Security** - Autenticación y autorización
- **Spring Data JPA** - Persistencia de datos
- **Hibernate** - ORM para mapeo objeto-relacional

### Frontend
- **Thymeleaf** - Motor de plantillas
- **Tailwind CSS** - Framework de estilos
- **JavaScript** - Interactividad del cliente

### Base de Datos
- **MySQL 8.0** - Sistema de gestión de base de datos

### Herramientas
- **Maven** - Gestión de dependencias
- **Git & GitHub** - Control de versiones

---

## Estructura del Proyecto

```

flexfit/
├── src/
│   ├── main/
│   │   ├── java/com/flexfit/flexfit/
│   │   │   ├── config/          # Configuraciones de seguridad
│   │   │   ├── controller/      # Controladores MVC
│   │   │   ├── model/           # Entidades JPA
│   │   │   ├── repository/      # Repositorios Spring Data
│   │   │   ├── enums/           # Enumeraciones
│   │   │   └── FlexfitApplication.java
│   │   └── resources/
│   │       ├── templates/       # Vistas Thymeleaf
│   │       │   ├── ejercicios/  # Vistas de ejercicios
│   │       │   ├── rutinas/     # Vistas de rutinas
│   │       │   └── *.html       # Vistas principales
│   │       └── application.properties
│   └── test/                    # Pruebas unitarias
├── pom.xml                      # Dependencias Maven
└── README.md

```plaintext

---

## Modelo de Datos

### Entidades Principales

```

Usuario
├── id (Long)
├── nombre (String)
├── email (String)
├── password (String)
└── rol (ADMIN/USER)

Ejercicio
├── id (Long)
├── nombre (String)
├── descripcion (String)
├── musculoPrimario (Enum)
├── tipoEntrenamiento (Enum)
└── urlVideo (String)

Rutina
├── id (Long)
├── nombre (String)
├── descripcion (String)
├── usuario (Usuario)
├── fechaCreacion (LocalDateTime)
└── ejercicios (List`<RutinaEjercicio>`)

RutinaEjercicio
├── id (Long)
├── rutina (Rutina)
├── ejercicio (Ejercicio)
├── series (Integer)
├── repeticiones (String)
└── orden (Integer)

```plaintext

---

## Instalación y Configuración

### Prerrequisitos
- Java JDK 17 o superior
- MySQL 8.0
- Maven 3.8+
- Git

### Pasos de Instalación

1. **Clonar el repositorio**
```bash
git clone https://github.com/junniorhdp/flexfit.git
cd flexfit
```

2. **Configurar la base de datos**


```sql
CREATE DATABASE flexfit_db;
```

3. **Configurar application.properties**


```plaintext
spring.datasource.url=jdbc:mysql://localhost:3306/flexfit_db
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
spring.jpa.hibernate.ddl-auto=update
```

4. **Ejecutar la aplicación**


```shellscript
mvn spring-boot:run
```

5. **Acceder a la aplicación**


```plaintext
http://localhost:8080
```

---

## Capturas de Pantalla

### Dashboard de Usuario
<img width="1865" height="915" alt="image" src="https://github.com/user-attachments/assets/faaa00c2-9bc6-45e7-af79-92f06de725db" />

Vista principal con estadísticas, calendario y acceso rápido a rutinas.

### Catálogo de Ejercicios
<img width="1863" height="915" alt="image" src="https://github.com/user-attachments/assets/b21c0f42-c9b9-4b33-8073-19fb65e6e245" />


Listado completo de ejercicios con filtros por músculo y tipo de entrenamiento.

### Gestión de Rutinas

<img width="1864" height="901" alt="image" src="https://github.com/user-attachments/assets/4cc82206-9070-47fd-aaee-8012d3d84480" />

Interfaz para crear, editar y gestionar rutinas personalizadas.

## Equipo de Desarrollo

**SENA - Centro de Servicios Financieros****Tecnólogo en Programación de Software****Ficha: 3197833**

---

## Estado del Proyecto

El proyecto se encuentra en desarrollo activo con las siguientes funcionalidades implementadas:

- Sistema de autenticación y autorización
- CRUD de ejercicios (Administrador)
- CRUD de rutinas (Usuario)
- Catálogo de ejercicios para usuarios
- Dashboard personalizado
- Calendario interactivo
- Perfil de usuario en proceso


---

## Licencia

Este proyecto fue desarrollado con fines educativos como parte del programa de formación del SENA.

---

<div>`<p>``<strong>`FlexFit`</strong>` - Tu compañero de entrenamiento`</p>`
`<p>`Desarrollado con dedicación por la Ficha 3197833`</p>`
`<p>`SENA 2025`</p>`

</div>
