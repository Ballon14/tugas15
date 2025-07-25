import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/mahasiswa.fxml"));
        primaryStage.setTitle("Data Mahasiswa");
        primaryStage.setScene(new Scene(root, 600, 500));
        primaryStage.setResizable(false); 
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}