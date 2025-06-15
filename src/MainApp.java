import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            // Muat file FXML untuk halaman login
            Parent root = FXMLLoader.load(getClass().getResource("/login/login.fxml"));

            // Atur judul jendela
            primaryStage.setTitle("Aplikasi Login Keamanan");
            
            // Tambahkan icon aplikasi (opsional)
            // primaryStage.getIcons().add(new Image("/images/app-icon.png"));

            // Atur scene dan tampilkan
            Scene scene = new Scene(root);
            
            // Tambahkan CSS styling (opsional)
            // scene.getStylesheets().add(getClass().getResource("/styles/application.css").toExternalForm());
            
            primaryStage.setScene(scene);
            primaryStage.setResizable(false); // Tidak dapat diubah ukurannya
            primaryStage.centerOnScreen(); // Posisi di tengah layar
            primaryStage.show();
            
        } catch (Exception e) {
            System.err.println("Error loading login page: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}