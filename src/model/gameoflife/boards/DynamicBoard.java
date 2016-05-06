package model.gameoflife.boards;

import model.gameoflife.MetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * This is our implementation of an expanding and shrinking dynamic
 * game board for Game Of Life
 *
 * @see Board
 * @see model.gameoflife.boards.Board.BoardType
 * @see FixedBoard
 * @see model.gameoflife.algorithms.Algorithm
 * @see MetaData
 */
public class DynamicBoard extends Board { 
    private final MetaData metadata;
    private final List<List<Byte>> currentGeneration;
    private final List<List<Byte>> firstGeneration;
    private final int MIN_ROW;
    private final int MIN_COL;

    //=========================================================================
    // Constructors
    //=========================================================================

    /**
     * Constructs a dynamic board with a specific size and an empty MetaData object
     *
     * @param rows Number of rows to create
     * @param columns Number of columns to create
     */
    public DynamicBoard(int rows, int columns) {
        MIN_COL = columns;
        MIN_ROW = rows;
        metadata = new MetaData();
        currentGeneration = new ArrayList<>();
        firstGeneration = new ArrayList<>();
        
        for (int row = 0; row < rows; row++) {
            currentGeneration.add(new ArrayList<>());
            firstGeneration.add(new ArrayList<>());
            for (int col = 0; col < columns; col++) {
                currentGeneration.get(row).add((byte) 0);
                firstGeneration.get(row).add((byte) 0);
            }
        }
    }

    /**
     * Constructs a dynamic board from a <code>byte</code> 2D array
     *
     * @param board A <code>byte</code> 2D array to create a dynamic board from
     * @param metadata The boards meta data
     */
    public DynamicBoard(byte[][] board, MetaData metadata) {
        MIN_COL = board.length;
        MIN_ROW = board[0].length;
        this.metadata = metadata;
        currentGeneration = new ArrayList<>();
        firstGeneration = new ArrayList<>();
        
        for (int row = 0; row < board.length; row++) {
            currentGeneration.add(new ArrayList<>());
            firstGeneration.add(new ArrayList<>());
            for (int col = 0; col < board[0].length; col++) {
                currentGeneration.get(row).add(board[row][col]);
                firstGeneration.get(row).add(board[row][col]);
            }
        }
    }
    
    //=========================================================================
    // Getters
    //=========================================================================

    /**
     * Returns the boards rows
     *
     * @return Number of rows
     */
    @Override
    public int getRows() {
        return currentGeneration.size();
    }

    /**
     * Returns the boards columns
     *
     * @return Number of columns
     */
    @Override
    public int getColumns() {
        return currentGeneration.get(0).size();
    }

    /**
     * Returns the meta data object
     *
     * @return The meta data object
     * @see MetaData
     */
    @Override
    public MetaData getMetaData() {
        return metadata;
    }
    
    /**
     * Returns the <code>byte</code> value of the cell at the given position
     *
     * @param row The specified row
     * @param column The specified column
     * @return Returns the <code>byte</code> value of a cell on the
     * given position
     *
     */
    @Override
    public byte getCellAliveState(int row, int column) {
        if (row > getRows() - 1 || row < 0 || column > getColumns() - 1 || column < 0) {
            return (byte)0;
        } else {
            return currentGeneration.get(row).get(column);
        }
    }

    /**
     * This method checks if a given position is inside the array. If not, the array will
     * be expanded to fit the given positions.
     * @param row The specified row position
     * @param column The specified column position
     * @return true if the specified positions is inside the array, else false
     */
    private boolean insideArrayBoundaries(int row, int column) {
        if (row >= getRows() || row < 0 || column >= getColumns() || column < 0) {

            if(row < 0) {
                addTopRow(Math.abs(row));
            } else if(row >= getRows()) {
                addBottomRow(row - (getRows() - 1));
            }

            if(column < 0) {
                addLeftColumn(Math.abs(column));
            } else if(column >= getColumns()) {
                addRightColumn(column - (getColumns() - 1));
            }
            return false;
        } else {
            return true;
        }
    }
    //=========================================================================
    // Setters
    //=========================================================================
    
    /**
     *  Sets the <code>byte</code> value of the cell at the given position
     * to the <code>byte</code> value given in aliveState.
     * 
     * @param row The specified row position
     * @param column The specified column position
     * @param aliveState <code>0</code> or <code>1</code> to represent dead or alive cell
     */
    @Override
    public void setCellAliveState(int row, int column, byte aliveState) {
        if (!insideArrayBoundaries(row, column)) {
            if(row > getRows()) {
                row = getRows()-1;
            } else if(row < 0) {
                row = 0;
            }

            if(column > getColumns()) {
                column = getColumns()-1;
            } else if(column < 0) {
                column = 0;
            }
            currentGeneration.get(row).set(column, aliveState);
        } else {
            currentGeneration.get(row).set(column, aliveState);
        }
    }
    
    //=========================================================================
    // Generation-methods
    //=========================================================================

    /**
     * This method resets the current game board to the first generation board.
     */
    @Override
    public void resetBoard() {
        currentGeneration.clear();
        
        for (int row = 0; row < firstGeneration.size(); row++) {
            currentGeneration.add(new ArrayList<>());
            for (int col = 0; col < firstGeneration.get(0).size(); col++) {
                currentGeneration.get(row).add(col, firstGeneration.get(row).get(col));
            }
        }
    }

    /**
     * This method checks all the edges on the board
     */
    private void checkEdges() {
        checkTop();
        checkBottom();
        checkRight();
        checkLeft();
    }

    /**
     * Adds a top row to the game board
     * @param numberOfRows Number of rows to add
     */
    private void addTopRow(int numberOfRows) {
        for (int i = 0; i < numberOfRows; i++) {
            currentGeneration.add(0, new ArrayList<>());
            for (int col = 0; col < currentGeneration.get(currentGeneration.size() - 1).size(); col++) {
                currentGeneration.get(0).add((byte) 0);
            }
        }
    }

    /**
     * Adds a bottom row to the board
     * @param numberOfRows Number of rows to add
     */
    private void addBottomRow(int numberOfRows) {
            for (int i = 0; i < numberOfRows; i++) {
                currentGeneration.add(new ArrayList<>());
                for (int col = 0; col < currentGeneration.get(0).size(); col++) {
                    currentGeneration.get(currentGeneration.size() - 1).add((byte) 0);
            }
        }
    }

    /**
     *  Removes a top row from the game board
     * @param numberOfRows Number of rows to remove
     */
    private void removeTopRow(int numberOfRows) {
        for (int i = 0; i < numberOfRows; i++) {
            currentGeneration.remove(0);
        }
    }
    /**
     *  Removes a bottom row from the game board
     * @param numberOfRows Number of rows to remove
     */
    private void removeBottomRow(int numberOfRows) {
        for (int i = 0; i < numberOfRows; i++) {
            currentGeneration.remove(currentGeneration.size() - 1);
        }
    }

    /**
     * Adds a left column to the game
     * @param numberOfColumns Number of columns to add
     */
    private void addLeftColumn(int numberOfColumns) {
            for (int i = 0; i < numberOfColumns; i++) {
                currentGeneration.stream().forEach((col) -> col.add(0, (byte) 0));
        }
    }
    /**
     * Adds a right column to the game
     * @param numberOfColumns Number of columns to add
     */
    private void addRightColumn(int numberOfColumns) {
        for (int i = 0; i < numberOfColumns; i++) {
            currentGeneration.stream().forEach((col) -> col.add((byte) 0));
        }
    }
    /**
     * Removes a left column from the game
     * @param numberOfColumns Number of columns to remove
     */
    private void removeLeftColumn(int numberOfColumns) {
        for (int i = 0; i < numberOfColumns; i++) {
            for (List<Byte> row : currentGeneration) {
                row.remove(0);
            }
        }
    }
    /**
     * Removes a right column from the game
     * @param numberOfColumns Number of columns to remove
     */
    private void removeRightColumn(int numberOfColumns) {
        for (int i = 0; i < numberOfColumns; i++) {
            for (List<Byte> row : currentGeneration) {
                row.remove(currentGeneration.get(0).size() - 1);
            }
        }
    }

    /**
     * This method checks the top rows and will add or remove rows
     * dependant on the cell activity near the top of the game board
     */
    private void checkTop() {
        final int sum1 = currentGeneration
                .get(0)
                .stream()
                .mapToInt(w -> Integer.parseInt(w.toString()))
                .sum();
        final int sum2 = currentGeneration
                .get(1)
                .stream()
                .mapToInt(w -> Integer.parseInt(w.toString()))
                .sum();
        final int sum3 = currentGeneration
                .get(2)
                .stream()
                .mapToInt(w -> Integer.parseInt(w.toString()))
                .sum();
        final int sum4 = currentGeneration
                .get(3)
                .stream()
                .mapToInt(w -> Integer.parseInt(w.toString()))
                .sum();
        final int sum5 = currentGeneration
                .get(4)
                .stream()
                .mapToInt(w -> Integer.parseInt(w.toString()))
                .sum();
        final int remove = sum1 + sum2 + sum3 + sum4 + sum5;
        final int add = sum1 + sum2;

        if (add != 0) {
            addTopRow(1);
        } else if (remove == 0 && currentGeneration.size() > MIN_ROW) {
            removeTopRow(1);
        }
    }

    /**
     * This method checks the bottom rows and will add or remove rows
     * dependant on the cell activity near the bottom of the game board
     */
    private void checkBottom() {
        final int rows = currentGeneration.size();
        final int sum1 = currentGeneration
                .get(rows -1)
                .stream()
                .mapToInt(w -> Integer.parseInt(w.toString()))
                .sum();
        final int sum2 = currentGeneration
                .get(rows -2)
                .stream()
                .mapToInt(w -> Integer.parseInt(w.toString()))
                .sum();
        final int sum3 = currentGeneration
                .get(rows -3)
                .stream()
                .mapToInt(w -> Integer.parseInt(w.toString()))
                .sum();
        final int sum4 = currentGeneration
                .get(rows -4)
                .stream()
                .mapToInt(w -> Integer.parseInt(w.toString()))
                .sum();
        final int sum5 = currentGeneration
                .get(rows -5)
                .stream()
                .mapToInt(w -> Integer.parseInt(w.toString()))
                .sum();
        final int remove = sum1 + sum2 + sum3 + sum4 + sum5;
        final int add = sum1 + sum2;

        if (add != 0) {
            addBottomRow(1);
        } else if(remove == 0 && currentGeneration.size() > MIN_ROW) {
            removeBottomRow(1);
        }
    }

    /**
     * This method checks the left column and will add or remove columns
     * dependant on the cell activity near the leftmost part the game board
     */
    private void checkLeft() {
        final int rows = currentGeneration.size();
        int sum1 = 0;
        int sum2 = 0;
        int sum3 = 0;
        int sum4 = 0;
        int sum5 = 0;

        for (int row = 0; row < rows; row++) {
            sum1 += currentGeneration.get(row).get(0);
            sum2 += currentGeneration.get(row).get(1);
            sum3 += currentGeneration.get(row).get(2);
            sum4 += currentGeneration.get(row).get(3);
            sum5 += currentGeneration.get(row).get(4);
        }
        final int remove = sum1 + sum2 + sum3 +sum4 + sum5;
        final int add = sum1 + sum2;

        if (add != 0) {
           addLeftColumn(1);
        } else if(remove == 0 && currentGeneration.get(0).size() > MIN_COL){
            removeLeftColumn(1);
        }
    }

    /**
     * This method checks the right column and will add or remove columns
     * dependant on the cell activity near the rightmost part the game board
     */
    private void checkRight() {
        int sum1 = 0;
        int sum2 = 0;
        int sum3 = 0;
        int sum4 = 0;
        int sum5 = 0;
        final int columns = currentGeneration.get(0).size();

        for (List<Byte> e : currentGeneration) {
            sum1 += e.get(columns - 1);
            sum2 += e.get(columns - 2);
            sum3 += e.get(columns - 3);
            sum4 += e.get(columns - 4);
            sum5 += e.get(columns - 5);
        }
        final int remove = sum1 + sum2 + sum3 + sum4 + sum5;
        final int add = sum1 + sum2;

        if (add != 0) {
            addRightColumn(1);
        } else if (remove == 0 && currentGeneration.get(0).size() > MIN_COL){
            removeRightColumn(1);
        }
    }

    /**
     * This method is inherited from {@link Board} and is used the check if we
     * need to expand the edges before running the algorithm
     */
    @Override
    public void beforeUpdate() {
        checkEdges();
    }

    /**
     * Sets the first generation board. Used with {@link #resetBoard()}
     */
    @Override
    public void setFirstGeneration() {
        firstGeneration.clear();
        for (int row = 0; row < currentGeneration.size() -1; row++) {
            firstGeneration.add(new ArrayList<>());
            for (int col = 0; col < currentGeneration.get(0).size() -1; col++) {
                firstGeneration.get(row).add(col, currentGeneration.get(row).get(col));
            }
        }
    }

    //=========================================================================
    // Misc.
    //=========================================================================

    /**
     * This method is inherited from {@link Object} and is used to clone a dynamic
     * game board with its meta data
     * @return A clone of this class' game board
     */
    @Override
    public DynamicBoard clone() {
        byte[][] boardClone = new byte[getRows()][getColumns()];
        MetaData metaDataClone = metadata.clone();
        for (int row = 0; row < currentGeneration.size(); row++) {
            for (int col = 0; col < currentGeneration.get(0).size(); col++) {
                boardClone[row][col] = currentGeneration.get(row).get(col);
            }
        }
        return new DynamicBoard(boardClone, metaDataClone);
    }

    /**
     * Method that gives the smallest array to fit a specified pattern
     * @return Returns an array of minrow maxrow mincolumn maxcolumn
     *
     * @author Henrik Lieng
     */
    public int[] getBoundingBox() {
        int[] boundingBox = new int[4];
        boundingBox[0] = currentGeneration.size();
        boundingBox[1] = 0;
        boundingBox[2] = currentGeneration.get(0).size();
        boundingBox[3] = 0;
        
        for (int i = 0; i < currentGeneration.size(); i++) {
            for (int j = 0; j < currentGeneration.get(i).size(); j++) {
                if ((currentGeneration.get(i).get(j) == 0)) {
                    continue;
                }
                if (i < boundingBox[0]) {
                    boundingBox[0] = i;
                }
                if (i > boundingBox[1]) {
                    boundingBox[1] = i;
                }
                if (j < boundingBox[2]) {
                    boundingBox[2] = j;
                }
                if (j > boundingBox[3]) {
                    boundingBox[3] = j;
                }
            }
        }
        return boundingBox;
    }
    
    /**
     *  Method that returns the game board as a String. Used for Unit Testing with JUnit 4
     *
     * @return String The contents of the game board as a String
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < currentGeneration.size(); i++) {
            for (int j = 0; j < currentGeneration.get(0).size(); j++) {
                sb.append(currentGeneration.get(i).get(j));
            }
        }
        return sb.toString();
    }
}
