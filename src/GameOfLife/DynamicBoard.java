package GameOfLife;    

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author Robin
 */
public class DynamicBoard extends Board { 
    private final MetaData metadata;
    private final List<List<Byte>> currentGeneration;
    private final List<List<Byte>> firstGeneration;
    int sum1 = 0;
    int sum2 = 0;
    int sum3 = 0;
    int sum4 = 0;
    final int total = sum1 + sum2 + sum3 + sum4;
    
    //=========================================================================
    // Constructors
    //=========================================================================
    
    public DynamicBoard(int rows, int columns) {
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
//        System.out.println("row: " + row);
//        System.out.println("column: " + column);
        return currentGeneration.get(row).get(column);
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
        if(aliveState != 0 || aliveState != 1) {
            throw new RuntimeException("Invalid number in cell state");
        }
        currentGeneration.get(row).set(column, aliveState);
    }
    
    //=========================================================================
    // Generation-methods
    //=========================================================================
    
    @Override
    public void resetBoard() {
        currentGeneration.clear();
        for(int row = 0; row < firstGeneration.size(); row++) {
            for(int col = 0; col < firstGeneration.get(0).size(); col++) {
                currentGeneration.add(new ArrayList<>());
                currentGeneration.get(row).add(firstGeneration.get(row).get(col));
            }
        }
    }
    
    private void checkEdges() {
        System.out.println(currentGeneration.get(0).size());
        System.out.println(currentGeneration.size());

        //Top
        checkTop();

        //Left

        checkLeft();

        //Right

        checkRight();

        //Bottom

        // checkBottom();


    }

    private void checkTop() {
        final int rows = currentGeneration.size();
        final int columns = currentGeneration.get(0).size();

        sum1 = currentGeneration.get(0).stream().mapToInt(w -> Integer.parseInt(w.toString())).sum();
        sum2 = currentGeneration.get(1).stream().mapToInt(w -> Integer.parseInt(w.toString())).sum();
        sum3 = currentGeneration.get(2).stream().mapToInt(w -> Integer.parseInt(w.toString())).sum();
        sum4 = currentGeneration.get(3).stream().mapToInt(w -> Integer.parseInt(w.toString())).sum();
        System.out.println("sum øverste rad: " + sum1);
        System.out.println("sum nest øverste rad: " + sum2);
        System.out.println("sum tredje øverste rad: " + sum3);

        if(sum1 > 0) {
            System.out.println("rows before add:" + currentGeneration.get(0).size());
            System.out.println("cols before add:" + currentGeneration.size());
            System.out.println("Adding three rows");

            currentGeneration.add(0, new ArrayList<>());
            currentGeneration.add(1, new ArrayList<>());
            currentGeneration.add(2, new ArrayList<>());

            for (int col = 0; col < columns; col++) {
                currentGeneration.get(0).add((byte)0);
                currentGeneration.get(1).add((byte)0);
                currentGeneration.get(2).add((byte)0);
            }
            /*
            int newArraySize = (int)Math.ceil((currentGeneration.size() * 3) / 2) +1;
            int result = (int)Math.ceil(rows - newArraySize);

            for (int i = 0; i < result; i++) {
                currentGeneration.add(i, new ArrayList<>());
                currentGeneration.get(i).add((byte)0);
            }
            */
            System.out.println("rows after add:" + currentGeneration.get(0).size());
            System.out.println("cols after add:" + currentGeneration.size());
        } else if (sum2 > 0) {
            System.out.println("rows before add:" + currentGeneration.get(0).size());
            System.out.println("cols before add:" + currentGeneration.size());
            System.out.println("Adding one row");
            currentGeneration.add(0, new ArrayList<>());
            for (int col = 0; col < columns; col++) {
                currentGeneration.get(0).add((byte)0);
            }
        } else if (total == 0) {
            currentGeneration.remove(0);
            System.out.println("Removing");
            System.out.println("rows after remove:" + currentGeneration.get(0).size());
            System.out.println("cols after remove:" + currentGeneration.size());
        }
    }


    private void checkLeft() {
        final int rows = currentGeneration.size();
        final int columns = currentGeneration.get(0).size();

        for (int row = 0; row < rows; row++) {

            sum1 += currentGeneration.get(row).get(0);
            sum2 += currentGeneration.get(row).get(1);
            sum3 += currentGeneration.get(row).get(2);
            sum4 += currentGeneration.get(row).get(3);
            System.out.println("Left 1:" + sum1 + " 2:" + sum2);
        }
        if (sum1 > 0) {
            for (int row = 0; row < rows; row++) {
                currentGeneration.get(row).add(0, (byte) 0);
                currentGeneration.get(row).add(1, (byte) 0);
                currentGeneration.get(row).add(2, (byte) 0);
            }
        } else if (sum2 > 0) {
            for (int row = 0; row < rows; row++) {
                currentGeneration.get(row).add(0, (byte) 0);
            }
        } else if(total == 0){
            for (int row = 0; row < rows; row++) {
                currentGeneration.get(row).remove(0);
                System.out.println("Removing left");
                System.out.println("rows after remove:" + currentGeneration.get(0).size());
                System.out.println("cols after remove:" + currentGeneration.size());
            }
        }
    }

    private void checkRight() {
        final int rows = currentGeneration.size();
        final int columns = currentGeneration.get(0).size();

        for (int row = 0; row < rows; row++) {
            sum1 += currentGeneration.get(row).get(columns - 1);
            sum2 += currentGeneration.get(row).get(columns - 2);
        }
        if (sum1 > 0) {
            for (int row = 0; row < rows; row++) {
                currentGeneration.get(row).add(rows -1,(byte) 0);
                currentGeneration.get(row).add(rows -2, (byte) 0);
                currentGeneration.get(row).add(rows -3, (byte) 0);
            }
        } else if (sum2 > 0) {
            for (int row = 0; row < rows; row++) {
                currentGeneration.get(row).add((byte) 0);
            }
        } else if (total == 0){
            for (int row = 0; row < rows; row++) {
                currentGeneration.get(row).remove(columns - 1);
                System.out.println("Removing left");
                System.out.println("rows after remove:" + currentGeneration.get(0).size());
                System.out.println("cols after remove:" + currentGeneration.size());
            }
        }
    }
    private void checkBottom() {
        final int rows = currentGeneration.size();
        final int columns = currentGeneration.get(0).size();

        sum1 = currentGeneration.get(rows -1).stream().mapToInt(w -> Integer.parseInt(w.toString())).sum();
        sum2 = currentGeneration.get(rows -2).stream().mapToInt(w -> Integer.parseInt(w.toString())).sum();
        sum3 = currentGeneration.get(rows -3).stream().mapToInt(w -> Integer.parseInt(w.toString())).sum();
        sum4 = currentGeneration.get(rows -4).stream().mapToInt(w -> Integer.parseInt(w.toString())).sum();
        System.out.println("sum nederste rad: " + sum1);
        System.out.println("sum nest nederste rad: " + sum2);
        System.out.println("sum tredje nederste rad: " + sum3);

        if (sum1 > 0) {
            System.out.println("rows before add:" + currentGeneration.get(0).size());
            System.out.println("cols before add:" + currentGeneration.size());
            System.out.println("Adding");
            currentGeneration.add((rows-1), new ArrayList<>());
            currentGeneration.add((rows-2), new ArrayList<>());
            currentGeneration.add((rows-3), new ArrayList<>());

            for (int bottomCol = 0; bottomCol < columns; bottomCol++) {
                currentGeneration.get(rows - 1).add((byte) 0);
                currentGeneration.get(rows - 2).add((byte) 0);
                currentGeneration.get(rows - 3).add((byte) 0);
            }
        } else if(sum2 > 0) {
            currentGeneration.add((rows-1), new ArrayList<>());
            for (int bottomCol = 0; bottomCol < columns; bottomCol++) {
                currentGeneration.get(rows - 1).add((byte) 0);
            }
        } else if (total == 0) {
            currentGeneration.remove(rows - 1);
            System.out.println("Removing bottom row");
            System.out.println("Bottom rows after remove:" + currentGeneration.get(0).size());
            System.out.println("Bottom cols after remove:" + currentGeneration.size());
        }
    }
    @Override
    public byte[][] countNeighbours() {
        byte[][] neighbourArray = new byte[currentGeneration.size()][currentGeneration.get(0).size()];
        
        for(int row = 1; row < currentGeneration.size()-1; row++) {
            for(int col = 1; col < currentGeneration.get(0).size()-1; col++) {
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
        checkEdges();
        byte[][] neighbourArray = countNeighbours();
        
        for(int row = 0; row < currentGeneration.size(); row++) {
            for(int col = 0; col < currentGeneration.get(0).size(); col++) {
                currentGeneration.get(row).set(col,((neighbourArray[row][col]== 3) || (currentGeneration.get(row).get(col) == 1 && neighbourArray[row][col] == 2 )) ? (byte)1 : (byte)0);
            }
        }
    }
    
    //=========================================================================
    // Misc.
    //=========================================================================
    
    /**
     *  Method that returns the game board as a String. Used for Unit Testing with JUnit 4
     *
     * @return String The contents of the game board as a String
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        /*for (int i = 0; i < currentGeneration.length; i++) {
            for (int j = 0; j < currentGeneration[0].length; j++) {
                sb.append(currentGeneration[i][j]);
            }
        }*/
        return sb.toString();
    }
    private int getIntValue(int row, int col) {
        return Byte.toUnsignedInt(currentGeneration.get(row).get(col));
    }
}
