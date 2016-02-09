/**
 *
 */
package GameOfLife;


/**
 * @author
 *
 */
public class FixedBoard extends Board{
    private Cell[][] cell;
    
    public FixedBoard(int rows, int columns) {
            super(rows, columns);

    }

    /**
     * @return the rows
     */
    public int getRow() {
            return 0;
    }

    /**
     * @return the columns
     */
    @Override
    public int getColumn() {
            return 0;
    }
    
    @Override
    public Board getBoard() {
            return null;

    }
    
    @Override
    public void setBoard() {

    }
    
    @Override
    public String toString() {
            return "A";
    }
}
