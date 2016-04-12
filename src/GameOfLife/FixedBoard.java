package GameOfLife;

/**
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
    private final byte[][] firstGeneration;
    
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
        firstGeneration = new byte[board.length][board[0].length];

    	for (int row = 1; row < board.length - 1; row++) {
            for (int col = 1; col < board[0].length - 1; col++) {
                currentGeneration[row][col] = board[row][col];
                firstGeneration[row][col] = board[row][col];
            }
    	}
    }

    /**
     * This method return the number of rows in the game board
     *
     * @return The number of rows
     */
    public int getRows() {
        return currentGeneration.length;
    }

    /**
     * This method return the number of columns in the game board
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
     * @see MetaData
     */
    public MetaData getMetaData() {
        return metadata;
    }

    /**
     * This method returns a reference to the current game board
     *
     * @return The board's current generation
     */
    public byte[][] getBoardReference() {
    	return currentGeneration;
    }

    /**
     * This method resets the board to the origin board
     */
    public void resetBoard() {
        for (int row = 1; row < firstGeneration.length - 1; row++) {
            System.arraycopy(firstGeneration[row], 1, 
                    currentGeneration[row], 1, firstGeneration[0].length - 1 - 1);
    	}
    }

    /**
     * Returns the <code>byte</code> value of the cell at the given position
     *
     * @param row The x position on the board
     * @param column The y position on the board
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
     * @param row The x position on the board
     * @param column The y position on the board
     * @param aliveState sets the <code>byte</code> value of a cell on the
     * given position
     */
    public void setCellAliveState(int row, int column, byte aliveState) {
        if (aliveState == 0 || aliveState == 1) {
            currentGeneration[row][column] = aliveState;
        } else {
            throw new RuntimeException("Invalid number in cell state: " + aliveState);
        }
    }

    /**
     * This is the game algorithm that calculates a if a cell should be alive or dead in the next generation
     *
     *
     * @version 3.0 Decreased the number of if's and increased the performance using a neighbourhood array
     */
    public void nextGeneration() {
        byte[][] neighbourArray = new byte[currentGeneration.length][currentGeneration[0].length];
        
        for (int row = 1; row < currentGeneration.length - 1; row++) {
            for (int col = 1; col < currentGeneration[0].length - 1; col++) {
                if (currentGeneration[row][col] == 1) {
                    neighbourArray[row - 1][col - 1]++;
                    neighbourArray[row - 1][col]++;
                    neighbourArray[row - 1][col + 1]++; 
                    neighbourArray[row][col - 1]++;
                    neighbourArray[row][col + 1]++;
                    neighbourArray[row + 1][col - 1]++;
                    neighbourArray[row + 1][col]++;
                    neighbourArray[row + 1][col + 1]++;
                }
            }
        }
        
        for (int row = 1; row < currentGeneration.length-1; row++) {
            for (int col = 1; col < currentGeneration[0].length-1; col++) {
                currentGeneration[row][col] = ((neighbourArray[row][col]== 3) ||
                        (currentGeneration[row][col] == 1 && 
                        neighbourArray[row][col] == 2 )) ? (byte)1 : (byte)0;
            }
        }
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