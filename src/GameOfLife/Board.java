package GameOfLife;

/**
<<<<<<< HEAD
 * This is an abstract class that represents the game board in Game Of Life
 *
 * @see FixedBoard
 * @see DynamicBoard
 */

public abstract class Board {
    public abstract int getRows();
    public abstract int getColumns();
    public abstract MetaData getMetaData();

    public abstract byte getCellAliveState(int row, int column);
    public abstract void setCellAliveState(int row, int column, byte aliveState);
    
    public abstract void resetBoard();
    
    public abstract byte[][] countNeighbours();
    public abstract void nextGeneration();
    //public void concurrentNextGeneration();
    //Annenhver rad/kolonne
    public abstract void setIsDynamic(boolean isDynamic);
    
    public abstract Board clone();
    public abstract String toString();

    public abstract void setFirstGeneration();
}
