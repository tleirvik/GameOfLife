package controller;

import javafx.scene.input.*;
import model.filemanagement.EncodeType;
import model.gameoflife.boards.Board;
import model.gameoflife.MetaData;
import model.util.DialogBoxes;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import model.gameoflife.GameOfLife;

/**
 * FXML Controller class for the Pattern Editor
 *
 * This controller class handles all the actions performed in
 * the its related view.
 * 
 * @author Stian Reistad Rogeberg.
 * @author Terje Leirvik.
 * @author Robin Sean Aron Lundh.
 */
public class EditorController implements Draw {
    @FXML private MenuBar menuBar;
    @FXML private Canvas patternCanvas;
    @FXML private Canvas strip;
    @FXML private Button closeButton;
    @FXML private TextField authorTextField;
    @FXML private TextField titleTextField;
    @FXML private TextField descriptionTextField;
    @FXML private TextField rulesTextField;

    private double cellSize;
    private GameOfLife patternGame;
    private GameOfLife originalGame;
    private MetaData metaData;
    private FileController fileController;
    private Color[] colors;

    /**
     * This method clones a game from the main view. It also initializes
     * some keyboard shortcuts and sets the pattern.
     * 
     * @param game The {@link GameOfLife} to be cloned to the main view.
     * @param fileController is a reference to the class that takes care of
     * saving and reading files.
     * 
     * @see FileController
     */
    public void initializeEditor(GameOfLife game, Color[] colors, FileController fileController) {
        originalGame = game;
        this.colors = colors;
        patternGame = game.clone();
        this.fileController = fileController;

        final String os = System.getProperty ("os.name");
        if (os != null && os.startsWith ("Mac"))
            menuBar.useSystemMenuBarProperty ().set (true);

        initializeKeyboardShortcuts();
        setPattern();
    }
   
    /**
     * This method closes the editor.
     */
    @FXML
    public void handleCloseButton() {
        Stage s = (Stage) closeButton.getScene().getWindow();
        originalGame.setBoard(patternGame.getBoard());
        s.close();
    }
    
    /**
     * This method will save the board if it is not empty.
     */
    @FXML
    public void saveBoard() {
        Stage owner = (Stage) patternCanvas.getScene().getWindow();
        if (!trim()) {
            return;
        }
        fileController.saveBoard(patternGame, EncodeType.RLE, owner);
    }
    
    /**
     * This method calculates where to draw a cell when the user clicks on
     * the canvas.
     * @param e is the mouse event that was triggered.
     */
    @FXML
    public void handleMouseClick(MouseEvent e) {
        int row = (int) (e.getY() / cellSize);
        int col = (int) (e.getX() / cellSize);
        
        if(row < patternGame.getRows() && col < patternGame.getColumns()) {
            patternGame.setCellAliveState(row, col, (patternGame
                    .getCellAliveState(row, col) == 1) ? (byte) 0 : (byte) 1);
            draw();
        }
    }
    
    /**
     * This method saves the current pattern to the .gif format.
     * @see model.filemanagement.otherformats.GIFSaver
     */
    @FXML
    public void saveToGif() {
        fileController.saveAnimation(patternGame, 
                (Stage) patternCanvas.getScene().getWindow());
        patternGame.setFirstGeneration();
    }
    
    /**
     * This method calls the {@link #trim()} method which reduces the array to smallest possible
     */
    @FXML
    public void handleTrimButton() {
        trim();
    }
    
    /**
     * This method updates the strip view with the current pattern when the 
     * user clicks a button.
     */
    @FXML
    public void updateStrip() {
        patternGame.setFirstGeneration();
        final double stripCellSize = strip.getHeight() / patternGame.getRows();
        final double generationWidth = stripCellSize * patternGame.getColumns();
        final double padding = 25;
        strip.setWidth((generationWidth + padding) * 20);
        double offset_X = 0;
        
        final GraphicsContext gc = strip.getGraphicsContext2D();
        gc.clearRect(0, 0, strip.widthProperty().doubleValue(), 
                strip.heightProperty().doubleValue());
        
        for (int i = 0; i < 20; i++) {
            patternGame.update();
            drawStrip(gc, offset_X, stripCellSize);
            offset_X += generationWidth + padding;
        }
        patternGame.resetGame();
    }
    
    /**
     * This method ignores empty rows and creates a new trimmed board with
     * the pattern.
     * @return true if the board is not empty.
     */
    private boolean trim() {
        int[] bBox = patternGame.getBoard().getBoundingBox();
        
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
                        patternGame.getCellAliveState(oldRow, oldColumn);
            }
        }
        patternGame.loadGame(trimmedBoard, patternGame.getMetaData(), Board.BoardType.FIXED);
        return true;
    }
    
    /**
     * This method sets the pattern with meta data.
     */
    public void setPattern() {
        metaData = patternGame.getMetaData();
        
        double cellWidth = patternCanvas.getWidth() / patternGame.getRows();
        double cellHeight = patternCanvas.getHeight() / patternGame.getColumns();
        cellSize = (cellWidth < cellHeight) ? cellWidth : cellHeight;

        authorTextField.setText(metaData.getAuthor());
        descriptionTextField.setText(metaData.getComment());
        titleTextField.setText(metaData.getName());
        rulesTextField.setText("S" + metaData.getRuleString()[0] + "/B" + 
                metaData.getRuleString()[1]);
        draw();
    }
    
    /**
     * This method draws the pattern to the strip view.
     * 
     * @param gc Is the {@link GraphicsContext} buffer used to draw on a canvas.
     * @param offset_X is the x position from where to draw.
     * @param stripCellSize is the size of the cells.
     */
    private void drawStrip(GraphicsContext gc, double offset_X, double stripCellSize) {        
        final int rows = patternGame.getRows();
        final int columns = patternGame.getColumns();
        double x = offset_X;
        double y = 0;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                if (patternGame.getCellAliveState(row, col) == 1) {
                    gc.fillRect(x, y, stripCellSize, stripCellSize);
                }
                x += stripCellSize;
            }
            x = offset_X;
            y += stripCellSize;
        }

        final double start_x = offset_X;
        final double start_y = 0;
        final double end_y = stripCellSize * rows;
        final double end_x = offset_X + stripCellSize * columns;

        drawPadding(gc, start_x, start_y, end_x, end_y);
    }

    /**
     * This method draws padding around each generations in the strip view.
     * 
     * @param gc Is the {@link GraphicsContext} buffer used to draw on a canvas.
     * @param start_x is the x-position from where to start drawing the padding.
     * @param start_y is the y-position from where to start drawing the padding.
     * @param end_x is x-position where to stop drawing the padding.
     * @param end_y is y-position where to stop drawing the padding.
     */
    private void drawPadding(GraphicsContext gc, double start_x, double start_y,
            double end_x, double end_y) {
        // top
        gc.strokeLine(start_x, start_y, end_x, start_y);
        // left
        gc.strokeLine(start_x, start_y, start_x, end_y);
        // right
        gc.strokeLine(end_x, start_y, end_x, end_y);
        // bottom
        gc.strokeLine(start_x, end_y, end_x, end_y);
    }
    
    /**
     * This method draws the pattern.
     */
    private void draw() {
        draw(patternGame, patternCanvas.getGraphicsContext2D(),colors, 0, 0, cellSize, true);
    }
    
    /**
     * This method returns the width of the pattern.
     * 
     * @return a double value of the width.
     */
    private double getPatternWidth() {
        return cellSize * patternGame.getColumns();
    }
    
    /**
     * This method returns the height of the pattern.
     * 
     * @return a double value of the height.
     */
    private double getPatternHeight() {
        return cellSize * patternGame.getRows();
    }
    
    /**
     * This method initializes keyboard shortcuts.
     */
    private void initializeKeyboardShortcuts() {
        Menu file = menuBar.getMenus().get(0);
        //File Menu
        file.getItems().get(0).setAccelerator(new KeyCodeCombination(KeyCode.B, KeyCombination.SHORTCUT_DOWN));
        file.getItems().get(1).setAccelerator(new KeyCodeCombination(KeyCode.G, KeyCombination.SHORTCUT_DOWN));
    }
}
