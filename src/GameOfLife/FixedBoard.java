/**
 *
 */
package GameOfLife;


/**
 * @author
 *
 */
public class FixedBoard extends Board{

	public FixedBoard(int rows, int columns) {
		super(rows, columns);

	}
	private Cell[][] cell;

	/**
	 * @return the rows
	 */
	public int getRow() {
		return 0;
	}

	/**
	 * @return the columns
	 */
	public int getColumn() {
		return 0;
	}
	public Board getBoard() {
		return null;

	}
	public void setBoard() {

	}
	@Override
	public String toString() {
		return "A";
	}
}
