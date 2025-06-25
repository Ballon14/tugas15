package controller;

import database.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InputMatakuliahController {
    @FXML
    private TextField kodeMatakuliahField;
    @FXML
    private TextField namaMatakuliahField;
    @FXML
    private TextField sksField;
    @FXML
    private Label statusLabel;

    private Runnable onSelesai;
    public void setOnSelesai(Runnable onSelesai) { this.onSelesai = onSelesai; }

    @FXML
    private void handleSimpan() {
        String kode = kodeMatakuliahField.getText().trim();
        String nama = namaMatakuliahField.getText().trim();
        String sksText = sksField.getText().trim();
        if (kode.isEmpty() || nama.isEmpty() || sksText.isEmpty()) {
            statusLabel.setText("Semua field harus diisi!");
            statusLabel.getStyleClass().removeAll("status-label-success");
            if (!statusLabel.getStyleClass().contains("status-label-error")) statusLabel.getStyleClass().add("status-label-error");
            return;
        }
        int sks;
        try {
            sks = Integer.parseInt(sksText);
        } catch (NumberFormatException e) {
            statusLabel.setText("SKS harus berupa angka!");
            statusLabel.getStyleClass().removeAll("status-label-success");
            if (!statusLabel.getStyleClass().contains("status-label-error")) statusLabel.getStyleClass().add("status-label-error");
            return;
        }
        String query = "INSERT INTO matakuliah (kode_matakuliah, nama_matakuliah, sks) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, kode);
            statement.setString(2, nama);
            statement.setInt(3, sks);
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                statusLabel.setText("Data berhasil disimpan!");
                statusLabel.getStyleClass().removeAll("status-label-error");
                if (!statusLabel.getStyleClass().contains("status-label-success")) statusLabel.getStyleClass().add("status-label-success");
                clearFields();
                if (onSelesai != null) onSelesai.run();
            } else {
                statusLabel.setText("Gagal menyimpan data!");
                statusLabel.getStyleClass().removeAll("status-label-success");
                if (!statusLabel.getStyleClass().contains("status-label-error")) statusLabel.getStyleClass().add("status-label-error");
            }
        } catch (SQLException e) {
            statusLabel.setText("Error: " + e.getMessage());
            statusLabel.getStyleClass().removeAll("status-label-success");
            if (!statusLabel.getStyleClass().contains("status-label-error")) statusLabel.getStyleClass().add("status-label-error");
        }
    }

    @FXML
    private void handleBatal() {
        if (onSelesai != null) onSelesai.run();
        Stage stage = (Stage) kodeMatakuliahField.getScene().getWindow();
        stage.close();
    }

    private void clearFields() {
        kodeMatakuliahField.clear();
        namaMatakuliahField.clear();
        sksField.clear();
    }
} 