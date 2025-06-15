package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DashboardController {

    @FXML
    private Button logoutButton;

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label loginTimeLabel;

    @FXML
    private Label systemInfoLabel;

    private String currentUser;
    private LocalDateTime loginTime;

    @FXML
    public void initialize() {
        loginTime = LocalDateTime.now();
        updateSystemInfo();
    }

    public void setWelcomeMessage(String username) {
        this.currentUser = username;
        if (welcomeLabel != null) {
            welcomeLabel.setText("Selamat Datang, " + username + "!");
        }
        
        if (loginTimeLabel != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            loginTimeLabel.setText("Login pada: " + loginTime.format(formatter));
        }
    }

    private void updateSystemInfo() {
        if (systemInfoLabel != null) {
            String javaVersion = System.getProperty("java.version");
            String osName = System.getProperty("os.name");
            systemInfoLabel.setText("Java: " + javaVersion + " | OS: " + osName);
        }
    }

    @FXML
    public void logout() {
        // Konfirmasi logout
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Konfirmasi Logout");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Apakah Anda yakin ingin keluar dari aplikasi?");

        ButtonType yesButton = new ButtonType("Ya", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("Tidak", ButtonBar.ButtonData.NO);
        confirmAlert.getButtonTypes().setAll(yesButton, noButton);

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == yesButton) {
                performLogout();
            }
        });
    }

    private void performLogout() {
        try {
            System.out.println("User " + currentUser + " logout pada: " + 
                             LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));

            Stage stage = (Stage) logoutButton.getScene().getWindow();

            // Kembali ke halaman login
            Parent root = FXMLLoader.load(getClass().getResource("/login/login.fxml"));
            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();

            // Show logout success message
            showInfoAlert("Logout Berhasil", "Anda telah berhasil keluar dari sistem.");

        } catch (IOException e) {
            System.err.println("Error during logout: " + e.getMessage());
            showErrorAlert("Error", "Terjadi kesalahan saat logout: " + e.getMessage());
        }
    }

    @FXML
    private void showUserProfile() {
        Alert profileAlert = new Alert(Alert.AlertType.INFORMATION);
        profileAlert.setTitle("Profil Pengguna");
        profileAlert.setHeaderText("Informasi Akun");
        
        String profileInfo = "Username: " + currentUser + "\n" +
                           "Login Time: " + loginTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) + "\n" +
                           "Session Duration: " + getSessionDuration();
        
        profileAlert.setContentText(profileInfo);
        profileAlert.showAndWait();
    }

    private String getSessionDuration() {
        LocalDateTime now = LocalDateTime.now();
        long minutes = java.time.Duration.between(loginTime, now).toMinutes();
        return minutes + " menit";
    }

    @FXML
    private void refreshData() {
        updateSystemInfo();
        showInfoAlert("Data Diperbarui", "Informasi sistem telah diperbarui.");
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfoAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}