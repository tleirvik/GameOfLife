package game;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class StaticBoard extends Board{

	private int rows;
	private int columns;
	
	private Cell[][] boardGrid = null;
	
	public StaticBoard(int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
		
		boardGrid = new Cell[this.rows][this.columns];
	}

	public Cell[][] getBoardGrid() {
		
		return this.boardGrid;
		
	}
	
	@Override
	public void populateBoard(){
		
		boolean[][] seed = this.seedGenerator(rows, columns);
		
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < columns; j++) {
				getBoardGrid()[i][j] = new Cell(j,i,seed[i][j],Color.BLACK);
			}
		}
		//For � lage f�rste generasjon s� m� alle de 4 reglene iverksettes for alle cellene
		//Kj�r derfor nextGeneration() her f�r denne metoden avsluttes
		
	}
	@Override
	public int nearestNeighbour(Cell cell) {
		int count = 0;
		
		int xPos, yPos;
		
		//Et array for hver offset-verdi n�dvendig for � finne de 8 cellene omkring en celle
		int[][] position = { {0,-1}, {-1,-1}, {-1,0}, {-1,+1}, {0,+1}, {+1,+1}, {+1,0}, {+1,-1} };
		
		//position.length = 8 fordi det bare er 8 celler rundt
		for(int i = 0; i < position.length; i++) {

			xPos = cell.getXpos()+position[i][0];
			yPos = cell.getYpos()+position[i][1];

			xPos = cell.getXpos() + position[i][0];
			yPos = cell.getYpos() + position[i][1];

			System.out.println(xPos + " " + yPos);
			
			//TEST OM CELLEN LEVER
			if(xPos >= 0 && xPos < this.rows && yPos >= 0 && yPos < this.columns) {
				if(this.boardGrid[yPos][xPos].getIsAlive()) {
					count++;
				}
			}
		}
		return count;
	}
	
	@Override
	public void nextGeneration(){
		Cell[][] tempBoardGrid = new Cell[rows][columns];
		
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < columns; j++) {
				
				//Kopier cellen til et midlertidig array
				tempBoardGrid[i][j] = this.boardGrid[i][j];

				//Dead and has exactly 3 neighbours
				if(!tempBoardGrid[i][j].getIsAlive() && nearestNeighbour(tempBoardGrid[i][j]) == 3) {
					tempBoardGrid[i][j].setIsAlive(true);
				}

				//Alive and has less than 2 neighbours
				if(tempBoardGrid[i][j].getIsAlive() && nearestNeighbour(tempBoardGrid[i][j]) < 2) {
					tempBoardGrid[i][j].setIsAlive(false);
				}

				//Alive and has exactly 2 or 3 neighbours
				if(tempBoardGrid[i][j].getIsAlive() && nearestNeighbour(tempBoardGrid[i][j]) == 2 && nearestNeighbour(tempBoardGrid[i][j]) == 3) {
					tempBoardGrid[i][j].setIsAlive(true);
				}
				
				//Alive and has more than 3 neighbours
				if(tempBoardGrid[i][j].getIsAlive() && nearestNeighbour(tempBoardGrid[i][j]) > 3) {
					tempBoardGrid[i][j].setIsAlive(false);
				}
				
			}
		}
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < columns; j++) {
				this.boardGrid[i][j] = tempBoardGrid[i][j];
			}
		}
	}
}