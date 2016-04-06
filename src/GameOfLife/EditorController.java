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
import javafx.scene.input.MouseEvent;
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
    
    @FXML
    public void handleMouseClick(MouseEvent e) {
        
//        int column = board.getColumns() * (int) evt.getX() / 
//                super.getCanvas().widthProperty().intValue();
//        int row = (int) board.getRows() * (int) evt.getY() / 
//                super.getCanvas().heightProperty().intValue();
        
        int row = (int) (e.getY() / cellSize) + 1;
        int col = (int) (e.getX() / cellSize) + 1;
        
        pattern[row][col] = (pattern[row][col] == 1) ? (byte) 0 : (byte) 1;
        
        draw();
    }
    
    // sette brettet her?
    public void setPattern(byte[][] pattern) {
        if(pattern != null) {
            this.pattern = pattern;
            double cellWidth = patternCanvas.getWidth() / pattern.length;
            double cellHeight = patternCanvas.getHeight() / pattern[0].length;
            this.cellSize = (cellWidth < cellHeight) ? cellWidth : cellHeight;
            
            draw();
        }
    }
    
    // tegne mønsteret
    private void draw() {
        gc = patternCanvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, patternCanvas.getWidth(), 
                patternCanvas.getHeight());

        double x = 0;
        double y = 0;
        
        gc.setFill(Color.VIOLET);
        for(int row = 1; row < pattern.length - 1; row++) {
            for(int col = 1; col < pattern[0].length - 1; col++) {
                if (pattern[row][col] == 1) {
                    gc.fillRect(x, y, cellSize, cellSize);
                }
                x += cellSize; //Plusser på for neste kolonne
            }
            x = 0; //Reset X-verdien for neste rad
            y += cellSize; //Plusser på for neste rad
        }
        drawGridLines(gc);
    }
    
    // bør ha gridlines for å gjøre manipulering mer lesbar.
    private void drawGridLines(GraphicsContext gc) {
        gc.setLineWidth(0.6);
        gc.setStroke(Color.GRAY);
        
        final double height = getPatternHeight();
        final double width = getPatternWidth();
        
        
        // For hver kolonne, tegn en vertikal strek
        for(int col = 0; col <= pattern[0].length - 2; col++) {
            gc.strokeLine(cellSize * col, 0, cellSize * col, height);   
        }
        
        for(int row = 0; row <= pattern.length - 2; row++) {
            gc.strokeLine(0, cellSize * row, width, cellSize * row);
        }
    }
    
    private double getPatternWidth() {
        return cellSize * (pattern[0].length - 2);
    }
    
    private double getPatternHeight() {
        return cellSize * (pattern.length - 2);
    }
}
