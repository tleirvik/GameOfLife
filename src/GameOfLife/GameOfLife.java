package GameOfLife;

import java.util.Random;

/**
 * This is a controller class for the game and communication between the model and the view goes
 * through an object of this class
 */
public class GameOfLife {
    private FixedBoard board; 

    //Burde ikke lage brett i konstruktør, burde heller
    //Være en egen funksjon så man slipper å slette og lage ny GameOfLife.
    //Singleton?

    /**
     * A constructor that creates a new Game Of Life game with a specified number of rows and columns
     *
     * @param isDynamic Not in use, to be implemented
     * @param rows The number of rows for the game board
     * @param columns The number of columns for the game board
     */
    public GameOfLife(boolean isDynamic, int rows, int columns) {        
        board = new FixedBoard(rows, columns);
    }

    //Burde ikke lage brett i konstruktør, burde heller
    //Være en egen funksjon så man slipper å slette og lage ny GameOfLife.
    //Singleton?
    /**
     * A constructor that creates a new Game Of Life game with a user specified board
     *
     * @param board The game board the user wants to play
     * @param metadata The associated meta data to the board
     */
    public GameOfLife(byte[][] board, MetaData metadata) {
        this.board = new FixedBoard(board, metadata);
    }

    /**
     * This method generates a board with random number of dead and alive cells
     */
    public void populateRandomBoard() {
        Random random = new Random();
        for(int row = 0; row < board.getRows(); row++) {
            for(int col = 0; col < board.getColumns(); col++) {
                board.setCellAliveState(row,col,((random.nextInt(5) == 1) ? (byte)1 : (byte)0));
            }
        }
    }

    /**
     * This method returns the game board
     *
     * @return Returns the game board
     */
    public FixedBoard getBoard() {
    	return board;
        
    }

    /**
     * This method returns a reference to the game board
     *
     * @return Returns a reference to the game board
     */
    public byte[][] getBoardReference() {
    	return board.getBoardReference();
    }

    /**
     * This method calls the nextGeneration()-method in FixedBoard
     *
     * @see FixedBoard
     */
    public void update() {
        board.nextGeneration();
    }

    /**
     * This method returns the boards meta data
     *
     * @return Returns the boards meta data
     * @see MetaData
     */
    public MetaData getMetaData() {
        return board.getMetaData();
    }

    /**
     * This method resets the game to the origin board by calling resetBoard() in FixedBoard
     *
     * @see FixedBoard
     */
    public void resetGame() {
        board.resetBoard();
    }

    /**
     * Returns the cell value of the specified cell
     *
     * @param row the row value
     * @param column the column value
     * @return the status of the specified cell
     */
    public byte getCellAliveState(int row, int column) {
        return board.getCellAliveState(row, column);
    }

    /**
     * Sets the cell value of the specified cell
     *
     * @param row the row value
     * @param column the column value
     * @param isAlive the status of the specified cell
     */
    public void setCellAliveState(int row, int column, byte isAlive) {
        board.setCellAliveState(row, column, isAlive);
    }

    /**
     * This method returns the number of rows in the game board
     *
     * @return The number of rows in the game board
     */
    public int getRows() {
        return board.getRows();
    }

    /**
     * This method returns the number of columns in the game board
     *
     * @return The number of columns in the game board
     */
    public int getColumns() {
        return board.getColumns();
    }

}
