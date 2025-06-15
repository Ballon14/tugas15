import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Muat file FXML untuk halaman login
        Parent root = FXMLLoader.load(getClass().getResource("login/Login.fxml"));

        // Atur judul jendela
        primaryStage.setTitle("Aplikasi Login");

        // Atur scene dan tampilkan
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}