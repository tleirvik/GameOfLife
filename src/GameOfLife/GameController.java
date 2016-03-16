package GameOfLife;

/**
 *
 *
 *
 */
public class GameController {
    private GameOfLife gol;
    
    /**
    *
    * @param isDynamic
    * @param rows
    * @param columns
    */
    public void newGame(boolean isDynamic, int rows, int columns) {
        gol = new GameOfLife2D(isDynamic, rows, columns);
    }

    public void newGame(byte[][] board, boolean isDynamic) {
        gol = new GameOfLife2D(board, isDynamic);
    }

    public void play() {
        if(gol.getIsBoardEmpty()) {
            System.out.println("Board Empty");
             gol.populateRandomBoard();
            gol.setIsBoardEmpty(false);
        } else {
            gol.nextGeneration();
        }
    }

    //Yet another transportmetode. Vi må virkelig fjerne denne klassen.
    public FixedBoard getBoard() {
        //Stygg utførelse av dette, men ville ikke lage en abstrakt metode også.
        //Kanskje vi skal tenke litt på om abstrakt virkelig er nødvendig (mtp. stygg kode osv)?
        return ((GameOfLife2D) gol).getBoard();
    }
    
    public byte[][] getGameBoard() {
        return ((GameOfLife2D)gol).getGameBoard();
    }

    //Disse kunne faktisk blitt kalt fra ViewController til å lagre filen
    public void saveGame() {

    }

    //Kanskje putte dem i GameOfLife2D?
    public void loadGame() {

    }

    public void exportGame() {

    }

    public boolean getCellAliveStatus(int row, int column) {
        return gol.getCellAliveState(row, column);
    }

    public void setCellAliveStatus(int row, int column, boolean isAlive) {
        gol.setCellAliveState(row, column, isAlive);
    }

    public void setMetaData(MetaData metadata) {
        ((GameOfLife2D) gol).setMetaData(metadata);
    }
}