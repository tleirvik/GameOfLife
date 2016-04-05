/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameOfLife;

import java.io.File;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Stian Reistad Røgeberg, Terje Leirvik, Robin Sean Aron Lundh
 * 
 * This controller class controls handles all the actions performed in
 * the its related view.
 */
public class EditorController {
    @FXML private BorderPane patternController;
    @FXML private Canvas patternCanvas;
    @FXML private Button closeButton;
    
    private GraphicsContext gc;
    private byte[][] pattern;
    private double cellSize;
    
    
    /**
     * This method closes the editor window.
     */
    @FXML
    public void handleCloseButton() {
        Stage s = (Stage) closeButton.getScene().getWindow();
        s.close();
    }
    
    @FXML
    public void handleSaveButton() {
        Stage mainStage = (Stage) patternCanvas.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
    	fileChooser.setTitle("Save RLE Pattern to file");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("RLE files", "*.rle"));
        File saveRLEFile = fileChooser.showSaveDialog(mainStage);
    }
    
    // sette brettet her?
    public void setPattern(byte[][] pattern, double cellSize) {
        this.pattern = pattern;
        this.cellSize = cellSize;
        if(pattern != null && cellSize != 0) {
            draw();
        }
    }
    
    // tegne mønsteret
    private void draw() {
        gc = patternCanvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, patternCanvas.getWidth(), 
                patternCanvas.getHeight());
        
        for (int i = 0; i < pattern.length; i++) {
            for(int j = 0; j < pattern[0].length; j++) {
                if(pattern[i][j] == 1) {
                    gc.setFill(Color.VIOLET);
                    double x = Math.round(j * patternCanvas.getWidth() / pattern[0].length);
                    double y = Math.round(i * patternCanvas.getHeight() / pattern[i].length);
                    gc.fillRect(x, y, Math.round(patternCanvas.getWidth() / pattern[0].length),
                            Math.round(patternCanvas.getHeight() / pattern[i].length));
                }
            }
        }
        draw(gc);
    }
    
    // bør ha gridlines for å gjøre manipulering mer lesbar.
    private void draw(GraphicsContext gc) {
        gc.setLineWidth(0.6);
        gc.setStroke(Color.GRAY);

//        for(int y = 0; y <= pattern.length; y++) {
//            gc.strokeLine(0, 0 + (cellSize * y),
//                    patternCanvas.getWidth(), patternCanvas.getHeight());
//        }
    }
}
