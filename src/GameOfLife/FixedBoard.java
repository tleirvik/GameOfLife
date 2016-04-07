package GameOfLife;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Robin Sean Aron David Lundh, Terje Leirvik, Stian Reistad Røgeberg.
 * 
 * This class contains the gameboard in a fixed size that cannot be changed
 * after creation. It also contains the algorithm for Game Of Life specifically
 * made for a board of a fixed size.
 * 
 */
public class FixedBoard {
    private final MetaData metadata;
    private final byte[][] currentGeneration;
    private final byte[][] firstGeneration;
    // private final byte[][] previousGeneration;
    private int[] statsArray = new int[20];
    private int[] diffArray = new int[20];
    private int[] distanceArray = new int[20];
    int counter = 0;
    int previousValue = 0;
    
    /**
     * Constructs a board of a fixed size.
     *
     * @param rows The number of rows
     * @param columns The number of columns
     */
    public FixedBoard(int rows, int columns) {
        metadata = new MetaData();
    	currentGeneration = new byte[rows + 2][columns + 2];
        firstGeneration = new byte[rows + 2][columns + 2];
    }

    /**
     * Constructs a boad with the given two-dimensional <code>byte</code>-array and
     * adds the metadata.
     * 
     * @param board The two-dimensional <code>byte</code>-array whose data
     * are to be placed in the FixedBoard.
     * @param metadata The <code>MetaData</code> object that contains the metadata
     * related to the new board.
     * 
     * @see MetaData
     */
    public FixedBoard(byte[][] board, MetaData metadata) {
    	this.metadata = metadata;
    	currentGeneration = new byte[board.length][board[0].length];
        firstGeneration = new byte[board.length][board[0].length];

    	for(int row = 1; row < board.length - 1; row++) {
            for(int col = 1; col < board[0].length - 1; col++) {
                currentGeneration[row][col] = board[row][col];
                firstGeneration[row][col] = board[row][col];
            }
    	}
    }

    /**
     * 
     * @return The number of rows
     */
    public int getRows() {
        return currentGeneration.length;
    }

    /**
     *
     * @return The number of columns
     */
    public int getColumns() {
        return currentGeneration[0].length;
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

    /**
     * 
     * @return The board's current generation
     */
    public byte[][] getBoardReference() {
    	return currentGeneration;
    }
    
    public void resetBoard() {
        for(int row = 1; row < firstGeneration.length - 1; row++) {
            System.arraycopy(firstGeneration[row], 1, 
                    currentGeneration[row], 1, firstGeneration[0].length - 1 - 1);
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
        return currentGeneration[row][column];
    }

    /**
     * Sets the <code>byte</code> value of the cell at the given position
     * to the <code>byte</code> value given in aliveState. Throws a Runtime
     * Exception if the number does not equal 0 or 1.
     *
     * @param row
     * @param column
     * @param aliveState sets the <code>byte</code> value of a cell on the
     * given position
     */
    public void setCellAliveState(int row, int column, byte aliveState) {
        if(aliveState == 0 || aliveState == 1) {
            currentGeneration[row][column] = aliveState;
        } else {
            throw new RuntimeException("Invalid number in cell state: " + aliveState);
        }
    }
    
    public void nextGeneration() {
        byte[][] neighbourArray = new byte[currentGeneration.length][currentGeneration[0].length];
        
        for(int row = 1; row < currentGeneration.length-1; row++) {
            for(int col = 1; col < currentGeneration[0].length-1; col++) {
                if(currentGeneration[row][col] == 1) {
                    neighbourArray[row-1][col-1]++;
                    neighbourArray[row-1][col]++;
                    neighbourArray[row-1][col+1]++; 
                    neighbourArray[row][col-1]++;
                    neighbourArray[row][col+1]++;
                    neighbourArray[row+1][col-1]++;
                    neighbourArray[row+1][col]++;
                    neighbourArray[row+1][col+1]++;
                }
            }
        }
        
        for(int row = 1; row < currentGeneration.length-1; row++) {
            for(int col = 1; col < currentGeneration[0].length-1; col++) {
                currentGeneration[row][col] = ((neighbourArray[row][col]== 3) || (currentGeneration[row][col] == 1 && neighbourArray[row][col] == 2 )) ? (byte)1 : (byte)0;
            }
        }
    }

    public void nextGenerationWithStats(int iterations) {
        nextGeneration();
        String boardString = toString();
        String previousBoardString = "";
        final int stringCounter = countAliveCells(boardString);
        //System.out.println("cell counter" + stringCounter);
        //System.out.println("counter" + counter);
        statsArray[counter] = stringCounter;
        //System.out.println(statsArray[counter]);
        if (counter == 0) {
            diffArray[counter] = 0;
            previousValue = stringCounter;
            distanceArray[counter] = 0;
            // previousBoardString = boardString;
        } else {
            diffArray[counter] = (stringCounter - previousValue);
            distanceArray[counter] = (int)distance(boardString, previousBoardString);
            previousValue = stringCounter;
            previousBoardString = boardString;
        }

        System.out.println("distance :" + distanceArray[counter]);
        //System.out.println(distance(boardString, previousBoardString));
        counter++;
    }
    /*
    /**
     * Counts the amount of neighbours for the cell at the given position
     * 
     * @param row 
     * @param col
     * @return the amount of neighbours around the cell
     *//*
    public int countNeighbours(int row, int col) {        
        return currentGeneration[row-1][col-1] + 
                currentGeneration[row-1][col] + 
                currentGeneration[row-1][col+1] + 
                currentGeneration[row][col-1] + 
                currentGeneration[row][col+1] + 
                currentGeneration[row+1][col-1] + 
                currentGeneration[row+1][col] + 
                currentGeneration[row+1][col+1];
    }*/
    
    /*
    public void nextGeneration(int startRow, int endRow) {
        for(int row = startRow; row < endRow; row++) {
            for(int col = 0; col < currentGeneration[0].length; col++) {
                currentGeneration[row][col] = ((countNeighbours(row,col) == 3) || 
                        (currentGeneration[row][col] == 1 && 
                        countNeighbours(row,col) == 2 )) ? (byte)1 : (byte)0;
            }
        }
    }*/

    /**
     *  Method that returns the game board as a String. Used for Unit Testing with JUnit 4
     *
     * @return String The contents of the game board as a String
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < currentGeneration.length; i++) {
            for (int j = 0; j < currentGeneration[0].length; j++) {
                sb.append(currentGeneration[i][j]);
            }
        }
        return sb.toString();
    }
    public int[] getStatsArray() {
        return statsArray;
    }
    public int[] getDiffArray() {
        return diffArray;
    }
    private int countAliveCells(String boardString) {
        int stringCounter = 0;
        for (char c : boardString.toCharArray()) {
            if (c == '1') {
                ++stringCounter;
            }
        }
        return stringCounter;
    }
    // Levhenstein distance
    public double distance(String s1, String s2) {
        if (s1.equals(s2)){
            return 0;
        }

        if (s1.length() == 0) {
            return s2.length();
        }

        if (s2.length() == 0) {
            return s1.length();
        }

        // create two work vectors of integer distances
        int[] v0 = new int[s2.length() + 1];
        int[] v1 = new int[s2.length() + 1];
        int[] vtemp;

        // initialize v0 (the previous row of distances)
        // this row is A[0][i]: edit distance for an empty s
        // the distance is just the number of characters to delete from t
        for (int i = 0; i < v0.length; i++) {
            v0[i] = i;
        }

        for (int i = 0; i < s1.length(); i++) {
            // calculate v1 (current row distances) from the previous row v0
            // first element of v1 is A[i+1][0]
            //   edit distance is delete (i+1) chars from s to match empty t
            v1[0] = i + 1;

            // use formula to fill in the rest of the row
            for (int j = 0; j < s2.length(); j++) {
                int cost = (s1.charAt(i) == s2.charAt(j)) ? 0 : 1;
                v1[j + 1] = Math.min(
                        v1[j] + 1,              // Cost of insertion
                        Math.min(
                                v0[j + 1] + 1,  // Cost of remove
                                v0[j] + cost)); // Cost of substitution
            }

            // copy v1 (current row) to v0 (previous row) for next iteration
            //System.arraycopy(v1, 0, v0, 0, v0.length);

            // Flip references to current and previous row
            vtemp = v0;
            v0 = v1;
            v1 = vtemp;

        }

        return v0[s2.length()];
    }
}