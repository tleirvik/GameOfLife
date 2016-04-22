package GameOfLife;

import java.util.Random;

public class GameOfLife {
    private Board board;

    public void newEmptyGame(int rows, int columns) {
        board = new TorodialBoard(rows, columns);
    }
    
    public void newRandomGame(int rows, int columns) {
        board = new TorodialBoard(rows, columns);
        Random random = new Random();
        for(int row = 0; row < board.getRows(); row++) {
            for(int col = 0; col < board.getColumns(); col++) {
                board.setCellAliveState(row,col,((random.nextInt(5) == 1) ? (byte)1 : (byte)0));
            }
        }
    }

    public void loadGame(byte[][] board, MetaData metadata) {
        this.board = new TorodialBoard(board, metadata);
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
        board.nextGeneration();
    }

    @Override
    public GameOfLife clone() {
        GameOfLife clone = new GameOfLife();
        Board boardClone = board.clone();
        clone.setBoard(board);
        return clone;
    }
}
