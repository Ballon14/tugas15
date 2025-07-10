package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/uas";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection connect() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Terhubung ke database berhasil!");
        } catch (SQLException e) {
            System.err.println("Terjadi kesalahan saat terhubung ke database: " + e.getMessage());
        }
        return connection;
    }
}