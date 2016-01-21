package game;

import javafx.scene.paint.Color;

public class Cell {
	
	//GET SET isAlive dekrementerer eller inkrementerer
	//static int numberOfCellsAlive = 0;
	
	int x, y;
	public boolean isAlive;
	Color color;
	
	public Cell (int x,int y,boolean isAlive,Color color) {
		this.x			= x;
		this.y			= y;
		this.isAlive	= isAlive;
		this.color		= color;
	}
}
