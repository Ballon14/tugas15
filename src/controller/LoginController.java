package controller;

import database.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label securityCodeLabel;

    @FXML
    private TextField securityCodeField;

    @FXML
    private Button loginButton;

    @FXML
    private Label statusLabel;

    private String generatedCode;

    @FXML
    public void initialize() {
        generateNewSecurityCode();
        setupKeyEvents();
    }

    private void generateNewSecurityCode() {
        Random random = new Random();
        int code = 1000 + random.nextInt(9000);
        generatedCode = String.valueOf(code);
        securityCodeLabel.setText(generatedCode);
        
        if (securityCodeField != null) {
            securityCodeField.clear();
        }
    }

    private void setupKeyEvents() {
        // Navigasi dengan tombol Enter
        usernameField.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                passwordField.requestFocus();
            }
        });

        passwordField.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                securityCodeField.requestFocus();
            }
        });

        securityCodeField.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                handleLogin();
            }
        });
    }

    @FXML
    private void handleLoginButtonAction() {
        handleLogin();
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String securityCode = securityCodeField.getText().trim();

        // Validasi input kosong
        if (username.isEmpty() || password.isEmpty() || securityCode.isEmpty()) {
            showStatus("Harap isi semua field!", true);
            return;
        }

        // Validasi kode keamanan
        if (!securityCode.equals(generatedCode)) {
            showStatus("Kode keamanan salah!", true);
            generateNewSecurityCode();
            passwordField.clear();
            return;
        }

        // Validasi login dengan database
        if (validateUserFromDatabase(username, password)) {
            loginSuccess(username);
        } else {
            loginFailed();
        }
    }

    private boolean validateUserFromDatabase(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        
        try (Connection connection = DatabaseConnection.connect()) {
            if (connection == null) {
                showStatus("Gagal terhubung ke database!", true);
                return false;
            }

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, username);
                statement.setString(2, password);
                
                try (ResultSet resultSet = statement.executeQuery()) {
                    return resultSet.next(); // Mengembalikan true jika pengguna ditemukan
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            showStatus("Error database: " + e.getMessage(), true);
            return false;
        }
    }

    private void loginSuccess(String username) {
        System.out.println("Login berhasil untuk user: " + username);
        showStatus("Login berhasil!", false);
        
        try {
            navigateToDashboard(username);
        } catch (IOException e) {
            showStatus("Error membuka dashboard: " + e.getMessage(), true);
        }
    }

    private void loginFailed() {
        showStatus("Username atau password salah!", true);
        generateNewSecurityCode();
        passwordField.clear();
        securityCodeField.clear();
    }

    private void navigateToDashboard(String username) throws IOException {
        Stage stage = (Stage) loginButton.getScene().getWindow();
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/dashboard/dashboard.fxml"));
        Parent root = loader.load();
        
        // Mengatur pesan selamat datang di dashboard
        DashboardController dashboardController = loader.getController();
        dashboardController.setWelcomeMessage(username);
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    @FXML
    private void refreshSecurityCode() {
        generateNewSecurityCode();
    }

    private void showStatus(String message, boolean isError) {
        statusLabel.setText(message);
        if (isError) {
            statusLabel.setStyle("-fx-text-fill: #e74c3c;");
        } else {
            statusLabel.setStyle("-fx-text-fill: #27ae60;");
        }
    }
}