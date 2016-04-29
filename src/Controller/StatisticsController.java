package Controller;

import Model.GameOfLife.GameOfLife;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextField;
import javafx.util.Pair;

/**
 * FXML Controller class
 *
 * @author Stian Reistad Røgeberg, Terje Leirvik, Robin Sean Aron David Lundh
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
    
    public void initializeStatistics(GameOfLife game) {
        this.game = game.clone();
        livingCells.setName("Number of Living Cells");
        diffLivingCells.setName("Difference in living cells");
        similarity.setName("Similarity Measure");
        //xAxis.setTickLabelFormatter(S);
        lineChart.getData().add(livingCells);
        lineChart.getData().add(diffLivingCells);
        lineChart.getData().add(similarity);
        
        numberOfIterations.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.matches("[0-9]*")) {
                numberOfIterations.setText(oldValue);
            }
        });
    }
    
    @FXML
    public void viewStats() {
        String iterationString = numberOfIterations.getText();
        if(!iterationString.isEmpty()) {
            iterations = Integer.parseInt(iterationString);
            int[][] stats = collectStatistics(iterations);
            drawStats(stats);
        }
    }

    public int[][] collectStatistics(int iterations) {
        int[][] stats = new int[4][iterations];

        for (int i = 0; i < iterations; i++) {
            final Pair<Integer, Integer> livingCellsData = getLivingCellsData();
            stats[0][i] = livingCellsData.getKey();
            stats[3][i] = livingCellsData.getValue();
            //game.getBoard().nextGeneration();
        }

        game.resetGame();

        for (int i = 0; i < iterations; i++) {
            if ((i + 1) < iterations) {
                stats[1][i] = stats[0][i + 1] + stats[0][i];
            }
            int similarity1 = (int) (0.5 * stats[0][0] + 3.0 * stats[1][i] + 0.25 * stats[3][i]);
            int similarity2 = (int) (0.5 * stats[0][i] + 3.0 * stats[1][i] + 0.25 * stats[3][i]);
            int calculateSimilarity = Math.min(similarity1, similarity2) / Math.max(similarity1, similarity2);
            int calculatePercent = (int) Math.floor(calculateSimilarity * 100);
            stats[2][i] = calculatePercent;
        }

        return stats;
    }

    private void drawStats(int[][] stats) {
        livingCells.getData().clear();
        diffLivingCells.getData().clear();
        similarity.getData().clear();
        for(int i = 0; i < iterations; i++) {
            livingCells.getData().add(new XYChart.Data(i, stats[0][i]));
            diffLivingCells.getData().add(new XYChart.Data(i, stats[1][i]));
            similarity.getData().add(new XYChart.Data(i, stats[2][i]));
        }
    }

    private Pair<Integer,Integer> getLivingCellsData() {
        int livingCells = 0;
        int livingCellPosition = 0;

        for (int i = 0; i < game.getBoard().getRows(); i++) {
            for (int j = 0; j < game.getBoard().getColumns(); j++) {
                if(game.getCellAliveState(i, j) == 1) {
                    livingCells++;
                    livingCellPosition += i + j;
                }
            }
        }
        return new Pair(livingCells, livingCellPosition);
    }

}
