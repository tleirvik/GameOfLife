package Game;

import javafx.scene.paint.Color;

public class Board {

	private int columns;
	private int rows;
	
	public Cell[][] boardGrid = null;
	
	public Board(int columns, int rows) {
		this.columns = columns;
		this.rows = rows;
		
		boardGrid = new Cell[this.columns][this.rows];
	}
	
	public void populateBoard(){
		for(int i = 0; i < columns; i++) {
			for(int j = 0; j < rows; j++) {
				boardGrid[i][j] = new Cell(i,j,GameLogic.seedGenerator(),Color.BLACK);
			}
		}
	}
}
