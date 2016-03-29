package GameOfLife;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        BorderPane root = FXMLLoader.load(getClass().
            getResource("MainWindow.fxml"));

        Scene scene = new Scene(root);
        /*scene.getStylesheets().add(getClass().getResource("grafikk.css").
            toExternalForm());*/

        stage.setTitle("Game Of Life");
        stage.setScene(scene);

        stage.setMinHeight(650);
        stage.setMinWidth(810);

        stage.show();

    }

    public static void main(String[] args) {
    	launch(args);
    }
}
