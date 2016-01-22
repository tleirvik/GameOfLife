package game;

import javafx.scene.paint.Color;

public class StaticBoard {

	private int columns;
	private int rows;
	
	public Cell[][] boardGrid = null;
	
	public StaticBoard(int columns, int rows) {
		this.columns = columns;
		this.rows = rows;
		
		boardGrid = new Cell[this.rows][this.columns];
	}
	
	public void populateBoard(){
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < columns; j++) {
				boardGrid[i][j] = new Cell(j,i,GameLogic.seedGenerator(),Color.BLACK);
			}
		}
		//For � lage f�rste generasjon s� m� alle de 4 reglene iverksettes for alle cellene
		//Kj�r derfor nextGeneration() her f�r denne metoden avsluttes
		
	}
	
	public int nearestNeighbour(Cell celle) {
		int count = 0;
		
		int xPos, yPos;
		
		//Et array for hver offset-verdi n�dvendig for � finne de 8 cellene omkring en celle
		int[][] position = { {0,-1}, {-1,-1}, {-1,0}, {-1,+1}, {0,+1}, {+1,+1}, {+1,0}, {+1,-1} };
		
		//position.length = 8 fordi det bare er 8 celler rundt
		for(int i = 0; i < position.length; i++) {
			xPos = celle.x+position[i][0];
			yPos = celle.y+position[i][1];
			
			//Hindrer verdier under 0 og over gridst�rrelsen, som gir Arrays Out Of Bounds Exception
			if(xPos > -1 && yPos > -1 && xPos < boardGrid.length && yPos < boardGrid.length) {
				if(boardGrid[yPos][xPos].isAlive) {
				count++;
				}	
			}
		}
		return count;
	}
	
	public void nextGeneration(){
		Cell[][] tempBoardGrid = new Cell[rows][columns];
		
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < columns; j++) {
				
				//Kopier cellen til et midlertidig array
				tempBoardGrid[i][j] = boardGrid[i][j];
				
				//Hvis cellen lever, kj�r de f�rste 3 reglene
				if(boardGrid[i][j].isAlive) {
					
					//Any live cell with fewer than two live neighbours dies, as if by needs caused by underpopulation.
					if(this.nearestNeighbour(boardGrid[i][j]) < 2) {
						tempBoardGrid[i][j].isAlive = false;
					} else
					
					//Any live cell with more than three live neighbours dies, as if by overcrowding.
					if(this.nearestNeighbour(boardGrid[i][j]) > 3) {
						tempBoardGrid[i][j].isAlive = false;
					}
					
					//Any live cell with two or three live neighbours lives, unchanged, to the next generation.
					
				} else {
					//Any dead cell with exactly three live neighbours will come to life.
					if(this.nearestNeighbour(boardGrid[i][j]) == 3) {
						tempBoardGrid[i][j].isAlive = true;
					}
					
				}
			}
		}
		
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < columns; j++) {
				boardGrid[i][j] = tempBoardGrid[i][j];
			}
		}
	}
	
	
}
