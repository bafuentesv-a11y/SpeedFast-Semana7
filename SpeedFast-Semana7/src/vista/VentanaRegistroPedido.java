package vista;

import dao.PedidoDAO;
import model.GestorPedidos;
import model.Pedido;
import model.PedidoComida;
import model.PedidoEncomienda;
import model.PedidoExpress;

import javax.swing.*;
import java.awt.*;

public class VentanaRegistroPedido extends JFrame {

    private JTextField txtId;
    private JTextField txtDireccion;
    private JComboBox<String> cmbTipo;
    private JButton btnGuardar;
    private JButton btnVolver;

    public VentanaRegistroPedido() {

        setTitle("SpeedFast - Registrar Pedido");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblId = new JLabel("ID:");
        JLabel lblDireccion = new JLabel("Dirección:");
        JLabel lblTipo = new JLabel("Tipo:");

        txtId = new JTextField();
        txtDireccion = new JTextField();

        cmbTipo = new JComboBox<>(
                new String[]{"Comida", "Encomienda", "Express"}
        );

        btnGuardar = new JButton("Guardar");
        btnVolver = new JButton("Volver");

        panel.add(lblId);
        panel.add(txtId);

        panel.add(lblDireccion);
        panel.add(txtDireccion);

        panel.add(lblTipo);
        panel.add(cmbTipo);

        panel.add(new JLabel());
        panel.add(btnGuardar);

        panel.add(new JLabel());
        panel.add(btnVolver);

        add(panel);

        // Evento Guardar
        btnGuardar.addActionListener(e -> guardarPedido());

        // Evento Volver
        btnVolver.addActionListener(e -> dispose());

        setVisible(true);
    }

    private void guardarPedido() {

        String idTexto = txtId.getText().trim();
        String direccion = txtDireccion.getText().trim();
        String tipo = (String) cmbTipo.getSelectedItem();

        // Validar ID
        if (idTexto.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debe ingresar un ID.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        int id;

        try {
            id = Integer.parseInt(idTexto);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "El ID debe ser un número entero.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // Validar dirección
        if (direccion.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debe ingresar una dirección.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // Distancia solicitada por el modelo de Semana 5
        double distanciaKm = 0;

        // Crear el pedido según el tipo seleccionado
        Pedido pedido;

        switch (tipo) {

            case "Comida":
                pedido = new PedidoComida(
                        id,
                        direccion,
                        distanciaKm,
                        tipo
                );
                break;

            case "Encomienda":
                pedido = new PedidoEncomienda(
                        id,
                        direccion,
                        distanciaKm,
                        tipo
                );
                break;

            case "Express":
                pedido = new PedidoExpress(
                        id,
                        direccion,
                        distanciaKm,
                        tipo
                );
                break;

            default:
                JOptionPane.showMessageDialog(
                        this,
                        "Debe seleccionar un tipo de pedido.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
        }

        // Guardar en la base de datos
        PedidoDAO pedidoDAO = new PedidoDAO();
        pedidoDAO.guardar(pedido);

        // Mantener también el pedido en la lista de la aplicación
        GestorPedidos.agregarPedido(pedido);

        // Limpiar formulario
        txtId.setText("");
        txtDireccion.setText("");
        cmbTipo.setSelectedIndex(0);
    }
}