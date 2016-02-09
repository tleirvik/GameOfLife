package GameOfLife;
/**
 * 
 * @author stianreistadrogeberg
 * 
 * Implementer getter setter + variabel for om brettet er tomt (alle celler er false eller 0). 9/2/16
 */
public abstract class GameOfLife {

	private int state;
	private int currentState;
        private boolean isBoardEmpty; // er false hvis alle celler på brettet er døde.
        
        public GameOfLife(int state, int curState) {
            this.state = state;
            currentState = curState;
            isBoardEmpty = false; // strengt tatt ikke nødvendig (?)
        }
        
        public void setIsBoardEmpty(boolean isEmpty) {
            isBoardEmpty = isEmpty;
        }
        
        public boolean getIsBoardEmpty() {
            return isBoardEmpty;
        }

	public abstract void populateBoard();
	public int seedGeneration() {

		return 0;
	}
        
	public abstract void nextGeneration();
	public abstract int countNeighbours();
}
