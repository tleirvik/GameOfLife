package GameOfLife;

/**
 *
 *
 *
 */
public class GameController {
    private GameOfLife gol;
    private int num_Cores = Runtime.getRuntime().availableProcessors();
    
    /**
    *
    * @param isDynamic
    * @param rows
    * @param columns
    */
    public void newGame(boolean isDynamic, int rows, int columns) {
        gol = new GameOfLife(isDynamic, rows, columns);
    }

    public void newGame(byte[][] board, MetaData metadata) {
        gol = new GameOfLife(board, metadata);
    }

    public byte[][] play() {
        gol.update();
        return gol.getBoardReference();
    }
    
    public byte[][] getBoardReference() {
        return gol.getBoardReference();
    }
    
    public MetaData getMetadata() {
        return gol.getMetaData();
    }

    //Disse kunne faktisk blitt kalt fra ViewController til å lagre filen
    public void saveGame() {

    }

    //Kanskje putte dem i GameOfLife2D?
    public void loadGame() {

    }

    public void exportGame() {

    }

    public FixedBoard getBoard() {
        return gol.getBoard();
    }
    public void resetGame() {
        gol.resetGame();
    }

    public byte getCellAliveStatus(int row, int column) {
        return gol.getCellAliveState(row, column);
    }

    public void setCellAliveStatus(int row, int column, byte isAlive) {
        gol.setCellAliveState(row, column, isAlive);
    }
}