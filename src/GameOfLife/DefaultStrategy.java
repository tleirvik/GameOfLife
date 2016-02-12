package GameOfLife;

public class DefaultStrategy implements NextGenerationStrategy{

	private GameController gc = new GameController();


	@Override
	public void nextGeneration(Board boardGrid) {

		Cell[][] tempBoard = boardGrid.getBoard();

		for(int row = 0; row < boardGrid.getRow(); row++) {
    		for(int col = 0; col < boardGrid.getColumn(); col++) {

    			//Dead and has exactly 3 neighbours
                if(!tempBoard[row][col].getIsAlive() && gc.countNeighboursTransport(row,col) == 3) {
                    tempBoard[row][col].setIsAlive(true);
                }

                //Alive and has less than 2 neighbours
                if(tempBoard[row][col].getIsAlive() && gc.countNeighboursTransport(row,col) < 2) {
                    tempBoard[row][col].setIsAlive(false);
                }

                //Alive and has exactly 2 or 3 neighbours
                if(tempBoard[row][col].getIsAlive() && gc.countNeighboursTransport(row,col) == 2 && gc.countNeighboursTransport(row,col) == 3) {
                    tempBoard[row][col].setIsAlive(true);
                }

                //Alive and has more than 3 neighbours
                if(tempBoard[row][col].getIsAlive() && gc.countNeighboursTransport(row,col) > 3) {
                    tempBoard[row][col].setIsAlive(false);
                }
    		}
    	}

		//boardGrid.setBoard(tempBoard);

	}
}
