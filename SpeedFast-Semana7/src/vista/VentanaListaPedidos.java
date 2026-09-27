package vista;

import dao.PedidoDAO;
import model.Pedido;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VentanaListaPedidos extends JFrame {

    private JTable tablaPedidos;
    private DefaultTableModel modeloTabla;
    private JButton btnRefrescar;
    private JButton btnVolver;

    public VentanaListaPedidos() {
        setTitle("SpeedFast - Lista de Pedidos");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        modeloTabla = new DefaultTableModel();
        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Dirección");
        modeloTabla.addColumn("Tipo");
        modeloTabla.addColumn("Estado");

        tablaPedidos = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaPedidos);

        btnRefrescar = new JButton("Refrescar");
        btnVolver = new JButton("Volver");

        btnRefrescar.addActionListener(e -> refrescarTabla());
        btnVolver.addActionListener(e -> dispose());

        JPanel panelInferior = new JPanel();
        panelInferior.add(btnRefrescar);
        panelInferior.add(btnVolver);

        add(scrollPane, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);

        cargarPedidos();

        setVisible(true);
    }

    private void cargarPedidos() {

        PedidoDAO pedidoDAO = new PedidoDAO();

        for (Pedido pedido : pedidoDAO.listarTodos()) {

            modeloTabla.addRow(new Object[]{
                    pedido.getId(),
                    pedido.getDireccionEntrega(),
                    pedido.getTipoEntrega(),
                    pedido.getEstado()
            });
        }
    }

    private void refrescarTabla() {
        modeloTabla.setRowCount(0);
        cargarPedidos();
    }
}