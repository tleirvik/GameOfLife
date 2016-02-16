package GameOfLife;

/**
 * Abstract Board class
 *
 *
 */
public abstract class Board {

	private int rows;
	private int columns;


	/**
	 *  Constructor; sets rows & columns
	 * @param rows
	 * @param columns
	 */
	public Board(int rows, int columns) {

		this.rows = rows;
		this.columns = columns;
	}


	/**
	 *
	 * @return
	 */
	public int getRows() {
		return rows;
	}


	/**
	 *
	 * @return
	 */
	public int getColumns() {
		return columns;
	}


	/**
	 *
	 * @return
	 */
	public abstract byte[][] getCellArray();


	/**
	 *
	 * @param inputBoard
	 */
	public abstract void setCellArray(boolean[][] inputBoard);


	/**
	 *
	 * @param row
	 * @param column
	 * @return cell Alive state
	 */
	public abstract boolean getCellAliveState(int row, int column);


	/**
	 *
	 * @param row
	 * @param column
	 * @param isAlive
	 * @return
	 */
	public abstract void setCellAliveState(int row, int column, boolean isAlive);

}
