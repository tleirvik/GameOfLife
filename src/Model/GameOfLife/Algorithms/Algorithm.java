package Model.GameOfLife.Algorithms;

import java.util.concurrent.CyclicBarrier;

public interface Algorithm {
    void update();
    void updateConcurrent(int start, int stop, int clearStart, int clearStop, CyclicBarrier barrier);
}
