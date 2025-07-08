package controller;

import database.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import model.Mahasiswa;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class MahasiswaController {
    @FXML
    private TableView<Mahasiswa> tableMahasiswa;
    @FXML
    private TableColumn<Mahasiswa, Integer> colNo;
    @FXML
    private TableColumn<Mahasiswa, Integer> colNim;
    @FXML
    private TableColumn<Mahasiswa, String> colKelas;
    @FXML
    private TableColumn<Mahasiswa, Double> colIpk;
    @FXML
    private TableColumn<Mahasiswa, Integer> colSemester;
    @FXML
    private TableColumn<Mahasiswa, Integer> colTotalSks;
    @FXML
    private Button btnTambah;

    private ObservableList<Mahasiswa> mahasiswaList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colNo.setCellValueFactory(new PropertyValueFactory<>("no"));
        colNim.setCellValueFactory(new PropertyValueFactory<>("nim"));
        colKelas.setCellValueFactory(new PropertyValueFactory<>("kelas"));
        colIpk.setCellValueFactory(new PropertyValueFactory<>("ipk"));
        colSemester.setCellValueFactory(new PropertyValueFactory<>("semester"));
        colTotalSks.setCellValueFactory(new PropertyValueFactory<>("totalSks"));
        loadData();
    }

    private void loadData() {
        mahasiswaList.clear();
        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM mahasiswa")) {
            while (rs.next()) {
                mahasiswaList.add(new Mahasiswa(
                        // id diisi 0 atau rs.getInt("id") tapi tidak digunakan
                        0,
                        rs.getInt("no"),
                        rs.getInt("nim"),
                        rs.getString("kelas"),
                        rs.getDouble("ipk"),
                        rs.getInt("semester"),
                        rs.getInt("total_sks")
                ));
            }
            tableMahasiswa.setItems(mahasiswaList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleTambah(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/form_tambah_mahasiswa.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Tambah Data Mahasiswa");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
} 