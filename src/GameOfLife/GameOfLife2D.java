package GameOfLife;

public class GameOfLife2D extends GameOfLife{

	private Board boardGrid;
    private boolean isDynamic;
    private boolean isBoardEmpty;
    private int nextGenerationCounter;

    /**
     * a
     * @param isDynamic
     * @param r
     * @param c
     */
    public GameOfLife2D(boolean isDynamic, int rows, int columns) {

        this.isDynamic = isDynamic;
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
            boardGrid = new DynamicBoard(rows, columns);
        else
            boardGrid = new FixedBoard(rows, columns);
    }

    @Override
	public void populateRandomBoard() {
    	this.isBoardEmpty = false;
    	for(int row = 0; row < boardGrid.getRows(); row++) {
    		for(int col = 0; col < boardGrid.getColumns(); col++) {
                Cell cell = boardGrid.getCell(row,col);
                cell.setIsAlive(super.seedGeneration(5));// Hardkoder inputveriden til 5
    		}
    	}
    }

    @Override
	public void setIsBoardEmpty(boolean boardEmpty) {
		isBoardEmpty = boardEmpty;
	}

	@Override
	public boolean getIsBoardEmpty() {
		return isBoardEmpty;
	}

	public boolean[][] convertBoardToBoolean() {
		System.out.println("Henter brettet (convertBoardToBoolean)");
		boolean[][] grid = new boolean[boardGrid.getRows()][boardGrid.getColumns()];

		for(int row = 0; row < grid.length; row++) {
    		for(int col = 0; col < grid[row].length; col++) {
                grid[row][col] = boardGrid.getCell(row, col).getIsAlive();
    		}
    	}

		return grid;

	}

	@Override
	public void nextGeneration() {
		System.out.println("Kjører nextGeneration");
		nextGenerationCounter++;
		System.out.println(nextGenerationCounter);
            Cell[][] tempBoard = new Cell[boardGrid.getRows()][boardGrid.getColumns()];

            for(int i = 0; i < tempBoard.length; i++) {
                for(int j = 0; j < tempBoard[i].length; j++) {

                    tempBoard[i][j] = boardGrid.getCell(i, j).copy();

                    tempBoard[i][j].setIsAlive(countNeighbours(i, j));

                    System.out.print(boardGrid.getCell(i, j).getIsAlive());
                }
                System.out.println();
            }


            boardGrid.setCells(tempBoard);



            for(int i = 0; i < tempBoard.length; i++) {
                for(int j = 0; j < tempBoard[i].length; j++) {
                    System.out.print(boardGrid.getCell(i, j).getIsAlive());
                }
                System.out.println();
            }

	}

	@Override
	public boolean countNeighbours(int i, int j) {
            int neighbors = 0;

            if(j > 0) {
                if(boardGrid.getCell(i, (j - 1)).getIsAlive())
                        neighbors++;
                if( i > 0) {
                    if(boardGrid.getCell((i - 1), (j - 1)).getIsAlive())
                        neighbors++;
                }
                if(i < boardGrid.getRows() - 1) {
                    if(boardGrid.getCell((i + 1), (j - 1)).getIsAlive())
                        neighbors++;
                }
            }

            if(j < boardGrid.getColumns() - 1) {
                if(boardGrid.getCell(i, (j + 1)).getIsAlive())
                    neighbors++;
                if(i > 0) {
                    if(boardGrid.getCell((i - 1), (j + 1)).getIsAlive())
                        neighbors++;
                }
                if(i < boardGrid.getRows() - 1) {
                    if(boardGrid.getCell((i + 1), (j + 1)).getIsAlive())
                        neighbors++;
                }
            }

            if(i > 0) {
                if(boardGrid.getCell((i - 1), j).getIsAlive())
                    neighbors++;
            }
            if( i < boardGrid.getRows() - 1) {
                if(boardGrid.getCell((i + 1), j).getIsAlive())
                    neighbors++;
            }

            if(neighbors == 3)
                return true;

            return ( boardGrid.getCell(i, j).getIsAlive() && neighbors == 2 );
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
		return boardGrid.getCell(row, column).getIsAlive();
	}

	public void setCellAliveStatus(int row, int column, boolean isAlive) {
		boardGrid.getCell(row, column).setIsAlive(isAlive);
	}
	public int getNextGenerationCounter() {
		System.out.println("GOL:" + nextGenerationCounter);
		return nextGenerationCounter;

	}

}
