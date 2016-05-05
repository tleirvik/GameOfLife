package model.gameoflife.boards;

import model.gameoflife.MetaData;

/**
 * This is an abstract class that represents the game board in Game Of Life
 *
 * @see FixedBoard
 * @see DynamicBoard
 */

public abstract class Board {
    /**
     * Returns the boards rows
     *
     * @return Number of rows
     */
    public abstract int getRows();

    /**
     * Returns the boards columns
     *
     * @return Number of columns
     */
    public abstract int getColumns();

    /**
     * Returns the meta data object
     *
     * @return The meta data object
     * @see MetaData
     */
    public abstract MetaData getMetaData();

    /**
     * Returns the <code>byte</code> value of the cell at the given position
     *
     * @param row The specified row
     * @param column The specified column
     * @return Returns the <code>byte</code> value of a cell on the
     * given position
     *
     */
    public abstract byte getCellAliveState(int row, int column);

    /**
     *  Sets the <code>byte</code> value of the cell at the given position
     * to the <code>byte</code> value given in aliveState.
     *
     * @param row The specified row position
     * @param column The specified column position
     * @param aliveState <code>0</code> or <code>1</code> to represent dead or alive cell
     */
    public abstract void setCellAliveState(int row, int column, byte aliveState);

    /**
     * Sets the first generation board. Used with {@link #resetBoard()}
     */
    public abstract void resetBoard();
    /**
     * Sets the first generation board. Used with {@link #resetBoard()}
     */
    public abstract void setFirstGeneration();

    /**
     * This method is overrided from {@link Object} and is used to clone a dynamic
     * game board with its meta data
     * @return A clone of this class' game board
     */
    @Override
    public abstract Board clone();

    /**
     * Method that gives the smallest array to fit a specified pattern
     * @return Returns an array of minrow maxrow mincolumn maxcolumn
     *
     * @author Henrik Lieng
     */
    public abstract int[] getBoundingBox();

    /**
     * Enumerations of the board types available
     */
    public enum BoardType {
        FIXED,
        DYNAMIC
    }

    /**
     * This method is inherited from {@link Board} and is used the check if we
     * need to expand the edges before running the algorithm
     */
    public void beforeUpdate(){}
}
