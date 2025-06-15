package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Optional;

public class DashboardController {

    @FXML
    private Button logoutButton;

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label userInfoLabel;

    private String currentUser;

    public void setWelcomeMessage(String username) {
        this.currentUser = username;
        if (welcomeLabel != null) {
            welcomeLabel.setText("Selamat Datang, " + username + "!");
        }
        if (userInfoLabel != null) {
            userInfoLabel.setText("User: " + username);
        }
    }

    @FXML
    public void logout() {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Konfirmasi Logout");
        confirmationAlert.setHeaderText("Apakah Anda yakin ingin keluar?");
        confirmationAlert.setContentText("Anda akan keluar dari sistem.");
        
        ButtonType yesButton = new ButtonType("Ya, Keluar");
        ButtonType noButton = new ButtonType("Batal", ButtonBar.ButtonData.CANCEL_CLOSE);
        confirmationAlert.getButtonTypes().setAll(yesButton, noButton);

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        
        if (result.isPresent() && result.get() == yesButton) {
            performLogout();
        }
    }

    private void performLogout() {
        try {
            System.out.println("User " + currentUser + " logout");

            Stage stage = (Stage) logoutButton.getScene().getWindow();

            Parent root = FXMLLoader.load(getClass().getResource("/login/logout.fxml"));
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