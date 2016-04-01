package GameOfLife;

import java.util.Random;

public class GameOfLife {
    private FixedBoard board; 

    //Burde ikke lage brett i konstruktør, burde heller
    //Være en egen funksjon så man slipper å slette og lage ny GameOfLife.
    //Singleton?
    public GameOfLife(boolean isDynamic, int rows, int columns) {        
        board = new FixedBoard(rows, columns);
    }

    //Burde ikke lage brett i konstruktør, burde heller
    //Være en egen funksjon så man slipper å slette og lage ny GameOfLife.
    //Singleton?
    public GameOfLife(byte[][] board, MetaData metadata) {
        this.board = new FixedBoard(board, metadata);
    }

    public void populateRandomBoard() {
        Random random = new Random();
        for(int row = 0; row < board.getRows(); row++) {
            for(int col = 0; col < board.getColumns(); col++) {
                board.setCellAliveState(row,col,((random.nextInt(5) == 1) ? (byte)1 : (byte)0));
            }
        }
    }

    /**
     *
     * @return
     */
    public FixedBoard getBoard() {
    	return board;
        
    }
    
    public byte[][] getBoardReference() {
    	return board.getBoardReference();
    }

    public void update() {
        board.nextGeneration();
    }
    
    public MetaData getMetaData() {
        return board.getMetaData();
    }
    
    public void resetGame() {
        board.resetBoard();
    }
    
    /*
    public void updateWithThreads(int num_Cores) {
        Thread[] threads = new Thread[num_Cores];
        
        int rowsPerCore = board.getRows() / num_Cores;
        int leftoverRows = board.getRows() % num_Cores;
        int startRow = 0, endRow = rowsPerCore;
        
        for(int i = 0; i < num_Cores; i++) {
            if(leftoverRows != 0) {
                endRow++;
                threads[i] = new Thread(new NextGenerationWorker(startRow, endRow, board));
                leftoverRows--;
            } else {
                threads[i] = new Thread(new NextGenerationWorker(startRow, endRow, board));
            }
            threads[i].start();
            startRow=endRow;
            endRow+=rowsPerCore;
        }
        for(int i = 0; i < num_Cores; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException ex) {
                Logger.getLogger(GameOfLife.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        board.nextGenerationToCurrent();
    }*/

    public byte getCellAliveState(int row, int column) {
        return board.getCellAliveState(row, column);
    }

    public void setCellAliveState(int row, int column, byte isAlive) {
        board.setCellAliveState(row, column, isAlive);
    }

}
