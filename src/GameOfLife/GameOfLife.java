package GameOfLife;

import java.util.Random;

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
    public abstract boolean getCellAliveState(int row, int column);


    /**
     *
     * @param row
     * @param column
     * @param isAlive
     */
    public abstract void setCellAliveState(int row, int column, boolean isAlive);


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
    public abstract int countNeighbours(int row, int column);
}
