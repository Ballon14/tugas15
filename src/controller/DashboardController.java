package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Pagination;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.scene.layout.StackPane;
import javafx.scene.Node;

public class DashboardController {

    @FXML
    private Button logoutButton;

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label userInfoLabel;

    @FXML
    private TableView<Mahasiswa> mahasiswaTable;
    @FXML
    private TableColumn<Mahasiswa, String> colNama;
    @FXML
    private TableColumn<Mahasiswa, String> colNim;
    @FXML
    private TableColumn<Mahasiswa, String> colNoHp;
    @FXML
    private TableColumn<Mahasiswa, String> colEmail;
    @FXML
    private TableColumn<Mahasiswa, String> colJenisKelamin;
    @FXML
    private Pagination paginationMahasiswa;

    @FXML
    private TableView<Matakuliah> matakuliahTable;
    @FXML
    private TableColumn<Matakuliah, String> colKodeMatakuliah;
    @FXML
    private TableColumn<Matakuliah, String> colNamaMatakuliah;
    @FXML
    private TableColumn<Matakuliah, Integer> colSks;
    @FXML
    private Pagination paginationMatakuliah;

    @FXML
    private Button btnInputMahasiswa;
    @FXML
    private Button btnInputMatakuliah;

    @FXML
    private StackPane mainContent;
    @FXML
    private VBox tablePage;

    private static final int ROWS_PER_PAGE = 10;
    private ObservableList<Mahasiswa> mahasiswaList = FXCollections.observableArrayList();
    private ObservableList<Matakuliah> matakuliahList = FXCollections.observableArrayList();

    private String currentUser;

    private Parent inputMahasiswaPage;
    private Parent inputMatakuliahPage;

    public void setWelcomeMessage(String username) {
        this.currentUser = username;
        if (welcomeLabel != null) {
            welcomeLabel.setText("Selamat Datang, " + username + "!");
        }
        if (userInfoLabel != null) {
            userInfoLabel.setText("User: " + username);
        }
    }

    @FXML
    public void logout() {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Konfirmasi Logout");
        confirmationAlert.setHeaderText("Apakah Anda yakin ingin keluar?");
        confirmationAlert.setContentText("Anda akan keluar dari sistem.");
        
        ButtonType yesButton = new ButtonType("Ya, Keluar");
        ButtonType noButton = new ButtonType("Batal", ButtonBar.ButtonData.CANCEL_CLOSE);
        confirmationAlert.getButtonTypes().setAll(yesButton, noButton);

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        
        if (result.isPresent() && result.get() == yesButton) {
            performLogout();
        }
    }

    private void performLogout() {
        try {
            System.out.println("User " + currentUser + " logout");

            Stage stage = (Stage) logoutButton.getScene().getWindow();

            Parent root = FXMLLoader.load(getClass().getResource("/login/logout.fxml"));
            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();

        } catch (IOException e) {
            System.err.println("Error during logout: " + e.getMessage());
            showErrorAlert("Error", "Terjadi kesalahan saat logout: " + e.getMessage());
        }
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void initialize() {
        // Setup kolom mahasiswa
        colNama.setCellValueFactory(new PropertyValueFactory<>("namaLengkap"));
        colNim.setCellValueFactory(new PropertyValueFactory<>("nim"));
        colNoHp.setCellValueFactory(new PropertyValueFactory<>("noHp"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colJenisKelamin.setCellValueFactory(new PropertyValueFactory<>("jenisKelamin"));
        // Setup kolom matakuliah
        colKodeMatakuliah.setCellValueFactory(new PropertyValueFactory<>("kodeMatakuliah"));
        colNamaMatakuliah.setCellValueFactory(new PropertyValueFactory<>("namaMatakuliah"));
        colSks.setCellValueFactory(new PropertyValueFactory<>("sks"));
        // Load data awal
        loadMahasiswa();
        loadMatakuliah();
        // Setup pagination
        paginationMahasiswa.setPageFactory(this::createMahasiswaPage);
        paginationMatakuliah.setPageFactory(this::createMatakuliahPage);
    }

    private void loadMahasiswa() {
        mahasiswaList.clear();
        try (Connection connection = database.DatabaseConnection.connect();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM mahasiswa")) {
            while (rs.next()) {
                mahasiswaList.add(new Mahasiswa(
                    rs.getString("nama_lengkap"),
                    rs.getString("nim"),
                    rs.getString("no_hp"),
                    rs.getString("email"),
                    rs.getString("jenis_kelamin")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int pageCount = (int) Math.ceil((double) mahasiswaList.size() / ROWS_PER_PAGE);
        paginationMahasiswa.setPageCount(pageCount == 0 ? 1 : pageCount);
    }

    private void loadMatakuliah() {
        matakuliahList.clear();
        try (Connection connection = database.DatabaseConnection.connect();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM matakuliah")) {
            while (rs.next()) {
                matakuliahList.add(new Matakuliah(
                    rs.getString("kode_matakuliah"),
                    rs.getString("nama_matakuliah"),
                    rs.getInt("sks")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int pageCount = (int) Math.ceil((double) matakuliahList.size() / ROWS_PER_PAGE);
        paginationMatakuliah.setPageCount(pageCount == 0 ? 1 : pageCount);
    }

    private VBox createMahasiswaPage(int pageIndex) {
        int fromIndex = pageIndex * ROWS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, mahasiswaList.size());
        mahasiswaTable.setItems(FXCollections.observableArrayList(mahasiswaList.subList(fromIndex, toIndex)));
        return new VBox(mahasiswaTable);
    }

    private VBox createMatakuliahPage(int pageIndex) {
        int fromIndex = pageIndex * ROWS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, matakuliahList.size());
        matakuliahTable.setItems(FXCollections.observableArrayList(matakuliahList.subList(fromIndex, toIndex)));
        return new VBox(matakuliahTable);
    }

    @FXML
    private void handleInputMahasiswa(ActionEvent event) {
        try {
            if (inputMahasiswaPage == null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/dashboard/input_mahasiswa.fxml"));
                inputMahasiswaPage = loader.load();
                InputMahasiswaController controller = loader.getController();
                controller.setOnSelesai(this::showTablePage);
            }
            mainContent.getChildren().setAll(inputMahasiswaPage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleInputMatakuliah(ActionEvent event) {
        try {
            if (inputMatakuliahPage == null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/dashboard/input_matakuliah.fxml"));
                inputMatakuliahPage = loader.load();
                InputMatakuliahController controller = loader.getController();
                controller.setOnSelesai(this::showTablePage);
            }
            mainContent.getChildren().setAll(inputMatakuliahPage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showTablePage() {
        loadMahasiswa();
        loadMatakuliah();
        mainContent.getChildren().setAll(tablePage);
    }

    @FXML
    private void handleDashboard(ActionEvent event) {
        showTablePage();
    }

    // Model Mahasiswa
    public static class Mahasiswa {
        private final String namaLengkap, nim, noHp, email, jenisKelamin;
        public Mahasiswa(String namaLengkap, String nim, String noHp, String email, String jenisKelamin) {
            this.namaLengkap = namaLengkap;
            this.nim = nim;
            this.noHp = noHp;
            this.email = email;
            this.jenisKelamin = jenisKelamin;
        }
        public String getNamaLengkap() { return namaLengkap; }
        public String getNim() { return nim; }
        public String getNoHp() { return noHp; }
        public String getEmail() { return email; }
        public String getJenisKelamin() { return jenisKelamin; }
    }
    // Model Matakuliah
    public static class Matakuliah {
        private final String kodeMatakuliah, namaMatakuliah;
        private final int sks;
        public Matakuliah(String kodeMatakuliah, String namaMatakuliah, int sks) {
            this.kodeMatakuliah = kodeMatakuliah;
            this.namaMatakuliah = namaMatakuliah;
            this.sks = sks;
        }
        public String getKodeMatakuliah() { return kodeMatakuliah; }
        public String getNamaMatakuliah() { return namaMatakuliah; }
        public int getSks() { return sks; }
    }
}