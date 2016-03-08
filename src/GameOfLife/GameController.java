package GameOfLife;

/**
 *
 *
 *
 */
public class GameController {
    private GameOfLife gol;
    //private FileManagement fileManagement;



        /**
         *
     * @param isDynamic
     * @param rows
     * @param columns
         */
        public void newGame(boolean isDynamic, int rows, int columns) {
        gol = new GameOfLife2D(isDynamic, rows, columns);
    }

    //Burde ta imot et byte-board
    public void newGame(boolean[][] board, boolean isDynamic) {
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

    public boolean[][] getBooleanGrid() {
            return gol.convertBoardToBoolean();
    }

    //Yet another transportmetode. Vi må virkelig fjerne denne klassen.
    public FixedBoard getBoard() {
        //Stygg utførelse av dette, men ville ikke lage en abstrakt metode også.
        //Kanskje vi skal tenke litt på om abstrakt virkelig er nødvendig (mtp. stygg kode osv)?
        return ((GameOfLife2D) gol).getBoard();
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
            return gol.getCellAliveStatus(row, column);
    }

    public void setCellAliveStatus(int row, int column, boolean isAlive) {
            gol.setCellAliveStatus(row, column, isAlive);
    }

    public void setMetaData(MetaData metadata) {
            ((GameOfLife2D) gol).setMetaData(metadata);
    }
}