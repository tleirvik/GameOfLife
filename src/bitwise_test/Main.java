package bitwise_test;

import game.Cell;
import game.StaticBoard;
import java.util.BitSet;
import java.util.Random;

public class Main {

	public static void main(String[] args) {
		
		//Lag brett
		byte[][] board = new byte[10][10];
		
		//Fyll brett med celler
		game_Logic.populateBoard(board);
		
		for(int r = 0; r < 10; r++) {
			for(int c = 0; c < 10; c++) {
				if(bitWise_Logic.getIsAlive(r, c, board)) {
					System.out.print("O ");
				} else {
					/* Cell is dead */
					System.out.print("X ");
				}		
			}
			// Start ny rad
			System.out.println("");
		}	
		//Mellomrom mellom hver grid
		System.out.println("");
		System.out.println("");
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while(true) {
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//Hent brettets neste generasjon
			game_Logic.nextGeneration(board);
			
			
			for(int r = 0; r < 10; r++) {
				for(int c = 0; c < 10; c++) {
					if(bitWise_Logic.getIsAlive(r, c, board)) {
						System.out.print("O ");
					} else {
						/* Cell is dead */
						System.out.print("X ");
					}		
				}
				// Start ny rad
				System.out.println("");
			}	
			//Mellomrom mellom hver grid
			System.out.println("");
			System.out.println("");
		}	
	}
}

class game_Logic {
	
		static void nextGeneration(byte[][] grid) {
			
				//Debug-kommentarer er kommentert ut
				
				byte[][] tempBoardGrid = new byte[grid.length][grid.length];
				
				for(int i = 0; i < grid.length; i++) {
					for(int j = 0; j < grid.length; j++) {
						
						//Kopier cellen til et midlertidig array
				tempBoardGrid[i][j] = grid[i][j];
		
				//Dead and has exactly 3 neighbours
				if(!bitWise_Logic.getIsAlive(i, j, tempBoardGrid) && bitWise_Logic.nearestNeighbour(i, j, grid) == 3) {
					/*System.out.println("Celle på row " + i + " column " + j + " er død og har 3 naboer");*/
					bitWise_Logic.setIsAlive(i, j, tempBoardGrid, true);
				}
		
				//Alive and has less than 2 neighbours
				if(bitWise_Logic.getIsAlive(i, j, tempBoardGrid) && bitWise_Logic.nearestNeighbour(i, j, grid) < 2) {
					/*System.out.println("Celle på row " + i + " column " + j + " lever og har mindre enn 2 naboer naboer");*/
					bitWise_Logic.setIsAlive(i, j, tempBoardGrid, false);
				}
		
				//Alive and has exactly 2 or 3 neighbours
				if(bitWise_Logic.getIsAlive(i, j, tempBoardGrid) && bitWise_Logic.nearestNeighbour(i, j, grid) == 2 || bitWise_Logic.nearestNeighbour(i, j, grid) == 3) {
					/*System.out.println("Celle på row " + i + " column " + j + " lever og har eksakt 2 eller 3 naboer");*/
					bitWise_Logic.setIsAlive(i, j, tempBoardGrid, true);
				}
				
				//Alive and has more than 3 neighbours
				if(bitWise_Logic.getIsAlive(i, j, tempBoardGrid) && bitWise_Logic.nearestNeighbour(i, j, grid) > 3) {
					/*System.out.println("Celle på row " + i + " column " + j + " lever og har mer enn 3 naboer");*/
					bitWise_Logic.setIsAlive(i, j, tempBoardGrid, false);
				}
				
			}
		}
		for(int i = 0; i < grid.length; i++) {
			for(int j = 0; j < grid.length; j++) {
				grid[i][j] = tempBoardGrid[i][j];
			}
		}
	}
	
	static void populateBoard(byte[][] grid) {
		
		//Lag en glider
		int length = grid[0].length;
		
		bitWise_Logic.setIsAlive(/*row*/length-3, /*column*/length-2, grid, true);	//7, 8
		bitWise_Logic.setIsAlive(/*row*/length-4, /*column*/length-3, grid, true);	//6, 7
		bitWise_Logic.setIsAlive(/*row*/length-4, /*column*/length-4, grid, true);	//6, 6
		bitWise_Logic.setIsAlive(/*row*/length-3, /*column*/length-4, grid, true);	//7, 6
		bitWise_Logic.setIsAlive(/*row*/length-2, /*column*/length-4, grid, true);	//8, 6
		
		/*
		Random rand = new Random();
		for(int r = 0; r < 10; r++) {
			for(int c = 0; c < 10; c++) {
				if(rand.nextInt(5) != 1) {
					bitWise_Logic.setIsAlive(r, c, grid, false);
				} else {
					bitWise_Logic.setIsAlive(r, c, grid, true);
				}
			}
		}
		*/
	}
}

class bitWise_Logic {
	
	static void setIsAlive(int row, int column, byte[][] grid, boolean isAlive) {
		
		//Sett bit i første posisjon til 0 eller 1
		int pos = 0;
		
		if(isAlive) {
			grid[row][column] = (byte) (grid[row][column] | (1 << pos));
		} else {
			grid[row][column] = (byte) (grid[row][column] & (0 << pos));
		}
	}
	
	public static int nearestNeighbour(int row, int column, byte[][] grid) {
		int count = 0;
		
		//Debug-kommentarer er kommentert ut
		/*System.out.println("Sjekker: Row " + row + ". Column " + column );*/
		
		int[][] position = {
				{ -1,-1 }, { -1,0 }, { -1,+1 },
				
				{  0,-1 }, /* 0,0 */ {  0,+1 },
				
				{ +1,-1 }, { +1,0 }, { +1,+1 }
				};
		
		for(int i = 0; i < position.length; i++) {
			
			int xPos, yPos;
			
			yPos = row		+	position[i][0];
			xPos = column	+	position[i][1];
			
			//TEST OM CELLEN LEVER
			if(xPos >= 0 && xPos < grid.length && yPos >= 0 && yPos < grid.length) {
				if(bitWise_Logic.getIsAlive(yPos, xPos, grid)) {
					count++;
					
				} /*else {System.out.println(yPos + " Lever ikke " + xPos);}*/
			}
		}
		System.out.println("Antall levende celler rundt = " + count);
		return count;
	}
	static boolean getIsAlive(int row, int column, byte[][] grid) {
				
				//Finn bit-verdien 0 eller 1
				int alive = (grid[row][column] >> 0/* Posisjonen som man vil hente bit-en fra */) & 1;
				
				//returner svaret på denne "spørringen"
				return (alive == 1);
	}
}