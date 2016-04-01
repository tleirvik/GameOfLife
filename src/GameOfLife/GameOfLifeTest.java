package GameOfLife;
import org.junit.Before;
import org.junit.Test;
// import org.testng.annotations.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertArrayEquals;


public class GameOfLifeTest {

	GameOfLife gol = new GameOfLife(false, 11, 11);

	@Before
	public void setUp() throws Exception {
	}

	@org.testng.annotations.Test
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

		GameOfLife gol = new GameOfLife(inputArraySmallExploder, new MetaData());

        // Act



        // Assert
            /*
        // Is the boject an instance of GameOfLife2D
        assertTrue(gol instanceof GameOfLife);
        //
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

*/
        /*
        for (int i = 0; i <= 8; i++) {
            // System.out.println(i);
            gol.nextGeneration();
        }
        */
        gol.update();
        gol.update();
        gol.update();
        gol.update();
        for (int i = 0; i < gol.getBoardReference().length; i++) {
            for (int j = 0; j < gol.getBoardReference()[0].length; j++) {
                System.out.print(gol.getBoardReference()[i][j]);

            }
            System.out.println();
        }
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
        */
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
