package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import java.io.IOException;
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
    private Label statusLabel; // Tambahan untuk status message

    private String generatedCode;
    private int loginAttempts = 0;
    private static final int MAX_ATTEMPTS = 3;
    private boolean isBlocked = false;

    // Credentials (dalam aplikasi nyata, ini seharusnya dari database)
    private static final String VALID_USERNAME = "admin";
    private static final String VALID_PASSWORD = "admin123";

    @FXML
    public void initialize() {
        generateNewSecurityCode();
        setupEventHandlers();
    }

    private void setupEventHandlers() {
        // Enter key support
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

    private void generateNewSecurityCode() {
        Random random = new Random();
        int code = 1000 + random.nextInt(9000);
        generatedCode = String.valueOf(code);
        securityCodeLabel.setText("Kode Keamanan: " + generatedCode);
        securityCodeField.clear();
    }

    @FXML
    private void handleLoginButtonAction() throws IOException {
        if (isBlocked) {
            showErrorAlert("Akun Diblokir", "Terlalu banyak percobaan login. Coba lagi nanti.");
            return;
        }

        String inputUsername = usernameField.getText().trim();
        String inputPassword = passwordField.getText();
        String inputSecurityCode = securityCodeField.getText().trim();

        // Validasi input kosong
        if (inputUsername.isEmpty() || inputPassword.isEmpty() || inputSecurityCode.isEmpty()) {
            showErrorAlert("Input Tidak Lengkap", "Harap isi semua field yang diperlukan!");
            return;
        }

        // Validasi credentials
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
        
        // Reset attempts
        loginAttempts = 0;
        
        // Pindah ke dashboard
        navigateToDashboard();
    }

    private void loginFailed() {
        loginAttempts++;
        
        if (loginAttempts >= MAX_ATTEMPTS) {
            blockAccount();
        } else {
            int remainingAttempts = MAX_ATTEMPTS - loginAttempts;
            showErrorAlert("Login Gagal", 
                String.format("Username, password, atau kode keamanan salah!\n" +
                            "Sisa percobaan: %d", remainingAttempts));
        }
        
        // Generate kode baru dan reset security code field
        generateNewSecurityCode();
        passwordField.clear(); // Clear password for security
    }

    private void blockAccount() {
        isBlocked = true;
        showErrorAlert("Akun Diblokir", 
            "Terlalu banyak percobaan login yang gagal.\n" +
            "Akun diblokir sementara untuk keamanan.");
        
        // Auto unblock after 30 seconds (untuk demo)
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(30), e -> {
            isBlocked = false;
            loginAttempts = 0;
            showInfoAlert("Akun Dibuka", "Akun Anda sudah dapat digunakan kembali.");
        }));
        timeline.play();
    }

    private void navigateToDashboard() throws IOException {
        Stage stage = (Stage) loginButton.getScene().getWindow();
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/dashboard/dashboard.fxml"));
        Parent root = loader.load();
        
        // Pass username to dashboard controller
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
        showInfoAlert("Kode Diperbarui", "Kode keamanan baru telah dibuat.");
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