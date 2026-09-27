package vista;

import dao.ConexionDB;
import model.Pedido;
import model.PedidoComida;
import model.PedidoEncomienda;
import model.PedidoExpress;
import model.Repartidor;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class VentanaAsignarRepartidor extends JFrame {

    private JComboBox<Pedido> cmbPedidos;
    private JComboBox<Repartidor> cmbRepartidores;

    private JButton btnIniciarEntrega;
    private JButton btnVolver;
    private JButton btnRefrescar;

    private final List<Repartidor> repartidores;

    public VentanaAsignarRepartidor() {

        setTitle("SpeedFast - Asignar Repartidor");
        setSize(550, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // ==========================================
        // CREAR REPARTIDORES
        // ==========================================

        repartidores = new ArrayList<>();

        repartidores.add(
                new Repartidor(
                        "Repartidor 1",
                        true,
                        null
                )
        );

        repartidores.add(
                new Repartidor(
                        "Repartidor 2",
                        true,
                        null
                )
        );

        // ==========================================
        // COMPONENTES
        // ==========================================

        JLabel lblPedido =
                new JLabel("Pedido pendiente:");

        JLabel lblRepartidor =
                new JLabel("Repartidor disponible:");

        cmbPedidos = new JComboBox<>();
        cmbRepartidores = new JComboBox<>();

        btnIniciarEntrega =
                new JButton("Iniciar Entrega");

        btnRefrescar =
                new JButton("Refrescar");

        btnVolver =
                new JButton("Volver");

        // ==========================================
        // CARGAR INFORMACIÓN
        // ==========================================

        cargarPedidosPendientes();
        cargarRepartidoresDisponibles();

        // ==========================================
        // PANEL
        // ==========================================

        JPanel panel =
                new JPanel(new GridLayout(4, 2, 10, 10));

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        20, 20, 20, 20
                )
        );

        panel.add(lblPedido);
        panel.add(cmbPedidos);

        panel.add(lblRepartidor);
        panel.add(cmbRepartidores);

        panel.add(new JLabel());
        panel.add(btnIniciarEntrega);

        panel.add(btnRefrescar);
        panel.add(btnVolver);

        add(panel);

        // ==========================================
        // EVENTOS
        // ==========================================

        btnIniciarEntrega.addActionListener(
                e -> iniciarEntrega()
        );

        btnRefrescar.addActionListener(
                e -> {

                    cargarPedidosPendientes();
                    cargarRepartidoresDisponibles();

                }
        );

        btnVolver.addActionListener(
                e -> dispose()
        );

        setVisible(true);
    }

    // =========================================================
    // CARGAR PEDIDOS PENDIENTES DESDE MYSQL
    // =========================================================

    private void cargarPedidosPendientes() {

        cmbPedidos.removeAllItems();

        String sql =
                "SELECT id, direccion, tipo, estado " +
                        "FROM pedido " +
                        "WHERE estado <> 'ENTREGADO' " +
                        "ORDER BY id";

        try (
                Connection conexion = ConexionDB.conectar();
                PreparedStatement statement =
                        conexion.prepareStatement(sql);
                ResultSet resultado =
                        statement.executeQuery()
        ) {

            while (resultado.next()) {

                int id =
                        resultado.getInt("id");

                String direccion =
                        resultado.getString("direccion");

                String tipo =
                        resultado.getString("tipo");

                String estado =
                        resultado.getString("estado");

                Pedido pedido =
                        crearPedido(
                                id,
                                direccion,
                                tipo
                        );

                if (pedido != null) {

                    pedido.setEstado(estado);

                    cmbPedidos.addItem(pedido);
                }
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar los pedidos:\n"
                            + e.getMessage(),
                    "Error de base de datos",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // CREAR OBJETO PEDIDO SEGÚN EL TIPO
    // =========================================================

    private Pedido crearPedido(
            int id,
            String direccion,
            String tipo
    ) {

        double distanciaKm = 0;

        if (tipo.equalsIgnoreCase("Comida")) {

            return new PedidoComida(
                    id,
                    direccion,
                    distanciaKm,
                    "Comida"
            );

        } else if (tipo.equalsIgnoreCase("Encomienda")) {

            return new PedidoEncomienda(
                    id,
                    direccion,
                    distanciaKm,
                    "Encomienda"
            );

        } else if (tipo.equalsIgnoreCase("Express")) {

            return new PedidoExpress(
                    id,
                    direccion,
                    distanciaKm,
                    "Express"
            );
        }

        return null;
    }

    // =========================================================
    // CARGAR REPARTIDORES
    // =========================================================

    private void cargarRepartidoresDisponibles() {

        cmbRepartidores.removeAllItems();

        for (Repartidor repartidor : repartidores) {

            if (repartidor.isDisponible()) {

                cmbRepartidores.addItem(repartidor);
            }
        }
    }

    // =========================================================
    // INICIAR ENTREGA
    // =========================================================

    private void iniciarEntrega() {

        Pedido pedidoSeleccionado =
                (Pedido) cmbPedidos.getSelectedItem();

        Repartidor repartidorSeleccionado =
                (Repartidor) cmbRepartidores.getSelectedItem();

        // ------------------------------------------
        // VALIDAR PEDIDO
        // ------------------------------------------

        if (pedidoSeleccionado == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debes seleccionar un pedido.",
                    "SpeedFast",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        // ------------------------------------------
        // VALIDAR REPARTIDOR
        // ------------------------------------------

        if (repartidorSeleccionado == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debes seleccionar un repartidor.",
                    "SpeedFast",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        // ------------------------------------------
        // OBTENER ID DEL REPARTIDOR
        // ------------------------------------------

        int idRepartidor =
                obtenerIdRepartidor(
                        repartidorSeleccionado.getNombre()
                );

        if (idRepartidor == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo registrar el repartidor.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        // ------------------------------------------
        // CAMBIAR ESTADO A EN_REPARTO
        // ------------------------------------------

        if (!actualizarEstadoPedido(
                pedidoSeleccionado.getId(),
                "EN_REPARTO"
        )) {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo cambiar el estado del pedido.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        pedidoSeleccionado.setEstado(
                model.EstadoPedido.EN_REPARTO
        );

        repartidorSeleccionado.setDisponible(false);

        JOptionPane.showMessageDialog(
                this,
                "Entrega iniciada para el pedido #"
                        + pedidoSeleccionado.getId()
                        + "\nRepartidor: "
                        + repartidorSeleccionado.getNombre(),
                "SpeedFast",
                JOptionPane.INFORMATION_MESSAGE
        );

        // ------------------------------------------
        // EJECUTAR ENTREGA EN SEGUNDO PLANO
        // ------------------------------------------

        Thread hilo =
                new Thread(() -> {

                    try {

                        // Simular tiempo de entrega
                        Thread.sleep(2000);

                        // Cambiar objeto a ENTREGADO
                        pedidoSeleccionado.setEstado(
                                model.EstadoPedido.ENTREGADO
                        );

                        // Cambiar estado en MySQL
                        actualizarEstadoPedido(
                                pedidoSeleccionado.getId(),
                                "ENTREGADO"
                        );

                        // Registrar entrega en MySQL
                        registrarEntrega(
                                pedidoSeleccionado.getId(),
                                idRepartidor
                        );

                        SwingUtilities.invokeLater(() -> {

                            repartidorSeleccionado
                                    .setDisponible(true);

                            JOptionPane.showMessageDialog(
                                    this,
                                    "Pedido #"
                                            + pedidoSeleccionado.getId()
                                            + " entregado correctamente.",
                                    "Entrega finalizada",
                                    JOptionPane.INFORMATION_MESSAGE
                            );

                            cargarPedidosPendientes();
                            cargarRepartidoresDisponibles();
                        });

                    } catch (InterruptedException e) {

                        Thread.currentThread().interrupt();

                    }
                });

        hilo.start();
    }

    // =========================================================
    // OBTENER ID DEL REPARTIDOR
    // =========================================================

    private int obtenerIdRepartidor(String nombre) {

        String buscar =
                "SELECT id FROM repartidor WHERE nombre = ?";

        String insertar =
                "INSERT INTO repartidor (nombre) VALUES (?)";

        try (Connection conexion =
                     ConexionDB.conectar()) {

            // Primero buscar
            try (PreparedStatement statement =
                         conexion.prepareStatement(buscar)) {

                statement.setString(1, nombre);

                try (ResultSet resultado =
                             statement.executeQuery()) {

                    if (resultado.next()) {

                        return resultado.getInt("id");
                    }
                }
            }

            // Si no existe, crearlo
            try (PreparedStatement statement =
                         conexion.prepareStatement(
                                 insertar,
                                 Statement.RETURN_GENERATED_KEYS
                         )) {

                statement.setString(1, nombre);

                statement.executeUpdate();

                try (ResultSet resultado =
                             statement.getGeneratedKeys()) {

                    if (resultado.next()) {

                        return resultado.getInt(1);
                    }
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error con repartidor: "
                            + e.getMessage()
            );
        }

        return -1;
    }

    // =========================================================
    // ACTUALIZAR ESTADO DEL PEDIDO
    // =========================================================

    private boolean actualizarEstadoPedido(
            int idPedido,
            String nuevoEstado
    ) {

        String sql =
                "UPDATE pedido " +
                        "SET estado = ? " +
                        "WHERE id = ?";

        try (
                Connection conexion =
                        ConexionDB.conectar();

                PreparedStatement statement =
                        conexion.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    nuevoEstado
            );

            statement.setInt(
                    2,
                    idPedido
            );

            int filas =
                    statement.executeUpdate();

            return filas > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error al actualizar estado: "
                            + e.getMessage()
            );

            return false;
        }
    }

    // =========================================================
    // REGISTRAR ENTREGA
    // =========================================================

    private void registrarEntrega(
            int idPedido,
            int idRepartidor
    ) {

        String sql =
                "INSERT INTO entrega " +
                        "(id_pedido, id_repartidor, fecha, hora) " +
                        "VALUES (?, ?, ?, ?)";

        try (
                Connection conexion =
                        ConexionDB.conectar();

                PreparedStatement statement =
                        conexion.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    idPedido
            );

            statement.setInt(
                    2,
                    idRepartidor
            );

            statement.setDate(
                    3,
                    Date.valueOf(
                            LocalDate.now()
                    )
            );

            statement.setTime(
                    4,
                    Time.valueOf(
                            LocalTime.now()
                    )
            );

            statement.executeUpdate();

            System.out.println(
                    "Entrega registrada correctamente."
            );

        } catch (SQLException e) {

            System.out.println(
                    "Error al registrar entrega: "
                            + e.getMessage()
            );
        }
    }
}