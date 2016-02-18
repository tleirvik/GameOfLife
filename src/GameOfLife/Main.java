package GameOfLife;

import java.sql.Time;
import java.util.Timer;

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
            stage.show();

	}

    public static void main(String[] args) {

    	/*
    	GameOfLife2D gol = new GameOfLife2D(false, 10000, 10000);

    	long startTime = System.currentTimeMillis();
    	for(int i = 0; i < 1; i++) gol.nextGeneration();
    	long endTime = System.currentTimeMillis();


    	long duration = (endTime - startTime);

    	System.out.println(duration);
		*/

    	launch(args);
    }
}
