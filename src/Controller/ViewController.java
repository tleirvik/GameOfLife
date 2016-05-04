package Controller;

import Model.FileManagement.Decoders.RLEDecoder;
import Model.FileManagement.EncodeType;
import Model.GameOfLife.Boards.Board.BoardType;
import Model.GameOfLife.GameOfLife;
import Model.util.DialogBoxes;
import Model.util.Stopwatch;
import javafx.animation.Animation;
import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;


/**
 * This is a controller class for the main view.
 *
 * @author Stian Reistad Rogeberg.
 * @author Terje Leirvik.
 * @author Robin Sean Aron David Lundh.
 */
public class ViewController {

    //=========================================================================
    // JavaFX Fields
    //=========================================================================
    
    @FXML private MenuBar menuBar;
    @FXML private BorderPane root;

    @FXML private Button togglePlayButton;

    @FXML private Slider fpsSlider;
    @FXML private Label fpsLabel;

    @FXML private CheckBox toggleGrid;
    @FXML private Pane canvasParent;
    @FXML private Canvas gameCanvas;
    @FXML private ToggleButton fitToView;
    @FXML private Label statusBar;


    //=========================================================================
    // Property fields
    //=========================================================================

    private final DialogBoxes dialogBoxes = new DialogBoxes();
    private final Timeline timeline = new Timeline();
    private final GameOfLife gol = new GameOfLife();
    private final FileController fileController = new FileController();

    private double cellSize = 10;

    // Standard colors (Can be changed during runtime)
    private Color stdAliveCellColor = Color.BLACK;
    private Color stdBoardColor = Color.GHOSTWHITE;
    private Color stdBackgroundColor = Color.LIGHTGREY;
    private Color stdGridColor = Color.GREY;

    private final double stdGridLineWidth = 1;

    // Used to calculate the offset_X and offset_Y values
    private double offsetBegin_X = 0;
    private double offsetBegin_Y = 0;

    private double offset_X = 0;
    private double offset_Y = 0;

    private boolean drawGrid = true;

    private byte[][] holdingPattern = null;
    private boolean drawCell = false;
    
    //=========================================================================
    // Initializer
    //=========================================================================
    /**
     * This method initializes the timeline and the slider for setting the
     * animation speed. It also creates a deafult 20x20 dynamic board. The
     * fit to view button is set to true to fith the board to the screen.
     * 
     * This method also binds the canvas size to the view, initializes keyboard
     * shortcuts and sets the mouse events so that the user can draw on the
     * board, resize it and move the board.
     */
    public void initialize() {
        initializeTimeline();
        initializeFpsSlider();
        
        gol.newEmptyGame(20, 20, BoardType.DYNAMIC);        
        fitToView.setSelected(true);
        
        initializeBindCanvasSize();
        initializeKeyboardShortcuts();
        initializeMouseEventHandlers();
    }
    
    //=========================================================================
    // GUI Event handlers
    //=========================================================================
    
    //=================================
    //            FILE MENU
    //=================================
    
    /**
     * This method is activated when the user clicks the new game choice in the view
     * and opens a dialogbox where the user can set paramters for a new game. 
     * Then launches the new game.
     */
    @FXML
    public void newGame() {
        pauseGame();
        dialogBoxes.openNewGameDialog(gol, 
                (Stage) gameCanvas.getScene().getWindow());
        
        openGame();
    }
    
    /**
     * This method launches a FileChooser and lets the user select a file.
     * If the file is not null it creates an object of type RLEDecoder and
     * calls the method decode(). And starts a new game with the the selected
     * pattern from file.
     *
     * @see RLEDecoder
     */
    @FXML
    public void loadBoard() {
        pauseGame();
        fileController.loadBoard(gol, (Stage) gameCanvas.getScene().getWindow());
        holdingPattern = fileController.getBoard();
        openGame();
    }
        
    //==================
    //  SAVE BOARD AS
    //==================
    
    /**
     * This method will save the board in the format .rle.
     * 
     * @see FileSaver
     */
    @FXML
    public void saveBoardAsRLE() {
        //TODO Endre saveBoard() til å ta inn filtype RLE
        fileController.saveBoard(gol, EncodeType.RLE, 
                (Stage) gameCanvas.getScene().getWindow());
    }
    
    /**
     * This method will save the current board as a .wav representation when
     * the user interacts with this function in the view.
     */
    @FXML
    public void saveBoardAsWAV() {
        fileController.saveSound(gol, 
                (Stage) gameCanvas.getScene().getWindow());
    }
    
    /**
     * This method will save the current board as a .gif representation when
     * the user interacts with this function in the view.
     */
    @FXML
    public void saveBoardAsGIF() {
        fileController.saveAnimation(gol, 
                (Stage) gameCanvas.getScene().getWindow());
    }
    
    /**
     * This method will close the application.
     */
    @FXML
    public void closeApplication() {
        Platform.exit();
    }
    
    
    //=================================
    //            EDIT MENU
    //=================================
    
    /**
     * This method opens the pattern editor when the user interacts with
     * this function and pauses the animation of the current game.
     */
    @FXML
    public void openPatternEditor() {
        pauseGame();
        dialogBoxes.openPatternEditor(gol, fileController, 
                (Stage)gameCanvas.getScene().getWindow());
    }
    
    /**
     * This method opens a dialog box where the user can enter meta data.
     */
    @FXML
    public void openMetadataDialog() {
        pauseGame();
        dialogBoxes.metaDataDialogBox(gol.getMetaData(), 
                (Stage)gameCanvas.getScene().getWindow());
    }
    
    /**
     * This method changes the boards first generation to be equal to the
     * boards current generation.
     */
    @FXML
    public void setReset() {
        pauseGame();
        gol.setFirstGeneration();
    }
    
    /**
     * This method opens a option dialog where the user can set parameters 
     * such as colors.
     * 
     * @see DialogBoxes
     */
    @FXML
    private void openOptions() {
        dialogBoxes.openOptionsDialog(this, 
                (Stage) gameCanvas.getScene().getWindow());
        draw();
    }
    
    //=================================
    //            HELP MENU
    //=================================
    
    /**
     * 
     */
    @FXML
    public void openHelp() {
        dialogBoxes.openHelp((Stage) gameCanvas.getScene().getWindow());
    }
    
    
    
    
    //=================================
    //         BOTTOM CONTROLS
    //=================================
    
    /**
     * This method starts and pauses the animation of the current game
     * when the user clicks this button.
     */
    @FXML
    public void togglePlay() {
        if(timeline.getStatus() != Status.RUNNING) {
            togglePlayButton.setText("Pause");
            timeline.play();
        } else {
            pauseGame();
        }
    }
    
    /**
     * This method resets the board.
     */
    @FXML
    public void reset() {
        pauseGame();
        gol.resetGame();
        draw();
    }
    
    /**
     * This method opens a view to show statistics of the current board.
     */
    @FXML
    public void openStatistics() {
        pauseGame();
        dialogBoxes.openStatistics(gol, (Stage) gameCanvas.getScene().getWindow());
    }
    
    /**
     * This method handles the fit to view button and fits the current board
     * to the canvas.
     * 
     */
    @FXML
    public void handleFitToView() {
        if (fitToView.isSelected()) {
            fitToView();
            centerBoard();
            draw();
        }
    }
    
    /**
     * This method will turn the grid lines on or off when the user clicks 
     * this button.
     */
    @FXML
    public void handleToggleGrid() {
        drawGrid = !toggleGrid.isSelected();
        draw();
    }
    
    // TODO: 30.04.2016 Bedre tittel her
    //=========================================================================
    // GAME RELATED
    //=========================================================================
    
    /**
     * This method pauses the animation of the board.
     */
    public void pauseGame() {
        timeline.stop();
        togglePlayButton.setText("Play");
    }
    
    /**
     * Opens a currently loaded game, whether it is completely new
     * or loaded from a file.
     */
    public void openGame() {
        fitToView.setSelected(true);
        fitToView();
        centerBoard();
        draw();
    }

    /**
     * This method fits the current board to the canvas.
     */
    public void fitToView() {
        double cellWidth = gameCanvas.getWidth() / (gol.getColumns());
        double cellHeight = gameCanvas.getHeight() / (gol.getRows());

        if (cellWidth < cellHeight) {
            cellSize = cellWidth;
        } else {
            cellSize = cellHeight;
        }
    }

    //=========================================================================
    // Draw methods
    //=========================================================================

    /**
     * This method returns the boards width.
     * 
     * @return double value of the boards width.
     */
    private double getBoardWidth() {
    	return cellSize * gol.getColumns();
    }

    /**
     * This method returns the boards height.
     * 
     * @return Returns the double value of the boards height.
     */
    private double getBoardHeight() {
    	return cellSize * gol.getRows();
    }

    // TODO 30.04.2016 Fjern isXInsideGrid og isYInsideGrid
    /**
     * 
     * @param posX
     * @return Return true if the posX is inside the grid.
     */
    private boolean isXInsideGrid(double posX) {
    	return ((posX >= offset_X) && (posX <= offset_X + getBoardWidth()));
    }

    /**
     * 
     * @param posY
     * @return 
     */
    private boolean isYInsideGrid(double posY) {
    	return ((posY >= offset_Y) && (posY <= offset_Y + getBoardHeight()));
    }

    /**
     * This method will center the board.
     */
    private void centerBoard() {
            offset_X = gameCanvas.getWidth() / 2 - (getBoardWidth() / 2);
            offset_Y = gameCanvas.getHeight() / 2 - (getBoardHeight() / 2);
    }

    /**
     * This method draws the board.
     */
    private void draw() {
        final double start_X = Math.round(offset_X);
        final double start_Y = Math.round(offset_Y);
        final int rows = gol.getRows();
        final int columns = gol.getColumns();
        final double boardWidth = getBoardWidth();
        final double boardHeight = getBoardHeight();
        final double canvasWidth = gameCanvas.getWidth();
        final double canvasHeight = gameCanvas.getHeight();

        GraphicsContext gc = gameCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvasWidth, canvasHeight);

        gc.setFill(stdBackgroundColor);
        gc.fillRect(0, 0, canvasWidth, canvasHeight);
        
        gc.setFill(stdBoardColor);
        gc.fillRect(start_X, start_Y, boardWidth, boardHeight);

        
        int rowStart = (int) ((0 - (offset_Y - getBoardHeight())) / 
                cellSize) - gol.getRows();
        
        int rowEnd = (int) ((canvasParent.getHeight() - (offset_Y - 
                getBoardHeight())) / cellSize) - gol.getRows();
        
        int columnStart = (int) ((0 - (offset_X - getBoardWidth())) / 
                cellSize) - gol.getColumns();
        
        int columnEnd = (int) ((canvasParent.getWidth() - (offset_X - 
                getBoardWidth())) / cellSize) - gol.getColumns();

        int startRow = 0;
        int endRow = rows;
        if (rowStart > 0) {
            startRow = rowStart;
        }
        if (rowEnd < rows) {
            endRow = rowEnd + 1;
        }

        int startCol = 0;
        int endCol = columns;
        if (columnStart > 0) {
            startCol = columnStart;
        }
        if (columnEnd < columns) {
            endCol = columnEnd + 1;
        }

        gc.setFill(stdAliveCellColor);
        for (int row = startRow; row < endRow; row++) {
            for (int col = startCol; col < endCol; col++ ) {
                if (gol.getCellAliveState(row, col) == 1) {
                    gc.fillRect(start_X + cellSize * col, start_Y + cellSize * row, cellSize, cellSize);
                }
            }
        }

        if (drawGrid) {
            drawGridLines(gc);
        }
    }

    /**
     * This method draws grid lines.
     * @param gc the buffer used to draw on canvas.
     */
    private void drawGridLines(GraphicsContext gc) {
    	gc.setLineWidth(stdGridLineWidth);
    	gc.setStroke(stdGridColor);
        final int boardRowLength = gol.getRows();
        final int boardColumnLength = gol.getColumns();

    	double start_X = offset_X;
        double start_Y = offset_Y;

        double end_X = start_X + getBoardWidth();
        double end_Y = start_Y + getBoardHeight();

        for (int y = 0; y <= boardRowLength; y++) {
            gc.strokeLine(start_X, start_Y + (cellSize * y),
                    end_X, start_Y + (cellSize * y));
        }

        for (int x = 0; x <= boardColumnLength; x++) {
        	gc.strokeLine(start_X + (cellSize * x),
                        start_Y, start_X + (cellSize * x), end_Y);
        }

        gc.setFill(stdAliveCellColor);
        gc.fillOval(gameCanvas.getWidth()/2-2,
                gameCanvas.getHeight() / 2 - 2, 5, 5);

        gc.setFill(Color.RED);
        gc.fillOval(start_X + (getBoardWidth() / 2) - 2, start_Y +
                (getBoardHeight() / 2) - 2, 5, 5);

    }
    
    //================================================================================
    // Initialize-methods
    //================================================================================

    /**
     * This method initializes mouse events.
     */
    private void initializeMouseEventHandlers() {
        gameCanvas.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent e) -> {
            if (e.isPrimaryButtonDown()) {  //TEGNEMODUS
                gameCanvas.getScene().setCursor(Cursor.CROSSHAIR);

                double bClick_X = e.getX();
                double bClick_Y = e.getY();
                int row = (int) ((bClick_Y - (offset_Y - getBoardHeight())) / cellSize) - gol.getRows();
                int column = (int) ((bClick_X - (offset_X - getBoardWidth())) / cellSize) - gol.getColumns();
                System.out.println("Click_ row: " + row + " col: " + column);
                drawCell = (gol.getCellAliveState(row, column) != 1);

                double oldBoardHeight = getBoardHeight();
                double oldBoardWidth = getBoardWidth();

                if (holdingPattern != null) {
                    /*
                    final int M = testArray.length;
                    final int N = testArray[0].length;
                    byte[][] ret = new byte[N][M];
                    for(int r = 0; r < M; r++) {
                        for (int c = 0; c < N; c++) {
                            ret[c][M-1-r] = testArray[r][c];
                        }
                    }*/

                    int midRow = (holdingPattern.length - 1) / 2;
                    int midColumn = (holdingPattern[0].length - 1) / 2;

                    for(int i = 0; i < holdingPattern.length; i++) {
                        for(int j = 0; j < holdingPattern[i].length; j++) {
                            gol.setCellAliveState(row + i - midRow, 
                                    column + j - midColumn, 
                                    holdingPattern[i][j]);
                        }
                    }
                } else {
                    gol.setCellAliveState(row, column, (drawCell ? (byte) 1 : (byte) 0));
                }

                double newBoardHeight = getBoardHeight();
                double newBoardWidth = getBoardWidth();

                if (row < 0) {
                    offset_Y -= newBoardHeight - oldBoardHeight;
                }
                if (column < 0) {
                    offset_X -= newBoardWidth - oldBoardWidth;
                }
                draw();
            } else if (e.isSecondaryButtonDown()) {
                gameCanvas.getScene().setCursor(Cursor.HAND);
                fitToView.setSelected(false);
                offsetBegin_X = e.getX() - offset_X;
                offsetBegin_Y = e.getY() - offset_Y;
            }
        }); // end eventhandler

        gameCanvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, (MouseEvent e) -> {
            if (e.isPrimaryButtonDown()) {
                gameCanvas.getScene().setCursor(Cursor.CROSSHAIR);
                double bClick_X = e.getX();
                double bClick_Y = e.getY();
                int row    = (int) ((bClick_Y - (offset_Y - 
                        getBoardHeight())) / cellSize) - gol.getRows();
                int column = (int) ((bClick_X - (offset_X - 
                        getBoardWidth())) / cellSize) - gol.getColumns();
                
                gol.setCellAliveState(row, column, (drawCell ? (byte)1 : (byte)0));
                draw();
            } else if (e.isSecondaryButtonDown()) {
                gameCanvas.getScene().setCursor(Cursor.HAND);
                offset_X = e.getX() - offsetBegin_X;
                offset_Y = e.getY() - offsetBegin_Y;
                draw();
            } // end if
        }); // end eventhandler

        gameCanvas.setOnScroll((ScrollEvent event) -> {
            fitToView.setSelected(false);
            double scrollLocation_X = event.getX();
            double scrollLocation_Y = event.getY();
            double scrollAmount = event.getDeltaY();
            double zoomFactor = 1.1;

            if (scrollAmount < 0) {
                zoomFactor = 1 / zoomFactor;
            }

            offset_X -= (scrollLocation_X - offset_X) * (zoomFactor - 1);
            offset_Y -= (scrollLocation_Y - offset_Y) * (zoomFactor - 1);

            cellSize *= zoomFactor;
            draw();
        });
    }

    /**
     * This method initializes the slider for the animation speed.
     */
    private void initializeFpsSlider() {
        fpsSlider.setMin(1);
    	fpsSlider.setMax(60);
        fpsLabel.setText("1");
        fpsSlider.valueProperty().addListener(e -> {
            fpsLabel.setText(Integer.toString(fpsSlider.valueProperty().intValue()));
        });
        
        fpsSlider.valueChangingProperty().addListener((ObservableValue<? extends 
                Boolean> obs, Boolean wasChanging, Boolean isChanging) -> {
            int newValue = (int)fpsSlider.getValue();
            if (timeline.getStatus() == Animation.Status.RUNNING) {
                timeline.stop();
                initializeKeyFrame(newValue);
                timeline.play();
            } else {
                initializeKeyFrame(newValue);
            }
        });
    }

    /**
     * This method binds the canvas to the view.
     */
    private void initializeBindCanvasSize() {
        gameCanvas.heightProperty().bind(canvasParent.heightProperty());
    	gameCanvas.widthProperty().bind(canvasParent.widthProperty());  
        
        gameCanvas.heightProperty().addListener(e -> {
            if (fitToView.isSelected()) {
                fitToView();
                centerBoard();
            }
            draw();
    	});
    	gameCanvas.widthProperty().addListener(e -> {
            if (fitToView.isSelected()) {
                fitToView();
                centerBoard();
            }
            draw();   
    	});
    }

    /**
     * This method initializes the timeline on start up.
     */
    private void initializeTimeline() {
        timeline.setCycleCount(Animation.INDEFINITE);
        initializeKeyFrame(1);
    }

    /**
     * This method initializes the key frame.
     * @param fps number of frames per second.
     */
    private void initializeKeyFrame(int fps) {
        Duration duration = Duration.millis(1000/fps);
        KeyFrame keyFrame;
        keyFrame = new KeyFrame(duration, (ActionEvent e) -> {
            if (fitToView.isSelected()) {
                fitToView();
                centerBoard();
            }
            Stopwatch sw = new Stopwatch("Next generation threading");
            sw.start();
            gol.updateWithThreads();
            draw();
            sw.stop();
        });
        timeline.getKeyFrames().clear();
        timeline.getKeyFrames().add(0, keyFrame);
    }
    
    public void setStatusBarText(String s) {
        statusBar.setText(s);
    }

    /**
     * This method initializes keyboard shortcuts.
     */
    private void initializeKeyboardShortcuts() {
        Menu file = menuBar.getMenus().get(0);
        Menu edit = menuBar.getMenus().get(1);
        
        //File Menu
        file.getItems().get(0).setAccelerator(new KeyCodeCombination(KeyCode.N, 
                KeyCombination.SHORTCUT_DOWN));
        file.getItems().get(1).setAccelerator(new KeyCodeCombination(KeyCode.L, 
                KeyCombination.SHORTCUT_DOWN));
        file.getItems().get(2).setAccelerator(new KeyCodeCombination(KeyCode.S, 
                KeyCombination.SHORTCUT_DOWN));
        file.getItems().get(3).setAccelerator(new KeyCodeCombination(KeyCode.E, 
                KeyCombination.SHORTCUT_DOWN));
        
        //Edit Menu
        edit.getItems().get(0).setAccelerator(new KeyCodeCombination(KeyCode.P, 
                KeyCombination.SHORTCUT_DOWN));
        edit.getItems().get(1).setAccelerator(new KeyCodeCombination(KeyCode.M,
                KeyCombination.SHORTCUT_DOWN));
        edit.getItems().get(2).setAccelerator(new KeyCodeCombination(KeyCode.X, 
                KeyCombination.SHORTCUT_DOWN, KeyCombination.SHIFT_DOWN));
        edit.getItems().get(3).setAccelerator(new KeyCodeCombination(KeyCode.O, 
                KeyCombination.SHORTCUT_DOWN));
    }
    
    /**
     * This method sets the color for the background.
     * 
     * @param stdBackgroundColor the color to be used as background.
     */
    public void setBackgroundColor(Color stdBackgroundColor) {
        this.stdBackgroundColor = stdBackgroundColor;
    }
    
    /**
     * This method set the color for the background of the board.
     * 
     * @param stdBoardColor the color to be used as background color for the board.
     */
    public void setBoardBackgroundColor(Color stdBoardColor) {
        this.stdBoardColor = stdBoardColor;
    }
    
    /**
     * This method sets the color for the living cells in the board.
     * 
     * @param stdAliveCellColor the color for the living cells.
     */
    public void setCellColor(Color stdAliveCellColor) {
        this.stdAliveCellColor = stdAliveCellColor;
    }
    
    /**
     * This method set the color for the grid lines.
     * 
     * @param stdGridColor is the color to be used as grid lines.
     */
    public void setGridColor(Color stdGridColor) {
        this.stdGridColor = stdGridColor;
    }
    
    /**
     * This method returns the background color.
     * 
     * @return the color to be used as background.
     */
    public Color getBackgroundColor() {
        return stdBackgroundColor;
    }
    
    /**
     * This method return the color to be used on the board.
     * 
     * @return the color for the board.
     */
    public Color getBoardBackgroundColor() {
        return stdBoardColor;
    }
    
    /**
     * This method returns the color to be used on living cells.
     * 
     * @return the color of living cells.
     */
    public Color getCellColor() {
        return stdAliveCellColor;
    }
    
    /**
     * This method returns the color to be used on the grid lines.
     * 
     * @return the color for the grid lines.
     */
    public Color getGridColor() {
        return stdGridColor;
    }
}
