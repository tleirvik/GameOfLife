package Model.GameOfLife.Algorithms;

import Model.GameOfLife.Boards.Board;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Default implements Algorithm{
    private final byte[][] neighbourArray;
    private Board board;

    public Default(Board board) {
        neighbourArray = new byte[board.getRows() + 2][board.getColumns() + 2];
        this.board = board;
    }

    public void update() {
        countNeighbours(0, board.getRows());

        nextGeneration(0, board.getRows());

        clearNeighbours(0, neighbourArray.length);
    }

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
        clearNeighbours(0, neighbourArray.length);
    }

    public void countNeighbours(int start, int stop) {
        for(int row = start; row < stop; row++) {
            for(int col = 0; col < board.getColumns(); col++) {
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
        addNeighbour(row-1, col-1);
        addNeighbour(row-1, col);
        addNeighbour(row-1, col+1);
        addNeighbour(row, col-1);
        addNeighbour(row, col+1);
        addNeighbour(row+1, col-1);
        addNeighbour(row+1, col);
        addNeighbour(row+1, col+1);
    }

    public void nextGeneration(int start, int stop) {
        for(int row = start; row < stop; row++) {
            for(int col = 0; col < board.getColumns(); col++) {
                board.setCellAliveState(row, col,
                        (getNeighbours(row, col) == 3) || (board.getCellAliveState(row, col) == 1 &&
                                (getNeighbours(row, col) == 2 )) ? (byte)1 : (byte)0);

            }
        }
    }

    private void clearNeighbours(int start, int stop) {
        for(int row = start; row < stop; row++) {
            for(int col = 0; col < neighbourArray[0].length; col++) {
                neighbourArray[row][col] = 0;
            }
        }
    }

    private int getNeighbours(int row, int col) {
        return neighbourArray[row + 1][col + 1];
    }

    private void addNeighbour(int row, int col) {
        neighbourArray[row + 1][col + 1]++;
    }
}
