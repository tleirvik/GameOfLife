/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameOfLife;

import java.util.ArrayList;

/**
 *
 * @author stianreistadrogeberg
 */
public class nextGenerationWorkers {
    private Board board;
    private ArrayList<Thread> workers;
    
    public nextGenerationWorkers(int numWorkers, Board board) {
        this.board = board;
        for (int i = 0; i < 10; i++) {
            workers.add(new Thread(() -> {board.nextGenerationConcurrent();}));
        }
    }
    
    public void runWorkers() throws InterruptedException {
        for(Thread t : workers) {
            t.start();
        }
        for(Thread t : workers) {
            t.join();
        }
    }
}
