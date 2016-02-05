package view;

import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class GoLMain extends Application {

	@Override
	public void start( Stage primaryStage ) {
		try {

			// opprinnelig GridPane og myDocument.fxml
	        Node root =  FXMLLoader.load(getClass().
	                getResource("MainWindow.fxml"));

	        Scene scene = new Scene((Parent) root);


	    //    scene.getStylesheets().add(getClass().getResource("grafikk.css").
	      //          toExternalForm());

	        primaryStage.setResizable(true);
	        primaryStage.setScene(scene);
	        primaryStage.show();

		} catch( Exception e ) {

			e.printStackTrace();
		}
	}

	public static void main( String[] args ) {
		launch( args );
	}
}
