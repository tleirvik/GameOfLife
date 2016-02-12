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
            gol.populateRandomBoard();
        }


	}

	public boolean[][] getBooleanGrid() {
		boolean [][] tempgrid = gol.convertBoardToBoolean();
		for(int i = 0; i < 10; i++) {
			for(int j = 0; j < 10; j++) {
				System.out.println(tempgrid[i][j]);
			}
		}
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

/*	public Cell setCellAliveStatus(boolean isAlive) {
		gol.setCellAliveStatus(isAlive);
	} */
	public int countNeighboursTransport(int row, int column) {

		int temp = gol.countNeighbours(row, column);

		return temp;

	}
	public void setNextGenerationStrategyTransport() {

		System.out.println(gol.toString());
		gol.setNextGenerationStrategy(new BitwiseStrategy());
	}


	/**
	 *
	 */
}