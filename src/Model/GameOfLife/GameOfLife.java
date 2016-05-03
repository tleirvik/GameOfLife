package Model.GameOfLife;

import Model.GameOfLife.Algorithms.Algorithm;
import Model.GameOfLife.Algorithms.Default;
import Model.GameOfLife.Boards.Board;
import Model.GameOfLife.Boards.FixedBoard;
import Model.GameOfLife.Boards.DynamicBoard;
import Model.GameOfLife.Boards.Board.BoardType;
import java.util.Random;

public class GameOfLife {
    private Board board;
    private Algorithm algorithm;
    private final int NUM_THREADS = Runtime.getRuntime().availableProcessors();
    private NextGenerationWorkers nextGenerationWorkers;

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
    
    public void newEmptyGame(int rows, int columns, BoardType type) {
        newGame(rows, columns, type);
    }
    
    public void newRandomGame(int rows, int columns, BoardType type) {
        newGame(rows, columns, type);
        Random random = new Random();
        for(int row = 0; row < board.getRows(); row++) {
            for(int col = 0; col < board.getColumns(); col++) {
                board.setCellAliveState(row,col,((random.nextInt(5) == 1) ? (byte)1 : (byte)0));
            }
        }
        board.setFirstGeneration();
    }

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
    }
    
    public void setFirstGeneration() {
        board.setFirstGeneration();
    }

    public Board getBoard() {
    	return board;
    }
    
    public MetaData getMetaData() {
        return board.getMetaData();
    }
    
    public int getRows() {
        return board.getRows();
    }

    public int getColumns() {
        return board.getColumns();
    }
    
    public byte getCellAliveState(int row, int column) {
        return board.getCellAliveState(row, column);
    }

    public void setCellAliveState(int row, int column, byte isAlive) {
        board.setCellAliveState(row, column, isAlive);
    }
    
    public void setBoard(Board board) {
        this.board = board;
    }

    public void resetGame() {
        board.resetBoard();
    }
    
    public void update() {
        algorithm.beforeUpdate();
        algorithm.update();
    }
    
    

    public void updateWithThreads() {
        algorithm.beforeUpdate();
        nextGenerationWorkers.splitBoard();
        nextGenerationWorkers.createWorkers();
        try {
            nextGenerationWorkers.runWorkers();
        } catch (InterruptedException iE) {
            System.out.println("Føkk! " + iE.getStackTrace());
        }
    }
    
    public void setAlgorithm(Algorithm a) {
        algorithm = a;
    }

    @Override
    public GameOfLife clone() {
        GameOfLife clone = new GameOfLife();
        Board boardClone = board.clone();
        clone.setBoard(boardClone);
        
        clone.setAlgorithm(new Default(boardClone));
        
        return clone;
    }
}
