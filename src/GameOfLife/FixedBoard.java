/**
 *
 */
package GameOfLife;


/**
 * Concrete implementation of the abstract Board class.
 * The purpose of this class is to offer an array which the cells reside in
 *
 */
public class FixedBoard extends Board{

	private byte[][] currentCells;
	// private boolean isBoardEmpty; // er true hvis alle celler på brettet er døde.



	/**
	 *
	 * @param rows
	 * @param columns
	 */
    public FixedBoard(int rows, int columns) {
        super(rows, columns);

        currentCells = new byte[rows][columns];

        for(int row = 0; row < currentCells.length; row++) {
    		for(int col = 0; col < currentCells[row].length; col++) {
    			currentCells[row][col] = 0;
    		}
    	}
    }

    /**
     *
     * @param board
     */
    public FixedBoard(byte[][] board) {
        super(board.length, board[0].length);

        currentCells = new byte[board.length][board[0].length];

        for(int row = 0; row < board.length; row++) {
    		for(int col = 0; col < board[row].length; col++) {
    			currentCells[row][col] = board[row][col];
    		}
    	}
    }

    /**
     * @return the rows
     */
    @Override
    public int getRows() {
            return currentCells.length;
    }

    /**
     * @return the columns
     */
    @Override
    public int getColumns() {
            return currentCells[0].length;
    }


    /**
     *
     */
    public byte[][] getCellArray() {

    	byte[][] copy = new byte[currentCells.length][currentCells[0].length];

    	for(int row = 0; row < currentCells.length; row++) {
    		for(int col = 0; col < currentCells[row].length; col++) {

                copy[row][col] = currentCells[row][col];

    		}
    	}
    	return copy;
    }


    /**
	 * The purpose of this method is to set the setIsAalive value of the entire grid
	 *
	 * @param boolean[][] grid
	 *
	 */
    //Tidligere setBoard-metoden
    @Override
    public void setCellArray(boolean[][] inputBoard) {
    	System.out.println("Setter brettet");
    	for(int row = 0; row < inputBoard.length; row++) {
    		for(int col = 0; col < inputBoard[row].length; col++) {

    			if(inputBoard[row][col])
    				currentCells[row][col] = 1;
    			else
    				currentCells[row][col] = 0;
    		}
    	}
    }

    /**
     * Returnerer cellen på gitt posisjon
     *
     * @param row
     * @param column
     * @return Cell-object
     */
    @Override
    public boolean getCellAliveState(int row, int column) {
    	byte cell = currentCells[row][column];

    	if (cell == 1)
    		return true;
    	else
    		return false;
    }




    /**
     *
     * @param row
     * @param column
     * @param b
     */
    @Override
    public void setCellAliveState(int row, int column, boolean isAlive) {

    	if (isAlive)
    		currentCells[row][column] = 1;
    	else
    		currentCells[row][column] = 0;
    }


    /**
     *  This class' toString()-method
     *  @return String The size of the board(x,y aka rows and columns)
     */
    @Override
    public String toString() {
            return "FixedBoard";
    }
}
