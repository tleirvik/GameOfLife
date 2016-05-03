
import Model.GameOfLife.Boards.Board;
import Model.GameOfLife.Boards.FixedBoard;
import Model.GameOfLife.GameOfLife;
import Model.GameOfLife.MetaData;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 *  Test class for FixedBoard
 *
 *  @see FixedBoard
 *  @see org.junit.runners.JUnit4
 */
public class FixedBoardTest {
    private FixedBoard fb;
    private GameOfLife gol;
    byte[][] inputArraySmallExploder = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0},
            {0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
    };

    byte[][] inputArrayTumbler = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 1, 1, 0, 1, 1, 0, 0},
            {0, 0, 1, 1, 0, 1, 1, 0, 0},
            {0, 0, 0, 1, 0, 1, 0, 0, 0},
            {0, 1, 0, 1, 0, 1, 0, 1, 0},
            {0, 1, 0, 1, 0, 1, 0, 1, 0},
            {0, 1, 1, 0, 0, 0, 1, 1, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
    };
    byte[][] inputArrayGlider = {
            {0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},

    };
    /**
     *  JUnit setup method that runs before testing and creates a test board
     * @see org.junit.runners.JUnit4
     * @see FixedBoard
     */
    @Before
    public void setUp() {
        gol = new GameOfLife();
        gol.loadGame(inputArraySmallExploder, new MetaData(), Board.BoardType.FIXED);
        //fb = new FixedBoard(inputArraySmallExploder, new MetaData());
        assertTrue(gol instanceof GameOfLife);
        assertTrue(gol.getBoard() instanceof Board);

    }

    /**
     * Test the getRows()-method and asserts that we have 11 rows on the board
     * @see org.junit.runners.JUnit4
     * @see FixedBoard
     */
    @Test
    public void getRows() {
        // Arrange

        // Act
        int rows = gol.getRows();

        // Assert
        assertEquals(11, rows);
    }
    /**
     * Tests the getColumns()-method and asserts that we have 11 columns on the board
     * @see org.junit.runners.JUnit4
     * @see FixedBoard
     */
    @Test
    public void getColumns() {
        // Arrange

        // Act
        int columns = gol.getColumns();

        // Assert
        assertEquals(11, columns);
    }
    /**
     * Tests the getMetaData()-method and asserts that the method returns the meta data we specified
     * @see org.junit.runners.JUnit4
     * @see FixedBoard
     */
    @Test
    public void getMetaData() {
        // Arrange
        MetaData metaData = new MetaData();

        // Act
        metaData.setAuthor("Ola Nordmann");
        metaData.setComment("Kommentar");
        metaData.setName("Brett");
        String[] ruleString = {
                "B3","S23"
        };
        metaData.setRuleString(ruleString);
        gol.loadGame(inputArraySmallExploder, metaData, Board.BoardType.FIXED);
        // Assert
        assertEquals("Ola Nordmann", gol.getMetaData().getAuthor());
        assertEquals("Brett", gol.getMetaData().getName());
        assertEquals("Kommentar", gol.getMetaData().getComment());
        assertEquals("B3", gol.getMetaData().getRuleString()[0]);
        assertEquals("S23", gol.getMetaData().getRuleString()[1]);
    }

    /**
     * Tests the resetBoard()-method and asserts that the board is reset to the same board that the class was
     * instantiated with. Runs nextGeneration once and runs the method. Then asserts that the board is equal to
     * the starting board
     *
     * @see org.junit.runners.JUnit4
     * @see FixedBoard
     */
    @Test
    public void resetBoard() {
        // Arrange


        // Act
        gol.update();
        assertEquals("00000000000000000000000000000000000000000000000011100000000101000000001010000000001000000000" +
                "00000000000000000000000000000", gol.getBoard().toString());
        gol.resetGame();
        System.out.println(gol.getRows());
        // Assert
        assertEquals("000000000000000000000000000000000000000000000000010000000001110000000010100000000010000000" +
                "0000000000000000000000000000000", gol.getBoard().toString());
    }

    /**
     * Tests the getCellAliveState()-method and asserts that the method returns the correct byte value from the
     * board array
     *
     * @see org.junit.runners.JUnit4
     * @see FixedBoard
     */
    @Test
    public void getCellAliveState() {
        // Arrange
        byte cellState;

        // Act
        assertEquals(0, gol.getCellAliveState(8, 8));
        gol.setCellAliveState(8, 8, (byte)1);
         cellState = gol.getCellAliveState(8, 8);

        // Assert
        assertEquals(1, cellState);
    }
    /**
     * Tests the setCellAliveState()-method and asserts that the method is able to set a value in board array.
     *
     * @see org.junit.runners.JUnit4
     * @see FixedBoard
     */
    @Test
    public void setCellAliveState() throws Exception {
        // Arrange

        // Act
        gol.setCellAliveState(4 ,5, (byte)0);
        // Assert
        assertEquals(gol.getCellAliveState(4, 5), 0);
    }
    /**
     * Tests the nextGeneration()-method(Game Of Life game rules) and runs the method 10 times and asserts that we have
     * a game board that complies to the correct B3/S23 rules of game of life(verified manually and
     * on http://www.bitstorm.org/gameoflife/
     *
     * @see org.junit.runners.JUnit4
     * @see FixedBoard
     */
    @Test
    public void nextGeneration() {
        // Arrange
        String  inputArrayExploderAfter10Gen = "00001110000000100010000001000100001100000110100001000011000101" +
                "00011000010000101100000110000100010000001000100000001110000";

        // Act
        for (int i = 0; i <= 9; i++) {
            gol.update();
        }

        // Assert
        assertEquals(inputArrayExploderAfter10Gen, gol.getBoard().toString());
    }
    @Test
    public void nextGeneration2() {
        // Arrange
        gol.loadGame(inputArrayTumbler, new MetaData(), Board.BoardType.FIXED);

        String  inputArrayTumblerAfter30Gen = "0000000000000000000011011000000000000001010001101010111110001" +
                "11000000000";

        // Act
        for (int i = 0; i <= 29; i++) {
            gol.update();
        }

        // Assert
        assertEquals(inputArrayTumblerAfter30Gen, gol.getBoard().toString());
    }
    @Test
    public void nextGeneration3() {
        // Arrange
        gol.loadGame(inputArrayGlider, new MetaData(), Board.BoardType.FIXED);
        String  inputArrayGliderAfter30Gen = "000000000000000000000000000000000000000000000000000000000000000000000" +
                "00000000000000000000000000000000000000000000010000000000000001000000000000111000000000000000000000" +
                "0000000000000000000000000000000000000000000000000000000000";

        // Act
        for (int i = 0; i <= 29; i++) {
            gol.update();
        }

        // Assert
        assertEquals(inputArrayGliderAfter30Gen, gol.getBoard().toString());
    }
    @Test
    public void setFirstGeneration() {
        // Arrange
        gol.loadGame(inputArrayGlider, new MetaData(), Board.BoardType.FIXED);
        String inputBoard = gol.getBoard().toString();

        // Act
        gol.update();
        assertNotEquals(inputBoard, gol.getBoard().toString());
        gol.resetGame();

        // Assert
        assertEquals(inputBoard, gol.getBoard().toString());
    }

    @Test
    public void testClone() {
        // Arrange
        MetaData md = new MetaData();
        md.setName("Name");
        String inputBoard = fb.toString();
        String verifyBoard = "0000000000000000000000000000000000000000000000000000000000000000000000010000000000" +
                "011100000000001010000000000010000000000000000000000000000000000000000000000000000000000";
        // Act
        fb = new FixedBoard(inputArrayTumbler, md);
        FixedBoard fbClone = fb.clone();
        // Assert
        assertNotEquals(fb.hashCode(), fbClone.hashCode());
        assertEquals(verifyBoard, inputBoard);
        assertEquals("Name", fbClone.getMetaData().getName());
    }
    /**
     * Tests the toString()-method and asserts that the board is correctly translated from a byte array to a String
     *
     * @see org.junit.runners.JUnit4
     * @see FixedBoard
     */
    @Test
    public void testToString() {
        // Arrange
        String boardArray = "0000000000000000000000000000000000000000000000000100000000011100000000101000000" +
                "000100000000000000000000000000000000000000";

        // Act
        String s = fb.toString();

        // Assert
        assertEquals(boardArray, boardArray);
    }
}




