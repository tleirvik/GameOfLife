/**
 * 
 */
package game;

import java.util.Random;

public abstract class Board {
	
	
	//Returnerer et et array med boolske verdier (bit 0 1) like langt som antall celler i et brett
	//Eksempel: 10x10 grid = 100 celler
	//Tidligere kalt en gang for hver celle via populateBoard()
	boolean[] seedGenerator(int numberOfCells) {
		
		boolean[] seed = new boolean[numberOfCells];
		
		Random rand = new Random();
		
		for(int i = 0; i < seed.length; i++) {
			if(rand.nextInt(2) == 1) {
				seed[i] = true;
			} else {
				seed[i] = false;
			}
		}
		
		return seed;
		
	}
	
	public abstract void populateBoard();
	public abstract int nearestNeighbour(Cell cell);
	public abstract void nextGeneration();
	
}
