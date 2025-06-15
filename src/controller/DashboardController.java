package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController {

    @FXML
    private Button logoutButton;

    // Metode ini akan dipanggil saat tombol logout ditekan
    @FXML
    public void logout() throws IOException {
        System.out.println("Proses logout dimulai...");

        // Dapatkan stage saat ini dari tombol logout
        Stage stage = (Stage) logoutButton.getScene().getWindow();

        // Muat halaman logout.fxml
        Parent root = FXMLLoader.load(getClass().getResource("/login/logout.fxml"));
        Scene scene = new Scene(root);

        // Ganti scene saat ini dengan scene logout
        stage.setScene(scene);
        stage.show();
    }
}