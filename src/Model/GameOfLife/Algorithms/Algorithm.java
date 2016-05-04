package Model.GameOfLife.Algorithms;

import java.util.concurrent.CyclicBarrier;

/**
 * Interface for implementing the Game Of Life algorithm
 */
public interface Algorithm {

    /**
     * Method that does necessary checking of edges and if we need to expand or shrink the game board
     */
    void beforeUpdate();
    void update();
    void updateConcurrent(int start, int stop, int clearStart, int clearStop, CyclicBarrier barrier);
}
