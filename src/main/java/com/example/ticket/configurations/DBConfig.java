package com.example.ticket.configurations;

import io.swagger.v3.oas.annotations.media.Schema;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
@Schema(name = "DBConfig",
        description = "предоставляет метод getConnection, который возвращает соединение с базой данных PostgreSQL.")
public class DBConfig {

    private static String dbUrl = "jdbc:postgresql://localhost:5432/ticket";
    private static String username = "users";
    private static String password = "1234";

    /**
     * Получает соединение с базой данных PostgreSQL.
     *
     * @return Объект Connection, представляющий соединение с базой данных.
     * @throws RuntimeException Если произошла ошибка при установлении соединения.
     */
    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(dbUrl,username,password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }
}
