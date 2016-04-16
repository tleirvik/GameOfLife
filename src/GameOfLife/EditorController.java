/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameOfLife;

import FileManagement.GIFSaver;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import util.DialogBoxes;

import java.io.File;

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
    @FXML private MenuItem saveToGif;

    private double cellSize;
    private double cellSizeStrip;
    private GameOfLife game;
    private MetaData metaData;
    private DialogBoxes dialogBoxes;

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
        
        if(row < game.getRows() && col < game.getColumns()) {
            game.setCellAliveState(row, col,(game.getCellAliveState(row, col) == 1) ? (byte) 0 : (byte) 1);
            draw();
        }
    }
    
    @FXML
    public void saveToGif() {  
        game.setFirstGeneration();
        dialogBoxes.saveToGIFDialog(new GIFSaver(game));
    }
    
    // sette brettet her?
    public void setPattern(GameOfLife game) {
        this.game = game.clone();
        metaData = game.getMetaData();
        game.getBoard().setIsDynamic(false);
        
        double cellWidth = patternCanvas.getWidth() / game.getRows();
        double cellHeight = patternCanvas.getHeight() / game.getColumns();
        cellSize = (cellWidth < cellHeight) ? cellWidth : cellHeight;

        authorTextField.setText(metaData.getAuthor());
        descriptionTextField.setText(metaData.getComment());
        titleTextField.setText(metaData.getName());
        rulesTextField.setText("S" + metaData.getRuleString()[0] + "/B" + metaData.getRuleString()[1]);

        draw();
        
        saveToGif.setAccelerator(new KeyCodeCombination(KeyCode.G, KeyCombination.SHORTCUT_DOWN));
    }
    
    @FXML
    public void updateStrip() {
        game.setFirstGeneration();
        final double stripCellSize = strip.getHeight() / game.getRows();
        final double generationWidth = stripCellSize * game.getColumns();
        final double padding = 25;
        strip.setWidth((generationWidth + padding) * 20);
        double offset_X = 0;
        
        final GraphicsContext gc = strip.getGraphicsContext2D();
        gc.clearRect(0, 0, strip.widthProperty().doubleValue(), 
                strip.heightProperty().doubleValue());
        
        for(int i = 0; i < 20; i++) {
            game.update();
            drawStrip(gc, offset_X, stripCellSize);
            offset_X += generationWidth + padding;
        }
        
        game.resetGame();
    }
    
    private void drawStrip(GraphicsContext gc, double offset_X, double stripCellSize) {
        boolean isGenerationAlive = false;
        
        final int rows = game.getRows();
        final int columns = game.getColumns();
        double x = offset_X;
        double y = 0;

        for(int row = 0; row < rows; row++) {
            for(int col = 0; col < columns; col++) {
                if (game.getCellAliveState(row, col) == 1) {
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

        drawPadding(gc, start_x, start_y, end_x, end_y);
    }

    public void drawPadding(GraphicsContext gc, double start_x, double start_y, double end_x, double end_y) {
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
        final int rows = game.getRows();
        final int columns = game.getColumns();
        
        final GraphicsContext gc = patternCanvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, patternCanvas.getWidth(), 
                patternCanvas.getHeight());

        double x = 0;
        double y = 0;
        
        gc.setFill(Color.BLACK);
        for(int row = 0; row < rows; row++) {
            for(int col = 0; col < columns; col++) {
                if (game.getCellAliveState(row, col) == 1) {
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
        final int rows = game.getRows();
        final int columns = game.getColumns();
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
        return cellSize * game.getColumns();
    }
    
    private double getPatternHeight() {
        return cellSize * game.getRows();
    }

    public void setDialogBoxes(DialogBoxes dialogBoxes) {
        this.dialogBoxes = dialogBoxes;
    }
    public void initialize() {
        //game.getBoard().setIsDynamic(false);
    }
}
