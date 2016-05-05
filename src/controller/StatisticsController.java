package controller;

import model.gameoflife.Statistics;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextField;
import model.gameoflife.GameOfLife;

/**
 * FXML Controller class for the Pattern Editor
 */
public class StatisticsController {
    private final XYChart.Series livingCells = new XYChart.Series();
    private final XYChart.Series diffLivingCells = new XYChart.Series();
    private final XYChart.Series similarity = new XYChart.Series();
    private GameOfLife game;
    private int iterations;
    
    @FXML private TextField numberOfIterations;
    @FXML private LineChart lineChart;
    @FXML private NumberAxis xAxis;

    /**
     * Initializes the Statistics view and loads the game to collect statistics from
     * @param game The {@link GameOfLife} object to collect statistics from
     */
    public void initializeStatistics(GameOfLife game) {
        this.game = game.clone();
        
        livingCells.setName("Number of Living Cells");
        diffLivingCells.setName("Difference in living cells");
        similarity.setName("Similarity Measure");
        lineChart.getData().add(livingCells);
        lineChart.getData().add(diffLivingCells);
        lineChart.getData().add(similarity);
        
        numberOfIterations.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.matches("[0-9]*")) {
                numberOfIterations.setText(oldValue);
            }
        });
    }

    /**
     * Presents the statistics by instantiating a new {@link Statistics} object with {@link GameOfLife} and number
     * of iterations as parameters
     * @see Statistics
     */
    @FXML
    public void viewStats() {
        String iterationString = numberOfIterations.getText();
        if(!iterationString.isEmpty()) {
            iterations = Integer.parseInt(iterationString);
            Statistics statistics = new Statistics(game, iterations);
            drawStats(statistics);
        }
    }

    /**
     * Draws the statistics on a {@link XYChart}
     * @param statistics The {@link Statistics} object which holds the statistics data
     * @see Statistics
     * @see XYChart
     */
    private void drawStats(Statistics statistics) {
        livingCells.getData().clear();
        diffLivingCells.getData().clear();
        similarity.getData().clear();
        for(int i = 0; i < iterations; i++) {
            int[] numberOfLivingCells = statistics.getNumberOfLivingCells();
            int[] diffInLivingCells = statistics.getDiffInLivingCells();
            int[] sim = statistics.getSimilarity();
            livingCells.getData().add(new XYChart.Data(i, numberOfLivingCells[i]));
            if ((i + 1) < iterations) {
                diffLivingCells.getData().add(new XYChart.Data(i, diffInLivingCells[i]));
            }
            similarity.getData().add(new XYChart.Data(i, sim[i]));
        }
    }       
}
