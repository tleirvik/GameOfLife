package GameOfLife;

import static org.junit.Assert.*;

import org.junit.Test;

import javafx.scene.paint.Color;
/**
 * Unit test for the Cell class
 * @author tleirvik
 *
 */
public class CellTest {

	@Test
	/**
	 *  Constructor test
	 */
	public void testCell() {

		Cell cell = new Cell();
		assertEquals(cell.getIsAlive(), false); /* The constructor sets the default isAlive value to false */
		assertThat(cell, org.hamcrest.CoreMatchers.instanceOf(Cell.class)); /* Verifies that cell is an instance of Cell */
	}
	@Test
	/**
	 *  getIsAlive() test
	 */
	public void testGetIsAlive() {
		Cell cell = new Cell();
		assertEquals(cell.getIsAlive(), false);
		// assertThat(cell, instanceOf(Cell.class));
		assertThat(cell, org.hamcrest.CoreMatchers.instanceOf(Cell.class));

	}

	@Test
	/**
	 * testSetIsAlive() test
	 */
	public void testSetIsAlive() {
		Cell cell = new Cell();
		cell.setIsAlive(true);
		assertEquals(cell.getIsAlive(), true);
	}
	@Test
	/**
	 * testSetColor() test
	 */
	public void testSetColor() {
		Cell cell = new Cell();
		cell.setColor(Color.BLACK);
		assertEquals(cell.getColor(), Color.BLACK);

	}
	@Test
	/**
	 * testGetColor() test
	 */
	public void testGetColor() {
		Cell cell = new Cell();

		assertEquals(cell.getColor(), null);
		cell.setColor(Color.WHEAT);
		assertEquals(cell.getColor(), Color.WHEAT);
	}

	@Test
	/**
	 * toString() test
	 */
	public void testToString() {
		Cell cell = new Cell();
		cell.setColor(Color.BLACK);
		System.out.println(cell.toString());
		String s = new String ("Cell status: false Color: 0x000000ff");
		assertEquals(cell.toString(), s);
	}

}
