/**
 * 
 */
package game;

import java.util.Random;

/**
 * @author 
 *
 */
public abstract class Board {

	private int columns;
	private int rows;
	
	public Cell[][] boardGrid;
	
	/*
	
		this.columns = columns;
		this.rows = rows;
		boardGrid = new Cell[this.rows][this.columns];
	}
	*/
	
	//Burde egentlig returnere et array med boolean verdier (bit 0 1) like langt som antall celler i et brett
	//Eksempel: 10x10 grid = 100 celler
	static boolean seedGenerator(/*long seed*/) {
		//La bruker lage sin egen seed
		//Bruk datamaskinens klosse?
		
		Random rand = new Random();
		
		int randInt = rand.nextInt(2);
		
		if(randInt == 1) {
			return false;
		} else {
			return true;
		}
	}
	
	
	public abstract void populateBoard();
	public abstract int nearestNeighbour(Cell celle);
	public abstract void nextGeneration();
	
}
