package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
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

    private String generatedCode;

    // Metode ini akan dipanggil secara otomatis setelah FXML dimuat
    @FXML
    public void initialize() {
        generateAndSetSecurityCode();
    }

    private void generateAndSetSecurityCode() {
        Random random = new Random();
        int code = 1000 + random.nextInt(9000); // Menghasilkan angka antara 1000 dan 9999
        generatedCode = String.valueOf(code);
        securityCodeLabel.setText("Kode Keamanan: " + generatedCode);
    }

    @FXML
    private void handleLoginButtonAction() throws IOException {
        String username = "user"; // Username yang benar
        String password = "password"; // Password yang benar

        if (usernameField.getText().equals(username) &&
            passwordField.getText().equals(password) &&
            securityCodeField.getText().equals(generatedCode)) {

            // Login berhasil, pindah ke halaman dashboard
            System.out.println("Login Berhasil!");

            // Dapatkan stage saat ini dan ganti scene ke dashboard
            Stage stage = (Stage) loginButton.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/login/Dashboard.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } else {
            // Login gagal, tampilkan pesan error
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Gagal");
            alert.setHeaderText(null);
            alert.setContentText("Username, password, atau kode keamanan salah!");
            alert.showAndWait();
            
            // Generate kode baru setelah login gagal
            generateAndSetSecurityCode();
            securityCodeField.clear();
        }
    }
}