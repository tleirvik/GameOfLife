package GameOfLife;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 *
 *
 *
 */
public class GameController extends Application {

    
	private GameOfLife gol;
	//private FileManagement fileManagement;

	
        
	/**
	 *
	 */
	public void newGame() {
            // hardkoder input (helst en dialogboks for input)
            gol = new GameOfLife2D(false, 0, 0);
	}
        
    /**
	 *
	 */
	public boolean[][] play() {

		if(gol.getIsBoardEmpty()) {
        	System.out.println("Board Empty");
            gol.populateRandomBoard();
        }
		
		//returner brettet
		return  gol.convertBoardToBoolean();
            
	}
        
	/**
	 *
	 */
	public void saveGame() {

	}
        
	/**
	 *
	 */
	public void loadGame() {

	}
        
	/**
	 *
	 */
	public void exportGame() {

	}
        
	/**
	 *
	 */
	@Override
	public void start(Stage stage) throws Exception {
            BorderPane root = (BorderPane) FXMLLoader.load(getClass().
                getResource("MainWindow.fxml"));
            
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("grafikk.css").
                toExternalForm());
            
            stage.setTitle("Game Of Life");
            stage.setScene(scene);
            stage.show();
	}
        
    public static void main(String[] args) {
        launch(args);
    }
}
