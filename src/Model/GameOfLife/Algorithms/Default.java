package Model.GameOfLife.Algorithms;

import Model.GameOfLife.Boards.Board;
import java.util.ArrayList;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Default implements Algorithm{
    private final ArrayList<ArrayList<Byte>> neighbourArray;
    private Board board;

    public Default(Board board) {
        this.board = board;
        neighbourArray = new ArrayList<>();
        
        for (int row = 0; row < (board.getRows() + 2); row++) {
            neighbourArray.add(new ArrayList<>());
            for (int col = 0; col < (board.getColumns() + 2); col++) {
                neighbourArray.get(row).add((byte) 0);
            }
        }
    }
    
    @Override
    public void beforeUpdate() {
        board.beforeUpdate();
        checkNeighbourArraySize();
    }

    @Override
    public void update() {        
        checkNeighbourArraySize();
        
        countNeighbours(0, board.getRows());

        nextGeneration(0, board.getRows());

        clearNeighbours(0, neighbourArray.size());
    }

    @Override
    public void updateConcurrent(int start, int stop, int clearStart, int clearStop, CyclicBarrier barrier) {
        
        countNeighbours(start, stop);
        try {
            barrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }

        nextGeneration(start, stop);

        try {
            barrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }

        // TODO: 27.04.16 Fiks clearNeighbours slik at den er trådbasert 
        clearNeighbours(0, neighbourArray.size());
    }
    
    private void checkNeighbourArraySize() {
        final int neighbourArrayRows = neighbourArray.size() - 2;
        final int neighbourArrayColumns = neighbourArray.get(0).size() - 2;
        if (neighbourArrayRows < board.getRows()) {//Legg til rader
            final int diff = board.getRows() - neighbourArrayRows;
            final int columns = neighbourArray.get(0).size();
            for (int row = 0; row < diff; row++) {
                neighbourArray.add(0, new ArrayList<>());
                for (int col = 0; col < columns; col++) {
                    neighbourArray.get(0).add((byte) 0);
                } 
            }
        } else if (neighbourArrayRows > board.getRows()) {//Slett rader
            final int diff = neighbourArrayRows - board.getRows();
            for (int row = 0; row < diff; row++) {
                neighbourArray.remove(0);
            }
        }
        
        if (neighbourArrayColumns < board.getColumns()) {//Legg til kolonner
            final int diff = board.getColumns() - neighbourArrayColumns;
            for (int row = 0; row < diff; row++) {
                neighbourArray.stream().forEach((e) -> {
                    e.add((byte) 0);
                });
            }
        } else if (neighbourArrayColumns > board.getColumns()) {//Slett kolonner
            final int diff = neighbourArrayColumns - board.getColumns();
            
            for (int row = 0; row < diff; row++) {
                neighbourArray.stream().forEach((e) -> {
                    e.remove(0);
                });
            }
        }
        
    }

    private void countNeighbours(int start, int stop) {
        for (int row = start; row < stop; row++) {
            for (int col = 0; col < board.getColumns(); col++) {
                if(board.getCellAliveState(row, col) == 1) {
                    if (row == start || row == stop -1) {
                        synchronized (this) {
                            updateNeighbours(row, col);
                        }
                    } else {
                        updateNeighbours(row, col);
                    }
                }
            }
        }
    }

    private void updateNeighbours(int row, int col) {
        addNeighbour(row - 1, col - 1);
        addNeighbour(row - 1, col);
        addNeighbour(row - 1, col + 1);
        addNeighbour(row, col - 1);
        addNeighbour(row, col + 1);
        addNeighbour(row + 1, col - 1);
        addNeighbour(row + 1, col);
        addNeighbour(row + 1, col + 1);
    }

    public void nextGeneration(int start, int stop) {
        for (int row = start; row < stop; row++) {
            for (int col = 0; col < board.getColumns(); col++) {
                board.setCellAliveState(row, col,
                    (getNeighbours(row, col) == 3) || (board.getCellAliveState(row, col) == 1 &&
                        (getNeighbours(row, col) == 2 )) ? (byte)1 : (byte)0);
            }
        }
    }

    private void clearNeighbours(int start, int stop) {
        for (int row = start; row < stop; row++) {
            for (int col = 0; col < neighbourArray.get(0).size(); col++) {
                neighbourArray.get(row).set(col, (byte) 0);
            }
        }
    }

    private int getNeighbours(int row, int col) {
        return neighbourArray.get(row + 1).get(col + 1);
    }

    private void addNeighbour(int row, int col) {
        byte neighbours =neighbourArray.get(row + 1).get(col + 1);
        neighbourArray.get(row + 1).set(col + 1, (byte) (neighbours + 1));
    }
}
