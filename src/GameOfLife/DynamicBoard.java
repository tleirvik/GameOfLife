package GameOfLife;    

import java.util.ArrayList;
import java.util.List;

/**
 * @author Robin
 */
public class DynamicBoard extends Board { 
    private final MetaData metadata;
    private final List<List<Byte>> currentGeneration;
    private final List<List<Byte>> firstGeneration;
    private int sum1 = 0;
    private int sum2 = 0;
    private int sum3 = 0;
    private int sum4 = 0;
    private int sum5 = 0;
    // boolean isDynamic = true;
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
                currentGeneration.get(row).add((byte)0);
                firstGeneration.get(row).add((byte)0);
            }
        }
    }
        
    public DynamicBoard(byte[][] board, MetaData metadata) {
        MIN_COL = board.length;
        MIN_ROW = board[0].length;
        this.metadata = metadata;
        currentGeneration = new ArrayList<>();
        firstGeneration = new ArrayList<>();
        for(int row = 0; row < board.length; row++) {
            currentGeneration.add(new ArrayList<>());
            firstGeneration.add(new ArrayList<>());
            for(int col = 0; col < board[0].length; col++) {
                currentGeneration.get(row).add(board[row][col]);
                firstGeneration.get(row).add(board[row][col]);
            }
        }
        //addFrame();
    }
    /*
    public void addFrame() {
        final int columns = currentGeneration.get(0).size();
        currentGeneration.add(0, new ArrayList<>());
        currentGeneration.add(new ArrayList<>());
        for (int col = 0; col < columns; col++) {
            currentGeneration.get(0).add((byte)0);
        }
        for (int col = 0; col < currentGeneration.get(0).size(); col++) {
            currentGeneration.get(currentGeneration.size() - 1).add((byte)0);
        }
        for (List<Byte> e : currentGeneration) {
            e.add(0, (byte) 0);
        }
        for (List<Byte> e : currentGeneration) {
            e.add((byte) 0);
        }
    }
    */

    @Override
    public void removeFrame() {
        /*
        currentGeneration.remove(0);
        currentGeneration.remove(currentGeneration.size() -1);
        for (List<Byte> e : currentGeneration) {
            e.remove(0);
        }
        for (List<Byte> e : currentGeneration) {
            e.remove(currentGeneration.size() - 1);
        }
        */
    }
    //=========================================================================
    // Getters
    //=========================================================================
    
    public int getRows() {
        return currentGeneration.size();
    }

    public int getColumns() {
        return currentGeneration.get(0).size();
    }
    
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
    public byte getCellAliveState(int row, int column) {
        //if (isDynamic) {
            return currentGeneration.get(row).get(column);
       // } else {
      //      return currentGeneration.get(row - 1).get(column - 1)
        // }

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
     */
    public void setCellAliveState(int row, int column, byte aliveState) {

        if(aliveState == 0 || aliveState == 1) {
            //if (isDynamic) {
                currentGeneration.get(row).set(column, aliveState);
            //} else {
            //    currentGeneration.get(row -1).set(column -1, aliveState);
            //}
        } else {
            throw new RuntimeException("Invalid number in cell state");
        }
        
    }
    
    //=========================================================================
    // Generation-methods
    //=========================================================================
    public int[] getStatistics() {
        int livingCells = 0;
        int livingCellsPosition = 0;

        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < getColumns(); j++) {
                if (currentGeneration.get(i).get(j) == 1) {
                    livingCells++;
                    livingCellsPosition += i + j;
                }
            }
        }
        int[] statArray = {livingCells, livingCellsPosition};
        return statArray;
    }

    public  int countAliveCells() {
        int aliveCells = 0;
        for (int i = 0; i < currentGeneration.size(); i++) {
            for (int j = 0; j < currentGeneration.get(0).size(); j++) {
                if (currentGeneration.get(i).get(j) == 1) {
                    aliveCells++;
                }
            }
        }
        return aliveCells;
    }

    @Override
    public void resetBoard() {
        currentGeneration.clear();
        for(int row = 0; row < firstGeneration.size(); row++) {
            currentGeneration.add(new ArrayList<>());
            for(int col = 0; col < firstGeneration.get(0).size(); col++) {
                currentGeneration.get(row).add(col, firstGeneration.get(row).get(col));
            }
        }
    }
    
    private void checkEdges() {
        // Sexy one-liner som skriver ut hele array'et
        //currentGeneration.stream().forEach(i -> System.out.println(i));
        checkTop();
        checkBottom();
        checkLeft();
        checkRight();
    }

    private void checkTop() {
        final int columns = currentGeneration.get(0).size();
        sum1 = currentGeneration.get(0).stream().mapToInt(w -> Integer.parseInt(w.toString())).sum();
        sum2 = currentGeneration.get(1).stream().mapToInt(w -> Integer.parseInt(w.toString())).sum();
        sum3 = currentGeneration.get(2).stream().mapToInt(w -> Integer.parseInt(w.toString())).sum();
        sum4 = currentGeneration.get(3).stream().mapToInt(w -> Integer.parseInt(w.toString())).sum();
        sum5 = currentGeneration.get(4).stream().mapToInt(w -> Integer.parseInt(w.toString())).sum();
        int remove = sum1 + sum2 + sum3 + sum4 + sum5;
        int add = sum1 + sum2;

        if (add != 0) {
            currentGeneration.add(0, new ArrayList<>());
            for (int col = 0; col < columns; col++) {
                currentGeneration.get(0).add((byte)0);
            }
        } else if (remove == 0 && currentGeneration.size() < MIN_ROW) {
            currentGeneration.remove(0);
        }
    }

    private void checkBottom() {
            final int rows = currentGeneration.size();
            final int columns = currentGeneration.get(0).size();
            sum1 = currentGeneration.get(rows -1).stream().mapToInt(w -> Integer.parseInt(w.toString())).sum();
            sum2 = currentGeneration.get(rows -2).stream().mapToInt(w -> Integer.parseInt(w.toString())).sum();
            sum3 = currentGeneration.get(rows -3).stream().mapToInt(w -> Integer.parseInt(w.toString())).sum();
            sum4 = currentGeneration.get(rows -4).stream().mapToInt(w -> Integer.parseInt(w.toString())).sum();
            sum5 = currentGeneration.get(rows -5).stream().mapToInt(w -> Integer.parseInt(w.toString())).sum();
            int remove = sum1 + sum2 + sum3 + sum4 + sum5;
            int add = sum1 + sum2;

            if (add != 0) {
                currentGeneration.add(new ArrayList<>());
                for (int col = 0; col < columns; col++) {
                    currentGeneration.get(currentGeneration.size() - 1).add((byte)0);
                }
            } else if(remove == 0 && currentGeneration.size() < MIN_ROW) {
                currentGeneration.remove(currentGeneration.size() - 1);
            }
    }

    private void checkLeft() {
        final int rows = currentGeneration.size();
        sum1 = 0;
        sum2 = 0;
        sum3 = 0;
        sum4 = 0;
        sum5 = 0;
        for (int row = 0; row < rows; row++) {
            sum1 += currentGeneration.get(row).get(0);
            sum2 += currentGeneration.get(row).get(1);
            sum3 += currentGeneration.get(row).get(2);
            sum4 += currentGeneration.get(row).get(3);
            sum5 += currentGeneration.get(row).get(4);
        }
        int remove = sum1 + sum2 + sum3 + sum4 + sum5;
        int add = sum1 + sum2;

        if (add != 0) {

            for (List<Byte> e : currentGeneration) {
                e.add(0, (byte) 0);
            }
        } else if(remove == 0 && currentGeneration.get(0).size() < MIN_COL){
            for (List<Byte> e : currentGeneration) {
                e.remove(0);
            }
        }
    }

    private void checkRight() {
        /*
        Hvorfor må summene bare nulles i denne metoden?
         */
        sum1 = 0;
        sum2 = 0;
        sum3 = 0;
        sum4 = 0;
        sum5 = 0;
        final int rows = currentGeneration.size();
        final int columns = currentGeneration.get(0).size();

        for (List<Byte> e : currentGeneration) {
            sum1 += e.get(columns - 1);
            sum2 += e.get(columns - 2);
            sum3 += e.get(columns - 3);
            sum4 += e.get(columns - 4);
            sum5 += e.get(columns - 5);
        }

        int remove = sum1 + sum2 + sum3 + sum4 + sum5;
        int add = sum1 + sum2;

        if (add != 0) {
            for (List<Byte> e : currentGeneration) {
                e.add((byte) 0);
            }
        } else if (remove == 0 && currentGeneration.get(0).size() < MIN_COL){
            for (List<Byte> e : currentGeneration) {
                e.remove(rows - 1);
            }
        }
    }

    @Override
    public byte[][] countNeighbours() {
        byte[][] neighbourArray = new byte[currentGeneration.size()][currentGeneration.get(0).size()];
        
        for(int row = 0; row < currentGeneration.size() -1; row++) {
            for(int col = 0; col < currentGeneration.get(0).size() -1; col++) {
                if(currentGeneration.get(row).get(col) == 1) {
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
        return neighbourArray;
    }
    
    @Override
    public void nextGeneration() {
        //if (isDynamic) {
        checkEdges();

        byte[][] neighbourArray = countNeighbours();
        
        for(int row = 0; row < currentGeneration.size() -1; row++) {
            for(int col = 0; col < currentGeneration.get(0).size() -1; col++) {
                currentGeneration.get(row).set(col,((neighbourArray[row][col]== 3) || (currentGeneration.get(row).get(col) == 1 && neighbourArray[row][col] == 2 )) ? (byte)1 : (byte)0);
            }
        }
    }

    @Override
    public void nextGenerationConcurrent() {

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
        if(currentGeneration.size() == 0) return "";
        int[] boundingBox = getBoundingBox();
        String str = "";
        for(int i = boundingBox[0]; i <= boundingBox[1]; i++) {
            for(int j = boundingBox[2]; j <= boundingBox[3]; j++) {
                if(currentGeneration.get(i).get(j) == 1) {
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
        for(int i = 0; i < currentGeneration.size(); i++) {
            for(int j = 0; j < currentGeneration.get(i).size(); j++) {
                if((currentGeneration.get(i).get(j) == 1)) continue;
                if(i < boundingBox[0]) {
                    boundingBox[0] = i;
                }
                if(i > boundingBox[1]) {
                    boundingBox[1] = i;
                }
                if(j < boundingBox[2]) {
                    boundingBox[2] = j;
                }
                if(j > boundingBox[3]) {
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
