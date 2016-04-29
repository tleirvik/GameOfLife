/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.GameOfLife;

import Model.GameOfLife.Algorithms.Algorithm;
import Model.GameOfLife.Boards.Board;
import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;

/**
 *
 * @author stianreistadrogeberg
 */
public final class NextGenerationWorkers {
    private final Board board;
    private final Algorithm algorithm;
    private final ArrayList<Thread> workers;
    private final int numWorkers;
    private int[][] segments;
    private CyclicBarrier barrier;

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

    public CyclicBarrier getBarrier() {
        return barrier;
    }
    public void createWorkers() {
        for (int i = 0; i < numWorkers; i++) {
            final int start = segments[i][0];
            final int stop = segments[i][1];
            final int clearStart = segments[i][2];
            final int clearStop = segments[i][3];
            System.out.println("start:" + start + " stop:" + stop);
            workers.set(i, new Thread(new NextGenerationRunnable(start, stop, clearStart, clearStop, barrier, algorithm)));
        }
    }

    public void runWorkers() throws InterruptedException {
        workers.stream().forEach((t) -> {
            t.start();
        });

        for(Thread t : workers) {
            t.join();
        }
    }

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
    private static class NextGenerationRunnable implements Runnable {
        private CyclicBarrier barrier;
        private final int start;
        private final int stop;
        private final int clearStart;
        private final int clearStop;
        private final Algorithm algorithm;


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

