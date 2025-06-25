package controller;

import database.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.control.ToggleGroup;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InputMahasiswaController {
    @FXML
    private TextField namaLengkapField;
    @FXML
    private TextField nimField;
    @FXML
    private TextField noHpField;
    @FXML
    private TextField emailField;
    @FXML
    private RadioButton lakiRadio;
    @FXML
    private RadioButton perempuanRadio;
    @FXML
    private Label statusLabel;
    private ToggleGroup genderGroup;
    private Runnable onSelesai;

    @FXML
    public void initialize() {
        genderGroup = new ToggleGroup();
        lakiRadio.setToggleGroup(genderGroup);
        perempuanRadio.setToggleGroup(genderGroup);
    }

    @FXML
    private void handleSimpan() {
        String namaLengkap = namaLengkapField.getText().trim();
        String nim = nimField.getText().trim();
        String noHp = noHpField.getText().trim();
        String email = emailField.getText().trim();
        String jenisKelamin = lakiRadio.isSelected() ? "Laki-laki" : (perempuanRadio.isSelected() ? "Perempuan" : "");
        if (namaLengkap.isEmpty() || nim.isEmpty() || noHp.isEmpty() || email.isEmpty() || jenisKelamin.isEmpty()) {
            statusLabel.setText("Semua field harus diisi!");
            return;
        }
        String query = "INSERT INTO mahasiswa (nama_lengkap, nim, no_hp, email, jenis_kelamin) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, namaLengkap);
            statement.setString(2, nim);
            statement.setString(3, noHp);
            statement.setString(4, email);
            statement.setString(5, jenisKelamin);
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                statusLabel.setText("Data berhasil disimpan!");
                clearFields();
                if (onSelesai != null) onSelesai.run();
            } else {
                statusLabel.setText("Gagal menyimpan data!");
            }
        } catch (SQLException e) {
            statusLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleBatal() {
        if (onSelesai != null) onSelesai.run();
        Stage stage = (Stage) namaLengkapField.getScene().getWindow();
        stage.close();
    }

    private void clearFields() {
        namaLengkapField.clear();
        nimField.clear();
        noHpField.clear();
        emailField.clear();
        lakiRadio.setSelected(false);
        perempuanRadio.setSelected(false);
    }

    public void setOnSelesai(Runnable onSelesai) {
        this.onSelesai = onSelesai;
    }
} 