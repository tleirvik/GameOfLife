package model.gameoflife.boards;

import model.gameoflife.MetaData;

/**
 * @author Robin Sean Aron David Lundh, Terje Leirvik, Stian Reistad Roegeberg.
 * 
 * This class contains the gameboard in a fixed size that cannot be changed
 * after creation. It also contains the algorithm for Game Of Life specifically
 * made for a board of a fixed size.
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
    	currentGeneration = new byte[rows][columns];
        firstGeneration = new byte[rows][columns];
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
    	currentGeneration = new byte[board.length][board[0].length];
        firstGeneration = new byte[board.length][board[0].length];

    	for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[0].length; col++) {
                currentGeneration[row][col] = board[row][col];
                firstGeneration[row][col] = board[row][col];
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
    @Override
    public int getRows() {
        return currentGeneration.length;
    }

    /**
     *
     * @return The number of columns
     */
    @Override
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
    @Override
    public MetaData getMetaData() {
        return metadata;
    }
    
    /**
     * Returns the <code>byte</code> value of the cell at the given position
     *
     * @param row The specified row position
     * @param column The specified column position
     * @return Returns the <code>byte</code> value of a cell on the
     * given position
     */
    @Override
    public byte getCellAliveState(int row, int column) {
        if (row > getRows()-1 || row < 0 || column > getColumns()-1 || column < 0) {
            System.out.println("outside array");
            return 0;
        } else {
            return currentGeneration[row][column];
        }
    }

    //=========================================================================
    // Setters
    //=========================================================================
    
    /**
     * Sets the <code>byte</code> value of the cell at the given position
     * to the <code>byte</code> value given in aliveState. Throws a Runtime
     * Exception if the number does not equal 0 or 1.
     *
     * @param row The specified row position
     * @param column The specified column position
     * @param aliveState sets the <code>byte</code> value of a cell on the
     * given position
     */
    @Override
    public void setCellAliveState(int row, int column, byte aliveState) {
        if (row > getRows()-1 || row < 0 || column > getColumns()-1 || column < 0) {
            return;
        } else {
            if (aliveState == 0 || aliveState == 1) {
                currentGeneration[row][column] = aliveState;
            } else {
                throw new RuntimeException("Invalid number in cell state: " + aliveState);
            }
        }

    }

    /**
     *  This method saves the board to an array within this class for later
     *  retrieval. Abstract method inherited from superclass Board
     */
    @Override
    public void setFirstGeneration() {
        for (int row = 0; row < currentGeneration.length; row++) {
            for (int col = 0; col < currentGeneration[0].length; col++) {
                firstGeneration[row][col] = currentGeneration[row][col];
            }
        }
    }

    //=========================================================================
    // Generation-methods
    //=========================================================================

    /**
     * Sets the first generation board. Used with {@link #resetBoard()}
     */
    @Override
    public void resetBoard() {
        for (int row = 0; row < currentGeneration.length; row++) {
            for (int col = 0; col < currentGeneration[0].length; col++) {
                currentGeneration[row][col] = firstGeneration[row][col];
            }
        }
    }

    //=========================================================================
    // Misc.
    //=========================================================================

    /**
     * This method is inherited from {@link Object} and is used to clone a fixed
     * game board with its meta data
     * @return A clone of this class' game board
     */
    @Override
    public FixedBoard clone() {
        byte[][] boardClone = java.util.Arrays
                .stream(currentGeneration)
                .map(el -> el.clone())
                .toArray($ -> currentGeneration.clone());
        MetaData metaDataClone = metadata.clone();
        
        return new FixedBoard(boardClone, metaDataClone);
    }
    /**
     * Method that gives the smallest array to fit a specified pattern
     * @return Returns an array of minrow maxrow mincolumn maxcolumn
     *
     * @author Henrik Lieng
     */
    @Override
    public int[] getBoundingBox() {
        int[] boundingBox = new int[4]; // minrow maxrow mincolumn maxcolumn
        boundingBox[0] = currentGeneration.length;
        boundingBox[1] = 0;
        boundingBox[2] = currentGeneration[0].length;
        boundingBox[3] = 0;
        
        for (int i = 0; i < currentGeneration.length; i++) {
            for (int j = 0; j < currentGeneration[0].length; j++) {
                if ((currentGeneration[i][j] == 0)) {
                    continue;
                }
                if (i < boundingBox[0]) {
                    boundingBox[0] = i;
                }
                if (i > boundingBox[1]) {
                    boundingBox[1] = i;
                }
                if (j < boundingBox[2]) {
                    boundingBox[2] = j;
                }
                if (j > boundingBox[3]) {
                    boundingBox[3] = j;
                }
            }
        }
        return boundingBox;
    }
    
    /**
     *  Method that returns the game board as a String. Used for Unit Testing with JUnit 4
     *
     * @return String The contents of the game board as a String
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (byte[] currentGeneration1 : currentGeneration) {
            for (int j = 0; j < currentGeneration[0].length; j++) {
                sb.append(currentGeneration1[j]);
            }
        }
        return sb.toString();
    }
}