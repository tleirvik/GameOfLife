package Controller;

import Model.FileManagement.Decoders.RLEDecoder;
import Model.FileManagement.EncodeType;
import Model.FileManagement.OtherFormats.WavSaver;
import Model.GameOfLife.Boards.Board.BoardType;
import Model.GameOfLife.GameOfLife;
import Model.util.DialogBoxes;
import Model.util.Stopwatch;
import Wav.WavFileException;
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

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *Denne klassen lytter på hendelser i .fxml
 *
 */
public class ViewController {

    //=========================================================================
    // JavaFX Fields
    //=========================================================================
    
    @FXML private MenuBar menuBar;
    @FXML private BorderPane root;

    @FXML private Button togglePlayButton;
    
    @FXML private ColorPicker gridColorPicker;
    @FXML private ColorPicker cellColorPicker;
    @FXML private ColorPicker backgroundColorPicker;
    @FXML private ColorPicker boardColorPicker;

    @FXML private Slider fpsSlider;
    @FXML private Label fpsLabel;

    @FXML private CheckBox toggleGrid;
    @FXML private Pane canvasParent;
    @FXML private Canvas gameCanvas;
    @FXML private ToggleButton fitToView;
    @FXML private ToggleButton toggleDrawMove;
    @FXML private Label statusBar;


    //=========================================================================
    // Property fields
    //=========================================================================

    private final DialogBoxes dialogBoxes = new DialogBoxes();
    private final Timeline timeline = new Timeline();
    private final GameOfLife gol = new GameOfLife();
    private final FileController fileController = new FileController();

    private double cellSize = 10;

    //Standard colors (Can be changed during runtime)
    private Color stdAliveCellColor = Color.BLACK;
    private Color stdBoardColor = Color.GHOSTWHITE;
    private Color stdBackgroundColor = Color.LIGHTGREY;
    private Color stdGridColor = Color.GREY;

    private final double stdGridLineWidth = 1;

    //Used to calculate the offset_X and offset_Y values
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
    public void initialize() {
        initializeTimeline();
        initializeFpsSlider();
        
        gol.newEmptyGame(20, 20, BoardType.DYNAMIC);        
        openGame();
        
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
    
    @FXML
    public void newGame() {
        timeline.stop();
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
        timeline.stop();
        fileController.loadBoard(gol, holdingPattern, (Stage) gameCanvas.getScene().getWindow());
    }
        
    //==================
    //  SAVE BOARD AS
    //==================
    
    @FXML
    public void saveBoardAsRLE() {
        //TODO Endre saveBoard() til å ta inn filtype RLE
        fileController.saveBoard(gol, EncodeType.RLE, 
                (Stage) gameCanvas.getScene().getWindow());
    }
    
    @FXML
    public void saveBoardAsLife105() {
        fileController.saveBoard(gol, EncodeType.LIFE105, 
                (Stage) gameCanvas.getScene().getWindow());
    }
    
    @FXML
    public void saveBoardAsLife106() {
        fileController.saveBoard(gol, EncodeType.LIFE106, 
                (Stage) gameCanvas.getScene().getWindow());
    }
    
    @FXML
    public void saveBoardAsWAV() {
        fileController.saveSound(gol, 
                (Stage) gameCanvas.getScene().getWindow());
    }
    
    @FXML
    public void saveBoardAsJPEG() {
        fileController.saveImage(gol, 
                (Stage) gameCanvas.getScene().getWindow());
    }
    
    @FXML
    public void saveBoardAsGIF() {
        fileController.saveAnimation(gol, 
                (Stage) gameCanvas.getScene().getWindow());
    }
    
    @FXML
    public void closeApplication() {
        Platform.exit();
    }
    
    
    //=================================
    //            EDIT MENU
    //=================================
    
    @FXML
    public void openPatternEditor() {
        timeline.stop();
        dialogBoxes.openPatternEditor(gol, fileController, 
                (Stage)gameCanvas.getScene().getWindow());
    }
    
    @FXML
    public void openMetadataDialog() {
        dialogBoxes.metaDataDialogBox(gol.getMetaData(), 
                (Stage)gameCanvas.getScene().getWindow());
    }
    
    @FXML
    public void setReset() {
        timeline.stop();
        gol.setFirstGeneration();
    }
    
    // TODO: 30.04.2016 Options-meny for å sette boolske flagg (med checkbokser)
    @FXML
    private void openOptions() {

    }
    
    //=================================
    //            HELP MENU
    //=================================
    
    @FXML
    public void openAbout() {
        //TODO: FIKS ABOUT
        
    }
    
    //=================================
    //            SIDEBAR
    //=================================
    
    /**
     *  Method that changes the background color according to
     *  the value selected from the ColorPicker
     *
     */
    @FXML
    public void changeBackgroundColor() {
        stdBackgroundColor = backgroundColorPicker.getValue();
        draw();
    }

    /**
     *  Method that changes the board color according to
     *  the value selected from the ColorPicker
     *
     */
    @FXML
    public void changeBoardColor() {
        stdBoardColor = boardColorPicker.getValue();
        draw();
    }

    /**
     *  Method that changes the cell color according to
     *  the value selected from the ColorPicker
     *
     */
    @FXML
    public void changeCellColor() {
        stdAliveCellColor = cellColorPicker.getValue();
        draw();
    }

    /**
     *  Method that changes the grid color according to
     *  the value selected from the ColorPicker
     *
     */
    @FXML
    public void changeGridColor() {
        stdGridColor = gridColorPicker.getValue();
        draw();
    }
    
    @FXML
    public void handleFitToView() {
        if(fitToView.isSelected()) {
            fitToView();
            centerBoard();
            draw();
        }
    }
    
    @FXML
    public void handleToggleGrid() {
        drawGrid = !toggleGrid.isSelected();
        draw();
    }
    
    //=================================
    //         BOTTOM CONTROLS
    //=================================
    
    @FXML
    public void togglePlay() {
        if(timeline.getStatus() != Status.RUNNING) {
            togglePlayButton.setText("Pause");
            timeline.play();
        } else {
            togglePlayButton.setText("Play");
            timeline.stop();
        }
    }
    
    @FXML
    public void reset() {
        timeline.stop();
        gol.resetGame();
        draw();
    }
    
    @FXML
    public void openPatternList() {
        //TODO: IMPLEMENTER DROPDOWN ELLER DIALOGBOKS
        //dialogBoxes.openPatternList();
    }
    
    @FXML
    public void openStatistics() {
        timeline.stop();
        dialogBoxes.statistics(gol, (Stage) gameCanvas.getScene().getWindow());
    }
    
    // TODO: 30.04.2016 Bedre tittel her
    //=========================================================================
    // GAME RELATED
    //=========================================================================
    
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

    public void fitToView() {
        double cellWidth = gameCanvas.getWidth() / (gol.getColumns());
        double cellHeight = gameCanvas.getHeight() / (gol.getRows());

        if(cellWidth < cellHeight) {
            cellSize = cellWidth;
        } else {
            cellSize = cellHeight;
        }
    }

    //=========================================================================
    // Draw methods
    //=========================================================================

    private double getBoardWidth() {
    	return cellSize * gol.getColumns();
    }

    private double getBoardHeight() {
    	return cellSize * gol.getRows();
    }

    // TODO 30.04.2016 Fjern isXInsideGrid og isYInsideGrid
    private boolean isXInsideGrid(double posX) {
    	return ((posX >= offset_X) && (posX <= offset_X + getBoardWidth()));
    }

    private boolean isYInsideGrid(double posY) {
    	return ((posY >= offset_Y) && (posY <= offset_Y + getBoardHeight()));
    }

    /**
     * This method center the board.
     */
    private void centerBoard() {
            offset_X = gameCanvas.getWidth() / 2 - (getBoardWidth() / 2);
            offset_Y = gameCanvas.getHeight() / 2 - (getBoardHeight() / 2);
    }

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

        //Burde være en translatePositionToIndexArray
        int rowStart = (int) ((0 - (offset_Y - getBoardHeight())) / cellSize) - gol.getRows();
        int rowEnd = (int) ((canvasParent.getHeight() - (offset_Y - getBoardHeight())) / cellSize) - gol.getRows();
        int columnStart = (int) ((0 - (offset_X - getBoardWidth())) / cellSize) - gol.getColumns();
        int columnEnd = (int) ((canvasParent.getWidth() - (offset_X - getBoardWidth())) / cellSize) - gol.getColumns();

        int startRow = 0;
        int endRow = rows;
        if(rowStart > 0) {
            startRow = rowStart;
        }
        if(rowEnd < rows) {
            endRow = rowEnd + 1;
        }

        int startCol = 0;
        int endCol = columns;
        if(columnStart > 0) {
            startCol = columnStart;
        }
        if(columnEnd < columns) {
            endCol = columnEnd + 1;
        }

        gc.setFill(stdAliveCellColor);
        for(int row = startRow; row < endRow; row++) {
            for(int col = startCol; col < endCol; col++ ) {
                if (gol.getCellAliveState(row, col) == 1) {
                    gc.fillRect(start_X + cellSize * col, start_Y + cellSize * row, cellSize, cellSize);
                }
            }
        }

        if(drawGrid) {
            drawGridLines(gc);
        }
    }

    private void drawGridLines(GraphicsContext gc) {
    	gc.setLineWidth(stdGridLineWidth);
    	gc.setStroke(stdGridColor);
        final int boardRowLength = gol.getRows();
        final int boardColumnLength = gol.getColumns();

    	double start_X = offset_X;
        double start_Y = offset_Y;

        // Slutten av strekene vil være startposisjonen + bredden/høyde til alle cellene lagt sammen
        double end_X = start_X + getBoardWidth();
        double end_Y = start_Y + getBoardHeight();

    	// For hver rad, tegn en horisontal strek
        for(int y = 0; y <= boardRowLength; y++) {
            gc.strokeLine(start_X, start_Y + (cellSize * y),
                    end_X, start_Y + (cellSize * y));
        }

        // For hver kolonne, tegn en vertikal strek
        for(int x = 0; x <= boardColumnLength; x++) {
        	gc.strokeLine(start_X + (cellSize * x),
                        start_Y, start_X + (cellSize * x), end_Y);
        }

        //EVENTUELLE DEBUG-TEGNINGER
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
            } else if (e.isSecondaryButtonDown()) { //FLYTTEFUNKSJON
                gameCanvas.getScene().setCursor(Cursor.HAND);
                fitToView.setSelected(false);
                offsetBegin_X = e.getX() - offset_X;
                offsetBegin_Y = e.getY() - offset_Y;
            }
        }); // end eventhandler

        gameCanvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, (MouseEvent e) -> {
            if (e.isPrimaryButtonDown()) {  //TEGNEMODUS
                gameCanvas.getScene().setCursor(Cursor.CROSSHAIR);
                double bClick_X = e.getX();
                double bClick_Y = e.getY();

                if ( isXInsideGrid(bClick_X) && isYInsideGrid(bClick_Y) ) {

                    int row    = (int) ((bClick_Y - (offset_Y - getBoardHeight())) / cellSize) - gol.getRows();
                    int column = (int) ((bClick_X - (offset_X - getBoardWidth())) / cellSize) - gol.getColumns();
                    // unngår out of bounds exception,
                    // holder seg innenfor arrayets lengde
                    // tegner kun dersom man er innenfor lengde
                    System.out.println("row " + row + " col " + column);
                    if((row < gol.getRows()) && (row >= 0) && (column < gol.getColumns()) && (column >= 0)) {
                        gol.setCellAliveState(row, column, (drawCell ? (byte)1 : (byte)0));
                        draw();
                    }
                }
            } else if (e.isSecondaryButtonDown()) { //FLYTTEFUNKSJON
                gameCanvas.getScene().setCursor(Cursor.HAND);
                offset_X = e.getX() - offsetBegin_X;
                offset_Y = e.getY() - offsetBegin_Y;
                draw();
            } // end if
        });

        gameCanvas.setOnScroll((ScrollEvent event) -> {
            fitToView.setSelected(false);
            double scrollLocation_X = event.getX();
            double scrollLocation_Y = event.getY();
            double scrollAmount = event.getDeltaY();
            double zoomFactor = 1.1;

            if(scrollAmount < 0) {
                zoomFactor = 1 / zoomFactor;
            }

            offset_X -= (scrollLocation_X - offset_X) * (zoomFactor - 1);
            offset_Y -= (scrollLocation_Y - offset_Y) * (zoomFactor - 1);

            cellSize *= zoomFactor;
            draw();
        });
    }

    private void initializeFpsSlider() {
        fpsSlider.setMin(10);
    	fpsSlider.setMax(60);
        fpsSlider.setSnapToTicks(true);
        fpsSlider.setShowTickMarks(true);
        fpsSlider.setShowTickLabels(true);
        fpsSlider.setMajorTickUnit(10);
        fpsSlider.setMinorTickCount(5);
        fpsLabel.setText("10");
        fpsSlider.valueChangingProperty().addListener((ObservableValue<? extends Boolean> obs, Boolean wasChanging, Boolean isChanging) -> {
            
                int newValue = (int)fpsSlider.getValue();
                if(timeline.getStatus() == Animation.Status.RUNNING) {
                    timeline.stop();
                    initializeKeyFrame(newValue);
                    timeline.play();
                } else {
                    initializeKeyFrame(newValue);
                }
            
        });
    }

    private void initializeBindCanvasSize() {
        gameCanvas.heightProperty().addListener(e -> {
            draw();
    	});
    	gameCanvas.widthProperty().addListener(e -> {
            draw();
    	});
        
        gameCanvas.heightProperty().bind(canvasParent.heightProperty());
    	gameCanvas.widthProperty().bind(canvasParent.widthProperty());
    }

    private void initializeTimeline() {
        timeline.setCycleCount(Animation.INDEFINITE);
        initializeKeyFrame(30);
    }

    private void initializeKeyFrame(int fps) {
        Duration duration = Duration.millis(1000/fps);
        KeyFrame keyFrame;
        keyFrame = new KeyFrame(duration, (ActionEvent e) -> {
            if(fitToView.isSelected()) {
                fitToView();
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

    private void initializeKeyboardShortcuts() {
        Menu file = menuBar.getMenus().get(0);
        Menu edit = menuBar.getMenus().get(1);
        
        //File Menu
        file.getItems().get(0).setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.SHORTCUT_DOWN));
        file.getItems().get(1).setAccelerator(new KeyCodeCombination(KeyCode.L, KeyCombination.SHORTCUT_DOWN));
        file.getItems().get(2).setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.SHORTCUT_DOWN));
        file.getItems().get(3).setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.SHORTCUT_DOWN));
        
        //Edit Menu
        edit.getItems().get(0).setAccelerator(new KeyCodeCombination(KeyCode.P, KeyCombination.SHORTCUT_DOWN));
        edit.getItems().get(1).setAccelerator(new KeyCodeCombination(KeyCode.M, KeyCombination.SHORTCUT_DOWN));
        edit.getItems().get(2).setAccelerator(new KeyCodeCombination(KeyCode.X, KeyCombination.SHORTCUT_DOWN, KeyCombination.SHIFT_DOWN));
        edit.getItems().get(3).setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.SHORTCUT_DOWN));
    }
}
