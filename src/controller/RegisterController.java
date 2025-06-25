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

public class RegisterController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Button registerButton;
    @FXML
    private Button backToLoginButton;
    @FXML
    private Label statusLabel;

    @FXML
    public void handleRegisterButtonAction() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showStatus("Harap isi semua field!", true);
            return;
        }
        if (!password.equals(confirmPassword)) {
            showStatus("Password tidak cocok!", true);
            return;
        }
        if (isUsernameTaken(username)) {
            showStatus("Username sudah digunakan!", true);
            return;
        }
        if (registerUser(username, password)) {
            showStatus("Registrasi berhasil! Silakan login.", false);
            clearFields();
        } else {
            showStatus("Registrasi gagal!", true);
        }
    }

    private boolean isUsernameTaken(String username) {
        String query = "SELECT username FROM users WHERE LOWER(username) = LOWER(?)";
        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            showStatus("Error database: " + e.getMessage(), true);
            return true;
        }
    }

    private boolean registerUser(String username, String password) {
        String query = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, password);
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            showStatus("Error database: " + e.getMessage(), true);
            return false;
        }
    }

    private void clearFields() {
        usernameField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
    }

    @FXML
    public void handleBackToLoginButtonAction() {
        try {
            Stage stage = (Stage) backToLoginButton.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/login/login.fxml"));
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
        } catch (IOException e) {
            showStatus("Gagal kembali ke login: " + e.getMessage(), true);
        }
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