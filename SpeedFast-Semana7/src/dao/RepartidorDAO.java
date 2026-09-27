package dao;

import model.Repartidor;
import model.ZonaDeCarga;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RepartidorDAO {

    public List<Repartidor> listarTodos() {

        List<Repartidor> repartidores = new ArrayList<>();

        String sql = "SELECT id, nombre FROM repartidor";

        try (Connection conexion = ConexionDB.conectar();
             PreparedStatement statement = conexion.prepareStatement(sql);
             ResultSet resultado = statement.executeQuery()) {

            while (resultado.next()) {

                String nombre = resultado.getString("nombre");

                Repartidor repartidor = new Repartidor(
                        nombre,
                        true,
                        new ZonaDeCarga()
                );

                repartidores.add(repartidor);
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al listar los repartidores: "
                            + e.getMessage()
            );
        }

        return repartidores;
    }
}