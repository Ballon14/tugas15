package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;

public class DashboardController {

    @FXML
    private Button logoutButton;

    @FXML
    private Label welcomeLabel;

    private String currentUser;

    @FXML
    public void initialize() {
        // Initialization code if needed
    }

    public void setWelcomeMessage(String username) {
        this.currentUser = username;
        if (welcomeLabel != null) {
            welcomeLabel.setText("Selamat Datang, " + username + "!");
        }
    }

    @FXML
    public void logout() {
        try {
            System.out.println("User " + currentUser + " logout");

            Stage stage = (Stage) logoutButton.getScene().getWindow();

            // Kembali ke halaman login
            Parent root = FXMLLoader.load(getClass().getResource("/login/login.fxml"));
            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();

        } catch (IOException e) {
            System.err.println("Error during logout: " + e.getMessage());
            showErrorAlert("Error", "Terjadi kesalahan saat logout: " + e.getMessage());
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