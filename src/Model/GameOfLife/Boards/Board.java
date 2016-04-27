package Model.GameOfLife.Boards;

import Model.GameOfLife.MetaData;

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
    public abstract void setFirstGeneration();
        
    public abstract Board clone();
    public abstract String toString();
    
    public enum BoardType {
        FIXED,
        DYNAMIC,
        TORODIAL
    }
    public void beforeUpdate(){}
}
