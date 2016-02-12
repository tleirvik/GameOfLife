package GameOfLife;

public class GameOfLife2D extends GameOfLife{

	private Board boardGrid;
    private boolean isDynamic;
    private boolean isBoardEmpty;

    protected NextGenerationStrategy nextGenerationStrategy;

    /* Strategy pattern */
    public void applyNextGeneration() {
    	// next gen method
    }
    /* Må endre constructor til å sette default strategy */
    public void setNextGenerationStrategy(NextGenerationStrategy nextGenerationStrategy) {
    	this.nextGenerationStrategy = nextGenerationStrategy;
    }

    /**
     * a
     * @param isDynamic
     * @param r
     * @param c
     */
    public GameOfLife2D(boolean isDynamic, int rows, int columns) {

        this.isDynamic = isDynamic;
        this.isBoardEmpty = true;
        this.nextGenerationStrategy = new DefaultStrategy();

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

		// Kaller den abstraherte metoden for algoritmen.
		nextGenerationStrategy.nextGeneration(boardGrid);

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
