package Model.GameOfLife;

import java.util.stream.IntStream;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author stianreistadrogeberg
 */
public class Statistics {
    private final GameOfLife game;
    private final int iterations;
    private final int[] numberOfLivingCells;
    private final int[] geometrics;
    private final int[] diffInLivingCells;
    private final int[] similarity;
    
    public Statistics(GameOfLife game, int iterations) {
        this.game = game;
        this.iterations = iterations;
        numberOfLivingCells = new int[iterations];
        geometrics = new int[iterations];
        diffInLivingCells = new int[iterations];
        similarity = new int[iterations];

        collectStatistics();
    }
    
    private void collectStatistics() {
        //Living cells and position
        for (int i = 0; i < iterations; i++) {
            for (int row = 0; row < game.getRows(); row++) {
                for (int col = 0; col < game.getColumns(); col++) {
                    if (game.getCellAliveState(row, col) == 1) {
                        numberOfLivingCells[i]++;
                        geometrics[i] += row + col;
                    }
                }   
            }
            System.out.println(geometrics[i] + " " + i);
            game.update();
        }
        game.resetGame();
        
        if(IntStream.of(numberOfLivingCells).sum() == 0) {
            return;
        }
        
        for (int i = 0; i < iterations; i++) {
            if ((i + 1) < iterations) {
                diffInLivingCells[i] = numberOfLivingCells[i + 1] - numberOfLivingCells[i];
            }
            double similarity1 = (0.5 * numberOfLivingCells[0] + 3.0 * diffInLivingCells[i] + 0.25 * geometrics[i]);
            double similarity2 = (0.5 * numberOfLivingCells[i] + 3.0 * diffInLivingCells[i] + 0.25 * geometrics[i]);
            double calculateSimilarity = Math.min(similarity1, similarity2) / Math.max(similarity1, similarity2);
            int calculatePercent = (int) Math.floor(calculateSimilarity * 100);
            similarity[i] = calculatePercent;
        }
    }

    /**
     * @return the iterations
     */
    public int getIterations() {
        return iterations;
    }

    /**
     * @return the number of living cells
     */
    public int[] getNumberOfLivingCells() {
        return numberOfLivingCells;
    }

    /**
     * @return the difference in living cells
     */
    public int[] getDiffInLivingCells() {
        return diffInLivingCells;
    }

    /**
     * @return the similarity
     */
    public int[] getSimilarity() {
        return similarity;
    }
    
    /**
     * @return the similarity
     */
    public int[] getGeometrics() {
        return geometrics;
    }
    
    
    
}
