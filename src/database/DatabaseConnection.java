package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    
    private static String URL = "jdbc:mysql://localhost:3306/javadb";
    private static String USER = "iqbal";
    private static String PASSWORD = "iqbal";
    
    public static Connection connect() {
        Connection connection = null;
        
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database connected successfully");
        } catch (SQLException e) {
            System.err.println("Error connecting to database: " + e.getMessage());
        }
        
        return connection;
    }
}