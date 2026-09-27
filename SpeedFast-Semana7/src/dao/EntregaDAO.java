package dao;

import model.Entrega;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EntregaDAO {

    public void guardar(Entrega entrega) {

        String sqlEntrega = "INSERT INTO entrega " +
                "(id_pedido, id_repartidor, fecha, hora) " +
                "VALUES (?, ?, ?, ?)";

        String sqlEstado = "UPDATE pedido SET estado = ? WHERE id = ?";

        try (Connection conexion = ConexionDB.conectar();
             PreparedStatement statementEntrega =
                     conexion.prepareStatement(sqlEntrega);
             PreparedStatement statementEstado =
                     conexion.prepareStatement(sqlEstado)) {

            // Guardar la entrega
            statementEntrega.setInt(1, entrega.getIdPedido());
            statementEntrega.setInt(2, entrega.getIdRepartidor());
            statementEntrega.setDate(
                    3,
                    java.sql.Date.valueOf(entrega.getFecha())
            );
            statementEntrega.setTime(
                    4,
                    java.sql.Time.valueOf(entrega.getHora())
            );

            statementEntrega.executeUpdate();

            // Cambiar el estado del pedido a ENTREGADO
            statementEstado.setString(1, "ENTREGADO");
            statementEstado.setInt(2, entrega.getIdPedido());

            statementEstado.executeUpdate();

            System.out.println(
                    "Entrega guardada correctamente y pedido actualizado a ENTREGADO."
            );

        } catch (SQLException e) {

            System.out.println(
                    "Error al guardar la entrega: "
                            + e.getMessage()
            );
        }
    }
}