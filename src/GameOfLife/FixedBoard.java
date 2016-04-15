package GameOfLife;

/**
 * @author Robin Sean Aron David Lundh, Terje Leirvik, Stian Reistad Røgeberg.
 * 
 * This class contains the gameboard in a fixed size that cannot be changed
 * after creation. It also contains the algorithm for Game Of Life specifically
 * made for a board of a fixed size.
 * 
 */
public class FixedBoard extends Board{
    private final MetaData metadata;
    private final byte[][] currentGeneration;
    private final byte[][] firstGeneration;
    
    //=========================================================================
    // Constructors
    //=========================================================================
    
    /**
     * Constructs a board of a fixed size.
     *
     * @param rows The number of rows
     * @param columns The number of columns
     */
    public FixedBoard(int rows, int columns) {
        metadata = new MetaData();
    	currentGeneration = new byte[rows + 2][columns + 2];
        firstGeneration = new byte[rows + 2][columns + 2];
    }

    /**
     * Constructs a board with the given two-dimensional <code>byte</code>-array and
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
    	this.metadata =  metadata;
    	currentGeneration = new byte[board.length + 2][board[0].length + 2];
        firstGeneration = new byte[board.length + 2][board[0].length + 2];

    	for(int row = 0; row < board.length; row++) {
            for(int col = 0; col < board[0].length; col++) {
                currentGeneration[row + 1][col + 1] = board[row][col];
                firstGeneration[row + 1][col + 1] = board[row][col];
            }
    	}
    }
    
    //=========================================================================
    // Getters
    //=========================================================================

    /**
     * 
     * @return The number of rows
     */
    public int getRows() {
        return currentGeneration.length - 2;
    }

    /**
     *
     * @return The number of columns
     */
    public int getColumns() {
        return currentGeneration[0].length - 2;
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
     * Returns the <code>byte</code> value of the cell at the given position
     *
     * @param row
     * @param column
     * @return Returns the <code>byte</code> value of a cell on the
     * given position
     */
    public byte getCellAliveState(int row, int column) {
        return currentGeneration[row + 1][column + 1];
    }
    
    //=========================================================================
    // Setters
    //=========================================================================
    
    /**
     * Sets the <code>byte</code> value of the cell at the given position
     * to the <code>byte</code> value given in aliveState. Throws a Runtime
     * Exception if the number does not equal 0 or 1.
     *
     * @param row
     * @param column
     * @param aliveState sets the <code>byte</code> value of a cell on the
     * given position
     */
    @Override
    public void setCellAliveState(int row, int column, byte aliveState) {
        if(aliveState == 0 || aliveState == 1) {
            currentGeneration[row + 1][column + 1] = aliveState;
        } else {
            throw new RuntimeException("Invalid number in cell state: " + aliveState);
        }
    }
    
    @Override
    public void setFirstGeneration() {
        for (int row = 1; row < currentGeneration.length-1; row++) {
            for (int col = 1; col < currentGeneration[0].length-1; col++) {
                firstGeneration[row][col] = currentGeneration[row][col];
            }
        }
    }
    
    //=========================================================================
    // Generation-methods
    //=========================================================================
    
    public void resetBoard() {
        for(int row = 1; row < currentGeneration.length-1; row++) {
            for(int col = 1; col < currentGeneration[0].length-1; col++) {
                currentGeneration[row][col] = firstGeneration[row][col];
            }
        }
    }
    
    @Override
    public byte[][] countNeighbours() {
        byte[][] neighbourArray = new byte[currentGeneration.length][currentGeneration[0].length];
        
        for(int row = 1; row < currentGeneration.length-1; row++) {
            for(int col = 1; col < currentGeneration[0].length-1; col++) {
                if(currentGeneration[row][col] == 1) {
                    neighbourArray[row-1][col-1]++;
                    neighbourArray[row-1][col]++;
                    neighbourArray[row-1][col+1]++; 
                    neighbourArray[row][col-1]++;
                    neighbourArray[row][col+1]++;
                    neighbourArray[row+1][col-1]++;
                    neighbourArray[row+1][col]++;
                    neighbourArray[row+1][col+1]++;
                }
            }
        }
        return neighbourArray;
    }
    
    @Override
    public void nextGeneration() {
        byte[][] neighbourArray = countNeighbours();
        
        for(int row = 1; row < currentGeneration.length-1; row++) {
            for(int col = 1; col < currentGeneration[0].length-1; col++) {
                currentGeneration[row][col] = ((neighbourArray[row][col]== 3) || (currentGeneration[row][col] == 1 && neighbourArray[row][col] == 2 )) ? (byte)1 : (byte)0;
            }
        }
    }
    public void setIsDynamic(boolean isDynamic) {

    }

    //=========================================================================
    // Misc.
    //=========================================================================
    
    @Override
    public FixedBoard clone() {
        byte[][] boardClone = java.util.Arrays.stream(currentGeneration).map(el -> el.clone()).toArray($ -> currentGeneration.clone());
        MetaData metaDataClone = metadata.clone();
        return new FixedBoard(boardClone, metaDataClone);
    }
    
    /**
     *  Method that returns the game board as a String. Used for Unit Testing with JUnit 4
     *
     * @return String The contents of the game board as a String
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < currentGeneration.length; i++) {
            for (int j = 0; j < currentGeneration[0].length; j++) {
                sb.append(currentGeneration[i][j]);
            }
        }
        return sb.toString();
    }
}