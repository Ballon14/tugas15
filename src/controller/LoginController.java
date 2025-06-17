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
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

public class LoginController {

    private void generateNewSecurityCode() {
        Random random = new Random();
        int code = 1000 + random.nextInt(9000);
        generatedCode = String.valueOf(code);
        securityCodeLabel.setText(generatedCode);
        if (securityCodeField != null) {
            securityCodeField.clear();
        }
    }

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
        setupEventHandlers();
    }

    private void setupEventHandlers() {
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
                try {
                    handleLoginButtonAction();
                } catch (IOException e) {
                    showErrorAlert("Error", "Terjadi kesalahan sistem: " + e.getMessage());
                }
            }
        });
    }

    @FXML
    private void handleLoginButtonAction() throws IOException {
        String query = SELECT * FROM users WHERE username = ? AND password = ?;
        try (Connection connection = database.DatabaseConnection.connect()) {
            if (username.getText().isEmpty() || password.getText().isEmpty()){
                System.out.println("Please fill in all fields.");
            }
        }
        String inputSecurityCode = securityCodeField.getText().trim();

        if (inputUsername.isEmpty() || inputPassword.isEmpty() || inputSecurityCode.isEmpty()) {
            statusLabel.setText("Harap isi semua field!");
            return;
        }

        if (validateCredentials(inputUsername, inputPassword, inputSecurityCode)) {
            loginSuccess();
        } else {
            loginFailed();
        }
    }

    private boolean validateCredentials(String username, String password, String securityCode) {
        return VALID_USERNAME.equals(username) && 
               VALID_PASSWORD.equals(password) && 
               generatedCode.equals(securityCode);
    }

    private void loginSuccess() throws IOException {
        System.out.println("Login berhasil untuk user: " + usernameField.getText());
        statusLabel.setText("Login berhasil!");
        
        navigateToDashboard();
    }

    private void loginFailed() {
        statusLabel.setText("Username, password, atau kode keamanan salah!");
        generateNewSecurityCode();
        passwordField.clear();
    }

    private void navigateToDashboard() throws IOException {
        Stage stage = (Stage) loginButton.getScene().getWindow();
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/dashboard/dashboard.fxml"));
        Parent root = loader.load();
        
        DashboardController dashboardController = loader.getController();
        dashboardController.setWelcomeMessage(usernameField.getText());
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    @FXML
    private void refreshSecurityCode() {
        generateNewSecurityCode();
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}