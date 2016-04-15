package GameOfLife;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage stage) throws Exception {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainWindow.fxml"));
            Parent root = (Parent)loader.load();
            ViewController controller = (ViewController)loader.getController();
            controller.setMainStageDialogBoxes(stage);

            Scene scene = new Scene(root);

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
