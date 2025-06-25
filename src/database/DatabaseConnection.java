package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://100.96.165.28:3306/javadb";
    private static final String USER = "iqbal";
    private static final String PASSWORD = "iqbal";
    
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