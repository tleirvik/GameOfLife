package controller;


import game.StaticBoard;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Duration;

public class MainWindowController {

	/**
     * Klassevariabler for klassen Rutenett04
     */
    @FXML private Canvas canvas;
    //private Resizable resizable;
    @FXML private Button playButton;
    @FXML private ColorPicker colorPicker = new ColorPicker();
    @FXML private ColorPicker cellColor = new ColorPicker();
    private final int cellSize = 60;
    private boolean[][] currentCell = new boolean[cellSize][cellSize];
    private boolean[][] nextCell = new boolean[cellSize][cellSize];
    private Timeline timeline;
    private boolean playGame;
    private boolean drawGrid;
    @FXML private ColorPicker gridColor = new ColorPicker();


    /**
     * Metoden er referert til fra fxml dokumentet.
     * En attributt i Canvas-taggen
     * @param e
     */
    @FXML
    public void handleMouseClick(MouseEvent e) {
        int j = cellSize * (int) e.getX() / canvas.widthProperty().intValue();
        int i = cellSize * (int) e.getY() / canvas.heightProperty().intValue();
        currentCell[i][j] = !currentCell[i][j];
        draw();
    }

    @FXML
    public Color handleCellColor() {
        //drawGrid = true;
        return cellColor.getValue();
    }

    @FXML
    public Color handleGridClr() {
        return gridColor.getValue();
    }

    @FXML
    public void resetGrid() {
        currentCell = new boolean[cellSize][cellSize];
        draw();
    }

    @FXML
    public void handleColorPicker() {

    }

    public void handlePlayButton() {
        playGame = !playGame;
        if(playGame) {
            timeline = new Timeline();
            timeline.setCycleCount(Animation.INDEFINITE);
            Duration duration = Duration.millis(130);
            KeyFrame keyFrame = new KeyFrame(duration, (ActionEvent e) -> {
                nextGeneration();
            });

            timeline.getKeyFrames().add(keyFrame);
            timeline.play();
        } else {
            timeline.pause(); // endret til det nye test.fxml dokumentet
            draw();
        }
    }

    @FXML
    public void handlePauseButton() {
        playGame = !playGame;
        timeline.pause();
    }

    private void print() {
        System.out.println("Hei " );
    }

    /**
     * Farger ruten som trykkes på spillbrettet
     */
    public void draw() {
       GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.widthProperty().intValue(),
                canvas.heightProperty().intValue());
        for(int i = 0; i < cellSize; i++) {
            for(int j = 0; j < cellSize; j++) {
                if(currentCell[i][j]) {
                    gc.setFill(Color.GREEN);
                    int x = j * (int) canvas.getWidth() / cellSize;
                    int y = i * (int) canvas.getHeight() / cellSize;
                    gc.fillRect(x, y, (int) (canvas.getWidth() - 2) / cellSize,
                            (int) (canvas.getHeight() - 2) / cellSize);
                    //System.out.println("i = " + i + ". j = " + j);
                }
            }
        }
        gc.setStroke(handleGridColor());
        gc.setLineWidth(2);
        draw(gc);
    }

    /**
     * Tegner rutenettet
     * @param gc
     */
    public void draw(GraphicsContext gc) {
        if(cellSize > 100)
            gc.setLineWidth(1.0);

        for(int x = 0; x <= cellSize; x++) {
            int xPos = x * (int) canvas.getWidth() / cellSize;
            gc.strokeLine(xPos, 0, xPos, (int) canvas.getHeight());
        }

        for(int y = 0; y <= cellSize; y++) {
            int yPos = y * (int) canvas.getHeight() / cellSize;
            gc.strokeLine(0, yPos, (int) canvas.getWidth(), yPos);
        }
    }

    public void nextGeneration() {
        for(int i = 0; i < cellSize; i++) {
            for(int j = 0; j < cellSize; j++) {
                nextCell[i][j] = liveOrDie(i, j);
            }
        }
        for(int i = 0; i < cellSize; i++) {
            System.arraycopy(nextCell[i], 0, currentCell[i], 0, cellSize);
        }
        draw();
    }

    /**
     * Avgjør om cellen skal leve eller dø
     * @param i
     * @param j
     * @return
     */
    public boolean liveOrDie(int i, int j) {
        int neighbors = 0;
        if(j > 0) {
            if(currentCell[i][j - 1]) {
                neighbors++;
            }
            if(i > 0) {
                if(currentCell[i - 1][j - 1])
                    neighbors++;
            }
            if(i < cellSize - 1) {
                if(currentCell[i + 1][j - 1])
                    neighbors++;
            }
        }
        if(j < cellSize - 1) {
            if(currentCell[i][j + 1])
                neighbors++;
            if(i > 0) {
                if(currentCell[i - 1][j + 1])
                    neighbors++;
            }
            if(i < cellSize - 1) {
                if(currentCell[i + 1][j + 1])
                    neighbors++;
            }
        }

        if(i > 0) {
            if(currentCell[i - 1][j])
                neighbors++;
        }
        if(i < cellSize - 1) {
            if(currentCell[i + 1][j])
                neighbors++;
        }

        if(neighbors == 3)
            return true;

        return currentCell[i][j] && neighbors == 2;
    }

    public Color handleGridColor() {
        return gridColor.getValue();
    }
}