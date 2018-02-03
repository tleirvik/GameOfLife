package no.tleirvik;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ResourceBundle;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/MainWindow.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        scene.getStylesheets().add(getClass().getResource(
                "/stylesheet.css").toExternalForm());

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
