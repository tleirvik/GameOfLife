package GameOfLife;

import javafx.scene.paint.Color;


/**
 *
 * The Cell class.
 *
 */
public class Cell {

	private boolean isAlive;
	private Color color;


	/**
	 *
	 * Constructor : sets isAlive to false;
	 *
	 */
	public Cell() {
		isAlive = false;
	}

	/**
	 *
	 * @return boolean isAlive
	 */
	public boolean getIsAlive() {
		return this.isAlive;
	}
	/**
	 *
	 */
	public void setIsAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}
	/**
	 *
	 * @return Color color
	 */
	public Color getColor() {
		return this.color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	/**
	 * Har laget en enkel toString() . Denne bør bygges ut.
	 */
	@Override
	public String toString() {
		return "Cell status: " + this.getIsAlive() + " Color: " + this.getColor();
	}

}

