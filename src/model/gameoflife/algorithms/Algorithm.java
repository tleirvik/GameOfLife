package model.gameoflife.algorithms;

import java.util.concurrent.CyclicBarrier;

/**
 * Interface for implementing the Game Of Life algorithm
 */
public interface Algorithm {

    /**
     * Method that does necessary checking of edges and if we need to expand or shrink the game board
     */
    void beforeUpdate();

    /**
     * Method that runs the algorithm
     */
    void update();

    /**
     * Method that runs the algorithm multithreaded
     *
     * @param start The start position of the segment
     * @param stop The end position of the segment
     * @param clearStart The start of the clear neighbours method
     * @param clearStop The end of the clear neighbours method
     * @param barrier The {@link CyclicBarrier} to use in the algorithm
     */
    void updateConcurrent(int start, int stop, int clearStart, int clearStop, CyclicBarrier barrier);
}
