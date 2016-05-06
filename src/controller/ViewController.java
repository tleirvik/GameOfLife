package controller;

import model.filemanagement.decoders.RLEDecoder;
import model.filemanagement.EncodeType;
import model.gameoflife.boards.Board.BoardType;
import model.util.DialogBoxes;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.gameoflife.GameOfLife;

/**
 * FXML Controller class for the Pattern Editor
 *
 * @author Stian Reistad Rogeberg.
 * @author Terje Leirvik.
 * @author Robin Sean Aron David Lundh.
 *
 */
public class ViewController implements Draw {

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
    private ScheduledService<Void> scheduledService;
    private final GameOfLife gol = new GameOfLife();
    private final FileController fileController = new FileController();

    private double cellSize = 10;

    // Standard colors (Can be changed during runtime)
    private Color[] colors = new Color[] {
            Color.color(0, 0, 0, 0), Color.GHOSTWHITE, Color.BLACK, Color.GREY
    };

    // Used to calculate the offset_X and offset_Y values
    private double offsetBegin_X = 0;
    private double offsetBegin_Y = 0;

    private double offset_X = 0;
    private double offset_Y = 0;

    private boolean drawGrid = true;

    private byte[][] holdingPattern = null;
    private boolean drawCell = false;
    private boolean isRunning = false;
    
    //=========================================================================
    // Initializer
    //=========================================================================
    /**
     * This method initializes the slider for setting the
     * animation speed. It also creates a deafult 20x20 dynamic board. The
     * fit to view button is set to true to fith the board to the screen.
     * 
     * This method also binds the canvas size to the view, initializes keyboard
     * shortcuts and sets the mouse events so that the user can draw on the
     * board, resize it and move the board.
     */
    public void initialize() {
        gol.newEmptyGame(20, 20, BoardType.DYNAMIC);
        fitToView.setSelected(true);

        initializeFpsSlider();
        initializeBindCanvasSize();
        
        Platform.runLater(() -> {
            initializeKeyboardShortcuts();
        });
        initializeMouseEventHandlers();
        initializeScheduledService();
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
       
        if ((gol.getRows() > 500) && (gol.getColumns() > 500)) {
            toggleGrid.setSelected(true);
            drawGrid = false;
        } else {
            toggleGrid.setSelected(false);
            drawGrid = true;
        }
        
        holdingPattern = fileController.getBoard();
        openGame();
    }
        
    //==================
    //  SAVE BOARD AS
    //==================
    
    /**
     * This method will save the board in the format .rle.
     * 
     * @see model.filemanagement.FileSaver
     */
    @FXML
    public void saveBoardAsRLE() {
        pauseGame();
        //TODO Endre saveBoard() til aa ta inn filtype RLE
        fileController.saveBoard(gol, EncodeType.RLE, 
                (Stage) gameCanvas.getScene().getWindow());
    }
    
    /**
     * This method will save the current board as a .model.filemanagement.otherformats.wav representation when
     * the user interacts with this function in the view.
     */
    @FXML
    public void saveBoardAsWAV() {
        pauseGame();
        fileController.saveSound(gol, 
                (Stage) gameCanvas.getScene().getWindow());
    }
    
    /**
     * This method will save the current board as a .gif representation when
     * the user interacts with this function in the view.
     */
    @FXML
    public void saveBoardAsPNG() {
        pauseGame();
        fileController.saveImage(gol, 
                (Stage) gameCanvas.getScene().getWindow());
    }
    
    /**
     * This method will save the current board as a .gif representation when
     * the user interacts with this function in the view.
     */
    @FXML
    public void saveBoardAsGIF() {
        pauseGame();
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
        dialogBoxes.openPatternEditor(gol, colors, fileController,
                (Stage)gameCanvas.getScene().getWindow());
        draw();
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
        pauseGame();
        colors = dialogBoxes.openOptionsDialog(this,
                (Stage) gameCanvas.getScene().getWindow());
        draw();
    }

    private void draw() {
        draw(gol, gameCanvas.getGraphicsContext2D(),colors, offset_X, offset_Y, cellSize, drawGrid);
    }
    //=================================
    //            ABOUT MENU
    //=================================
    
    /**
     * This method opens a new window with information about the application.
     */
    @FXML
    public void openHelp() {
        pauseGame();
        dialogBoxes.openHelp((Stage) gameCanvas.getScene().getWindow());
    }

    /**
     * This method opens a new window with the JavaDoc.
     */
    @FXML
    public void openJavaDoc() {
        pauseGame();
        dialogBoxes.openJavaDoc((Stage) gameCanvas.getScene().getWindow());
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
        if(!isRunning) {
            isRunning = true;
            togglePlayButton.setText("Pause");
            scheduledService.restart();
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
        isRunning = false;
        scheduledService.cancel();
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

    /**
     *  Returns true if the X position is inside the grid
     * @param posX The position on the X-axis
     * @return Return true if the posX is inside the grid.
     */
    private boolean isXInsideGrid(double posX) {
    	return ((posX >= offset_X) && (posX <= offset_X + getBoardWidth()));
    }

    /**
     * Returns true if the Y position is inside the grid
     * @param posY The position on the Y-axis
     * @return Return true if the posY is inside the grid.
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

    
    //================================================================================
    // Initialize-methods
    //================================================================================

    /**
     * This method initializes mouse events.
     */
    private void initializeMouseEventHandlers() {
        gameCanvas.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent e) -> {
            if (e.isShortcutDown()) {
                gameCanvas.getScene().setCursor(Cursor.HAND);
                fitToView.setSelected(false);
                offsetBegin_X = e.getX() - offset_X;
                offsetBegin_Y = e.getY() - offset_Y;
            } else if (e.isPrimaryButtonDown()) {  //TEGNEMODUS
                gameCanvas.getScene().setCursor(Cursor.CROSSHAIR);

                double bClick_X = e.getX();
                double bClick_Y = e.getY();
                int row = (int) ((bClick_Y - (offset_Y - getBoardHeight())) / cellSize) - gol.getRows();
                int column = (int) ((bClick_X - (offset_X - getBoardWidth())) / cellSize) - gol.getColumns();
                drawCell = (gol.getCellAliveState(row, column) != 1);

                double oldBoardHeight = getBoardHeight();
                double oldBoardWidth = getBoardWidth();

                if (holdingPattern != null) {
                    int midRow = (holdingPattern.length - 1) / 2;
                    int midColumn = (holdingPattern[0].length - 1) / 2;

                    for(int i = 0; i < holdingPattern.length; i++) {
                        for(int j = 0; j < holdingPattern[i].length; j++) {
                            gol.setCellAliveState(row + i - midRow, 
                                    column + j - midColumn, 
                                    holdingPattern[i][j]);
                        }
                    }
                    holdingPattern = null;
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
            }
        }); // end eventhandler

        gameCanvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, (MouseEvent e) -> {
             if (e.isShortcutDown()) {
                gameCanvas.getScene().setCursor(Cursor.HAND);
                offset_X = e.getX() - offsetBegin_X;
                offset_Y = e.getY() - offsetBegin_Y;
                draw();
            } else {
                gameCanvas.getScene().setCursor(Cursor.CROSSHAIR);
                double bClick_X = e.getX();
                double bClick_Y = e.getY();
                int row    = (int) ((bClick_Y - (offset_Y - 
                        getBoardHeight())) / cellSize) - gol.getRows();
                int column = (int) ((bClick_X - (offset_X - 
                        getBoardWidth())) / cellSize) - gol.getColumns();
                
                gol.setCellAliveState(row, column, (drawCell ? (byte)1 : (byte)0));
                draw();
            }// end if
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
            Duration duration = new Duration(1000/newValue);
            scheduledService.setPeriod(duration);
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
     *  This method is called by a {@link ScheduledService} and calls other methods that are required
     *  <br>
     *  First we call {@link #fitToView()} (if the Fit To View {@link ToggleButton} is  selected )
     *  to resize the board and position it on the center of the {@link Canvas}. Then we call
     *  {@link GameOfLife#updateWithThreads()} ()} to run the algorithm and lastly we call
     *  {@link #draw()} to redraw the {@link Canvas}
     */
    private void update() {
        if (fitToView.isSelected()) {
            fitToView();
            centerBoard();
        }
        gol.update();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                draw();
            }
        });
    }


    private void initializeScheduledService() {
        scheduledService = new ScheduledService<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        update();
                        return null;
                    }
                };
            }
        };
        Duration duration = Duration.millis(1000/60);
        scheduledService.setPeriod(duration);
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
        final String os = System.getProperty ("os.name");
        if (os != null && os.startsWith ("Mac"))
            menuBar.useSystemMenuBarProperty ().set (true);
        
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
        
        gameCanvas.getScene().getWindow().addEventFilter(
                KeyEvent.KEY_PRESSED, e -> {
                    if(e.isShortcutDown() && e.getCode().isArrowKey()) {
                        switch(e.getCode()) {
                            case UP:
                                offset_Y-=cellSize;
                                break;
                            case DOWN:
                                offset_Y+=cellSize;
                                break;
                            case LEFT:
                                offset_X-=cellSize;
                                break;
                            case RIGHT:
                                offset_X+=cellSize;
                                break;
                        }
                        draw();
                    }
                });
    }
    
    /**
     * This method sets the four colors used when drawing the game;
     * <ul>
     *     <li>Background color</li>
     *     <li>Dead cell color</li>
     *     <li>Alive cell color</li>
     *     <li>Grid color</li>
     * </ul>
     * 
     * @param colors The board colors
     * @see Color
     */
    public void setColors(Color[] colors) {
        this.colors = colors;
    }

    /**
     * Returns the boards color
     * @return The board colors
     * @see Color
     */
    public Color[] getColors() {
        return colors;
    }
}
