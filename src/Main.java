import controller.ViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainWindow.fxml"));
        Parent root = (Parent) loader.load();
        ViewController controller = (ViewController) loader.getController();

        Scene scene = new Scene(root);


        scene.getStylesheets().add(getClass().getResource(
                "/view/mainWindowStyleSheet.css").toExternalForm());

        stage.setTitle("Game Of Life");
        stage.setScene(scene);
        stage.setMinHeight(690);
        stage.setMinWidth(910);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
