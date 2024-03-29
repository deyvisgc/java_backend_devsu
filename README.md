# Proyecto de Servicios Bancarios Devsu

Este proyecto es un sistema bancario que consta de dos microservicios, clientes y movimientos. Está desarrollado en Java utilizando el framework Spring Boot y utiliza una base de datos MySQL. Además, se utiliza Docker para la gestión de contenedores, Swagger para la documentación de la API y Postman para realizar pruebas de integración .

## Pruebas Unitarias

Se han implementado pruebas unitarias para garantizar la funcionalidad adecuada de las diferentes partes de la aplicación. Las pruebas unitarias se encuentran en el directorio `src/test/java/com.example.prueba_tecnica` y pueden ejecutarse utilizando un entorno de desarrollo integrado (IDE) compatible con pruebas unitarias de Java, como IntelliJ IDEA o Eclipse.

## Pruebas de Integración de API y Servicio

Se han implementado pruebas de integración para verificar la interacción correcta entre los diferentes componentes de la aplicación, incluidos los controladores de la API y los servicios. Estas pruebas se encuentran en el directorio `src/test/java/com.example.prueba_tecnica/integration` y pueden ejecutarse junto con las pruebas unitarias.

## ms-clientes

El proyecto incluye operaciones CRUD (Crear, Leer, Actualizar y Eliminar) para la gestión de clientes. Las operaciones CRUD se realizan a través de una API RESTful utilizando los métodos HTTP estándar (POST, GET, PUT, DELETE). La documentación de la API se puede encontrar en [Swagger](https://swagger.io/), y está disponible en [localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) una vez que la aplicación esté en ejecución. Además de la gestión de clientes, el microservicio también ofrece la posibilidad de crear una cuenta asociada al cliente simultáneamente utilizando Feign. Esto proporciona una experiencia fluida al usuario al permitirle realizar múltiples acciones en un solo paso.

## ms-movimientos

El proyecto incluye operaciones CRUD (Crear, Leer, Actualizar y Eliminar) para la gestión de clientes. Las operaciones CRUD se realizan a través de una API RESTful utilizando los métodos HTTP estándar (POST, GET, PUT, DELETE). La documentación de la API se puede encontrar en [Swagger](https://swagger.io/), y está disponible en [localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html) una vez que la aplicación esté en ejecución. Es importante destacar que las cuentas creadas se relacionan con los clientes para permitir el acceso al nombre del cliente en los reportes de movimientos por rango de fechas y cliente. Esto facilita la generación de informes personalizados que incluyan información relevante del cliente asociado a cada movimiento.


## Base de Datos MySQL

La aplicación utiliza una base de datos MySQL que esta en un contenedor dentro del docker-compose para almacenar la información de ambos microservicios. Las credenciales para la base de datos MySQL son las siguientes:

- **Host:** localhost
- **Puerto:** 3306
- **Nombre de la base de datos:** basedatos
- **Usuario:** devsu
- **Contraseña:** marzo2024

## Ejecución con Docker Compose

La aplicación se puede ejecutar fácilmente utilizando Docker Compose. Asegúrate de tener Docker y Docker Compose instalados en tu sistema. Luego, sigue estos pasos:

1. Clona este repositorio en tu máquina local.

2. Navega al directorio raíz del proyecto.

3. Ejecuta el siguiente comando para iniciar la aplicación junto con una instancia de MySQL:

    ```bash
    docker-compose up
    ```

4. Una vez que la aplicación esté en ejecución, puedes acceder a la documentación de la API en [localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html).