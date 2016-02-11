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
public class GameController {

    
	private GameOfLife gol;
	private FileManagement fileManagement;

	
        
	/**
	 *
	 */
	public void newGame() {
            // hardkoder input (helst en dialogboks for input)
            gol = new GameOfLife2D(!true, 0, 0);
	}
        
    /**
	 *
	 */
	public void play() {

		if(gol.getIsBoardEmpty()) {
        	System.out.println("Board Empty");
            gol.populateBoard();
        }
		
            
	}
	
	public boolean[][] getBooleanGrid() {
		return gol.convertBoardToBoolean();
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
}
