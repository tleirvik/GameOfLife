package GameOfLife;

import java.util.List;

public class GameOfLife2D extends GameOfLife{
    private FixedBoard board;
    private boolean isBoardEmpty = false;

    /**
     * a
     * @param isDynamic
     * @param rows
     * @param columns
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
        if(isDynamic) {
            // board = new DynamicBoard(rows, columns);
        } else {
            board = new FixedBoard(rows, columns);
        }
        
        //board.setMetaData(new MetaData());
    }


    public GameOfLife2D(byte[][] board, boolean isDynamic) {

    	int rows = board.length;
    	int columns = board[0].length;

    	if(isDynamic) {
            // this.board = new DynamicBoard(rows, columns);
        } else {
            this.board = new FixedBoard(rows, columns);
        }

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
     * @return
     */
    public FixedBoard getBoard() {
    	return board;
    }
    
    public byte[][] getGameBoard() {
    	return board.getCellArray();
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
        
        return neighbors;
    }

    @Override
    public void nextGeneration() {
        byte[][] tempBoard = new byte[board.getRows()][board.getColumns()];

        for(int row = 0; row < board.getRows(); row++) {
            for(int col = 0; col < board.getColumns(); col++) {
                tempBoard[row][col] = ((countNeighbours(row,col) == 3) || ( board.getCellAliveState(row, col) && countNeighbours(row,col) == 2 )) ? (byte)1 : (byte)0;
            }
        }
        board.setCellArray(tempBoard);
    }

    public boolean getCellAliveState(int row, int column) {
        return board.getCellAliveState(row, column);
    }

    public void setCellAliveState(int row, int column, boolean isAlive) {
        board.setCellAliveState(row, column, isAlive);
    }

    public void setMetaData(MetaData metadata) {
        board.setMetaData(metadata);
    }

}
