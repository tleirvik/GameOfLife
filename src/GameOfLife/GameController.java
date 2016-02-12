package GameOfLife;

/**
 *
 *
 *
 */
public class GameController {

    
	private GameOfLife gol;
	//private FileManagement fileManagement;

	
        
	/**
	 *
	 */
	public void newGame(boolean isDynamic, int rows, int columns) {
            gol = new GameOfLife2D(isDynamic, rows, columns);
	}
        
	public void play() {

		if(gol.getIsBoardEmpty()) {
        	System.out.println("Board Empty");
            gol.populateRandomBoard();
        }
		
		gol.nextGeneration();
	}
	
	public boolean[][] getBooleanGrid() {
		return gol.convertBoardToBoolean();
	}
        
	public void saveGame() {

	}
       
	public void loadGame() {

	}
        
	public void exportGame() {

	}

	public boolean getCellAliveStatus(int row, int column) {
		return gol.getCellAliveStatus(row, column);
	}
	
	public void setCellAliveStatus(int row, int column, boolean isAlive) {
		gol.setCellAliveStatus(row, column, isAlive);
	}
	
}