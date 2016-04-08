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

        // Verify that the input array is the same +2 in length/width after input
        byte[][] inputArraySmallExploderAfterInput = {
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},

        };
        // Split the array two one dimension
        byte[] row0 = inputArraySmallExploderAfterInput[0];
        byte[] row1 = inputArraySmallExploderAfterInput[1];
        byte[] row2 = inputArraySmallExploderAfterInput[2];
        byte[] row3 = inputArraySmallExploderAfterInput[3];
        byte[] row4 = inputArraySmallExploderAfterInput[4];
        byte[] row5 = inputArraySmallExploderAfterInput[5];
        byte[] row6 = inputArraySmallExploderAfterInput[6];
        byte[] row7 = inputArraySmallExploderAfterInput[7];
        byte[] row8 = inputArraySmallExploderAfterInput[8];
        byte[] row9 = inputArraySmallExploderAfterInput[9];
        byte[] row10 = inputArraySmallExploderAfterInput[10];
        byte[] row11 = inputArraySmallExploderAfterInput[11];
        byte[] row12 = inputArraySmallExploderAfterInput[12];

        // Array after 10 generations
        byte[][] inputArraySmallExploderAfter10Gen = {
                {0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0},
                {0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0},
                {0, 1, 1, 0 ,0 ,0 ,0 ,0, 1, 1, 0, 0, 0},
                {1, 0, 0, 0, 0, 1, 0, 0, 0 ,0, 1, 0, 0},
                {1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0},
                {1, 0, 0, 0, 0, 1, 0, 0, 0 ,0, 1, 0, 0},
                {0, 1, 1, 0 ,0 ,0 ,0 ,0, 1, 1, 0, 0, 0},
                {0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0},
                {0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        };

        byte[] row0After10Gen = inputArraySmallExploderAfter10Gen[0];
        byte[] row1After10Gen = inputArraySmallExploderAfter10Gen[1];
        byte[] row2After10Gen = inputArraySmallExploderAfter10Gen[2];
        byte[] row3After10Gen = inputArraySmallExploderAfter10Gen[3];
        byte[] row4After10Gen = inputArraySmallExploderAfter10Gen[4];
        byte[] row5After10Gen = inputArraySmallExploderAfter10Gen[5];
        byte[] row6After10Gen = inputArraySmallExploderAfter10Gen[6];
        byte[] row7After10Gen = inputArraySmallExploderAfter10Gen[7];
        byte[] row8After10Gen = inputArraySmallExploderAfter10Gen[8];
        byte[] row9After10Gen = inputArraySmallExploderAfter10Gen[9];
        byte[] row10After10Gen = inputArraySmallExploderAfter10Gen[10];
        byte[] row11After10Gen = inputArraySmallExploderAfter10Gen[11];
        byte[] row12After10Gen = inputArraySmallExploderAfter10Gen[12];



        // Act

        GameOfLife gol = new GameOfLife(inputArraySmallExploder, new MetaData());

        /*
        for (int i = 0; i <= 8; i++) {
            // System.out.println(i);
            gol.update();
        }
        */
        // Assert
        byte[][] board = gol.getBoardReference();

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                System.out.print(board[i][j]);
            }
            System.out.println();
        }

        // Is the boject an instance of GameOfLife2D
        assertTrue(gol instanceof GameOfLife);


        // Assert that getBoardReference returns the same board with +2 elements in length/width
        /*
        assertArrayEquals(row0, gol.getBoardReference()[0]);
        assertArrayEquals(row1, gol.getBoardReference()[1]);
        assertArrayEquals(row2, gol.getBoardReference()[2]);
        assertArrayEquals(row3, gol.getBoardReference()[3]);
        assertArrayEquals(row4, gol.getBoardReference()[4]);
        assertArrayEquals(row5, gol.getBoardReference()[5]);
        assertArrayEquals(row6, gol.getBoardReference()[6]);
        assertArrayEquals(row7, gol.getBoardReference()[7]);
        assertArrayEquals(row8, gol.getBoardReference()[8]);
        assertArrayEquals(row9, gol.getBoardReference()[9]);
        assertArrayEquals(row10, gol.getBoardReference()[10]);
        assertArrayEquals(row11, gol.getBoardReference()[10]);
        assertArrayEquals(row12, gol.getBoardReference()[10]);
*/
        /*
        for (int i = 0; i <= 9; i++) {
            // System.out.println(i);
            gol.update();
        }
        */
        gol.update();
        gol.update();
        gol.update();
        gol.update();
        gol.update(); // OK - 5.gen
        gol.update(); // OK - 6.gen
        gol.update(); // Feil - Den mangler 1 6 posisjon på første rad. Noe bounding box feil?
        // gol.update(); // Mangler 3 1'ere på første rad
        System.out.println();

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                System.out.print(board[i][j]);
            }
            System.out.println();
        }
        System.out.println(gol.getBoard().toString());
        /*
        assertArrayEquals(row0After10Gen, gol.getBoardReference()[0]);
        assertArrayEquals(row1After10Gen, gol.getBoardReference()[1]);
        assertArrayEquals(row2After10Gen, gol.getBoardReference()[2]);
        assertArrayEquals(row3After10Gen, gol.getBoardReference()[3]);
        assertArrayEquals(row4After10Gen, gol.getBoardReference()[4]);
        assertArrayEquals(row5After10Gen, gol.getBoardReference()[5]);
        assertArrayEquals(row6After10Gen, gol.getBoardReference()[6]);
        assertArrayEquals(row7After10Gen, gol.getBoardReference()[7]);
        assertArrayEquals(row8After10Gen, gol.getBoardReference()[8]);
        assertArrayEquals(row9After10Gen, gol.getBoardReference()[9]);
        assertArrayEquals(row10After10Gen, gol.getBoardReference()[10]);
        assertArrayEquals(row11After10Gen, gol.getBoardReference()[11]);
        assertArrayEquals(row12After10Gen, gol.getBoardReference()[12]);
*/

    }

    @Test
    public void testToString() throws Exception {
        // Arrange
        FixedBoard fb = new FixedBoard(inputArraySmallExploder, new MetaData());
        String testString = new String("0000000000000000000000000000000000000000000000000100000000011100000000101000000000100000000000000000000000000000000000000");
        assertEquals(fb.toString(), testString);
    }
}