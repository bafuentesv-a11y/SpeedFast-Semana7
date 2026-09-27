package dao;

import model.EstadoPedido;
import model.Pedido;
import model.PedidoComida;
import model.PedidoEncomienda;
import model.PedidoExpress;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    public void guardar(Pedido pedido) {

        String sql = "INSERT INTO pedido (id, direccion, tipo, estado) VALUES (?, ?, ?, ?)";

        try (Connection conexion = ConexionDB.conectar();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setInt(1, pedido.getId());
            statement.setString(2, pedido.getDireccionEntrega());
            statement.setString(3, pedido.getTipoEntrega());
            statement.setString(4, pedido.getEstado().name());

            statement.executeUpdate();

            System.out.println("Pedido guardado correctamente.");

        } catch (SQLException e) {
            System.out.println("Error al guardar el pedido: " + e.getMessage());
        }
    }

    public List<Pedido> listarTodos() {

        List<Pedido> pedidos = new ArrayList<>();

        String sql = "SELECT id, direccion, tipo, estado FROM pedido";

        try (Connection conexion = ConexionDB.conectar();
             PreparedStatement statement = conexion.prepareStatement(sql);
             ResultSet resultado = statement.executeQuery()) {

            while (resultado.next()) {

                int id = resultado.getInt("id");
                String direccion = resultado.getString("direccion");
                String tipo = resultado.getString("tipo");
                String estadoTexto = resultado.getString("estado");

                EstadoPedido estado = EstadoPedido.valueOf(estadoTexto);

                double distanciaKm = 0;

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
                        continue;
                }

                pedido.setEstado(estado);
                pedidos.add(pedido);
            }

        } catch (SQLException e) {
            System.out.println(
                    "Error al listar los pedidos: " + e.getMessage()
            );
        }

        return pedidos;
    }
}