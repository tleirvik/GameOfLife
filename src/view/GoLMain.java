package view;
	
import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

public class GoLMain extends Application {
	
	@Override
	public void start( Stage primaryStage ) {
		try {
			
			URL fxmlUrl = getClass().getResource( "MainWindow.fxml" );
		    AnchorPane gameRoot = FXMLLoader.load( fxmlUrl );
			Scene scene = new Scene( gameRoot, 600, 550 );
			primaryStage.setScene( scene );
			primaryStage.setMaxHeight(550);
			primaryStage.setMaxWidth(600);
			primaryStage.setResizable(false);
			primaryStage.setTitle("Game Of Life");
			primaryStage.show();
			
		} catch( Exception e ) {
			
			e.printStackTrace();
		}
	}
	
	public static void main( String[] args ) {
		launch( args );
	}
}
