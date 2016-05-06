package model.gameoflife;


import javafx.scene.control.Alert;
import model.util.DialogBoxes;
import java.util.Random;
import model.gameoflife.algorithms.Algorithm;
import model.gameoflife.algorithms.Default;
import model.gameoflife.boards.Board;
import model.gameoflife.boards.Board.BoardType;
import model.gameoflife.boards.DynamicBoard;
import model.gameoflife.boards.FixedBoard;

/**
 * This is the Game Of Life class
 */
public class GameOfLife {
    private Board board;
    private Algorithm algorithm;
    private final int NUM_THREADS = Runtime.getRuntime().availableProcessors();
    private NextGenerationWorkers nextGenerationWorkers;

    /**
     * Creates a new game of a specified size and the default algorithm
     *
     * @param rows The number of rows
     * @param columns The number of columns
     * @param type The board type(Fixed or Dynamic)
     */
    private void newGame(int rows, int columns, BoardType type) {
        switch(type) {
            case FIXED: 
                board = new FixedBoard(rows, columns);
                break;
                
            case DYNAMIC:
                board = new DynamicBoard(rows, columns);
                break;
        }
        algorithm = new Default(this.board);
        nextGenerationWorkers = new NextGenerationWorkers(NUM_THREADS, board, algorithm);

    }

    /**
     * Creates a new empty game of a specified size and the default algorithm
     *
     * @param rows The number of rows
     * @param columns The number of columns
     * @param type The board type(Fixed or Dynamic)
     */
    public void newEmptyGame(int rows, int columns, BoardType type) {
        newGame(rows, columns, type);
    }
    /**
     * Creates a new random game of a specified size and the default algorithm
     *
     * @param rows The number of rows
     * @param columns The number of columns
     * @param type The board type(Fixed or Dynamic)
     */
    public void newRandomGame(int rows, int columns, BoardType type) {
        newGame(rows, columns, type);
        Random random = new Random();
        for(int row = 0; row < board.getRows(); row++) {
            for(int col = 0; col < board.getColumns(); col++) {
                board.setCellAliveState(row,col,
                        ((random.nextInt(5) == 1) ? (byte)1 : (byte)0));
            }
        }
        board.setFirstGeneration();
    }

    /**
     * Loads a new game from a <code>byte</code> board
     * @param board The <code>byte</code> board to load
     * @param metadata The boards associated {@link MetaData} object
     * @param type The type of {@link BoardType}
     */
    public void loadGame(byte[][] board, MetaData metadata, BoardType type) {
        switch(type) {
            case FIXED: 
                this.board = new FixedBoard(board, metadata);
                break;
                
            case DYNAMIC:
                this.board = new DynamicBoard(board, metadata);
                break;
        }
        algorithm = new Default(this.board);
        nextGenerationWorkers = new NextGenerationWorkers(NUM_THREADS, this.board, algorithm);
    }

    /**
     * Saves the board for later retriveal
     */
    public void setFirstGeneration() {
        board.setFirstGeneration();
    }

    /**
     * Returns the game board
     * @return The game board
     */
    public Board getBoard() {
    	return board;
    }

    /**
     * Returns the associated meta data
     * @return The {@link MetaData} object
     */
    public MetaData getMetaData() {
        return board.getMetaData();
    }

    /**
     * Returns the number of rows
     * @return The number of rows
     */
    public int getRows() {
        return board.getRows();
    }

    /**
     * Returns the numver of columns
     * @return The number of columns
     */
    public int getColumns() {
        return board.getColumns();
    }

    /**
     * Returns the <code>byte</code> value of the cell at the given position
     *
     * @param row The specified row
     * @param column The specified column
     * @return Returns the <code>byte</code> value of a cell on the
     * given position
     *
     * @see Board
     *
     */
    public byte getCellAliveState(int row, int column) {
        return board.getCellAliveState(row, column);
    }

    /**
     *  Sets the <code>byte</code> value of the cell at the given position
     * to the <code>byte</code> value given in aliveState.
     *
     * @param row The specified row position
     * @param column The specified column position
     * @param isAlive <code>0</code> or <code>1</code> to represent dead or alive cell
     * @see Board
     */
    public void setCellAliveState(int row, int column, byte isAlive) {
        board.setCellAliveState(row, column, isAlive);
    }

    /**
     * Sets this' class game board
     * @param board The board to set
     */
    public void setBoard(Board board) {
        this.board = board;
        algorithm = new Default(board);
        nextGenerationWorkers = new NextGenerationWorkers(NUM_THREADS, this.board, algorithm);
    }

    /**
     * Reset the game board to the first generation. See {@link Board#setFirstGeneration()}
     *
     */
    public void resetGame() {
        board.resetBoard();
    }

    /**
     * This method is run before the algorithm to check whether we need to expand or shrink
     * the game board
     */
    public void update() {
        algorithm.beforeUpdate();
        algorithm.update();
    }

    /**
     * This is the threaded method that is run before the algorithm to check whether we need to expand or shrink
     * the game board
     */
    public void updateWithThreads() {
        algorithm.beforeUpdate();
        nextGenerationWorkers.splitBoard();
        nextGenerationWorkers.createWorkers();
        try {
            nextGenerationWorkers.runWorkers();
        } catch (InterruptedException iE) {
            DialogBoxes.openAlertDialog(Alert.AlertType.ERROR, "Error"
                    , "InterruptedException Error", iE.getStackTrace().toString());
        }
    }

    /**
     * This method sets the algorithm to use
     * @param a The {@link Algorithm}
     * @see Algorithm
     */
    public void setAlgorithm(Algorithm a) {
        algorithm = a;
    }

    /**
     * This method is inherited from {@link Object} and is used to clone a dynamic
     * game board with its meta data
     * @return A clone of this class' game board
     */
    @Override
    public GameOfLife clone() {
        GameOfLife clone = new GameOfLife();
        Board boardClone = board.clone();
        clone.setBoard(boardClone);
        clone.setAlgorithm(new Default(boardClone));
        
        return clone;
    }
}
