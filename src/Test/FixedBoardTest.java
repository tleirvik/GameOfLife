package Test;

import GameOfLife.FixedBoard;
import GameOfLife.MetaData;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
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
    private final byte[][] inputArraySmallExploder = {
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
        assertEquals(rows, 11);
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
        assertEquals(columns, 11);
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
        assertEquals(metaData.getAuthor(), "Ola Nordmann");
        assertEquals(metaData.getName(), "Brett");
        assertEquals(metaData.getComment(), "Kommentar");
        assertEquals(metaData.getRuleString()[0], "B3");
        assertEquals(metaData.getRuleString()[1], "S23");
    }

    /**
     * Tests if the getBoardReference-method returns a reference to an identical board
     * @see org.junit.runners.JUnit4
     * @see FixedBoard
     */
    @Test
    public void getBoardReference() {
        // Arrange
        FixedBoard fb = new FixedBoard(inputArraySmallExploder, new MetaData());
        byte[][] testBoard = fb.getBoardReference();
        byte[] testRow = testBoard[5];

        // Act
        byte[][] fbCompare = fb.getBoardReference();
        byte[] fbCompareTestRow = fbCompare[5];

        // Assert
        assertEquals(Arrays.hashCode(testBoard), Arrays.hashCode(fbCompare));
        assertArrayEquals(testRow, fbCompareTestRow);
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
        assertEquals(fb.toString(), "000000000000000000000000000000000000000000000000111000000001010000000010100000" +
                "0000100000000000000000000000000000000000000");
        fb.resetBoard();
        // Assert
        assertEquals(fb.toString(), "00000000000000000000000000000000000000000000000001000000000111000000001010000" +
                "00000100000000000000000000000000000000000000");
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

        // Act
        byte cellState = fb.getCellAliveState(4, 5);

        // Assert
        assertEquals(cellState, 1);
    }
    /**
     * Tests the setCellAliveState()-method and asserts that the method is able to set a value in board array.
     *
     * @see org.junit.runners.JUnit4
     * @see FixedBoard
     */
    @Test
    public void setCellAliveState() {
        // Arrange

        // Act
        fb.setCellAliveState(4 ,5, (byte)0);
        // Assert
        assertEquals(fb.getCellAliveState(4, 5), 0);
    }
    /**
     * Tests the nextGeneration()-method(Game Of Life game rules) and runs the method 10 times and asserts that we have
     * a game board that complies to the correct B3/S23 rules of game of life(verified manually and
     * with the Game Of Life implementation @ http://www.bitstorm.org/gameoflife/
     *
     * @see org.junit.runners.JUnit4
     * @see FixedBoard
     */
    @Test
    public void nextGeneration() {
        // Arrange
        String s = "00000000000000100010000001000100001100000110000001000000000101000000000100000011000001100001000" +
                "10000001000100000000000000";

        // Act
        for (int i = 0; i <= 9; i++) {
            // System.out.println(i);
            fb.nextGeneration();
        }

        // Assert
        assertEquals(s, fb.toString());
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
        String boardArray = "0000000000000000000000000000000000000000000000000100000000011100000000101000000000100000" +
                "000000000000000000000000000000000";

        // Act
        String s = fb.toString();

        // Assert
        assertEquals(s, boardArray);
    }
}




