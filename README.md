# Microservicios para tarjetas Tullave
Consiste en un ejercicio de implementación de microservicios en Spring Boot que representa parte de la funcionalidad de las tarjetas Tullave del SITP. Se conforma por un servicio que expone los recursos de la cuenta y otro para las transacciones que realiza el cliente con la tarjeta.

## Comenzando
Para obtener una copia del proyecto, puede clonar este repositorio con el siguiente comando git...
> git clone https://github.com/frjtorres/tl.git

También puede descargar el archivo .zip que contiene el proyecto ingresando a la opción 'clone or download'.

### Pre-requisitos
* Java JDK - 1.8 o superior. Para su instalación da click [aqui](https://www.oracle.com/java/technologies/javase-downloads.html).
* Gradle - Version 5.4.1 o superior. Para su instalación da click [aqui](https://gradle.org/install/).

### Instalación
* Instalar el microservicio cuentas.
    >cd cuentas
    >gradle cuentas install
* Instalar el microservicio transacciones.
    >cd transacciones
    >gradle transacciones install

### Ejecución
* Construir y ejecutar el microservicio cuentas.
    >cd cuentas
    >gradle cuentas clean build
    >gradlew bootRun
* Construir y ejecutar el microservicio transacciones.
    >cd transacciones
    >gradle transacciones clean build
    >gradlew bootRun

## Base de datos
La base de datos de ambos microservicios está configurada por defecto para desarrollo y pruebas con H2 y producción con Oracle Database. En caso de requerir el uso de otra base de datos, se debe modificar los archivos de propiedades de cada microservicio.

## Ejecutando las pruebas
Las pruebas para el microservicio cuentas solamente se conforma de pruebas unitarias y para el microservicio transacciones solamente se conforma de pruebas de integración. Las pruebas de integración se constituyen por contratos definidos con [Spring Cloud Contract](https://spring.io/projects/spring-cloud-contract). Los contratos están ubicados en los recursos del microservicio cuentas **(tl\cuentas\src\test\resources\contracts)** que para ejectuarlos, el microservicio debe estar instalado. 

* Ejecución de pruebas unitarias
    >cd cuentas
    >gradle test --tests *Unit
* Ejecución de pruebas de integración
    >cd transacciones
    >gradle test --tests *Integration

## Construido con
* [Gradle](https://gradle.org/) - Gestor y constructor de proyectos.
* [Lombok](https://projectlombok.org/) - Librería para definición de modelos.
* [Spring Boot](https://spring.io/projects/spring-boot) - Framework para aplicaciones Java.
