# Vitae - Proyecto de Aplicaciones Web

El presente trabajo, realizado para la materia *Proyecto de Aplicaciones Web*, consiste en el desarrollo de una aplicación web que facilita la reserva de turnos médicos, unificando las distintas obras sociales.
Cuenta principalmente con las siguientes funcionalidades:

- <b>Reserva de Turnos</b>: Se permite a los usuarios reservar turnos médicos con aquellos que atiendan con su obra social.

- <b>Gestión de Turnos</b>: Se permite a los usuarios gestionar sus turnos, como por ejemplo cancelando aquellos a los que no puedan asistir.

- <b>Sistema de Búsqueda Avanzada</b>: Se ofrece un sistema de búsqueda avanzada que permite a los usuarios filtrar los médicos por especialidad, obra social y disponibilidad horaria.

- <b>Notificaciones via Mail</b>: Se envían notificaciones via mail a los usuarios para notificarlos de actualizaciones en sus turnos, así como recordarles los próximos.

- <b>Manejo de Archivos</b>: Se ofrece a los usuarios un sistema de subida de archivos para cargar tanto documentación requerida para los pacientes como resultados para los médicos.

- <b>Manejo de Oficinas y Disponibilidad</b>: Se ofrece a los doctores la posibilidad de manejar su disponibilidad, sus ausencias y los detalles relacionados a sus oficinas.

- <b>Perfil Público</b>: Los doctores cuentan con perfiles públicos que pueden ser editados y accedidos por los pacientes.

- <b>Sistema de Calificaciones</b>: Se permite a los usuarios calificar la atención del médico para ofrecer referencias a otros usuarios.

<details>
  <summary>Contenidos</summary>
  <ol>
    <li><a href="#instalación">Instalación</a></li>
    <li><a href="#usuarios-de-testing">Usuarios de Testing</a></li>
    <li><a href="#correcciones">Correcciones</a></li>
    <li><a href="#integrantes">Integrantes</a></li>
  </ol>
</details>

## Instalación:

El deploy del proyecto se encuentra en los servidores del ITBA, y se puede acceder mediante el siguiente [enlace](http://pawserver.it.itba.edu.ar/paw-2025a-11/).

Para la instalación local, el proyecto se divide en dos módulos: **Backend (API)** y **Frontend (Cliente Web)**. Se requiere una base de datos PostgreSQL.

Se debe clonar el repositorio mediante:

- HTTPS:
```sh
  git clone https://[USERNAME]@bitbucket.org/itba/paw-2025a-11.git
```

* SSH:
```sh
git clone git@bitbucket.org:itba/paw-2025a-11.git
```

### 1. Backend (API)

Dentro de la carpeta `/backend`, se debe generar el archivo `application.properties` en `src/main/resources` con la configuración correspondiente.

```properties
datasource.url=jdbc:postgresql://localhost:5432/vitae
datasource.username=postgres
datasource.password=postgres
app.base-url=http://localhost:8080/api

# Configuración Mail
mail.username=
mail.password=
mail.port=
mail.host=
```

#### Configuración JWT:
Se debe generar un archivo llamado jwtSecret.key dentro de la carpeta src/main/resources.
Este archivo debe contener únicamente una clave secreta (string aleatoria larga) que será utilizada para firmar los tokens de seguridad.


A continuación, se debe compilar el proyecto y ejecutar la API:

```sh
cd backend
mvn clean package
mvn jetty:run
```

### 2. Frontend (Cliente)

Se requiere tener instalado **Node.js** y **npm**.
Dirigirse a la carpeta del frontend e instalar las dependencias:

```sh
cd frontend
npm install
```

Crear un archivo `.env` en la raíz de la carpeta `frontend` para configurar la conexión con la API:

```env
VITE_API_BASE_URL=http://localhost:8080/api
```

> Aclaracion: Si se clonó el repositorio completo, es posible que este archivo ya exista con la configuración por defecto.

Finalmente, ejecutar el servidor de desarrollo:

```sh
npm run dev
```

La aplicación estará disponible en `http://localhost:5173`.

Para poder correr los tests del Frontend se debe ejecutar

```sh
npm test
```

<p align="right">(<a href="#vitae---proyecto-de-aplicaciones-web">Volver</a>)</p>

## Usuarios de Testing:

Para facilitar la prueba de la aplicación en el deploy, se incluyen usuarios predefinidos con distintos perfiles.
Se puede acceder a los mismos con las siguientes credenciales.

### Usuario Paciente:

* **Email:** paciente.vitae@gmail.com
* **Contraseña:** Paciente123

### Usuario Médico:

* **Email:** doctor.vitae.paw@gmail.com
* **Contraseña:** Doctor123

> Nota: Las credenciales son válidas tanto para la página de Vitae como para Gmail, permitiendo verificar el correcto funcionamiento
> de la aplicación así como el envío/recepción de correos electrónicos.

<p align="right">(<a href="#vitae---proyecto-de-aplicaciones-web">Volver</a>)</p>

## Correcciones:

- La aplicación sigue generando una cookie JSESSIONID pese a que debiera ser stateless. Esto es un <b>error grave</b>.
- Su API no es RESTful. Estos son <b>errores conceptuales graves</b>. La siguiente es una lista no exhaustiva de errores encontrados:
    - `/api/appointments` retorna data distinta según quien pregunta. Esto implica por un lado que los recursos no tienen una URN, y por otro que la información es propensa a mezclarse. Por ejemplo el mostrar información cruzada cuando un proxy decide cachear resultados.
    - Entidades como specialty o coverage no viven bajo una única URN. Si bien a priori están expuestas como entidades de primer nivel bajo `/specialties` y `/coverages`; se encuentran listas de ambas entidades en otros endpoints como `/doctors/{id}/specialties`.
- La aplicación sigue configurando Spring WebMVC aunque no se debiera usar más.
- Muy buena cobertura de tests en el frontend.

Su nota es un 7.

<p align="right">(<a href="#vitae---proyecto-de-aplicaciones-web">Volver</a>)</p>

## Integrantes:

Nicolás Bellavitis (64001) - nbellavitisalzate@itba.edu.ar

José María Benegas Lynch (64242) - jobenegaslynch@itba.edu.ar

Agustín Galán (64098) - aggalan@itba.edu.ar

Santiago Maffeo (64245) - smaffeo@itba.edu.ar

Ignacio Pedemonte Berthoud (64908) - ipedemonteberthoud@itba.edu.ar

<p align="right">(<a href="#vitae---proyecto-de-aplicaciones-web">Volver</a>)</p>
