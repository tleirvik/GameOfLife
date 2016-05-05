package model.gameoflife.algorithms;

import model.gameoflife.boards.Board;
import java.util.ArrayList;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * This is the default implementation of Conway S23/B3 Game Of Life rules and it implements the {@link Algorithm}
 * interface
 * @see Algorithm
 */
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

    /**
     * Method that checks if we need to expand the game board before running the algorithm
     */
    @Override
    public void beforeUpdate() {
        board.beforeUpdate();
        checkNeighbourArraySize();
    }

    /**
     * This method checks if we need to expand the game board and runs {@link #countNeighbours(int, int)},
     * {@link #nextGeneration(int, int)} which is our algorithm. Lastly it clears the neighbour array using
     * {@link #clearNeighbours(int, int)}
     */
    @Override
    public void update() {        
        checkNeighbourArraySize();
        
        countNeighbours(0, board.getRows());

        nextGeneration(0, board.getRows());

        clearNeighbours(0, neighbourArray.size());
    }

    /**
     * This is the concurrent version of our algorithm and each thread will call this method and run the algorithm on
     * a specified segment of the game board. The {@link CyclicBarrier} synchronizes the threads and makes them wait
     * before calling the {@link #nextGeneration(int, int)} method. We also use {@link CyclicBarrier} to prevent
     * synchronizing problems when clearing the neighbour array in the {@link #clearNeighbours(int, int)} method
     *
     * @param start The start position of the segment
     * @param stop The end position of the segment
     * @param clearStart The start of the clear neighbours method
     * @param clearStop The end of the clear neighbours method
     * @param barrier The {@link CyclicBarrier} to use in the algorithm
     */
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
        clearNeighbours(start, stop);
    }

    /**
     * Checks the size of the neighbor array and expands or shrinks it if necessary
     */
    private void checkNeighbourArraySize() {
        final int neighbourArrayRows = neighbourArray.size() - 2;
        final int neighbourArrayColumns = neighbourArray.get(0).size() - 2;
        if (neighbourArrayRows < board.getRows()) {
            final int diff = board.getRows() - neighbourArrayRows;
            final int columns = neighbourArray.get(0).size();
            for (int row = 0; row < diff; row++) {
                neighbourArray.add(0, new ArrayList<>());
                for (int col = 0; col < columns; col++) {
                    neighbourArray.get(0).add((byte) 0);
                } 
            }
        } else if (neighbourArrayRows > board.getRows()) {
            final int diff = neighbourArrayRows - board.getRows();
            for (int row = 0; row < diff; row++) {
                neighbourArray.remove(0);
            }
        }
        
        if (neighbourArrayColumns < board.getColumns()) {
            final int diff = board.getColumns() - neighbourArrayColumns;
            for (int row = 0; row < diff; row++) {
                neighbourArray.stream().forEach((e) -> {
                    e.add((byte) 0);
                });
            }
        } else if (neighbourArrayColumns > board.getColumns()) {
            final int diff = neighbourArrayColumns - board.getColumns();
            for (int row = 0; row < diff; row++) {
                neighbourArray.stream().forEach((e) -> {
                    e.remove(0);
                });
            }
        }
    }

    /**
     * This is a synchronized method that counts cells that are alive within a segment.
     * The access to {@link #updateNeighbours(int, int)} is restricted so that we don't get problems with
     * threads interfering with each other
     * @param start Start of the segment
     * @param stop The end of the segment
     * @see Thread
     */
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

    /**
     * Method that adds neighbors to the neighbor array
     * @param row The specified row
     * @param col The specified column
     */
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

    /**
     * The single thread Game Of Life algorithm. Sets the life of the cell according to the S23/B3
     * @param start Start of the segment
     * @param stop The end of the segment
     */
    public void nextGeneration(int start, int stop) {

        for (int row = start; row < stop; row++) {

            for (int col = 0; col < board.getColumns(); col++) {
                int neighbours = getNeighbours(row, col);
                if (neighbours < 2 && neighbours > 3) {
                    continue;
                }
                board.setCellAliveState(row, col,
                (neighbours == 3) || (board.getCellAliveState(row, col) == 1 &&
                        (neighbours == 2 )) ? (byte)1 : (byte)0);
            }
        }
    }

    /**
     * Method that clears the neighbor array in the given positions
     * @param start Start of the segment
     * @param stop The end of the segment
     */
    private void clearNeighbours(int start, int stop) {
        for (int row = start; row < stop; row++) {
            for (int col = 0; col < neighbourArray.get(0).size(); col++) {
                    neighbourArray.get(row).set(col, (byte) 0);
            }
        }
    }

    /**
     * Returns the cell status on the given position
     * @param row The specified row position
     * @param col The specified column position
     * @return The status of a cell on a given position
     */
    private int getNeighbours(int row, int col) {
        return neighbourArray.get(row + 1).get(col + 1);
    }

    /**
     * Adds a neighbour cell on a given position
     * @param row The specified row position
     * @param col The specified column position
     */
    private void addNeighbour(int row, int col) {
        byte neighbours =neighbourArray.get(row + 1).get(col + 1);
        neighbourArray.get(row + 1).set(col + 1, (byte) (neighbours + 1));
    }
}
