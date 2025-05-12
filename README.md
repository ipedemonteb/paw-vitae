# Vitae - Proyecto de Aplicaciones Web

El presente trabajo, realizado para la materia *Proyecto de Aplicaciones Web*, consiste en el desarrollo de una aplicación web que facilita la reserva de turnos médicos, unificando las distintas obras sociales.
Cuenta principalmente con las siguientes funcionalidades:

- <b>Reserva de Turnos</b>: Se permite a los usuarios reservar turnos médicos con aquellos que atiendan con su obra social.

- <b>Gestión de Turnos</b>: Se permite a los usuarios gestionar sus turnos, como por ejemplo cancelando aquellos a los que no puedan asistir.

- <b>Sistema de Búsqueda Avanzada</b>: Se ofrece un sistema de búsqueda avanzada que permite a los usuarios filtrar los médicos por especialidad, obra social y disponibilidad horaria.

- <b>Notificaciones via Mail</b>: Se envían notificaciones via mail a los usuarios para notificarlos de actualizaciones en sus turnos, así como recordarles los próximos. 

- <b>Manejo de Archivos</b>: Se ofrece a los usuarios un sistema de subida de archivos para cargar tanto documentación requerida para los pacientes como resultados para los médicos.

- <b>Sistema de Calificaciones</b>: Se permite a los usuarios calificar la atención del médico para ofrecer referencias a otros usuarios.

<details>
  <summary>Contenidos</summary>
  <ol>
    <li><a href="#instalación">Instalación</a></li>
    <li><a href="#usuarios-de-testing">Usuarios de Testing</a></li>
    <li><a href="#integrantes">Integrantes</a></li>
  </ol>
</details>

## Instalación:

El deploy del proyecto se encuentra en los servidores del ITBA, y se puede acceder mediante el siguiente [enlace](http://pawserver.it.itba.edu.ar/paw-2025a-11/).
En caso de querer instalar el proyecto localmente y realizar el deploy en un Application Container, se debe contar con una base de datos PostgreSQL con permisos adecuados.
Luego, se debe clonar el repositorio mediante:
- HTTPS:
  ```sh
  git clone https://[USERNAME]@bitbucket.org/itba/paw-2025a-11.git
  ```
- SSH:
  ```sh
  git clone git@bitbucket.org:itba/paw-2025a-11.git
  ```

A continuación, se debe generar el archivo ```application.properties``` en la carpeta ```src/main/resources``` con la configuración correspondiente. 
El siguiente ejemplo muestra un caso de dicho archivo:
```properties
datasource.url=
datasource.username=
datasource.password=
security.secret-file=webapp/src/main/resources/secret.key
app.base-url=
mail.username=
mail.password=
mail.port=
mail.host=
```
Luego, se debe generar el archivo ```secret.key``` a la misma altura que se encuentra el application.properties y pegar la key correspondiente.

A continuación, se debe compilar el proyecto y generar el archivo ```.war``` ejecutando:
```sh
mvn clean package
```

Finalmente, se realiza el deploy local correspondiente. La aplicación se encarga automáticamente de la creación de la base de datos y de las tablas necesarias.

<p align="right">(<a href="#vitae---proyecto-de-aplicaciones-web">Volver</a>)</p>

## Usuarios de Testing:

Para facilitar la prueba de la aplicación en el deploy, se incluyen usuarios predefinidos con distintos perfiles. 
Se puede acceder a los mismos con las siguientes credenciales.

### Usuario Paciente:
- **Email:** paciente.vitae@gmail.com
- **Contraseña:** Paciente123

### Usuario Médico:
- **Email:** doctor.vitae.paw@gmail.com
- **Contraseña:** Doctor123

> Nota: Las credenciales son válidas tanto para la página de Vitae como para Gmail, permitiendo verificar el correcto funcionamiento 
> de la aplicación así como el envío/recepción de correos electrónicos.

<p align="right">(<a href="#vitae---proyecto-de-aplicaciones-web">Volver</a>)</p>

## Integrantes:

Nicolás Bellavitis (64001) - nbellavitisalzate@itba.edu.ar 

José María Benegas Lynch (64242) - jobenegaslynch@itba.edu.ar

Agustín Galán (64098) - aggalan@itba.edu.ar

Santiago Maffeo (64245) - smaffeo@itba.edu.ar

Ignacio Pedemonte Berthoud (64908) - ipedemonteberthoud@itba.edu.ar


<p align="right">(<a href="#vitae---proyecto-de-aplicaciones-web">Volver</a>)</p>