package controller;

import database.DatabaseConnection;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class MahasiswaController {
    @FXML
    private TableView<ObservableList<Object>> tableMahasiswa;
    @FXML
    private TableColumn<ObservableList<Object>, Integer> colNo;
    @FXML
    private TableColumn<ObservableList<Object>, Integer> colNim;
    @FXML
    private TableColumn<ObservableList<Object>, String> colKelas;
    @FXML
    private TableColumn<ObservableList<Object>, Double> colIpk;
    @FXML
    private TableColumn<ObservableList<Object>, Integer> colSemester;
    @FXML
    private TableColumn<ObservableList<Object>, Integer> colTotalSks;
    @FXML
    private Button btnTambah;

    @FXML
    public void initialize() {
        colNo.setCellValueFactory(data -> new SimpleObjectProperty<>((Integer) data.getValue().get(0)));
        colNim.setCellValueFactory(data -> new SimpleObjectProperty<>((Integer) data.getValue().get(1)));
        colKelas.setCellValueFactory(data -> new SimpleObjectProperty<>((String) data.getValue().get(2)));
        colIpk.setCellValueFactory(data -> new SimpleObjectProperty<>((Double) data.getValue().get(3)));
        colSemester.setCellValueFactory(data -> new SimpleObjectProperty<>((Integer) data.getValue().get(4)));
        colTotalSks.setCellValueFactory(data -> new SimpleObjectProperty<>((Integer) data.getValue().get(5)));
        loadData();
    }

    private void loadData() {
        ObservableList<ObservableList<Object>> mahasiswaList = FXCollections.observableArrayList();
        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id, nim, kelas, ipk, semester, total_sks FROM mahasiswa")) {
            while (rs.next()) {
                ObservableList<Object> row = FXCollections.observableArrayList();
                row.add(rs.getInt("id"));
                row.add(rs.getInt("nim"));
                row.add(rs.getString("kelas"));
                row.add(rs.getDouble("ipk"));
                row.add(rs.getInt("semester"));
                row.add(rs.getInt("total_sks"));
                mahasiswaList.add(row);
            }
            tableMahasiswa.setItems(mahasiswaList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleTambah(ActionEvent event) {
        try {
            Parent formTambah = FXMLLoader.load(getClass().getResource("/view/form_tambah_mahasiswa.fxml"));
            Stage stage = (Stage) tableMahasiswa.getScene().getWindow();
            stage.getScene().setRoot(formTambah);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
} 