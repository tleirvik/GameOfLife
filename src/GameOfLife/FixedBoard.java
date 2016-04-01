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
    private final byte[][] buffer1;
    private final byte[][] buffer2;
    private final byte[][] firstGeneration;
    private boolean bufferSwap = false;
    
    /**
     * Constructs a board of a fixed size.
     *
     * @param rows The number of rows
     * @param columns The number of columns
     */
    public FixedBoard(int rows, int columns) {
        metadata = new MetaData();
    	buffer1 = new byte[rows+2][columns+2];
        buffer2 = new byte[rows+2][columns+2];
        firstGeneration = new byte[rows+2][columns+2];
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
    	buffer1 = new byte[board.length+2][board[0].length+2];
        buffer2 = new byte[board.length+2][board[0].length+2];
        firstGeneration = new byte[board.length+2][board[0].length+2];

    	for(int row = 1; row < board.length-1; row++) {
            for(int col = 1; col < board[0].length-1; col++) {
                buffer1[row][col] = board[row][col];
                firstGeneration[row][col] = board[row][col];
            }
    	}
    }

    /**
     * 
     * @return The number of rows
     */
    public int getRows() {
        return buffer1.length;
    }

    /**
     *
     * @return The number of columns
     */
    public int getColumns() {
        return buffer1[0].length;
    }
    
    /**
     * Swaps the current buffer that the nextGeneration()-method writes to.
     */
    public void swapBuffer() {
        bufferSwap = !bufferSwap;
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
    public byte[][] getBoardReference() {
    	if(bufferSwap) {
            return buffer2;
        } else {
            return buffer1;
        }
    }
    
    public void resetBoard() {
        for(int row = 1; row < firstGeneration.length-1; row++) {
            for(int col = 1; col < firstGeneration[0].length-1; col++) {
                buffer1[row][col] = firstGeneration[row][col];
            }
    	}
        bufferSwap = false;
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
        if(bufferSwap) {
            return buffer1[row][column];
        } else {
            return buffer2[row][column];
        }
    }

    /**
     * Sets the <code>byte</code> value of the cell at the given position
     * to the <code>byte</code> value given in aliveState. Throws a Runtime
     * Exception if the number does not equal 0 or 1.
     *
     * @param row
     * @param column
     * @param isAlive sets the <code>byte</code> value of a cell on the
     * given position
     */
    public void setCellAliveState(int row, int column, byte aliveState) {
        if(aliveState != 0 || aliveState != 1) {
            throw new RuntimeException("Invalid number in cell state");
        } else if(bufferSwap) {
            buffer2[row][column] = aliveState;
        } else {
            buffer2[row][column] = aliveState;
        }
        
    }
    
    /**
     * Counts the amount of neighbours for the cell at the given position
     * 
     * @param row 
     * @param col
     * @return the amount of neighbours around the cell
     */
    public int countNeighbours(int row, int col, byte[][] currentBuffer) {        
        return currentBuffer[row-1][col-1] + 
                currentBuffer[row-1][col] + 
                currentBuffer[row-1][col+1] + 
                currentBuffer[row][col-1] + 
                currentBuffer[row][col+1] + 
                currentBuffer[row+1][col-1] + 
                currentBuffer[row+1][col] + 
                currentBuffer[row+1][col+1];
    }
    
    public void nextGeneration() {
        byte[][] currentBuffer;
        byte[][] nextBuffer;
        if(bufferSwap) {
            currentBuffer = buffer2;
            nextBuffer = buffer1;
        } else {
            currentBuffer = buffer1;
            nextBuffer = buffer2;
        }
        for(int row = 1; row < currentBuffer.length-1; row++) {
            for(int col = 1; col < currentBuffer[0].length-1; col++) {
                nextBuffer[row][col] = ((countNeighbours(row,col,currentBuffer) == 3) || 
                        (currentBuffer[row][col] == 1 && 
                        countNeighbours(row,col,currentBuffer) == 2 )) ? (byte)1 : (byte)0;
            }
        }
        swapBuffer();
    }
    
    public void nextGeneration(int startRow, int endRow) {
        byte[][] currentBuffer;
        byte[][] nextBuffer;
        if(bufferSwap) {
            currentBuffer = buffer2;
            nextBuffer = buffer1;
        } else {
            currentBuffer = buffer1;
            nextBuffer = buffer2;
        }
        for(int row = startRow; row < endRow; row++) {
            for(int col = 0; col < currentBuffer[0].length; col++) {
                nextBuffer[row][col] = ((countNeighbours(row,col,currentBuffer) == 3) || 
                        (currentBuffer[row][col] == 1 && 
                        countNeighbours(row,col,currentBuffer) == 2 )) ? (byte)1 : (byte)0;
            }
        }
    }
}