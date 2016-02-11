/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameOfLife;

/**
 *
 * @author stianreistadrogeberg
 */
public class GameOfLife3D extends GameOfLife {

    public GameOfLife3D(int state, int curState) {
        // super(state, curState);
    }

    @Override
    public void populateBoard() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void nextGeneration() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int countNeighbours() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    /**
     *  Too be implemented
     */
	@Override
	public void setIsBoardEmpty(boolean boardEmpty) {
		// TODO Auto-generated method stub
	}
	/**
	 *  Too be implemented
	 */
	@Override
	public boolean getIsBoardEmpty() {
		// TODO Auto-generated method stub
		return false;
	}
	/**
	 *  Too be implemented
	 */
	@Override
	public boolean[][] convertBoardToBoolean() {
		// TODO Auto-generated method stub
		return null;
	}
}
