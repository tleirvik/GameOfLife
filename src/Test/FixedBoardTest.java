package Test;

import GameOfLife.FixedBoard;
import GameOfLife.GameOfLife;
import GameOfLife.MetaData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.hamcrest.*;

import static org.junit.Assert.*;

/**
 * Created by tleirvik on 08.04.16.
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

    @Before
    public void setUp() throws Exception {
        FixedBoard fb = new FixedBoard(inputArraySmallExploder, new MetaData());


    }

    @Test
    public void getRows() throws Exception {
        // Arrange
        FixedBoard fb = new FixedBoard(inputArraySmallExploder, new MetaData());
        int rows = fb.getRows();
        // Act
        // Assert
        assertEquals(fb.getRows(), 11);

    }

    @Test
    public void getColumns() throws Exception {
        // Arrange
        FixedBoard fb = new FixedBoard(inputArraySmallExploder, new MetaData());
        int columns = fb.getColumns();
        // Act
        // Assert
        assertEquals(fb.getColumns(), 11);
    }

    @Test
    public void getMetaData() throws Exception {
        // Arrange
        MetaData metaData = new MetaData();
        metaData.setAuthor("Ola Nordmann");
        metaData.setComment("Kommentar");
        metaData.setName("Brett");
        String[] ruleString = {
                "B3","S23"
                };
        metaData.setRuleString(ruleString);
        FixedBoard fb = new FixedBoard(inputArraySmallExploder, new MetaData());
        // Act
        // Assert
        assertEquals(metaData.getAuthor(), "Ola Nordmann");
        assertEquals(metaData.getName(), "Brett");
        assertEquals(metaData.getComment(), "Kommentar");
        assertEquals(metaData.getRuleString()[0], "B3");
        assertEquals(metaData.getRuleString()[1], "S23");
    }

    /* Må endres */
    @Test
    public void getBoardReference() throws Exception {
        // Arrange
        FixedBoard fb = new FixedBoard(inputArraySmallExploder, new MetaData());
        byte[][] testBoard = fb.getBoardReference();
        // Act
        byte[][] fbCompare = fb.getBoardReference();
        // Assert
        assertEquals(testBoard, fbCompare);
    }

    @Test
    public void resetBoard() throws Exception {
        // Arrange
        FixedBoard fb = new FixedBoard(inputArraySmallExploder, new MetaData());
        // Act
        fb.nextGeneration();
        assertEquals(fb.toString(), "0000000000000000000000000000000000000000000000001110000000010100000000101000000000100000000000000000000000000000000000000");
        fb.resetBoard();
        // Assert
        assertEquals(fb.toString(), "0000000000000000000000000000000000000000000000000100000000011100000000101000000000100000000000000000000000000000000000000");
    }

    @Test
    public void getCellAliveState() throws Exception {
        // Arrange
        FixedBoard fb = new FixedBoard(inputArraySmallExploder, new MetaData());
        // Act
        byte cellState = fb.getCellAliveState(4, 5);
        // Assert
        assertEquals(cellState, 1);
    }

    @Test
    public void setCellAliveState() throws Exception {
        // Arrange
        FixedBoard fb = new FixedBoard(inputArraySmallExploder, new MetaData());
        // Act
        fb.setCellAliveState(4 ,5, (byte)0);
        // Assert
        assertEquals(fb.getCellAliveState(4, 5), 0);
    }

    @Test
    public void nextGeneration() throws Exception {
        // Arrange
        FixedBoard fb = new FixedBoard(inputArraySmallExploder, new MetaData());

        // Act
        for (int i = 0; i <= 9; i++) {
            // System.out.println(i);
            fb.nextGeneration();
            System.out.println(i);
        }
        // Assert
        String s = "0000000000000010001000000100010000110000011000000100000000010100000000010000001100000110000100010000001000100000000000000";
        assertEquals(s, fb.toString());

        // Is the boject an instance of GameOfLife2D
        assertTrue(fb instanceof FixedBoard);
    }

    @Test
    public void testToString() throws Exception {
        // Arrange
        FixedBoard fb = new FixedBoard(inputArraySmallExploder, new MetaData());
        String testString = new String("0000000000000000000000000000000000000000000000000100000000011100000000101000000000100000000000000000000000000000000000000");
        assertEquals(fb.toString(), testString);
    }
}