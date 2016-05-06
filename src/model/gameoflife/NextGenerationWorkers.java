package model.gameoflife;

import model.gameoflife.algorithms.Algorithm;
import model.gameoflife.boards.Board;
import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;
// TODO: 04.05.2016 JAVADOKKINGS TODO 
/**
 * This class constructs worker threads and splits the board into segments
 *
 * @see Thread
 * @see Board
 * @see Algorithm
 * @see Runnable
 */
public final class NextGenerationWorkers {
    private final Board board;
    private final Algorithm algorithm;
    private final ArrayList<Thread> workers;
    private final int numWorkers;
    private int[][] segments;
    private CyclicBarrier barrier;

    /**
     * This creates a spcified number of workers to use in the multi threaded algorithm
     *
     * @param numWorkers The number of workers to use
     * @param board The {@link Board} to use
     * @param algorithm The {@link Algorithm} to use
     */
    public NextGenerationWorkers(int numWorkers, Board board, Algorithm algorithm) {
        this.numWorkers = numWorkers;
        this.algorithm = algorithm;
        barrier = new CyclicBarrier(numWorkers);
        workers = new ArrayList<>();
        for (int i = 0; i < numWorkers; i++) {
            workers.add(null);
        }
        segments = new int[numWorkers][4];
        this.board = board;
        splitBoard();
    }

    /**
     * Returns the cyclic barrier that is used for restricting thread access
     *
     * @return The cyclic barrier
     * @see CyclicBarrier
     */
    public CyclicBarrier getBarrier() {
        return barrier;
    }

    /**
     * This method creates workers for each segment of the game board
     */
    public void createWorkers() {
        for (int i = 0; i < numWorkers; i++) {
            final int start = segments[i][0];
            final int stop = segments[i][1];
            final int clearStart = segments[i][2];
            final int clearStop = segments[i][3];
            workers.set(i, new Thread(new NextGenerationRunnable(start, stop, clearStart, clearStop, barrier, algorithm)));
        }
    }

    /**
     * Starts and joins the threads
     *
     * @throws InterruptedException Thrown if a thread is interrupted
     * @see Thread
     */
    public void runWorkers() throws InterruptedException {
        workers.stream().forEach((t) -> {
            t.start();
        });

        for(Thread t : workers) {
            t.join();
        }
    }

    /**
     * This method divides the game board into segments
     */
    public void splitBoard() {
        int boardRowLength = board.getRows();
        int boardChunkSize = boardRowLength / numWorkers;
        int boardRowsLeft = boardRowLength % numWorkers;

        int clearRowLength = board.getRows() + 2;
        int clearChunkSize = clearRowLength / numWorkers;
        int clearRowsLeft = clearRowLength % numWorkers;

        segments[0][0] = 0;
        segments[0][1] = boardChunkSize;
        segments[0][2] = 0;
        segments[0][3] = clearChunkSize;

        for (int i = 1; i < segments.length; i++) {
            segments[i][0] = segments[i - 1][1];
            segments[i][1] = segments[i][0] + boardChunkSize;
            if (boardRowsLeft > 0) {
                segments[i][1]++;
                boardRowsLeft--;
            }

            segments[i][2] = segments[i - 1][2];
            segments[i][3] = segments[i][2] + clearChunkSize;

        }
        if (clearRowsLeft > 0) {
            segments[segments.length-1][3]+= clearRowsLeft;
        }
    }

    /**
     * This class is the Next Generation Runnable
     */
    private static class NextGenerationRunnable implements Runnable {
        private CyclicBarrier barrier;
        private final int start;
        private final int stop;
        private final int clearStart;
        private final int clearStop;
        private final Algorithm algorithm;

        /**
         * Creates a {@link Runnable}
         *
         * @param start The start position of the board segment
         * @param stop The end position of the board segment
         * @param clearStart The position where the thread should start emptying the neighbour array
         * @param clearStop The position where the thread should stop emptying the neighbour array
         * @param barrier The cyclic barrier
         * @param algorithm The {@link Algorithm} to use
         */
        public NextGenerationRunnable(int start, int stop, int clearStart, int clearStop, CyclicBarrier barrier, Algorithm algorithm) {
            this.barrier = barrier;
            this.start = start;
            this.stop = stop;
            this.clearStart = clearStart;
            this.clearStop = clearStop;
            this.algorithm = algorithm;
        }

        @Override
        public void run() {
            algorithm.updateConcurrent(start, stop, clearStart, clearStop, barrier);
        }

    }
}

