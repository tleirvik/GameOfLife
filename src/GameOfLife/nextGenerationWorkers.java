/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameOfLife;

import GameOfLife.Boards.Board;
import java.util.ArrayList;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author stianreistadrogeberg
 */
public class nextGenerationWorkers {
    private final Board board;
    private final ArrayList<Thread> workers;
    private final ArrayList<Thread> neighbourWorkers;
    private final int numWorkers;
    private int[][] segments;

    public nextGenerationWorkers(int numWorkers, Board board) {
        workers = new ArrayList<>();
        neighbourWorkers = new ArrayList<>();

        this.numWorkers = numWorkers;
        segments = new int[numWorkers][2];
        this.board = board;
        splitBoard();
        for (int i = 0; i < numWorkers; i++) {
            final int start = segments[i][0];
            final int stop = segments[i][1];
            neighbourWorkers.add(new Thread(() -> {
                board.countNeighboursConcurrent(start,stop);
            }));
            workers.add(new Thread(() -> {
                board.nextGenerationConcurrent(start, stop);
            }));
        }
    }

    public void runWorkers() throws InterruptedException {
        for (Thread t : neighbourWorkers) {
            t.start();
        }

        for (Thread t : neighbourWorkers) {
            t.join();
        }

        for(Thread t : workers) {
            t.start();
        }

        for(Thread t : workers) {
            t.join();
        }
    }

    private void splitBoard() {
        int rowLength = board.getRows(); // -1 for å lage en splitt mellom segmentene
        int chunkSize = rowLength / numWorkers;
        int rowsLeft = rowLength % numWorkers;
        for (int i = 0; i < segments.length; i++) {
            segments[i][0] = chunkSize * i;
            segments[i][1] = segments[i][0] + chunkSize;
        }
    }
}
