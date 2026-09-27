package vista;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.GridLayout;

public class VentanaPrincipal extends JFrame {

    private JButton btnRegistrar;
    private JButton btnListar;
    private JButton btnIniciarEntrega;

    public VentanaPrincipal() {

        setTitle("SpeedFast - Gestión de Entregas");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Título
        JLabel titulo = new JLabel(
                "SPEEDFAST - GESTIÓN DE ENTREGAS",
                JLabel.CENTER
        );

        // Panel de botones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(3, 1, 10, 10));

        // Crear botones
        btnRegistrar = new JButton("Registrar Pedido");
        btnListar = new JButton("Listar Pedidos");
        btnIniciarEntrega = new JButton("Asignar Repartidor / Iniciar Entrega");

        // Agregar botones al panel
        panelBotones.add(btnRegistrar);
        panelBotones.add(btnListar);
        panelBotones.add(btnIniciarEntrega);

        // Agregar componentes a la ventana
        add(titulo, BorderLayout.NORTH);
        add(panelBotones, BorderLayout.CENTER);

        // Abrir ventana de registro
        btnRegistrar.addActionListener(
                e -> new VentanaRegistroPedido()
        );

        // Abrir ventana de lista
        btnListar.addActionListener(
                e -> new VentanaListaPedidos()
        );

        // Abrir ventana de asignación
        btnIniciarEntrega.addActionListener(
                e -> new VentanaAsignarRepartidor()
        );

        setVisible(true);
    }
}