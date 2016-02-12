package GameOfLife;

import java.util.Random;

/**
 *
 * @author stianreistadrogeberg
 *
 * Implementer getter setter + variabel for om brettet er tomt (alle celler er false eller 0). 9/2/16
 */
public abstract class GameOfLife {

    private boolean isBoardEmpty; // er true hvis alle celler på brettet er døde.

    public abstract void setIsBoardEmpty(boolean boardEmpty);
    public abstract boolean getIsBoardEmpty();
	public abstract void populateRandomBoard();
	public abstract void nextGeneration();
	public abstract int countNeighbours(int row, int column);
	public abstract boolean[][] convertBoardToBoolean();


	/**
	 * The purpose of this method is to generate a pseudo-random boolean value.
	 *
	 * @param limit
	 * @return boolean
	 */
	public boolean seedGeneration(int limit) {

		Random random = new Random();

		if (random.nextInt(limit) != 1)
			return false;
		else
			return true;
	}
	public abstract void setNextGenerationStrategy(NextGenerationStrategy nextGenerationStrategy);
}
