# Aplicación Paint MVC en Java 

Aplicación de dibujo vectorial desarrollada en Java aplicando el patrón arquitectónico Modelo-Vista-Controlador (MVC). Permite la creación interactiva de figuras geométricas, su persistencia en una base de datos relacional (MySQL) y la exportación del lienzo a formato vectorial estandarizado (SVG).

## Características Principales

* **Herramientas de Dibujo:**
    * Puntos y Rectas.
    * Circunferencias (cálculo de radio mediante la distancia entre dos clicks).
    * Polígonos Regulares (selección de lados mediante un control deslizante interactivo).
    * Polígonos Irregulares (con algoritmo de validación para evitar el cruce de vértices).
* **Personalización:** Selección independiente de color de línea (trazo) y color de relleno mediante interfaz nativa.
* **Persistencia de Datos (CRUD):** * Almacenamiento completo del lienzo temporal en una base de datos MySQL.
    * Capacidad para guardar, limpiar y restaurar el estado exacto del lienzo respetando el orden de profundidad de las figuras.
* **Exportación:** Generación automática de código y exportación del lienzo a formato `.svg` puro para su visualización en navegadores web o editores vectoriales.

## Tecnologías y Arquitectura

* **Lenguaje:** Java
* **Interfaz Gráfica:** Java Swing
* **Base de Datos:** MySQL
* **Conectividad:** JDBC (Java Database Connectivity)
* **Arquitectura:** Patrón de diseño MVC (Modelo-Vista-Controlador) para la separación estricta de la lógica de negocio, la interfaz gráfica y la gestión de eventos de usuario.

## Requisitos de Instalación

1.  Tener instalado el **JDK de Java** (versión 8 o superior).
2.  Disponer de un servidor MySQL local, como **XAMPP** o similar, operando en el puerto `3306`.
3.  Añadir el **MySQL Connector/J** (Driver JDBC) a las dependencias o librerías del proyecto en tu IDE (NetBeans, Eclipse, IntelliJ).
4.  No es necesario ejecutar scripts SQL manualmente; la aplicación crea automáticamente la base de datos `TareaPaint` y las tablas relacionales necesarias en su primera ejecución.

## Uso de la Aplicación

1.  Inicia la aplicación.
2.  Selecciona la herramienta deseada en el menú desplegable superior.
3.  Ajusta el número de lados (si dibujas un polígono regular) usando el slider.
4.  Elige los colores de trazo y relleno.
5.  Interactúa con el lienzo mediante clicks del ratón (izquierdo para establecer puntos, derecho para cerrar polígonos irregulares).
6.  Utiliza los botones de la barra superior para guardar el progreso en la base de datos, restaurarlo o exportar el resultado a formato SVG.
