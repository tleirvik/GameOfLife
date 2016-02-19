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
            rows = 10;
        }
        if(columns < 2) {
            columns = 10;
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
    	for(int row = 0; row < board.getRows(); row++) {
    		for(int col = 0; col < board.getColumns(); col++) {
                board.setCellAliveState(row,col, super.seedGenerator(5));
    		}
    	}
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
	public void nextGeneration() {
        boolean[][] tempBoard = new boolean[board.getRows()][board.getColumns()];

        for(int i = 0; i < tempBoard.length; i++) {
            for(int j = 0; j < tempBoard[i].length; j++) {
                tempBoard[i][j] = board.getCellAliveState(i, j);
                tempBoard[i][j] = countNeighbours(i, j);
            }
        }
        board.setCellArray(tempBoard);
	}

	@Override
	public boolean countNeighbours(int i, int j) {
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

            if(neighbors == 3)
                return true;

            return ( board.getCellAliveState(i, j) && neighbors == 2 );
	}

	/*

	@Override
	public void nextGeneration() {

		Cell[][] tempBoard = boardGrid.getCells();

		for(int row = 0; row < boardGrid.getRows(); row++) {
    		for(int col = 0; col < boardGrid.getColumns(); col++) {

    			//Dead and has exactly 3 neighbours
                if(!tempBoard[row][col].getIsAlive() && countNeighbours(row,col) == 3) {
                    tempBoard[row][col].setIsAlive(true);
                }

                //Alive and has less than 2 neighbours
                if(tempBoard[row][col].getIsAlive() && countNeighbours(row,col) < 2) {
                    tempBoard[row][col].setIsAlive(false);
                }

                //Alive and has exactly 2 or 3 neighbours UNØDVENDIG??
                /*if(tempBoard[row][col].getIsAlive() && countNeighbours(row,col) == 2 && countNeighbours(row,col) == 3) {
                    tempBoard[row][col].setIsAlive(true);
                }

                //Alive and has more than 3 neighbours
                if(tempBoard[row][col].getIsAlive() && countNeighbours(row,col) > 3) {
                    tempBoard[row][col].setIsAlive(false);
                }
    		}
    	}

		boardGrid.setCells(tempBoard);
	}

	@Override
	public int countNeighbours(int row, int column) {

		int neighbours = 0;

        int rowPos, columnPos;

        //Et array for hver offset-verdi nødvendig for å finne de 8 cellene omkring en celle
        int[][] position = { {0,-1}, {-1,-1}, {-1,0},
    						{-1,+1}, /* CELLE HER	{0,+1},
    						{+1,+1}, {+1,0},  {+1,-1}
    					};

        //position.length = 8 fordi det bare er 8 celler rundt
        for(int i = 0; i < position.length; i++) {

            rowPos = row + position[i][0];
            columnPos = column + position[i][1];

            //TEST OM CELLEN LEVER
            if(rowPos >= 0 && rowPos < boardGrid.getRows() && columnPos >= 0 && columnPos < boardGrid.getColumns()) {
                if(boardGrid.getCell(rowPos, columnPos).getIsAlive()) {
                    neighbours++;
                }
            }
        }
        return neighbours;
	}*/

	public boolean getCellAliveStatus(int row, int column) {
		return board.getCellAliveState(row, column);
	}

	public void setCellAliveStatus(int row, int column, boolean isAlive) {
		board.setCellAliveState(row, column, isAlive);
	}

}
