/**
 * 
 */
package game;

/**
 * @author 
 *
 */
public abstract class Board {

	private int columns;
	private int rows;
	
	public Cell[][] boardGrid = null;
	
	public Board(int columns, int rows) {
		this.columns = columns;
		this.rows = rows;
		
		boardGrid = new Cell[this.rows][this.columns];
	}
	
	
	public abstract void populateBoard();
	public abstract int nearestNeighbour(Cell celle);
	public abstract void nextGeneration();
	
}
