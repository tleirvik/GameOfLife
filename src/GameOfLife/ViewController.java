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

    /*
     *		v LISTENERS v
     */

    @FXML
    public void newGame() {
    	//Lag pop-up-box der brukeren kan velge parametre til spillet
        gController.newGame(); //send parametrene videre
        gc = gameCanvas.getGraphicsContext2D();
    }

    @FXML
    public void play() {
        boolean [][] grid = gController.play();
        
        draw(grid);
    }
    
    
    /*
     *		^ LISTENERS ^
     */
    
    
    /**
     * DRAW-METODER BURDE VÆRE STATIC (?) SÅ DE KAN KALLES FRA GAMECONTROLLER
     */
    public void draw(boolean[][] grid) {
        gc.clearRect(0, 0, gameCanvas.widthProperty().intValue(),
        gameCanvas.heightProperty().intValue());
        
        
        for(int row = 0; row < grid.length; row++) {
    		for(int col = 0; col < grid[row].length; col++) {
                if(grid[row][col]) {
                	int x = row * (int) (gameCanvas.getWidth() / 10);
                	int y = col * (int) (gameCanvas.getWidth() / 10);
                	
                	gc.setFill(Color.WHITE);
                	gc.fillRect(x, y,(int) (gameCanvas.getWidth() / 10),(int) (gameCanvas.getWidth() / 10));
                }
    		}
    	}
        
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
