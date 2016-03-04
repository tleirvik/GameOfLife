package GameOfLife;

public class GameOfLife2D extends GameOfLife{

	private Board board;
    private boolean isBoardEmpty = false;

    /**
     * a
     * @param isDynamic
     * @param r
     * @param c
     */
    public GameOfLife2D(boolean isDynamic, int rows, int columns) {
    	this.isBoardEmpty = true;

        // sjekker at vi ikke får et tomt array eller 1 x 1 grid
        // Lager 10x10 array
        if(rows < 2) {
            rows = 1;
        }
        if(columns < 2) {
            columns = 1;
        }

        // bestemmer hvilket brettype som er valgt
        if(isDynamic)
            board = new DynamicBoard(rows, columns);
        else
            board = new FixedBoard(rows, columns);

    }


    public GameOfLife2D(boolean[][] board, boolean isDynamic) {

    	int rows = board.length;
    	int columns = board[0].length;

    	if(isDynamic)
            this.board = new DynamicBoard(rows, columns);
        else
            this.board = new FixedBoard(rows, columns);

    	this.board.setCellArray(board);
	}


	/**
     *
     */
    @Override
	public void populateRandomBoard() {
    	this.isBoardEmpty = false;
    	/*
    	for(int row = 0; row < board.getRows(); row++) {
    		for(int col = 0; col < board.getColumns(); col++) {
                board.setCellAliveState(row,col, super.seedGenerator(5));
    		}
    	}
    	*/
    }


    /**
     *
     * @param boardEmpty
     */
    @Override
	public void setIsBoardEmpty(boolean boardEmpty) {
		isBoardEmpty = boardEmpty;
	}


    /**
     *
     * @return
     */
	@Override
	public boolean getIsBoardEmpty() {
		return isBoardEmpty;
	}


	/**
	 * Returns a two-dimensional boolean array based on the current game board
	 * @return boolean[][]
	 */
	public boolean[][] convertBoardToBoolean() {
		boolean[][] grid = new boolean[board.getRows()][board.getColumns()];

		for(int row = 0; row < grid.length; row++) {
    		for(int col = 0; col < grid[row].length; col++) {
                grid[row][col] = board.getCellAliveState(row, col);
    		}
    	}
		return grid;
	}

	@Override
	public int countNeighbours(int i, int j) {
            int neighbors = 0;

            if(j > 0) {
                if(board.getCellAliveState(i, (j - 1)))
                        neighbors++;
                if( i > 0) {
                    if(board.getCellAliveState((i - 1), (j - 1)))
                        neighbors++;
                	}
                	if(i < board.getRows() - 1) {
            			if(board.getCellAliveState((i + 1), (j - 1)))
            				neighbors++;
                	}
            }

            if(j < board.getColumns() - 1) {
                if(board.getCellAliveState(i, (j + 1)))
                    neighbors++;
                if(i > 0) {
                    if(board.getCellAliveState((i - 1), (j + 1)))
                        neighbors++;
                }
                if(i < board.getRows() - 1) {
                    if(board.getCellAliveState((i + 1), (j + 1)))
                        neighbors++;
                }
            }

            if(i > 0) {
                if(board.getCellAliveState((i - 1), j))
                    neighbors++;
            }
            if( i < board.getRows() - 1) {
                if(board.getCellAliveState((i + 1), j))
                    neighbors++;
            }
            /*
            if(neighbors == 3)
                return true;

            return ( board.getCellAliveState(i, j) && neighbors == 2 );
            */
            return neighbors;
	}

	@Override
	public void nextGeneration() {

		boolean[][] tempBoard = new boolean[board.getRows()][board.getColumns()];

		for(int row = 0; row < board.getRows(); row++) {
    		for(int col = 0; col < board.getColumns(); col++) {
    			tempBoard[row][col] = (countNeighbours(row,col) == 3) || ( board.getCellAliveState(row, col) && countNeighbours(row,col) == 2 );
    		}
    	}
		board.setCellArray(tempBoard);
	}

	public boolean getCellAliveStatus(int row, int column) {
		return board.getCellAliveState(row, column);
	}

	public void setCellAliveStatus(int row, int column, boolean isAlive) {
		board.setCellAliveState(row, column, isAlive);
	}

}
