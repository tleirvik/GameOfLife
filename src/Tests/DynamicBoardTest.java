package Tests;

import GameOfLife.DynamicBoard;
import GameOfLife.MetaData;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by tleirvik on 15.04.2016.
 */
public class DynamicBoardTest {
    DynamicBoard dynamicBoard;
    MetaData metaData = new MetaData();
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
        metaData.setAuthor("Ola Nordmann");
        metaData.setComment("Kommentar");
        metaData.setName("Pattern name");
        String[] s = new String[2];
        s[0] = "B3";
        s[1] = "S23";
        metaData.setRuleString(s);
        dynamicBoard = new DynamicBoard(inputArraySmallExploder, metaData);

        assertTrue(dynamicBoard instanceof DynamicBoard);
        // System.out.println(dynamicBoard.toString());
    }

    @Test
    public void addFrame() throws Exception {
        // Arrange

        // Act
        assertEquals(13, dynamicBoard.getRows());
        assertEquals(13, dynamicBoard.getColumns());

        dynamicBoard.addFrame();

        int rows = dynamicBoard.getRows();
        int columns = dynamicBoard.getColumns();

        // Assert
        // Hvorfor legges to til på addFrame()?

        /*assertEquals(13, rows);
        assertEquals(13, columns);*/
    }

    @Test
    public void getRows() throws Exception {
        // Arrange

        // Act
        int rows = dynamicBoard.getRows();

        // Assert
        assertEquals(11, (rows -2));
    }

    @Test
    public void getColumns() throws Exception {
        // Arrange

        // Act
        int columns = dynamicBoard.getColumns();

        // Assert
        assertEquals(11, (columns -2));
    }

    @Test
    public void getMetaData() throws Exception {
        // Arrange

        MetaData md = dynamicBoard.getMetaData();

        // Act

        // Assert
        assertEquals("Ola Nordmann", md.getAuthor());
        assertEquals("Pattern name", md.getName());
        assertEquals("Kommentar", md.getComment());
        assertEquals("B3", md.getRuleString()[0]);
        assertEquals("S23", md.getRuleString()[1]);
    }

    @Test
    public void getCellAliveState() throws Exception {
        // Arrange

        byte cellState;

        // Act

        cellState = dynamicBoard.getCellAliveState(7,7);

        // Assert
        assertEquals(cellState, 1);

    }

    @Test
    public void setCellAliveState() throws Exception {
        // Arrange
        byte toSet = 1;

        // Act
        byte verify = dynamicBoard.getCellAliveState(3,3);
        assertEquals(verify, 0);
        dynamicBoard.setCellAliveState(3,3, toSet);

        // Assert
        assertEquals(dynamicBoard.getCellAliveState(3,3), 1);
    }

    @Test
    public void resetBoard() throws Exception {
        // Arrange

        // Act
        String compareBefore = dynamicBoard.toString();
        dynamicBoard.nextGeneration();
        String compareAfter = dynamicBoard.toString();
        dynamicBoard.resetBoard();
        String compareAfterReset = dynamicBoard.toString();

        // Assert
        assertEquals(compareBefore, compareBefore);
        assertNotEquals(compareAfter, compareBefore);
        assertEquals(compareBefore, compareAfterReset);

    }

    @Test
    public void countNeighbours() throws Exception {
        // Arrange

        // Act

        // Assert
    }

    @Test
    public void nextGeneration() throws Exception {
        // Arrange

        // Act

        // Assert
    }

    @Test
    public void setFirstGeneration() throws Exception {
        // Arrange

        // Act

        // Assert
    }

    @Test
    public void setIsDynamic() throws Exception {
        // Arrange

        // Act


        // Assert
    }

    @Test

    public void testClone() throws Exception {
        // Arrange
        DynamicBoard cloneBoard = new DynamicBoard(inputArraySmallExploder, new MetaData());
        String verify = "0000000000000000000000000000000000000000000000001110000000010100000000101000000000100" +
                "00000000000000000000000000000000000000000000000";
        // Act
        cloneBoard.nextGeneration();
        System.out.println();
        String compare = cloneBoard.toString();

        // Assert
        assertEquals(verify, compare);
    }

    @Test
    public void testToString() throws Exception {
        // Arrange
        String verify = "0000000000000000000000000000000000000000000000000000000000000000000000010000000000011" +
                "100000000001010000000000010000000000000000000000000000000000000000000000000000000000";

        // Act
        String compare = dynamicBoard.toString();

        // Assert
        assertEquals(verify, compare);
    }

}