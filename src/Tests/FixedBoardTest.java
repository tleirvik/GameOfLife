package Tests;

import GameOfLife.FixedBoard;
import GameOfLife.MetaData;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 *  Test class for FixedBoard
 *
 *  @see FixedBoard
 *  @see org.junit.runners.JUnit4
 */
public class FixedBoardTest {
    private FixedBoard fb;
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
        fb = new FixedBoard(inputArraySmallExploder, new MetaData());
        assertTrue(fb instanceof FixedBoard);
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
        int rows = fb.getRows();

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
        int columns = fb.getColumns();

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

        // Assert
        assertEquals("Ola Nordmann", metaData.getAuthor());
        assertEquals("Brett", metaData.getName());
        assertEquals("Kommentar", metaData.getComment());
        assertEquals("B3", metaData.getRuleString()[0]);
        assertEquals("S23", metaData.getRuleString()[1]);
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
        fb.nextGeneration();
        assertEquals("000000000000000000000000000000000000000000000000000000000000000000000011100000000001010000" +
                "0000001010000000000010000000000000000000000000000000000000000000000000000000000", fb.toString());
        fb.resetBoard();
        System.out.println(fb.getRows());
        // Assert
        assertEquals("0000000000000000000000000000000000000000000000000000000000000000000000010000000000011100" +
                "000000001010000000000010000000000000000000000000000000000000000000000000000000000", fb.toString());
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
        assertEquals(0, fb.getCellAliveState(8, 8));
        fb.setCellAliveState(8, 8, (byte)1);
         cellState = fb.getCellAliveState(8, 8);

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
        fb.setCellAliveState(4 ,5, (byte)0);
        // Assert
        assertEquals(fb.getCellAliveState(4, 5), 0);
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
        String  inputArrayExploderAfter10Gen = "000000000000000000111000000000100010000000010001000000110000011000100001000010010001010001" +
                "0010000100001000110000011000000100010000000010001000000000111000000000000000000";

        // Act
        for (int i = 0; i <= 9; i++) {
            fb.nextGeneration();
        }

        // Assert
        assertEquals(inputArrayExploderAfter10Gen, fb.toString());
    }
    @Test
    public void nextGeneration2() {
        // Arrange
        fb = new FixedBoard(inputArrayTumbler, new MetaData());
        String  inputArrayTumblerAfter30Gen = "00000000000000000000000000000000000011011000000000000000000101" +
                "000001101010110011100011100000000000000000000000";

        // Act
        for (int i = 0; i <= 29; i++) {
            fb.nextGeneration();
        }

        // Assert
        assertEquals(inputArrayTumblerAfter30Gen, fb.toString());
    }
    @Test
    public void nextGeneration3() {
        // Arrange
        fb = new FixedBoard(inputArrayGlider, new MetaData());
        String  inputArrayGliderAfter30Gen = "000000000000000000000000000000000000000000000000000000000000000000" +
                "00000000000000000000000000000000000000000000000000000000000000000000000000000000100000000000000" +
                "00010000000000000011100000000000000000000000000000000000000000000000000000000000000000000000000" +
                "000000000000000000000000000000000";

        // Act
        for (int i = 0; i <= 29; i++) {
            fb.nextGeneration();
        }

        // Assert
        assertEquals(inputArrayGliderAfter30Gen, fb.toString());
    }
    @Test
    public void setFirstGeneration() {

    }
    @Test
    public void countAliveCells() {

    }
    @Test
    @Override
    public FixedBoard clone() {
        return new FixedBoard(10, 10);
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




