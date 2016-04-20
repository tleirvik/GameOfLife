package GameOfLife;    

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Robin
 */
public class DynamicBoard extends Board { 
    private final MetaData metadata;
    private final List<List<Byte>> buffer_1;
    private final List<List<Byte>> buffer_2;
    private final List<List<Byte>> firstGeneration;
    boolean bufferSwap = false;
    private final int MIN_ROW;
    private final int MIN_COL;



    //=========================================================================
    // Constructors
    //=========================================================================
    
    public DynamicBoard(int rows, int columns) {
        MIN_COL = columns;
        MIN_ROW = rows;
        metadata = new MetaData();
        buffer_1 = new ArrayList<>();
        buffer_2 = new ArrayList<>();
        firstGeneration = new ArrayList<>();
        
        for (int row = 0; row < rows; row++) {
            buffer_1.add(new ArrayList<>());
            buffer_2.add(new ArrayList<>());
            firstGeneration.add(new ArrayList<>());
            for (int col = 0; col < columns; col++) {
                buffer_1.get(row).add((byte)0);
                buffer_2.get(row).add((byte)0);
                firstGeneration.get(row).add((byte)0);
            }
        }
    }
        
    public DynamicBoard(byte[][] board, MetaData metadata) {
        MIN_COL = board.length;
        MIN_ROW = board[0].length;
        this.metadata = metadata;
        buffer_1 = new ArrayList<>();
        buffer_2 = new ArrayList<>();
        firstGeneration = new ArrayList<>();
        for(int row = 0; row < board.length; row++) {
            buffer_1.add(new ArrayList<>());
            buffer_2.add(new ArrayList<>());
            firstGeneration.add(new ArrayList<>());
            for(int col = 0; col < board[0].length; col++) {
                buffer_1.get(row).add(board[row][col]);
                buffer_2.get(row).add(board[row][col]);
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
        return buffer_1.size();
    }

    public int getColumns() {
        return buffer_1.get(0).size();
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
        if (bufferSwap) {
            return buffer_2.get(row).get(column);
        } else {
            return buffer_1.get(row).get(column);
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
     */
    public void setCellAliveState(int row, int column, byte aliveState) {

        if(aliveState == 0 || aliveState == 1) {
            if (bufferSwap) {
                buffer_2.get(row).set(column, aliveState);
            } else {
                buffer_1.get(row).set(column, aliveState);
            }
        } else {
            throw new RuntimeException("Invalid number in cell state");
        }
        
    }
    
    //=========================================================================
    // Generation-methods
    //=========================================================================
    public int[] getStatistics() {
        /*int livingCells = 0;
        int livingCellsPosition = 0;

        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < getColumns(); j++) {
                if (currentGeneration.get(i).get(j) == 1) {
                    livingCells++;
                    livingCellsPosition += i + j;
                }
            }
        }
        int[] statArray = {livingCells, livingCellsPosition};*/
        return new int[2];
    }

    public  int countAliveCells() {
        /*int aliveCells = 0;
        for (int i = 0; i < currentGeneration.size(); i++) {
            for (int j = 0; j < currentGeneration.get(0).size(); j++) {
                if (currentGeneration.get(i).get(j) == 1) {
                    aliveCells++;
                }
            }
        }*/
        return 0;
    }

    @Override
    public void resetBoard() {
        buffer_1.clear();
        buffer_2.clear();
        for(int row = 0; row < firstGeneration.size(); row++) {
            buffer_1.add(new ArrayList<>());
            buffer_2.add(new ArrayList<>());
            for(int col = 0; col < firstGeneration.get(0).size(); col++) {
                buffer_1.get(row).add(col, firstGeneration.get(row).get(col));
                buffer_2.get(row).add(col, firstGeneration.get(row).get(col));
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
        final int columns = buffer_1.get(0).size();
        
        int sum1 = buffer_1.get(0).stream().mapToInt(w -> Integer.parseInt(w.toString())).sum() +
               buffer_2.get(0).stream().mapToInt(w -> Integer.parseInt(w.toString())).sum();
        int sum2 = buffer_1.get(1).stream().mapToInt(w -> Integer.parseInt(w.toString())).sum() +
               buffer_2.get(1).stream().mapToInt(w -> Integer.parseInt(w.toString())).sum();
        int sum3 = buffer_1.get(2).stream().mapToInt(w -> Integer.parseInt(w.toString())).sum() + 
               buffer_2.get(2).stream().mapToInt(w -> Integer.parseInt(w.toString())).sum();
        int sum4 = buffer_1.get(3).stream().mapToInt(w -> Integer.parseInt(w.toString())).sum() + 
               buffer_2.get(3).stream().mapToInt(w -> Integer.parseInt(w.toString())).sum();
        int sum5 = buffer_1.get(4).stream().mapToInt(w -> Integer.parseInt(w.toString())).sum() + 
               buffer_2.get(4).stream().mapToInt(w -> Integer.parseInt(w.toString())).sum();
        int remove = sum1 + sum2 + sum3 + sum4 + sum5;
        int add = sum1 + sum2;

        if (add != 0) {
            buffer_1.add(0, new ArrayList<>());
            buffer_2.add(0, new ArrayList<>());
            for (int col = 0; col < columns; col++) {
                buffer_1.get(0).add((byte)0);
                buffer_2.get(0).add((byte)0);
            }
        } else if (remove == 0 && buffer_1.size() < MIN_ROW) {
            buffer_1.remove(0);
            buffer_2.remove(0);
        }
    }

    private void checkBottom() {
        int rows = buffer_1.size();
        final int columns = buffer_1.get(0).size();
            
        int sum1 = buffer_1.get(rows - 1).stream().mapToInt(w -> Integer.parseInt(w.toString())).sum() +
               buffer_2.get(rows - 1).stream().mapToInt(w -> Integer.parseInt(w.toString())).sum();
        int sum2 = buffer_1.get(rows - 2).stream().mapToInt(w -> Integer.parseInt(w.toString())).sum() +
               buffer_2.get(rows - 2).stream().mapToInt(w -> Integer.parseInt(w.toString())).sum();
        int sum3 = buffer_1.get(rows - 3).stream().mapToInt(w -> Integer.parseInt(w.toString())).sum() + 
               buffer_2.get(rows - 3).stream().mapToInt(w -> Integer.parseInt(w.toString())).sum();
        int sum4 = buffer_1.get(rows - 4).stream().mapToInt(w -> Integer.parseInt(w.toString())).sum() + 
               buffer_2.get(rows - 4).stream().mapToInt(w -> Integer.parseInt(w.toString())).sum();
        int sum5 = buffer_1.get(rows - 5).stream().mapToInt(w -> Integer.parseInt(w.toString())).sum() + 
               buffer_2.get(rows - 5).stream().mapToInt(w -> Integer.parseInt(w.toString())).sum();
            
        int remove = sum1 + sum2 + sum3 + sum4 + sum5;
        int add = sum1 + sum2;

        if (add != 0) {
            buffer_1.add(new ArrayList<>());
            buffer_2.add(new ArrayList<>());
            rows = buffer_1.size() - 1;
            for (int col = 0; col < columns; col++) {
                buffer_1.get(rows).add((byte)0);
                buffer_2.get(rows).add((byte)0);
            }
        } else if(remove == 0 && buffer_1.size() < MIN_ROW) {
            buffer_1.remove(buffer_1.size() - 1);
            buffer_2.remove(buffer_1.size() - 1);
        }
    }

    private void checkLeft() {
        int sum1 = 0;
        int sum2 = 0;
        int sum3 = 0;
        int sum4 = 0;
        int sum5 = 0;
        final int rows = buffer_1.size();
        
        for (int row = 0; row < rows; row++) {
            sum1 += buffer_1.get(row).get(0) + 
                    buffer_2.get(row).get(0);
            sum2 += buffer_1.get(row).get(1) + 
                    buffer_2.get(row).get(0);
            sum3 += buffer_1.get(row).get(2) + 
                    buffer_2.get(row).get(0);
            sum4 += buffer_1.get(row).get(3) + 
                    buffer_2.get(row).get(0);
            sum5 += buffer_1.get(row).get(4) + 
                    buffer_2.get(row).get(0);
        }
        int remove = sum1 + sum2 + sum3 + sum4 + sum5;
        int add = sum1 + sum2;

        if (add != 0) {
            for (int row = 0; row < rows; row++) {
                buffer_1.get(row).add(0, (byte)0);
                buffer_2.get(row).add(0, (byte)0);
            }
        } else if(remove == 0 && buffer_1.get(0).size() < MIN_COL){
            for (int row = 0; row < rows; row++) {
                buffer_1.get(row).remove(0);
                buffer_2.get(row).remove(0);
            }
        }
    }

    private void checkRight() {
        int sum1 = 0;
        int sum2 = 0;
        int sum3 = 0;
        int sum4 = 0;
        int sum5 = 0;
        final int rows = buffer_1.size();
        final int columns = buffer_1.get(0).size();

        for (int row = 0; row < rows; row++) {
            sum1 += buffer_1.get(row).get(columns - 1) + 
                    buffer_2.get(row).get(columns - 1);
            sum2 += buffer_1.get(row).get(columns - 2) + 
                    buffer_2.get(row).get(columns - 2);
            sum3 += buffer_1.get(row).get(columns - 3) + 
                    buffer_2.get(row).get(columns - 3);
            sum4 += buffer_1.get(row).get(columns - 4) + 
                    buffer_2.get(row).get(columns - 4);
            sum5 += buffer_1.get(row).get(columns - 5) + 
                    buffer_2.get(row).get(columns - 5);
        }

        int remove = sum1 + sum2 + sum3 + sum4 + sum5;
        int add = sum1 + sum2;

        if (add != 0) {
            for (int row = 0; row < rows; row++) {
                buffer_1.get(row).add((byte) 0);
                buffer_2.get(row).add((byte) 0);
            }
        } else if (remove == 0 && buffer_1.get(0).size() < MIN_COL){
            for (int row = 0; row < rows; row++) {
                buffer_1.get(row).remove(rows - 0);
                buffer_2.get(row).remove(rows - 0);
            }
        }
    }

    @Override
    public byte[][] countNeighbours() {
        
        return new byte[1][1];
    }
    
    private int countNeighbours(int row, int column, List<List<Byte>> currentBuffer) {
        return currentBuffer.get(row - 1).get(column - 1) + 
               currentBuffer.get(row - 1).get(column) + 
               currentBuffer.get(row - 1).get(column + 1) + 
               currentBuffer.get(row).get(column - 1) + 
               currentBuffer.get(row).get(column + 1) + 
               currentBuffer.get(row + 1).get(column - 1) +
               currentBuffer.get(row + 1).get(column) +
               currentBuffer.get(row + 1).get(column + 1);
    }
    
    @Override
    public void nextGeneration() {
        nextGenerationConcurrent();
        /*
        checkEdges();
        
        List<List<Byte>> currentBuffer;
        List<List<Byte>> nextBuffer;
        
        if(bufferSwap) {
            currentBuffer = buffer_2;
            nextBuffer = buffer_1;
        } else {
            currentBuffer = buffer_1;
            nextBuffer = buffer_2;
        }
        
        for(int row = 1; row < currentBuffer.size() - 2; row++) {
            for(int col = 1; col < currentBuffer.get(0).size() - 2; col++) {
                nextBuffer.get(row).set(col,
                    ((countNeighbours(row, col, currentBuffer)== 3) || 
                    (currentBuffer.get(row).get(col) == 1 && 
                    countNeighbours(row, col, currentBuffer) == 2 )) 
                            ? (byte)1 : (byte)0);
            }
        }
        bufferSwap = !bufferSwap;*/
    }
    
    public void nextGenerationConcurrent() {
        checkEdges();
        
        List<List<Byte>> currentBuffer;
        List<List<Byte>> nextBuffer;
        
        if(bufferSwap) {
            currentBuffer = buffer_2;
            nextBuffer = buffer_1;
        } else {
            currentBuffer = buffer_1;
            nextBuffer = buffer_2;
        }
        
        final int[][] distribution = new int[4][2];
        final int rowsPerThread = (getRows() - 2) / 4;
        int startRow = 1;
        int endRow = startRow + rowsPerThread;
        for (int i = 0; i < 4; i++) {
            distribution[i][0] = startRow;
            distribution[i][1] = endRow;
            
            startRow = endRow + 1;
            endRow+= rowsPerThread;
        }
        
        ArrayList<Thread> workers = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            final int n = i;
            workers.add(new Thread(() -> {
                for(int row = distribution[n][0]; row < distribution[n][1]; row++) {
                for(int col = 1; col < currentBuffer.get(0).size() - 2; col++) {
                        nextBuffer.get(row).set(col,
                        ((countNeighbours(row, col, currentBuffer)== 3) || 
                        (currentBuffer.get(row).get(col) == 1 && 
                            countNeighbours(row, col, currentBuffer) == 2 )) 
                                    ? (byte)1 : (byte)0);
                    }
                }
            }));
        }
        
        for (int i = 0; i < 4; i++) {
            workers.get(i).start();
        }
        
        for (int i = 0; i < 4; i++) {
            try {
                workers.get(i).join();
            } catch (InterruptedException ex) {
                Logger.getLogger(DynamicBoard.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
        bufferSwap = !bufferSwap;
    }

    @Override
    public void setFirstGeneration() {
        firstGeneration.clear();
        List<List<Byte>> currentBuffer;
        if(bufferSwap) {
            currentBuffer = buffer_2;
        } else {
            currentBuffer = buffer_1;
        }
        for (int row = 0; row < buffer_1.size() -1; row++) {
            firstGeneration.add(new ArrayList<>());
            for (int col = 0; col < buffer_1.get(0).size() -1; col++) {
                firstGeneration.get(row).add(col, currentBuffer.get(row).get(col));
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
        /*byte[][] boardClone = new byte[getRows()][getColumns()];
        MetaData metaDataClone = metadata.clone();
        for (int row = 0; row < currentGeneration.size(); row++) {
            for (int col = 0; col < currentGeneration.get(0).size(); col++) {
                boardClone[row][col] = currentGeneration.get(row).get(col);
            }
        }*/
        return new DynamicBoard(null, null);
    }

    public String getBoundingBoxPattern() {
        /*if(currentGeneration.size() == 0) return "";
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
        }*/
        return "";
    }

    private int[] getBoundingBox() {
        /*int[] boundingBox = new int[4]; // minrow maxrow mincolumn maxcolumn
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
        }*/
        return new int[0];
    }
    /**
     *  Method that returns the game board as a String. Used for Unit Testing with JUnit 4
     *
     * @return String The contents of the game board as a String
     */
    @Override
    public String toString() {
        /*//currentGeneration.stream().forEach(i -> System.out.println(i));
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < currentGeneration.size(); i++) {
            for (int j = 0; j < currentGeneration.get(0).size(); j++) {
                sb.append(currentGeneration.get(i).get(j));
            }
        }*/
        return "";
    }
}
