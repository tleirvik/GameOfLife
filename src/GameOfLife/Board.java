package GameOfLife;

/**
 *
 *
 *
 */
public abstract class Board {

	private int rows;
	private int columns;

	public Board(int rows, int columns) {

		this.rows = rows;
		this.columns = columns;
	}

	/**
	 * @return the rows
	 */
	public int getRow() {
		return rows;
	}

	/**
	 * @return the columns
	 */
	public int getColumn() {
		return columns;
	}
	/**
	 *
	 *
	 */
	public Board getBoard() {
		return null;

	}
	/**
	 *
	 */
	public void setBoard() {

	}
	/**
	 *
	 */
	@Override
	public String toString() {
		return "A";
	}
}
