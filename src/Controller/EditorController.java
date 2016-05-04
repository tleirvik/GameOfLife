/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.FileManagement.EncodeType;
import Model.GameOfLife.Boards.Board;
import Model.GameOfLife.GameOfLife;
import Model.GameOfLife.MetaData;
import Model.util.DialogBoxes;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javafx.scene.control.Alert;

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
    private GameOfLife game;
    private MetaData metaData;
    private FileController fileController;
    
    public void initializeEditor(GameOfLife game, FileController fileController) {
        this.game = game.clone();
        this.fileController = fileController;
        
        setPattern();
    }
    

    /**
     * This method closes the editor window.
     */
    @FXML
    public void handleCloseButton() {
        Stage s = (Stage) closeButton.getScene().getWindow();
        s.close();
    }
    
    @FXML
    public void saveBoard() {
        Stage owner = (Stage) patternCanvas.getScene().getWindow();
        if (!trim()) {
            return;
        }
        fileController.saveBoard(game, EncodeType.RLE, owner);
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
        fileController.saveAnimation(game, 
                (Stage) patternCanvas.getScene().getWindow());
        game.setFirstGeneration();
    }
    
    private boolean trim() {
        int[] bBox = game.getBoard().getBoundingBox();
        
        int trimmedBoardRows = (bBox[1] - bBox[0]) + 1;
        int trimmedBoardColumns = (bBox[3] - bBox[2]) + 1;
        
        if (trimmedBoardRows <= 0 || trimmedBoardColumns <= 0) {
            DialogBoxes.openAlertDialog(Alert.AlertType.WARNING, "Empty Board", 
                    "Error Saving Empty Board", "The current board is empty.");
            return false;
        }
        
        byte[][] trimmedBoard = new byte[trimmedBoardRows][trimmedBoardColumns];
        
        for (int oldRow = bBox[0], newRow = 0; oldRow <= bBox[1]; oldRow++, newRow++) {
            for (int oldColumn = bBox[2], newColumn = 0; oldColumn <= bBox[3]; oldColumn++, newColumn++) {
                trimmedBoard[newRow][newColumn] = 
                        game.getCellAliveState(oldRow, oldColumn);
            }
        }
        game.loadGame(trimmedBoard, game.getMetaData(), Board.BoardType.FIXED);
        return true;
    }
    
    // sette brettet her?
    public void setPattern() {
        metaData = game.getMetaData();
        
        double cellWidth = patternCanvas.getWidth() / game.getRows();
        double cellHeight = patternCanvas.getHeight() / game.getColumns();
        cellSize = (cellWidth < cellHeight) ? cellWidth : cellHeight;

        authorTextField.setText(metaData.getAuthor());
        descriptionTextField.setText(metaData.getComment());
        titleTextField.setText(metaData.getName());
        rulesTextField.setText("S" + metaData.getRuleString()[0] + "/B" + metaData.getRuleString()[1]);

        draw();
        
        //saveToGif.setAccelerator(new KeyCodeCombination(KeyCode.G, KeyCombination.SHORTCUT_DOWN));
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
        if (!(game.getBoard().getRows() > 100)) {
            drawGridLines(gc);
        }

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
}
