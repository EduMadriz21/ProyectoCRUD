package com.desarrollo.proyectocrud.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static final String URL =
            "jdbc:postgresql://127.0.0.1:5432/bdventas";

    private static final String USUARIO =
            "postgres";

    private static final String CONTRASENA =
            "12345678";

    public static Connection conectar() {

        try {

            Connection conexion = DriverManager.getConnection(
                    URL,
                    USUARIO,
                    CONTRASENA
            );

            System.out.println("Conexion exitosa a PostgreSQL");

            return conexion;

        } catch (SQLException e) {

            System.out.println("Error al conectar con PostgreSQL");
            System.out.println(e.getMessage());

            throw new RuntimeException(
                    "No se pudo conectar a la base de datos",
                    e
            );
        }
    }
}