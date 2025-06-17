package controller;

import database.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.IOException;
import java.sql.Connection;
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
                handleLoginButtonAction(null);
            }
        });
    }

    @FXML
    void handleLoginButtonAction(ActionEvent event) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection connection = DatabaseConnection.connect()) {
            if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
                System.out.println("Please fill in all fields.");
            }
            
            PreparedStatement login = connection.prepareStatement(query);
            login.setString(1, usernameField.getText());
            login.setString(2, passwordField.getText());
            ResultSet resultSet = login.executeQuery();
            
            if (resultSet.next()) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../dashboard/dashboard.fxml"));
                Parent root = null;
                try {
                    root = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Failed to load dashboard.fxml");
                    return;
                }
                
                Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } else {
                System.out.println("Login failed. Please check your username and password.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Database connection error.");
        }
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