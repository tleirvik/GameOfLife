package GameOfLife;
import org.junit.Before;
import org.testng.annotations.Test;
import org.hamcrest.*;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import org.junit.*;
import org.hamcrest.*;

import java.util.Arrays;

import static org.junit.Assert.*;

import static junit.framework.TestCase.fail;


public class GameOfLife2DTest {

	GameOfLife2D gol = new GameOfLife2D(false, 11, 11);

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testNextGeneration() {

        // Arrange
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
        byte[] row0 = inputArraySmallExploder[0];
        byte[] row1 = inputArraySmallExploder[1];
        byte[] row2 = inputArraySmallExploder[2];
        byte[] row3 = inputArraySmallExploder[3];
        byte[] row4 = inputArraySmallExploder[4];
        byte[] row5 = inputArraySmallExploder[5];
        byte[] row6 = inputArraySmallExploder[6];
        byte[] row7 = inputArraySmallExploder[7];
        byte[] row8 = inputArraySmallExploder[8];
        byte[] row9 = inputArraySmallExploder[9];
        byte[] row10 = inputArraySmallExploder[10];

        byte[][] inputArraySmallExploderAfter10Gen = {
                {0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0},
                {0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0},
                {0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0},
                {0, 1, 1, 0 ,0 ,0 ,0 ,0, 1, 1, 0},
                {1, 0, 0, 0, 0, 1, 0, 0, 0 ,0, 1},
                {1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 1, 0, 0, 0 ,0, 1},
                {0, 1, 1, 0 ,0 ,0 ,0 ,0, 1, 1, 0},
                {0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0},
                {0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0},
                {0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0},
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
       //  byte[] row11After10Gen = inputArraySmallExploderAfter10Gen[0];

		GameOfLife2D gol = new GameOfLife2D(inputArraySmallExploder, false);

        // Act



        // Assert
        // Is the boject an instance of GameOfLife2D
        assertTrue(gol instanceof GameOfLife2D);
        //
        assertArrayEquals(row0, gol.getGameBoard()[0]);
        assertArrayEquals(row1, gol.getGameBoard()[1]);
        assertArrayEquals(row2, gol.getGameBoard()[2]);
        assertArrayEquals(row3, gol.getGameBoard()[3]);
        assertArrayEquals(row4, gol.getGameBoard()[4]);
        assertArrayEquals(row5, gol.getGameBoard()[5]);
        assertArrayEquals(row6, gol.getGameBoard()[6]);
        assertArrayEquals(row7, gol.getGameBoard()[7]);
        assertArrayEquals(row8, gol.getGameBoard()[8]);
        assertArrayEquals(row9, gol.getGameBoard()[9]);
        assertArrayEquals(row10, gol.getGameBoard()[10]);

        /*
        for (int i = 0; i <= 8; i++) {
            // System.out.println(i);
            gol.nextGeneration();
        }
        */
        gol.nextGeneration();
        gol.nextGeneration();
        gol.nextGeneration(); // Her skjer det en feil.
        for (int i = 0; i < gol.getGameBoard().length; i++) {
            for (int j = 0; j < gol.getGameBoard()[0].length; j++) {
                System.out.print(gol.getGameBoard()[i][j]);

            }
            System.out.println();
        }
        assertArrayEquals(row0After10Gen, gol.getGameBoard()[0]);
        assertArrayEquals(row1After10Gen, gol.getGameBoard()[1]);
        assertArrayEquals(row2After10Gen, gol.getGameBoard()[2]);
        assertArrayEquals(row3After10Gen, gol.getGameBoard()[3]);
        assertArrayEquals(row4After10Gen, gol.getGameBoard()[4]);
        assertArrayEquals(row5After10Gen, gol.getGameBoard()[5]);
        assertArrayEquals(row6After10Gen, gol.getGameBoard()[6]);
        assertArrayEquals(row7After10Gen, gol.getGameBoard()[7]);
        assertArrayEquals(row8After10Gen, gol.getGameBoard()[8]);
        assertArrayEquals(row9After10Gen, gol.getGameBoard()[9]);
        assertArrayEquals(row10After10Gen, gol.getGameBoard()[10]);
        }
	}
/*
	@Test
	public void testCountNeighbours() {
		fail("Not yet implemented");
	}

	@Test
	public void testGameOfLife2DBooleanIntInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testGameOfLife2DBooleanArrayArrayBoolean() {
		fail("Not yet implemented");
	}
*/
