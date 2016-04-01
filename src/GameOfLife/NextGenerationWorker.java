package GameOfLife;

/**
 *
 * @author Robin
 */
public class NextGenerationWorker implements Runnable {
    private int startRow;
    private int endRow;
    private DynamicBoard workingBoard;
    
    public NextGenerationWorker(int startRow, int endRow, DynamicBoard workingBoard) {
        this.startRow = startRow;
        this.endRow = endRow;
        this.workingBoard = workingBoard;
    }
    
    @Override
    public void run() {
        workingBoard.nextGeneration(startRow, endRow);
    }    
}
