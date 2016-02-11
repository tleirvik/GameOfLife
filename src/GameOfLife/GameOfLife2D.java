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

    	// Hardkoder inputveriden til 5
    	super.seedGeneration(5);



	}
	@Override
	public void nextGeneration() {



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
	public int countNeighbours() {

		return 0;
	}

}
