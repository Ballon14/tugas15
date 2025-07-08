package controller;

import database.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class FormTambahMahasiswaController {
    @FXML
    private TextField tfNo;
    @FXML
    private TextField tfNim;
    @FXML
    private TextField tfKelas;
    @FXML
    private TextField tfIpk;
    @FXML
    private TextField tfSemester;
    @FXML
    private TextField tfTotalSks;
    @FXML
    private Button btnSimpan;
    @FXML
    private Button btnBatal;

    @FXML
    private void handleSimpan(ActionEvent event) {
        try (Connection conn = DatabaseConnection.connect()) {
            String sql = "INSERT INTO mahasiswa (no, nim, kelas, ipk, semester, total_sks) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(tfNo.getText()));
            stmt.setInt(2, Integer.parseInt(tfNim.getText()));
            stmt.setString(3, tfKelas.getText());
            stmt.setDouble(4, Double.parseDouble(tfIpk.getText()));
            stmt.setInt(5, Integer.parseInt(tfSemester.getText()));
            stmt.setInt(6, Integer.parseInt(tfTotalSks.getText()));
            stmt.executeUpdate();
            // Tutup window setelah simpan
            ((Stage) btnSimpan.getScene().getWindow()).close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBatal(ActionEvent event) {
        ((Stage) btnBatal.getScene().getWindow()).close();
    }
} 