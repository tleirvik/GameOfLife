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
    public GameOfLife2D(boolean isDynamic, int r, int c) {

        this.isDynamic = isDynamic;
        
        // sjekker at vi ikke f√•r et tomt array eller 1 x 1 grid
        if(r < 2) {
            r = 10;
        }
        if(c < 2) {
            c = 10;
        }
        
        // bestemmer hvilket brettype som er valgt
        if(isDynamic)
            boardGrid = new DynamicBoard(r, c);
        else
            boardGrid = new FixedBoard(r, c);
    }
    
    @Override
	public void setIsBoardEmpty(boolean boardEmpty) {
		isBoardEmpty = boardEmpty;
	}


	@Override
	public boolean getIsBoardEmpty() {
		return isBoardEmpty;
	}
        

	@Override
	public void populateBoard() {
		System.out.println("Populate");
		
		boolean[][] grid = { //Hardkodet for ¯yeblikket, mÂ byttes ut med noe dynamisk
                    {true, false, false, true, false, true, false, false, true, false},
                    {true, false, false, true, false, true, false, false, true, false},
                    {true, false, false, true, false, true, false, false, true, false},
                    {true, false, false, true, false, true, false, false, true, false},
                    {true, false, false, true, false, true, false, false, true, false},
                    {true, false, false, true, false, true, false, false, true, false},
                    {true, false, false, true, false, true, false, false, true, false},
                    {true, false, false, true, false, true, false, false, true, false},
                    {true, false, false, true, false, true, false, false, true, false},
                    {true, false, false, true, false, true, false, false, true, false}
                };
           
		boardGrid.setBoard(grid);
	}

	@Override
	public void nextGeneration() {
		

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
