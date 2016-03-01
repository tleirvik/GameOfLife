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

	public void newGame(boolean[][] board, boolean isDynamic) {
        gol = new GameOfLife2D(board, isDynamic);
	}

	public void play() {

		if(gol.getIsBoardEmpty()) {
        	System.out.println("Board Empty");
            gol.populateRandomBoard();

        	gol.setIsBoardEmpty(false);
        	/*
        	//{true, false, false, true}
        	gol.setCellAliveStatus(0,0,true);
        	gol.setCellAliveStatus(0,1,false);
        	gol.setCellAliveStatus(0,2,false);
        	gol.setCellAliveStatus(0,3,true);

        	//{false, true, true, false}
        	gol.setCellAliveStatus(1,0,false);
        	gol.setCellAliveStatus(1,1,true);
        	gol.setCellAliveStatus(1,2,true);
        	gol.setCellAliveStatus(1,3,false);

        	//{false, true, true, false}
        	gol.setCellAliveStatus(2,0,false);
        	gol.setCellAliveStatus(2,1,true);
        	gol.setCellAliveStatus(2,2,true);
        	gol.setCellAliveStatus(2,3,false);

        	//{true, false, false, true}
        	gol.setCellAliveStatus(3,0,true);
        	gol.setCellAliveStatus(3,1,false);
        	gol.setCellAliveStatus(3,2,false);
        	gol.setCellAliveStatus(3,3,true);
        	*/
        } else {
        	gol.nextGeneration();
        }

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