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
	
	public abstract Cell getCell(int row, int column);
	
	/**
	 *
	 */
	
	public abstract void setBoard(Cell[][] inputBoard);
	public abstract Cell[][] getBoard();
	/**
	 *
	 */
	@Override
	public String toString() {
		return "A";
	}
}
