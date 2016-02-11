package GameOfLife;

public class DynamicBoard extends Board{

	public DynamicBoard(int rows, int columns) {
		super(rows, columns);
		// TODO Auto-generated constructor stub
	}
	/**
	 *
	 * @param row
	 */
	public void increaseRow(int row) {

	}
	/**
	 *
	 * @param column
	 */
	public void increaseColumn(int column) {

	}
	/**
	 *
	 * @param row
	 */
	public void decreaseRow(int row) {

	}
	/**
	 *
	 * @param column
	 */
	public void decreaseColumn(int column) {

	}
	/**
	 *
	 * @return
	 */
	public boolean getIsDynamic() {
		return true;
	}
	/**
	 *
	 * @param dynamic
	 */
	public void setIsDynamic(boolean dynamic) {

	}
	/**
	 *
	 */
	public int getRow() {
		return 1;
	}
	/**
	 *
	 */
	public int getColumn() {
		return 1;
	}
	/**
	 *  Too be implemented
	 */
	@Override
	public Cell getCell(int row, int column) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * Too be implemented
	 */
	@Override
	public void setBoard(boolean[][] grid) {
		// TODO Auto-generated method stub

	}
}
