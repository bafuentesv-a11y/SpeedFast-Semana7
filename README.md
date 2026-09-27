# SpeedFast - Semana 7

## Desarrollo Orientado a Objetos II

Proyecto correspondiente a la Semana 7, enfocado en la conexión de una aplicación Java con una base de datos MySQL mediante JDBC.

## Descripción

SpeedFast es una aplicación desarrollada en Java que permite gestionar pedidos y repartidores de un servicio de entregas.

En esta versión se incorporó la persistencia de información mediante una base de datos MySQL, permitiendo registrar y consultar pedidos, además de gestionar la asignación de repartidores y registrar las entregas realizadas.

## Tecnologías utilizadas

- Java
- IntelliJ IDEA
- MySQL
- MySQL Workbench
- JDBC
- Git y GitHub
- Swing

## Base de datos

La aplicación utiliza una base de datos llamada:

`speedfast_db`

La base de datos contiene las siguientes tablas:

### repartidor

Almacena la información de los repartidores.

- `id`
- `nombre`

### pedido

Almacena la información de los pedidos.

- `id`
- `direccion`
- `tipo`
- `estado`

### entrega

Registra las entregas realizadas y relaciona los pedidos con los repartidores.

- `id`
- `id_pedido`
- `id_repartidor`
- `fecha`
- `hora`

La tabla `entrega` utiliza claves foráneas para relacionar los pedidos con los repartidores.

## Conexión a la base de datos

La conexión entre Java y MySQL se realiza mediante JDBC.

Para esto se implementó la clase:

`ConexionDB`

Además, se utilizaron clases DAO para realizar las operaciones con la base de datos:

- `PedidoDAO`
- `RepartidorDAO`
- `EntregaDAO`

Estas clases permiten separar la lógica de acceso a datos del resto de la aplicación.

## Funcionalidades

La aplicación permite:

- Registrar pedidos.
- Guardar los pedidos directamente en la base de datos.
- Consultar los pedidos almacenados.
- Visualizar los pedidos mediante una tabla.
- Consultar los pedidos pendientes.
- Seleccionar un pedido para realizar una entrega.
- Asignar un repartidor.
- Registrar la entrega realizada.
- Actualizar el estado del pedido a `ENTREGADO`.
- Mantener un historial de los pedidos registrados.

## Interfaz gráfica

La aplicación cuenta con distintas ventanas para gestionar las operaciones:

- **VentanaPrincipal:** menú principal de la aplicación.
- **VentanaRegistroPedido:** permite registrar nuevos pedidos.
- **VentanaListaPedidos:** permite visualizar los pedidos almacenados.
- **VentanaAsignarRepartidor:** permite seleccionar un pedido pendiente, asignar un repartidor e iniciar la entrega.

## Estructura del proyecto

```text
SpeedFast-Semana7
│
├── src
│   ├── dao
│   │   ├── ConexionDB.java
│   │   ├── PedidoDAO.java
│   │   ├── RepartidorDAO.java
│   │   └── EntregaDAO.java
│   │
│   ├── model
│   │   ├── Pedido.java
│   │   ├── PedidoComida.java
│   │   ├── PedidoEncomienda.java
│   │   ├── PedidoExpress.java
│   │   ├── Repartidor.java
│   │   ├── Entrega.java
│   │   ├── EstadoPedido.java
│   │   └── ...
│   │
│   ├── vista
│   │   ├── VentanaPrincipal.java
│   │   ├── VentanaRegistroPedido.java
│   │   ├── VentanaListaPedidos.java
│   │   └── VentanaAsignarRepartidor.java
│   │
│   └── main
│       └── Main.java
│
└── README.md
