package game;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Cell {
	
	static private int numberOfCellsAlive = 0;
	
	private int x, y;
	private boolean isAlive;
	
	//private Color color; //Vurder å slette, fjernet fra UML
	
	public Cell (int x,int y,boolean isAlive,Color color) {
		this.x			= x;
		this.y			= y;
		this.isAlive	= isAlive;
		Rectangle rect = new Rectangle(20,20);
		//this.color		= color; //Vurder å slette, fjernet fra UML
		rect.setOnDragDetected(new EventHandler<MouseEvent>() {
		    public void handle(MouseEvent me) {
		        System.out.println("DRAG DETECTED");
		    }
		});
	}
	
	public int getXpos() {
		return x;
	}
	
	public int getYpos() {
		return y;
	}
	
	public boolean getIsAlive() {
		return isAlive;
	}
	
	public void setIsAlive(boolean isAlive) {
		if(isAlive) {
			this.isAlive = true;
			numberOfCellsAlive++;
		} else {
			this.isAlive = false;
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
