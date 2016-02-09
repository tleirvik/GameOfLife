package GameOfLife;

import javafx.scene.paint.Color;
/**
 *
 *
 *
 */
public class Cell {

	private boolean isAlive;
	private Color color;

	public Cell() {
		isAlive = false;
	}
	
	/**
	 *
	 * @return
	 */
	public boolean getIsAlive() {
		return true;
	}
	/**
	 *
	 */
	public void setIsAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}
	/**
	 *
	 * @return
	 */
	public Color getColor() {
		return Color.BLACK;
	}
	/**
	 *
	 */
	@Override
	public String toString() {
		return "A";
	}

}

