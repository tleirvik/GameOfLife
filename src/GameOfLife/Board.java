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

	public int getRows() {
		return rows;
	}

	public int getColumns() {
		return columns;
	}
	
	public abstract Cell[][] getCells();
	public abstract void setCells(Cell[][] inputBoard);
	public abstract Cell getCell(int row, int column);

}
