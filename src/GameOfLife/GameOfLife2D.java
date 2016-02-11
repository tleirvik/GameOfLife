package GameOfLife;

public class GameOfLife2D extends GameOfLife{

	private Board boardGrid;
    private boolean isDynamic;
    private boolean isBoardEmpty;

    /**
     * a
     * @param isDynamic
     * @param r
     * @param c
     */
    public GameOfLife2D(boolean isDynamic, int rows, int columns) {

        this.isDynamic = isDynamic;

        // sjekker at vi ikke får et tomt array eller 1 x 1 grid
        // Lager 10x10 array
        if(rows < 2) {
            rows = 10;
        }
        if(columns < 2) {
            columns = 10;
        }

        // bestemmer hvilket brettype som er valgt
        if(isDynamic)
            boardGrid = new DynamicBoard(rows, columns);
        else
            boardGrid = new FixedBoard(rows, columns);
    }
    
    @Override
	public void populateRandomBoard() {
    	for(int row = 0; row < boardGrid.getRow(); row++) {
    		for(int col = 0; col < boardGrid.getColumn(); col++) {
                Cell cell = boardGrid.getCell(row,col);
                cell.setIsAlive(super.seedGeneration(5));// Hardkoder inputveriden til 5
    		}
    	}
    }
    
	@Override
	public void nextGeneration() {
		
		Cell[][] tempBoard = boardGrid.getBoard();
		
		for(int row = 0; row < boardGrid.getRow(); row++) {
    		for(int col = 0; col < boardGrid.getColumn(); col++) {
    			
    			//Dead and has exactly 3 neighbours
                if(!tempBoard[row][col].getIsAlive() && countNeighbours(row,col) == 3) {
                    tempBoard[row][col].setIsAlive(true);
                }
 
                //Alive and has less than 2 neighbours
                if(tempBoard[row][col].getIsAlive() && countNeighbours(row,col) < 2) {
                    tempBoard[row][col].setIsAlive(false);
                }
 
                //Alive and has exactly 2 or 3 neighbours
                if(tempBoard[row][col].getIsAlive() && countNeighbours(row,col) == 2 && countNeighbours(row,col) == 3) {
                    tempBoard[row][col].setIsAlive(true);
                }
                 
                //Alive and has more than 3 neighbours
                if(tempBoard[row][col].getIsAlive() && countNeighbours(row,col) > 3) {
                    tempBoard[row][col].setIsAlive(false);
                }
    		}
    	}
		
		boardGrid.setBoard(tempBoard);
	}
	
    @Override
	public void setIsBoardEmpty(boolean boardEmpty) {
		isBoardEmpty = boardEmpty;
	}
	@Override
	public boolean getIsBoardEmpty() {
		return isBoardEmpty;
	}




	// Konverterer brettet til et todimensjonalt boolsk array
	public boolean[][] convertBoardToBoolean() {

		boolean[][] grid = new boolean[boardGrid.getRow()][boardGrid.getColumn()];

		for(int row = 0; row < grid.length; row++) {
    		for(int col = 0; col < grid[row].length; col++) {
                grid[row][col] = boardGrid.getCell(row, col).getIsAlive();
    		}
    	}

		return grid;

	}

	@Override
	public int countNeighbours(int row, int column) {
		
		Cell[][] tempBoard = boardGrid.getBoard();
		
		int neighbours = 0;
        
        int rowPos, columnPos;
         
        //Et array for hver offset-verdi nødvendig for å finne de 8 cellene omkring en celle
        int[][] position = { {0,-1}, 
        					{-1,-1}, 
    						{-1,0}, 
    						{-1,+1}, 
    						{0,+1}, 
    						{+1,+1}, 
    						{+1,0}, 
    						{+1,-1} 
    					};
         
        //position.length = 8 fordi det bare er 8 celler rundt
        for(int i = 0; i < position.length; i++) {
 
            rowPos = row + position[i][0];
            columnPos = column + position[i][1];
             
            //TEST OM CELLEN LEVER
            if(rowPos >= 0 && rowPos < boardGrid.getRow() && columnPos >= 0 && columnPos < boardGrid.getColumn()) {
                if(boardGrid.getCell(rowPos, columnPos).getIsAlive()) {
                    neighbours++;
                }
            }
        }
        return neighbours;
	}

}
