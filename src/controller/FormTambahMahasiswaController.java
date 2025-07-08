package controller;

import database.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class FormTambahMahasiswaController {
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
            // Validasi NIM tidak boleh sama
            String cekNim = "SELECT COUNT(*) FROM mahasiswa WHERE nim = ?";
            PreparedStatement cekStmt = conn.prepareStatement(cekNim);
            cekStmt.setInt(1, Integer.parseInt(tfNim.getText()));
            java.sql.ResultSet rs = cekStmt.executeQuery();
            rs.next();
            if (rs.getInt(1) > 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Input Gagal");
                alert.setHeaderText(null);
                alert.setContentText("NIM sudah terdaftar! Silakan gunakan NIM lain.");
                alert.showAndWait();
                return;
            }
            // Simpan data jika NIM belum ada
            String sql = "INSERT INTO mahasiswa (nim, kelas, ipk, semester, total_sks) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(tfNim.getText()));
            stmt.setString(2, tfKelas.getText());
            stmt.setDouble(3, Double.parseDouble(tfIpk.getText()));
            stmt.setInt(4, Integer.parseInt(tfSemester.getText()));
            stmt.setInt(5, Integer.parseInt(tfTotalSks.getText()));
            stmt.executeUpdate();
            // Setelah simpan, kembali ke halaman utama
            Parent mainPage = FXMLLoader.load(getClass().getResource("/view/mahasiswa.fxml"));
            Stage stage = (Stage) btnSimpan.getScene().getWindow();
            stage.getScene().setRoot(mainPage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBatal(ActionEvent event) {
        try {
            Parent mainPage = FXMLLoader.load(getClass().getResource("/view/mahasiswa.fxml"));
            Stage stage = (Stage) btnBatal.getScene().getWindow();
            stage.getScene().setRoot(mainPage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
} 