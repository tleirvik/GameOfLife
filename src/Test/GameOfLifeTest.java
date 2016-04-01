package Test;

import org.junit.Test;
import GameOfLife.*;

import static org.junit.Assert.*;

/**
 * Created by tleirvik on 01.04.2016.
 */
public class GameOfLifeTest {

    @Test
    public void getBoard() throws Exception {

    }

    @Test
    public void getBoardReference() throws Exception {

    }

    @Test
    public void update() throws Exception {

        // Arrange

        // byte array x = 11, y = 11 with a small exploder
        byte[][] inputArraySmallExploder = {
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
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        };
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
        gol.update(); // Mangler 3 1'ere på første rad
        System.out.println();

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                System.out.print(board[i][j]);
            }
            System.out.println();
        }
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

    }

    @Test
    public void getMetaData() throws Exception {

    }

    @Test
    public void getCellAliveState() throws Exception {

    }

    @Test
    public void setCellAliveState() throws Exception {

    }
}