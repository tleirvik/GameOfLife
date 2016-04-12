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
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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
    @FXML private Canvas strip;
    @FXML private Button closeButton;
    @FXML private Button updateStripBtn;
    @FXML private HBox stripBox;
    @FXML private TextField authorTextField;
    @FXML private TextField titleTextField;
    @FXML private TextField descriptionTextField;
    @FXML private TextField rulesTextField;

    private double cellSize;
    private double cellSizeStrip;
    private FixedBoard board;
    private MetaData metaData;

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
        int row = (int) (e.getY() / cellSize);
        int col = (int) (e.getX() / cellSize);
        
        if(row < board.getRows() && col < board.getColumns()) {
            board.setCellAliveState(row, col,(board.getCellAliveState(row, col) == 1) ? (byte) 0 : (byte) 1);
            draw();
        }
    }
    
    
    
    // sette brettet her?
    public void setPattern(Board board) {
        final int rows = board.getRows();
        final int columns = board.getColumns();
        final byte[][] tempBoard = new byte[rows][columns];
        
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                tempBoard[row][col] = board.getCellAliveState(row, col);
            }
        }
        
        metaData = board.getMetaData();
        this.board = new FixedBoard(tempBoard, metaData);

        double cellWidth = patternCanvas.getWidth() / (rows);
        double cellHeight = patternCanvas.getHeight() / (columns);
        cellSize = (cellWidth < cellHeight) ? cellWidth : cellHeight;

        authorTextField.setText(metaData.getAuthor());
        descriptionTextField.setText(metaData.getComment());
        titleTextField.setText(metaData.getName());
        rulesTextField.setText("S" + metaData.getRuleString()[0] + "/B" + metaData.getRuleString()[1]);

        draw();
    }
    
    @FXML
    public void updateStrip() {
        final double stripCellSize = strip.getHeight() / (board.getRows());
        final double generationWidth = stripCellSize * (board.getColumns());
        final double padding = 25;
        strip.setWidth((generationWidth + padding) * 20);
        double offset_X = 0;
        
        final GraphicsContext gc = strip.getGraphicsContext2D();
        gc.clearRect(0, 0, strip.widthProperty().doubleValue(), 
                strip.heightProperty().doubleValue());
        
        for(int i = 0; i < 20; i++) {
            board.nextGeneration();
            drawStrip(gc, offset_X, stripCellSize);
            offset_X += generationWidth + padding;
        }
        
        board.resetBoard();
    }
    
    private void drawStrip(GraphicsContext gc, double offset_X, double stripCellSize) {
        boolean isGenerationAlive = false;
        
        final int rows = board.getRows();
        final int columns = board.getColumns();
        double x = offset_X;
        double y = 0;

        for(int row = 0; row < rows; row++) {
            for(int col = 0; col < columns; col++) {
                if (board.getCellAliveState(row, col) == 1) {
                    gc.fillRect(x, y, stripCellSize, stripCellSize);
                }
                x += stripCellSize; // Plusser på for neste kolonne
            }
            x = offset_X; // Reset X-verdien for neste rad
            y += stripCellSize; // Plusser på for neste rad
        }
        
        final double start_x = offset_X;
        final double start_y = 0;
        final double end_y = stripCellSize * rows;
        final double end_x = offset_X + stripCellSize * columns;
        
        // tegner en ramme rundt hver generasjon
        // topp
        gc.strokeLine(start_x, start_y, end_x, start_y);
        // venstre
        gc.strokeLine(start_x, start_y, start_x, end_y);
        // høyre
        gc.strokeLine(end_x, start_y, end_x, end_y);
        // bunn
        gc.strokeLine(start_x, end_y, end_x, end_y);
    }
    
    // tegne mønsteret
    private void draw() {
        final int rows = board.getRows();
        final int columns = board.getColumns();
        
        final GraphicsContext gc = patternCanvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, patternCanvas.getWidth(), 
                patternCanvas.getHeight());

        double x = 0;
        double y = 0;
        
        gc.setFill(Color.BLACK);
        for(int row = 0; row < rows; row++) {
            for(int col = 0; col < columns; col++) {
                if (board.getCellAliveState(row, col) == 1) {
                    gc.fillRect(x, y, cellSize, cellSize);
                }
                x += cellSize; // Plusser på for neste kolonne
            }
            x = 0; // Reset X-verdien for neste rad
            y += cellSize; // Plusser på for neste rad
        }
        drawGridLines(gc);
    }
    
    // bør ha gridlines for å gjøre manipulering mer lesbar.
    private void drawGridLines(GraphicsContext gc) {
        final int rows = board.getRows();
        final int columns = board.getColumns();
        final double height = getPatternHeight();
        final double width = getPatternWidth();
        
        gc.setLineWidth(0.6);
        gc.setStroke(Color.GRAY);
        
        // For hver kolonne, tegn en vertikal strek
        for(int col = 0; col <= columns; col++) {
            gc.strokeLine(cellSize * col, 0, cellSize * col, height);   
        }
        
        for(int row = 0; row <= rows; row++) {
            gc.strokeLine(0, cellSize * row, width, cellSize * row);
        }
    }
    
    private double getPatternWidth() {
        return cellSize * board.getColumns();
    }
    
    private double getPatternHeight() {
        return cellSize * board.getRows();
    }
}
