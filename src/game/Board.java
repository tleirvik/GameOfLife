/**
 * 
 */
package game;

import java.util.Random;
import javafx.scene.paint.Color;

public abstract class Board {
	
	
    //Returnerer et et array med boolske verdier (bit 0 1) like langt som antall celler i et brett
    //Eksempel: 10x10 grid = 100 celler
    //Tidligere kalt en gang for hver celle via populateBoard()
    boolean[][] seedGenerator(int rows, int columns) {

            

        Random rand = new Random();
        boolean[][] seed = new boolean[rows][columns];

        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                seed[i][j] = rand.nextBoolean();
            }
        }

        return seed;

    }
	
    public abstract void populateBoard();
    public abstract int nearestNeighbour(Cell cell);
    public abstract void nextGeneration();
	
}
