package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;

public class LogoutController {

    @FXML
    private Button backToLoginButton;

    @FXML
    public void backToLogin() {
        try {
            Stage stage = (Stage) backToLoginButton.getScene().getWindow();
            
            Parent root = FXMLLoader.load(getClass().getResource("/login/login.fxml"));
            Scene scene = new Scene(root);
            
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
            
        } catch (IOException e) {
            System.err.println("Error loading login page: " + e.getMessage());
            showErrorAlert("Error", "Terjadi kesalahan saat membuka halaman login: " + e.getMessage());
        }
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}