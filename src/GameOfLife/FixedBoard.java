package GameOfLife;

/**
 * @deprecated Deprecrated since 18.03.2016
 * @author Robin Sean Aron David Lundh, Terje Leirvik, Stian Reistad Røgeberg.
 * 
 * This class contains the gameboard in a fixed size that cannot be changed
 * after creation. It also contains the algorithm for Game Of Life specifically
 * made for a board of a fixed size.
 * 
 */
public class FixedBoard {
    private final MetaData metadata;
    private final byte[][] currentGeneration;
    private final byte[][] nextGeneration;
    
    /**
     * Constructs a board of a fixed size.
     *
     * @param rows The number of rows
     * @param columns The number of columns
     */
    public FixedBoard(int rows, int columns) {
        metadata = new MetaData();
    	currentGeneration = new byte[rows][columns];
        nextGeneration = new byte[rows][columns];
    }

    /**
     * Constructs a boad with the given two-dimensional <code>byte</code>-array and
     * adds the metadata.
     * 
     * @param board The two-dimensional <code>byte</code>-array whose data
     * are to be placed in the FixedBoard.
     * @param metadata The <code>MetaData</code> object that contains the metadata
     * related to the new board.
     * 
     * @see MetaData
     */
    public FixedBoard(byte[][] board, MetaData metadata) {
    	this.metadata = metadata;
    	currentGeneration = new byte[board.length][board[0].length];
        nextGeneration = new byte[board.length][board[0].length];

    	for(int row = 0; row < board.length; row++) {
            System.arraycopy(board[row], 0, currentGeneration[row], 
                    0, board[row].length);
    	}
    }

    /**
     * 
     * @return The number of rows
     */
    public int getRows() {
        return currentGeneration.length;
    }

    /**
     *
     * @return The number of columns
     */
    public int getColumns() {
        return currentGeneration[0].length;
    }


    /**
     *  This method returns the meta data from the
     *  board.
     *
     * @return MetaData Meta data from the board contained in
     * the class
     */
    public MetaData getMetaData() {
        return metadata;
    }

    /**
     * 
     * @return The board's current generation
     */
    public byte[][] getCurrentGeneration() {
    	byte[][] copy = new byte[currentGeneration.length]
                [currentGeneration[0].length];

    	for(int row = 0; row < currentGeneration.length; row++) {
            System.arraycopy(currentGeneration[row], 0, copy[row], 
                    0, currentGeneration[row].length);
    	}

    	return copy;
    }

    /**
     * Returns the <code>byte</code> value of the cell at the given position
     *
     * @param row
     * @param column
     * @return Returns the <code>byte</code> value of a cell on the
     * given position
     */
    public byte getCellAliveState(int row, int column) {
        return currentGeneration[row][column];
    }

    /**
     * Sets the <code>byte</code> value of the cell at the given position
     * to the <code>byte</code> value given in aliveState. Throws a Runtime
     * Exception if the number does not equal 0 or 1.
     *
     * @param row
     * @param column
     * @param aliveState
     */
    public void setCellAliveState(int row, int column, byte aliveState) {
        if(aliveState != 0 || aliveState != 1) {
            throw new RuntimeException("Invalid number in cell state");
        }
        currentGeneration[row][column] = aliveState;
    }
    
    /**
     * Counts the amount of neighbours for the cell at the given position
     * 
     * @param row 
     * @param col
     * @return the amount of neighbours around the cell
     */
    public int countNeighbours(int row, int col) {
        int neighbours = 0;
        
        if(col > 0) {
            if(currentGeneration[row][col - 1]==1)
                neighbours++;
            if( row > 0) {
                if(currentGeneration[row-1][col - 1]==1)
                    neighbours++;
            }
            if(row < currentGeneration.length - 1) {
                if(currentGeneration[row+1][col - 1]==1)
                    neighbours++;
            }
        }

        if(col < currentGeneration[0].length - 1) {
            if(currentGeneration[row][col + 1]==1)
                neighbours++;
            if(row > 0) {
                if(currentGeneration[row-1][col + 1]==1)
                    neighbours++;
            }
            if(row < currentGeneration.length - 1) {
                if(currentGeneration[row+1][col + 1]==1)
                    neighbours++;
            }
        }

        if(row > 0) {
            if(currentGeneration[row - 1][col]==1)
                neighbours++;
        }
        if( row < currentGeneration.length - 1) {
            if(currentGeneration[row + 1][col]==1)
                neighbours++;
        }
        
        return neighbours;
    }
    
    public void nextGeneration() {
        for(int row = 0; row < currentGeneration.length; row++) {
            for(int col = 0; col < currentGeneration[0].length; col++) {
                nextGeneration[row][col] = ((countNeighbours(row,col) == 3) || 
                        (currentGeneration[row][col] == 1 && 
                        countNeighbours(row,col) == 2 )) ? (byte)1 : (byte)0;
            }
        }
        nextGenerationToCurrent();
    }
    
    public void nextGeneration(int startRow, int endRow) {
        for(int row = 0; row < currentGeneration.length; row++) {
            for(int col = 0; col < currentGeneration[0].length; col++) {
                nextGeneration[row][col] = ((countNeighbours(row,col) == 3) || 
                        (currentGeneration[row][col] == 1 && 
                        countNeighbours(row,col) == 2 )) ? (byte)1 : (byte)0;
            }
        }
    }
    
    public void nextGenerationToCurrent() {
        for(int row = 0; row < currentGeneration.length; row++) {
            System.arraycopy(nextGeneration[row], 0, currentGeneration[row], 
                    0, currentGeneration[row].length);
    	}
    }
}