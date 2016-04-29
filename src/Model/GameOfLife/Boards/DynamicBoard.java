package Model.GameOfLife.Boards;

import Model.GameOfLife.MetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Robin
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
    
    @Override
    public int getRows() {
        return currentGeneration.size();
    }

    @Override
    public int getColumns() {
        return currentGeneration.get(0).size();
    }
    
    @Override
    public MetaData getMetaData() {
        return metadata;
    }
    
    /**
     * Returns the <code>byte</code> value of the cell at the given position
     *
     * @param row
     * @param column
     * @return Returns the <code>byte</code> value of a cell on the
     * given position
     */

    @Override
    public byte getCellAliveState(int row, int column) {
        if (checkArrayBoundaries(row, column)) {
            return 0;
        } else {
            return currentGeneration.get(row).get(column);
        }
            //Er det negativ row, legg på 0 posisjon
            //er det mer enn getrows, legg på siste ( .add(arraylist<>(); )
            //VIKTIG AT RAD SJEKKES FØR COLUMN
            //er det negativ column, legg til på 0 posisjon
            //er det mer enn getcolumns, legg på siste ( .add((byte) 0); )

            // Vi trenger å kalkulere hvor mange rows/cols som skal legges til
            // Lag egne add/remove metoder i dynboard
    }

    public boolean checkArrayBoundaries(int row, int column) {
        if (row > getRows() - 1 || row < 0 || column > getColumns() - 1 || column < 0) {
            if (row < 0) {
                System.out.println("Adding top rows: " + Math.abs(row));
                addRow(Math.abs(row), true);
            }
            if (row > getRows()) {
                addRow(row - getRows(), false);
            }
            if (column > getColumns() - 1) {
                System.out.println("Legger til " + (column - (getColumns() -1)));
                addColumn(column - getColumns(), false);
            }
            if (column < 0) {
                addColumn(Math.abs(column), true);
            }
            return true;
        } else {
            return false;
        }
    }
    //=========================================================================
    // Setters
    //=========================================================================
    
    /**
     *  Sets the <code>byte</code> value of the cell at the given position
     * to the <code>byte</code> value given in aliveState. Throws a Runtime
     * Exception if the number does not equal 0 or 1.
     * 
     * @param row
     * @param column
     * given position
     * @param aliveState
     */
    @Override
    public void setCellAliveState(int row, int column, byte aliveState) {
        if (checkArrayBoundaries(row, column)) {
            if (row < 0) {
                currentGeneration.get(0).set(column, aliveState);
            }
            if (column < 0) {
                currentGeneration.get(row).set(0, aliveState);
            }
        } else {
            currentGeneration.get(row).set(column, aliveState);
        }
    }
    
    //=========================================================================
    // Generation-methods
    //=========================================================================

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

    private void checkEdges() {
        checkTop();
        checkBottom();
        checkRight();
        checkLeft();
    }

    private void addRow(int numberOfRows, boolean position) {
        if (position) {
            for (int i = 0; i < numberOfRows +1; i++) {
                System.out.println("Adding top");
                currentGeneration.add(0, new ArrayList<>());
                for (int col = 0; col < currentGeneration.get(0).size(); col++) {
                    currentGeneration.get(0).add((byte) 0);
                }
            }
        } else {
            System.out.println("Adding bottom");
            for (int i = 0; i < numberOfRows +1; i++) {
                currentGeneration.add(new ArrayList<>());
                for (int col = 0; col < getColumns(); col++) {
                    currentGeneration.get(currentGeneration.size() - 1).add((byte) 0);
                }
            }
        }
    }

    private void removeRow(int numberOfRows, boolean position) {
        if (position) {
            for (int i = 0; i < numberOfRows; i++) {
                System.out.println("Removing top");
                currentGeneration.remove(0);
            }
        } else {
            System.out.println("Removing bottom");
            for (int i = 0; i < numberOfRows; i++) {
                currentGeneration.remove(currentGeneration.size() - 1);
            }
        }
    }

    private void removeColumn(int numberOfRows, boolean position) {
        if (position) {
            for (int i = 0; i < numberOfRows; i++) {
                System.out.println("Removing left");
                currentGeneration.stream().forEach((e) -> e.remove(0));
            }
        } else {
            System.out.println("Removing right");
            for (int i = 0; i < numberOfRows; i++) {
                currentGeneration.remove(currentGeneration.size() - 1);
            }
        }
    }

    private void addColumn(int numberOfColumns, boolean position) {
        if (position) {
            for (int i = 0; i < numberOfColumns +1; i++) {
                System.out.println("Adding left");
                currentGeneration.stream().forEach((e) -> {
                    e.add(0, (byte) 0);
                });
            }
        } else {
            System.out.println("Adding right");
            for (int i = 0; i < numberOfColumns +1; i++) {
                currentGeneration.stream().forEach((e) -> {
                    e.add((byte) 0);
                });
            }
        }
    }

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
            addRow(1, true);
        } else if (remove == 0 && currentGeneration.size() > MIN_ROW) {
            removeRow(1, true);
        }
    }

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
                addRow(1, false);
            } else if(remove == 0 && currentGeneration.size() > MIN_ROW) {
                removeRow(1, false);
            }
    }

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
            addColumn(1, true);
        } else if(remove == 0 && currentGeneration.get(0).size() > MIN_COL){
            removeColumn(1, true);
        }
    }

    private void checkRight() {
        System.out.println(currentGeneration.get(0).size());
        int sum1 = 0;
        int sum2 = 0;
        int sum3 = 0;
        int sum4 = 0;
        int sum5 = 0;
        final int rows = currentGeneration.size();
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
            addColumn(1, false);
        } else if (remove == 0 && currentGeneration.get(0).size() > MIN_COL){
            removeColumn(1, false);
        }
    }

    @Override
    public void beforeUpdate() {
        checkEdges();
    }

    public void countNeighbours() {
        byte[][] neighbourArray = new byte[currentGeneration.size()][currentGeneration.get(0).size()];
        
        for (int row = 0; row < currentGeneration.size() - 1; row++) {
            for (int col = 0; col < currentGeneration.get(0).size() - 1; col++) {
                if (currentGeneration.get(row).get(col) == 1) {
                    neighbourArray[row - 1][col - 1]++;
                    neighbourArray[row - 1][col]++;
                    neighbourArray[row - 1][col+1]++; 
                    neighbourArray[row][col - 1]++;
                    neighbourArray[row][col + 1]++;
                    neighbourArray[row + 1][col - 1]++;
                    neighbourArray[row + 1][col]++;
                    neighbourArray[row + 1][col + 1]++;
                }
            }
        }
    }

    @Override
    public void setFirstGeneration() {
        firstGeneration.clear();
        //firstGeneration.stream().forEach(System.out::println);
        for (int row = 0; row < currentGeneration.size() -1; row++) {
            firstGeneration.add(new ArrayList<>());
            for (int col = 0; col < currentGeneration.get(0).size() -1; col++) {
                firstGeneration.get(row).add(col, currentGeneration.get(row).get(col));
            }
        }
    }

    public void setIsDynamic(boolean isDynamic) {
        //this.isDynamic = isDynamic;
    }
    //=========================================================================
    // Misc.
    //=========================================================================

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

    public String getBoundingBoxPattern() {
        if (currentGeneration.isEmpty()) {
            return "";
        }
        
        int[] boundingBox = getBoundingBox();
        String str = "";
        
        for (int i = boundingBox[0]; i <= boundingBox[1]; i++) {
            for (int j = boundingBox[2]; j <= boundingBox[3]; j++) {
                if (currentGeneration.get(i).get(j) == 1) {
                    str = str + "1";
                } else {
                    str = str + "0";
                }
            }
        }
        return str;
    }

    private int[] getBoundingBox() {
        int[] boundingBox = new int[4]; // minrow maxrow mincolumn maxcolumn
        boundingBox[0] = currentGeneration.size();
        boundingBox[1] = 0;
        boundingBox[2] = currentGeneration.get(0).size();
        boundingBox[3] = 0;
        
        for (int i = 0; i < currentGeneration.size(); i++) {
            for (int j = 0; j < currentGeneration.get(i).size(); j++) {
                if ((currentGeneration.get(i).get(j) == 1)) {
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
        //currentGeneration.stream().forEach(i -> System.out.println(i));
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < currentGeneration.size(); i++) {
            for (int j = 0; j < currentGeneration.get(0).size(); j++) {
                sb.append(currentGeneration.get(i).get(j));
            }
        }
        return sb.toString();
    }
}
