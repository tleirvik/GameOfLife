package game;

import javafx.scene.paint.Color;

public class Cell {
	
	static private int numberOfCellsAlive = 0;
	
	private int x, y;
	private boolean isAlive;
	//private Color color; //Vurder å slette, fjernet fra UML
	
	public Cell (int x,int y,boolean isAlive,Color color) {
		this.x			= x;
		this.y			= y;
		this.isAlive	= isAlive;
		//this.color		= color; //Vurder å slette, fjernet fra UML
	}
	
	public boolean getIsAlive() {
		return isAlive;
	}
	
	public void setIsAlive(boolean isAlive) {
		if(isAlive = true) {
			isAlive = true;
			numberOfCellsAlive++;
		} else {
			isAlive = false;
			numberOfCellsAlive--;
		}
	}
	
	/* Vurder å slette, fjernet fra UML
	public Color getColor() {
		return color;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	*/
	
	public int getNumberOfCellsAlive() {
		return numberOfCellsAlive;
	}
}
