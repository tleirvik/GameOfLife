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
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

/**
 * FXML Controller class.
 *
 * This controller class handles all the actions performed in
 * the its related view.
 * 
 * @author Stian Reistad Rogeberg.
 * @author Terje Leirvik.
 * @author Robin Sean Aron Lundh.
 */
public class EditorController {
    @FXML private MenuBar menuBar;
    @FXML private Canvas patternCanvas;
    @FXML private Canvas strip;
    @FXML private Button closeButton;
    @FXML private TextField authorTextField;
    @FXML private TextField titleTextField;
    @FXML private TextField descriptionTextField;
    @FXML private TextField rulesTextField;

    private double cellSize;
    private GameOfLife game;
    private MetaData metaData;
    private FileController fileController;
    
    /**
     * This method clones a game from the main view. It also initializes
     * som keyboard shurtcuts and sets the pattern.
     * 
     * @param game is the game to be cloned the main view.
     * @param fileController is a reference to the class that takes care of
     * saving and reading files.
     * 
     * @see FileController
     */
    public void initializeEditor(GameOfLife game, FileController fileController) {
        this.game = game.clone();
        this.fileController = fileController;
        
        initializeKeyboardShortcuts();
        setPattern();
    }
   
    /**
     * This method closes the editor.
     */
    @FXML
    public void handleCloseButton() {
        Stage s = (Stage) closeButton.getScene().getWindow();
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
        fileController.saveBoard(game, EncodeType.RLE, owner);
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
        
        if(row < game.getRows() && col < game.getColumns()) {
            game.setCellAliveState(row, col,(game.getCellAliveState(row, col) == 1) ? (byte) 0 : (byte) 1);
            draw();
        }
    }
    
    /**
     * This method saves the current pattern to the .gif format.
     * @see GifSaver
     */
    @FXML
    public void saveToGif() {
        int rows = game.getRows();
        int cols = game.getColumns();
        if (rows <= 0 || cols <= cols) {
            DialogBoxes.openAlertDialog(Alert.AlertType.WARNING, "Empty Board", 
                    "Error Saving Empty Board as GIF", 
                    "The current board is empty.");
            return;
        }
        fileController.saveAnimation(game, 
                (Stage) patternCanvas.getScene().getWindow());
        game.setFirstGeneration();
    }
    
    /**
     * This method updates the strip view with the current pattern when the 
     * user clicks a button.
     */
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
        
        for (int i = 0; i < 20; i++) {
            game.update();
            drawStrip(gc, offset_X, stripCellSize);
            offset_X += generationWidth + padding;
        }
        
        game.resetGame();
    }
    
    /**
     * This method ignores empty rows and creates a new trimmed board with
     * the pattern.
     * @return boolean true if the board is not empty.
     */
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
    
    /**
     * This method sets the pattern with meta data.
     */
    public void setPattern() {
        metaData = game.getMetaData();
        
        double cellWidth = patternCanvas.getWidth() / game.getRows();
        double cellHeight = patternCanvas.getHeight() / game.getColumns();
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
     * @param gc is a buffer used to draw on a canvas.
     * @param offset_X is the x position from where to draw.
     * @param stripCellSize is the size of the cells.
     */
    private void drawStrip(GraphicsContext gc, double offset_X, double stripCellSize) {        
        final int rows = game.getRows();
        final int columns = game.getColumns();
        double x = offset_X;
        double y = 0;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                if (game.getCellAliveState(row, col) == 1) {
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
     * @param gc is a buffer used to draw on a canvas.
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
        final int rows = game.getRows();
        final int columns = game.getColumns();
        
        final GraphicsContext gc = patternCanvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, patternCanvas.getWidth(), 
                patternCanvas.getHeight());

        double x = 0;
        double y = 0;
        
        gc.setFill(Color.BLACK);
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
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
    
    /**
     * This method will draw gridlines if the pattern is over a specific size.
     * @param gc is a buffer used to draw on a canvas.
     */
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
    
    /**
     * This method returns the width of the pattern.
     * 
     * @return a double value of the width.
     */
    private double getPatternWidth() {
        return cellSize * game.getColumns();
    }
    
    /**
     * This method returns the height of the pattern.
     * 
     * @return a double value of the height.
     */
    private double getPatternHeight() {
        return cellSize * game.getRows();
    }
    
    /**
     * This method initializes keyboard shortcuts.
     */
    private void initializeKeyboardShortcuts() {
        Menu file = menuBar.getMenus().get(0);
        //File Menu
        file.getItems().get(1).setAccelerator(new KeyCodeCombination(KeyCode.G, KeyCombination.SHORTCUT_DOWN));
    }
}
