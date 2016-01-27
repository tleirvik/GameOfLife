/**
 * 
 */
package game;

import java.util.Random;

public abstract class Board {
	
	
    //Returnerer et todimensjonelt array med boolske verdier for om en celle lever eller er død
    //Tidligere kalt en gang for hver celle via populateBoard()
    boolean[][] seedGenerator(int rows, int columns) {

            

        Random rand = new Random();
        boolean[][] seed = new boolean[rows][columns];

        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
            	
            	if(rand.nextInt(5) == 1) {
            		seed[i][j] = true;
            	}
            	
                
            }
        }

        return seed;

    }
	
    public abstract void populateBoard();
    public abstract int nearestNeighbour(Cell cell);
    public abstract void nextGeneration();
	
}
