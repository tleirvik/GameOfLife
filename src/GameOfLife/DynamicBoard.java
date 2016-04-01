package GameOfLife;    

import java.util.ArrayList;
import java.util.List;

/**
 * @deprecated 
 * @author Robin
 */
public class DynamicBoard { 
    private final MetaData metadata;
    private final List<List<Byte>> currentGeneration;
    private final List<List<Byte>> nextGeneration;
	
    public DynamicBoard(int rows, int columns) {
        metadata = new MetaData();
        currentGeneration = new ArrayList<>();
        nextGeneration = new ArrayList<>();
        
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                currentGeneration.add(new ArrayList<>());
                currentGeneration.get(row).add((byte)0);
                nextGeneration.add(new ArrayList<>());
                nextGeneration.get(row).add((byte)0);
            }
        }
        //System.out.println(currentGeneration.size());
    }
        
    public DynamicBoard(byte[][] board, MetaData metadata) {
    	this.metadata = metadata;
        currentGeneration = new ArrayList<>();
        nextGeneration = new ArrayList<>();
        for(int row = 0; row < board.length; row++) {
            for(int col = 0; col < board[0].length; col++) {
                currentGeneration.add(new ArrayList<>());
                currentGeneration.get(row).add(board[row][col]);
                nextGeneration.add(new ArrayList<>());
                nextGeneration.get(row).add((byte)0);
            }
        }
    }		

    public int getRows() {
        return currentGeneration.size();
    }

    public int getColumns() {
        return currentGeneration.get(0).size();
    }
    
    /**
     *  This method returns the meta data from the
     *  board.
     *
     * @return MetaData Meta data from the board contained in
     * the class
     */
    public MetaData getMetaData() {
        return metadata;
    }

    public List<List<Byte>> getBoardReference() {
        return currentGeneration;
    }
    
    /**
     * Counts the amount of neighbours for the cell at the given position
     * 
     * @param row 
     * @param col
     * @return the amount of neighbours around the cell
     */
    public int countNeighbours(int row, int col) {
        /*
        if(col > 0) {
            if(currentGeneration.get(row).get(col - 1)==1)
                neighbours++;
            if( row > 0) {
                if(currentGeneration.get(row-1).get(col - 1)==1)
                    neighbours++;
            }
            if(row < currentGeneration.size() - 1) {
                if(currentGeneration.get(row+1).get(col - 1)==1)
                    neighbours++;
            }
        }

        if(col < currentGeneration.get(0).size() - 1) {
            if(currentGeneration.get(row).get(col + 1)==1)
                neighbours++;
            if(row > 0) {
                if(currentGeneration.get(row-1).get(col + 1)==1)
                    neighbours++;
            }
            if(row < currentGeneration.size() - 1) {
                if(currentGeneration.get(row+1).get(col + 1)==1)
                    neighbours++;
            }
        }

        if(row > 0) {
            if(currentGeneration.get(row - 1).get(col)==1)
                neighbours++;
        }
        if( row < currentGeneration.size() - 1) {
            if(currentGeneration.get(row + 1).get(col)==1)
                neighbours++;
        }
        */
        return currentGeneration.get(row - 1).get(col - 1) +
                currentGeneration.get(row - 1).get(col) +
                currentGeneration.get(row - 1).get(col + 1) +
                currentGeneration.get(row).get(col - 1) +
                currentGeneration.get(row).get(col + 1) +
                currentGeneration.get(row + 1).get(col - 1) +
                currentGeneration.get(row + 1).get(col) +
                currentGeneration.get(row + 1).get(col + 1);
    }
    
    public void nextGeneration() {
        checkEdges();
        for(int row = 0; row < currentGeneration.size(); row++) {
            for(int col = 0; col < currentGeneration.get(0).size(); col++) {
                currentGeneration.get(row);
                nextGeneration.get(row).set(col,((countNeighbours(row,col) == 3) || (currentGeneration.get(row).get(col) == 1 && countNeighbours(row,col) == 2 )) ? (byte)1 : (byte)0);
            }
        }
        nextGenerationToCurrent();
    }

    public void nextGeneration(int startRow, int endRow) {
        for(int row = startRow; row < endRow; row++) {
            for(int col = 0; col < currentGeneration.get(0).size(); col++) {
                nextGeneration.get(row).set(col,((countNeighbours(row,col) == 3) || (currentGeneration.get(row).get(col) == 1 && countNeighbours(row,col) == 2 )) ? (byte)1 : (byte)0);
            }
        }
    }
    
    public void nextGenerationToCurrent() {
        for(int row = 0; row < currentGeneration.size(); row++) {
            for(int col = 0; col < currentGeneration.size(); col++) {
                currentGeneration.get(row).set(col, nextGeneration.get(row).get(col));

            }
    	}
    }
    
    /**
     * Returns the <code>byte</code> value of the cell at the given position
     *
     * @param row
     * @param column
     * @return Returns the <code>byte</code> value of a cell on the
     * given position
     */
    public byte getCellAliveState(int row, int column) {
        return currentGeneration.get(row).get(column);
    }

    /**
     *  Sets the <code>byte</code> value of the cell at the given position
     * to the <code>byte</code> value given in aliveState. Throws a Runtime
     * Exception if the number does not equal 0 or 1.
     * 
     * @param row
     * @param column
     * @param isAlive sets the <code>byte</code> value of a cell on the
     * given position
     */
    public void setCellAliveState(int row, int column, byte aliveState) {
        if(aliveState != 0 || aliveState != 1) {
            throw new RuntimeException("Invalid number in cell state");
        }
        currentGeneration.get(row).set(column, aliveState);
    }

    private void checkEdges() {
        double sum = 0;
        //Top
        for(byte d : nextGeneration.get(0)) {
            sum += d;
        }
        if(sum > 0) {
            currentGeneration.add(0, new ArrayList<>());
        }
        
        //Left
        for(byte d : nextGeneration.get(0)) {
            sum += d;
        }
        if(sum > 0) {
            for(int row = 0; row < currentGeneration.get(row).size(); row++) {
                currentGeneration.get(row).add(0, (byte)0);
            }
        }
        
        //Right
        for(byte d : nextGeneration.get(0)) {
            sum += d;
        }
        if(sum > 0) {
            for(int row = 0; row < currentGeneration.get(row).size(); row++) {
                currentGeneration.get(row).add(currentGeneration.get(row).size(), (byte)0);
            }
        }
        
        //Bottom
        for(byte d : nextGeneration.get(nextGeneration.size() - 1)) {
            sum += d;
        }
        if(sum > 0) {
            currentGeneration.add(new ArrayList<>());
        }
    }
}
