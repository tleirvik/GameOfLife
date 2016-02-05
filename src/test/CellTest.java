package test;

import static org.junit.Assert.*;

import org.junit.Test;

import game.Cell;
import javafx.scene.paint.Color;



public class CellTest {

	@Test
	public void Constructortest() {
		
		Cell cell = new Cell(10,10,true, Color.BLACK);
		
		
		assertEquals(10, cell.getXpos());
		assertEquals(10, cell.getYpos());
		assertEquals(true, cell.getIsAlive());
		/* assertEquals(Color.BLACK, cell.getXpos()); */
	}

}
