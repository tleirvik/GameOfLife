package model.gameoflife;

import java.util.stream.IntStream;

/**
 * This class provides statistics for the game
 */
public class Statistics {
    private final GameOfLife game;
    private final int iterations;
    private final int[] numberOfLivingCells;
    private final int[] geometrics;
    private final int[] diffInLivingCells;
    private final int[] similarity;

    /**
     * Constructs a new {@link Statistics} object to be used for collecting statistics from
     * the specified game board
     *
     * @param game The {@link GameOfLife} game to be used for the statistics
     * @param iterations The number of generations used for the statistics
     */
    public Statistics(GameOfLife game, int iterations) {
        this.game = game;
        this.iterations = iterations;
        numberOfLivingCells = new int[iterations];
        geometrics = new int[iterations];
        diffInLivingCells = new int[iterations];
        similarity = new int[iterations];

        collectStatistics();
    }

    /**
     * This method collects statistics from the game board
     */
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
     * Return the number of living cells on the game board
     * @return the number of living cells
     */
    public int[] getNumberOfLivingCells() {
        return numberOfLivingCells;
    }

    /**
     * Return the difference in living cells between two generations
     * @return The difference in living cells data
     */
    public int[] getDiffInLivingCells() {
        return diffInLivingCells;
    }

    /**
     * Returns the similarity measure
     * @return The similarity measure data
     */
    public int[] getSimilarity() {
        return similarity;
    }
    
    /**
     * Returns the geometrics data
     * @return The geometrics data
     */
    public int[] getGeometrics() {
        return geometrics;
    }
    
    
    
}
