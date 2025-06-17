package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/javadb";
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
    
    // Method untuk menutup koneksi
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Koneksi database ditutup.");
            } catch (SQLException e) {
                System.err.println("Error saat menutup koneksi: " + e.getMessage());
            }
        }
    }
}