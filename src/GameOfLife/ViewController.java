package GameOfLife;

import java.util.Random;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 *Denne klassen lytter på hendelser i .fxml
 *
 */
public class ViewController {

    @FXML
    private Canvas gameCanvas;
    private final GameController gController = new GameController();
    private GraphicsContext gc;
    
    private int row = 10;
    private int column = 10;


    @FXML
    public void newGame() {
        gController.newGame();
        gc = gameCanvas.getGraphicsContext2D();
    }

    @FXML
    public void play() {
        gController.play();
        draw();
    }
    /**
     *
     */
    public void draw() {
        gc.clearRect(0, 0, gameCanvas.widthProperty().intValue(),
                gameCanvas.heightProperty().intValue());
        gc.setStroke(Color.RED);
        gc.setLineWidth(2.0);
        draw(gc);
    }

    /**
     *
     * @param gc
     */
    public void draw(GraphicsContext gc) {
        
        for(int x = 0; x <= row; x++) {
            int xPos = x * (int) gameCanvas.getWidth() / row;
            gc.strokeLine(xPos, 0, xPos, (int) gameCanvas.getHeight());
        }
        
        for(int y = 0; y <= column; y++) {
            int yPos = y * (int) gameCanvas.getHeight() / column;
            gc.strokeLine(0, yPos, (int) gameCanvas.getWidth(), yPos);
        }
    }
}
