package model;

public abstract class GameOfLife {

	private int state;
	private int currentState;

	public abstract void populateBoard();
	public int seedGeneration() {

		return 0;
	}
	public abstract void nextGeneration();
	public abstract int countNeighbours();
}
