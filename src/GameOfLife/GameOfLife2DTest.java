package GameOfLife;
import org.junit.Before;
import org.testng.annotations.Test;

import static org.junit.*;
import static org.hamcrest.*;

import static junit.framework.TestCase.fail;


public class GameOfLife2DTest {

	GameOfLife2D gol = new GameOfLife2D(false, 11, 11);

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testNextGeneration() {


        byte[][] inputArraySmallExploder = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        };

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
	}

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

}
