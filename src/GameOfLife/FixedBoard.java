/**
 *
 */
package GameOfLife;


/**
 * Concrete implementation of the abstract Board class.
 * The purpose of this class is to offer an array which the cells reside in
 *
 */
public class FixedBoard {
    private MetaData metadata;
    private byte[][] currentCells;

    /**
     *
     * @param rows
     * @param columns
     */
    public FixedBoard(int rows, int columns) {
        currentCells = new byte[rows][columns];

        for (byte[] currentCell : currentCells) {
            for (int col = 0; col < currentCell.length; col++) {
                currentCell[col] = 0;
            }
        }
    }

    /**
     *
     * @param board
     */
    public FixedBoard(boolean[][] board) {
        currentCells = new byte[board.length][board[0].length];

        for(int row = 0; row < board.length; row++) {
            for(int col = 0; col < board[row].length; col++) {
                if (board[row][col]) {
                    currentCells[row][col] = 1;
                } else {
                    currentCells[row][col] = 0;
                }
            }
    	}
    }
    
    
    /**
     *
     * @return
     */
    public int getRows() {
        return currentCells.length;
    }


    /**
     *
     * @return
     */
    public int getColumns() {
        return currentCells[0].length;
    }


    /**
     *  This method returns meta data from the
     *  board.
     *
     * @return MetaData Meta data from the board contained in
     * the class
     */
    public MetaData getMetaData() {
        return metadata;
    }

    /**
     * This method sets the meta data of the board
     * @param m
     */
    public void setMetaData(MetaData m) {
            metadata = m;
    }

    


    /**
     *
     * @return 
     */
    public byte[][] getCellArray() {
    	byte[][] copy = new byte[currentCells.length][currentCells[0].length];

    	for(int row = 0; row < currentCells.length; row++) {
            System.arraycopy(currentCells[row], 0, copy[row], 0, currentCells[row].length);
    	}
        
    	return copy;
    }


    /**
	 * The purpose of this method is to set the setIsAalive value of the entire grid
	 *
     * @param inputBoard
	 *
	 */
    //Tidligere setBoard-metoden
    public void setCellArray(boolean[][] inputBoard) {
    	System.out.println("Setter brettet");
    	for(int row = 0; row < inputBoard.length; row++) {
            for(int col = 0; col < inputBoard[row].length; col++) {
                if(inputBoard[row][col]) {
                    currentCells[row][col] = 1;
                } else {
                    currentCells[row][col] = 0;
                }
            }
    	}
    }

    /**
     * Returnerer cellen på gitt posisjon
     *
     * @param row
     * @param column
     * @return boolean Returns the boolean value of a cell on the
     * given position
     */
    public boolean getCellAliveState(int row, int column) {
    	byte cell = currentCells[row][column];

        return cell == 1;
    }

    /**
     *
     * @param row
     * @param column
     * @param isAlive
     */
    public void setCellAliveState(int row, int column, boolean isAlive) {
    	if (isAlive) {
            currentCells[row][column] = 1;
        } else {
            currentCells[row][column] = 0;
        }
    }
}
