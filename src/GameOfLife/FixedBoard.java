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

	private Cell[][] cells;

    public FixedBoard(int rows, int columns) {
        super(rows, columns);
        cells = new Cell[rows][columns];

        for(int row = 0; row < cells.length; row++) {
    		for(int col = 0; col < cells[row].length; col++) {
    			cells[row][col] = new Cell();
    		}
    	}
    }

    /**
     * @return the rows
     */
    @Override
    public int getRows() {
            return cells.length;
    }

    /**
     * @return the columns
     */
    @Override
    public int getColumns() {
            return cells[0].length;
    }

    public Cell[][] getCells() {
    	
    	Cell[][] copy = new Cell[cells.length][cells[0].length];
    	
    	for(int row = 0; row < cells.length; row++) {
    		for(int col = 0; col < cells[row].length; col++) {
    			
                copy[row][col] = copy[row][col];
                
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
    public void setCells(Cell[][] inputBoard) {
    	for(int row = 0; row < inputBoard.length; row++) {
    		for(int col = 0; col < inputBoard[row].length; col++) {
                cells[row][col].setIsAlive( inputBoard[row][col].getIsAlive() );
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
    public Cell getCell(int row, int column) {
    	return cells[row][column];
    }
    
    
    /**
     *  This class toString()-method
     *  @return String The size of the board(x,y aka rows and columns)
     */
    @Override
    public String toString() {
            return "A";
    }
}
