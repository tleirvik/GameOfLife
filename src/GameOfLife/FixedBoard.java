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

	private Cell[][] cell;

    public FixedBoard(int rows, int columns) {
        super(rows, columns);
        cell = new Cell[rows][columns];

        for(int row = 0; row < cell.length; row++) {
    		for(int col = 0; col < cell[row].length; col++) {
    			cell[row][col] = new Cell();
    		}
    	}
    }

    /**
     * @return the rows
     */
    public int getRow() {
            return cell.length;
    }

    /**
     * @return the columns
     */
    @Override
    public int getColumn() {
            return cell[0].length;
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
    	return cell[row][column];
    }
	/**
	 * The purpose of this method is to set the setIsAalive value of the entire grid
	 *
	 * @param boolean[][] grid
	 *
	 */
    //Tidligere setBoard-metoden
    public void setBoard(boolean[][] grid) {

    	for(int row = 0; row < grid.length; row++) {
    		for(int col = 0; col < grid[row].length; col++) {
    			System.out.println(cell[row][col]);
    			System.out.println(grid[row][col]);
                cell[row][col].setIsAlive( grid[row][col] );
    		}
    	}
    }
    /**
     *  This' class toString()-method
     *  @return String The size of the board(x,y aka rows and columns)
     */
    @Override
    public String toString() {
            return "A";
    }
}
