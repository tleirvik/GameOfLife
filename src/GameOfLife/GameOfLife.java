package GameOfLife;

import java.util.Random;

import com.sun.prism.paint.Color;

/**
 *
 * @author stianreistadrogeberg
 *
 * Implementer getter setter + variabel for om brettet er tomt (alle celler er false eller 0). 9/2/16
 */
public abstract class GameOfLife {

	/**
	 * The purpose of this method is to generate a pseudo-random boolean value.
	 *
	 * @param limit
	 * @return boolean
	 */
	public boolean seedGenerator(int limit) {
		Random random = new Random();

		if (random.nextInt(limit) != 1)
			return false;
		else
			return true;
	}


	/**
	 *
	 * @param row
	 * @param column
	 * @return
	 */
	public abstract boolean getCellAliveStatus(int row, int column);


	/**
	 *
	 * @param row
	 * @param column
	 * @param isAlive
	 */
	public abstract void setCellAliveStatus(int row, int column, boolean isAlive);


	/**
	 *
	 * @param boardEmpty
	 */
	public abstract void setIsBoardEmpty(boolean boardEmpty);


	/**
	 *
	 * @return
	 */
    public abstract boolean getIsBoardEmpty();


    /**
     *
     */
	public abstract void populateRandomBoard();


	/**
	 *
	 */
	public abstract void nextGeneration();


	/**
	 *
	 * @param row
	 * @param column
	 * @return
	 */
	public abstract boolean countNeighbours(int row, int column);

	public abstract boolean[][] convertBoardToBoolean();

}
