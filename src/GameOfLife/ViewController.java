package GameOfLife;

import FileManagement.FileLoader;
import FileManagement.RLEDecoder;
import FileManagement.RLEEncoder;
import javafx.animation.Animation;
import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Duration;
import util.DialogBoxes;
import util.Stopwatch;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;

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

    @FXML private ColorPicker gridColorPicker;
    @FXML private ColorPicker cellColorPicker;
    @FXML private ColorPicker backgroundColorPicker;
    @FXML private ColorPicker boardColorPicker;

    @FXML private Slider cellSizeSlider;
    @FXML private Slider fpsSlider;
    @FXML private Label cellSizeLabel;
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

    final private DialogBoxes dialogBoxes = new DialogBoxes(this);
    final private Timeline timeline = new Timeline();
    final private GameOfLife gol = new GameOfLife();

    //Slidere kan manipulere disse verdiene
    private double cellSize = 10;

    //Standard farger (Om ikke annet er spesifisert)
    private Color stdAliveCellColor = Color.BLACK;
    private Color stdBoardColor = Color.GHOSTWHITE;
    private Color stdBackgroundColor = Color.LIGHTGREY;
    private Color stdGridColor = Color.GREY;

    //Gridtykkelse
    private double stdGridLineWidth = 1;

    //Variabler trengt for å kalkulere differansen
    //mellom start og slutt for å finne ut hvor langt brukeren har dratt brettet
    private double offsetBegin_X = 0;
    private double offsetBegin_Y = 0;

    //Offset-verdi som blir enten positiv eller negativ avhengig av
    //hvordan brukeren flytter (canvas onDrag) grid-en
    //Jobber på at den forskyver startposisjonen øverst i venstre hjørne
    private double offset_X = 0;
    private double offset_Y = 0;

    private boolean drawGrid = true;
    private boolean drawBackground = true;
    private boolean drawBoardBackground = true;

    //
    //	CANVAS MOVE/DRAW FLAGS
    //
    private boolean holdingPattern = false; //Find out if user is holding pattern
    private boolean drawMode = false; //False for move, true for draw
    private boolean drawCell = false;
    //=========================================================================
    // Initializer
    //=========================================================================
    public void initialize() {
        initializeTimeline();
        initializeCellSizeSlider(1,1);
        initializeFpsSlider();
        
        gol.newEmptyGame(20, 20);        
        openGame();
        
        initializeBindCanvasSize();
        initializeKeyboardShortcuts();
        initializeMouseEventHandlers();
    }
    
    //=========================================================================
    // GUI Event handlers
    //=========================================================================

    /**
     * This method instansiates a new GameController
     * and calls a dialog box for input.
     */
    @FXML
    public void newGame() {
        timeline.stop();
        int[] rowCol = dialogBoxes.openNewGameDialog();
        
        if(rowCol[0] == 0 || rowCol[1] == 0) {
            return;
        } else {
            gol.newEmptyGame(rowCol[0], rowCol[1]);        
            openGame();
        }
    }
    
    @FXML
    public void newRandomGame() {
        timeline.stop();
        int[] rowCol;
    	rowCol = dialogBoxes.openNewGameDialog();
        
        if(rowCol == null) {
            return;
            
        }
        
        gol.newRandomGame(rowCol[1], rowCol[0]);        
        openGame();
    }
    
    @FXML
    public void openPatternEditor() {
        timeline.stop();
        dialogBoxes.openPatternEditor(gol);
    }
    
    public void loadGameBoardFromRLE(boolean online) {
        timeline.stop();
        statusBar.setText("");
        
        
        FileLoader fileLoader = new FileLoader();
                
        if(online) {
            String urlString = dialogBoxes.urlDialogBox();
            if(!urlString.equals("")) {
                if(!fileLoader.readGameBoardFromURL(urlString)) {
                    statusBar.setText("Could not load file from URL!");
                    return;
                }
            } else {
               return;
            }
        } else {
            List<ExtensionFilter> extFilter = new ArrayList<>();
            extFilter.add(new ExtensionFilter("RLE files", "*.rle"));
            extFilter.add(new ExtensionFilter("All Files", "*.*"));

            File selectedFile = dialogBoxes.fileChooser(extFilter, true);
            
            if(selectedFile != null) {
                if (!fileLoader.readGameBoardFromDisk(selectedFile)) {
                    return;
                }
            } else {
                return;
            }
        }
        
        gol.loadGame(fileLoader.getBoard(), fileLoader.getMetadata());
        openGame();
    }
    
    public void loadGameBoardFromURL() {
        loadGameBoardFromRLE(true);
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
    public void loadGameBoardFromDisk() {
        loadGameBoardFromRLE(false);
    }
    
    /**
     * Opens a currently loaded game, whether it is completely new
     * or loaded from a file.
     */
    public void openGame() {
        handleFitToView();
    }

    /**
     * This method launches a FileChooser and lets the user specify a
     * filename and save the current board.
     * If the file is not null it creates an object of type RLEEncoder and
     * calls the method encode().
     *
     * @see RLEDecoder
     */
    @FXML
    public void saveRLE() {
        List<ExtensionFilter> extFilter = new ArrayList<>();
        extFilter.add(new ExtensionFilter("RLE files", "*.rle"));
        extFilter.add(new ExtensionFilter("All Files", "*.*"));

        File selectedFile = dialogBoxes.fileChooser(extFilter, false);
                
        if(selectedFile != null) {
            RLEEncoder rleenc = new RLEEncoder(gol, selectedFile);
            if(!rleenc.encode()) {
                setStatusBarText("Could not open file!");
                return;
            }
            setStatusBarText("File saved to : " + selectedFile.getAbsolutePath());
        }        
    }

    @FXML
    public void play() {
        if(timeline.getStatus() != Status.RUNNING) {
            timeline.play();
        }
    }

    @FXML
    public void pause() {
        timeline.stop();
    }

    @FXML
    public void reset() {
        timeline.stop();
        gol.resetGame();
        draw();
    }
    
    @FXML
    public void setReset() {
        timeline.stop();
        gol.setFirstGeneration();
    }
    
    @FXML
    public void closeApplication() {
        Platform.exit();
    }

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

    //Funksjon som skal gi brukeren mulighet til å flytte grid-en
    //Endrer offset-verdiene over for å tilby dette til draw()-funksjonene

    @FXML
    void toggleDrawMove(ActionEvent event) {
    	drawMode = !drawMode;

    	if(drawMode) {
            toggleDrawMove.setText("Draw Mode");
        } else {
            toggleDrawMove.setText("Move Mode");
        }
    }

    @FXML
    public void handleFitToView() {
        double cellWidth = gameCanvas.getWidth() / (gol.getColumns());
        double cellHeight = gameCanvas.getHeight() / (gol.getRows());

        if(cellWidth < cellHeight) {
            cellSize = cellWidth;
        } else {
            cellSize = cellHeight;
        }
       
        initializeCellSizeSlider(cellSize, cellSize);
        centerBoard();
        draw();
    }

    @FXML
    public void handleToggleGrid() {
        drawGrid = !toggleGrid.isSelected();
        draw();
    }

    //=========================================================================
    // Draw methods
    //=========================================================================

    //Strengt talt ikke nødvendig med 0, men man kan bytte det med noe annet
    private double getGridStartPosX() {
    	return 0 + offset_X;
    }

    //Strengt talt ikke nødvendig med 0, men man kan bytte det med noe annet
    private double getGridStartPosY() {
    	return 0 + offset_Y;
    }

    private double getBoardWidth() {
    	return cellSize * gol.getColumns();
    }

    private double getBoardHeight() {
    	return cellSize * gol.getRows();
    }

    private boolean isXInsideGrid(double posX) {
    	return ((posX >= getGridStartPosX()) && (posX <= getGridStartPosX() + getBoardWidth()));
    }

    private boolean isYInsideGrid(double posY) {
    	return ((posY >= getGridStartPosY()) && (posY <= getGridStartPosY() + getBoardHeight()));
    }

    /**
     * This method center the board.
     */
    private void centerBoard() {
            offset_X = gameCanvas.getWidth() / 2 - (getBoardWidth() / 2);
            offset_Y = gameCanvas.getHeight() / 2 - (getBoardHeight() / 2);
    }

    private void draw() {
        final double start_X = Math.round(getGridStartPosX());
        final double start_Y = Math.round(getGridStartPosY());
        final int boardRowLength = gol.getRows();
        final int boardColumnLength = gol.getColumns();
        final double boardWidth = getBoardWidth();
        final double boardHeight = getBoardHeight();

        GraphicsContext gc = gameCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, gameCanvas.widthProperty().doubleValue(),
                gameCanvas.heightProperty().doubleValue());

        if(drawBackground) {
            gc.setFill(stdBackgroundColor);
            gc.fillRect(0, 0, gameCanvas.widthProperty().doubleValue(),
                    gameCanvas.heightProperty().doubleValue());
        }

        if(drawBoardBackground) {
            gc.setFill(stdBoardColor);
            gc.fillRect(start_X, start_Y, boardWidth, boardHeight);
        }

        double x = start_X;
        double y = start_Y;

        gc.setFill(stdAliveCellColor);
        for(int row = 0; row < boardRowLength; row++) {
            for(int col = 0; col < boardColumnLength; col++ ) {
                if (gol.getCellAliveState(row, col) == 1) {
                    gc.fillRect(x, y, cellSize, cellSize);
                }
                x += cellSize; //Plusser på for neste kolonne
            }
            x = start_X; //Reset X-verdien for neste rad
            y += cellSize; //Plusser på for neste rad
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

    	double start_X = getGridStartPosX();
        double start_Y = getGridStartPosY();

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
  // Dialog boxes
  //================================================================================

    

    /**
     * This method launches a dialog box where the user can specify
     * meta data for the game board.
     * Sets the meta data object with the information the user provides.
     *
     * @return void
     */
    private void metaDataDialogBox() {
    	dialogBoxes.metaDataDialogBox(gol.getMetaData());
    }

    @FXML
    private void openOptions() {

    }

    private void initializeMouseEventHandlers() {
        gameCanvas.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent e) -> {
            if(drawMode) {
                //TEGNEMODUS
                double bClick_X = e.getX();
                double bClick_Y = e.getY();

                if(isXInsideGrid(bClick_X) && isYInsideGrid(bClick_Y)) {
                    int row = (int) ((bClick_Y - (getGridStartPosY() - getBoardHeight())) / cellSize) - gol.getColumns();
                    int column = (int) ((bClick_X - (getGridStartPosX() - getBoardWidth())) / cellSize) - gol.getRows();
                    if(holdingPattern) {
                        //drawObject(row, column, pattern)
                        byte[][] testArray = new byte[][] {
                                    {0, 1, 0},
                                    {0, 0, 1},
                                    {1, 1, 1}
                        };

                        final int M = testArray.length;
                        final int N = testArray[0].length;
                        byte[][] ret = new byte[N][M];
                        for(int r = 0; r < M; r++) {
                            for (int c = 0; c < N; c++) {
                                ret[c][M-1-r] = testArray[r][c];
                            }
                        }

                        int mid = (0 + ret.length - 1) / 2;

                        for(int i = 0; i < ret.length; i++) {
                            for(int j = 0; j < ret[i].length; j++) {
                                gol.setCellAliveState(row + i -mid, column + j -mid, ret[i][j]);
                            }
                        }
                        holdingPattern = false;
                    } else {
                        drawCell = (gol.getCellAliveState(row, column) != 1);
                        gol.setCellAliveState(row, column, (drawCell ? (byte)1 : (byte)0));
                    }
                    draw();
                }

            } else {//FLYTTEFUNKSJON
                offsetBegin_X = e.getX() - getGridStartPosX();
                offsetBegin_Y = e.getY() - getGridStartPosY();
            } // end if
        } // end handle
        ); // end eventhandler

        gameCanvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, (MouseEvent e) -> {
            if(drawMode) {
                double bClick_X = e.getX();
                double bClick_Y = e.getY();

                if( isXInsideGrid(bClick_X) && isYInsideGrid(bClick_Y) && !holdingPattern ) {

                    int row    = (int) ((bClick_Y - (getGridStartPosY() - getBoardHeight())) / cellSize) - gol.getRows();
                    int column = (int) ((bClick_X - (getGridStartPosX() - getBoardWidth())) / cellSize) - gol.getColumns();

                    // unngår out of bounds exception,
                    // holder seg innenfor arrayets lengde
                    // tegner kun dersom man er innenfor lengde
                    if((row < gol.getRows()-1) && (column < gol.getColumns()-1)) {
                        gol.setCellAliveState(row, column, (drawCell ? (byte)1 : (byte)0));
                        draw();
                    }
                }
            } else {
                //FLYTTEFUNKSJON
                offset_X = e.getX() - offsetBegin_X;
                offset_Y = e.getY() - offsetBegin_Y;
                draw();
            } // end if
        });
        
        gameCanvas.setOnScroll((ScrollEvent event) -> {
            double scrollLocation_X = event.getX();
            double scrollLocation_Y = event.getY();
            double scrollAmount = event.getDeltaY();
            double zoomFactor = 1.01;

            if(scrollAmount < 0) {
                zoomFactor = 1 / zoomFactor;
            }

            //Midtpunkter av Canvas
            double center_X = gameCanvas.getWidth()  / 2;
            double center_Y = gameCanvas.getHeight() / 2;

            //Finner hvor startposisjonen ligger øverst i venstre hjørnet av Canvas.
            double start_X = center_X - (getBoardWidth() / 2) - offset_X;
            double start_Y = center_Y - (getBoardHeight() / 2) - offset_Y;


            //Variablene brukt til å tegne firkanten. Plusses med bredden/høyden etter hver iterasjon i for-løkken
            //offset-verdien bestemmer hvor grid-en skal tegnes avhengig av om brukeren har flyttet den ved å dra den (onDrag-funksjon)

            //Finn ut om brukeren har musen over grid-et
            if((isXInsideGrid(scrollLocation_X)) && (isYInsideGrid(scrollLocation_Y))) {
                cellSizeSlider.setValue(cellSize*zoomFactor);
                
                offset_X += (scrollLocation_X - center_X);
                offset_Y += (scrollLocation_Y - center_Y);
                
                draw();
            } else {
                cellSizeSlider.setValue(cellSize*zoomFactor);
                draw();
            }
        });
    }

    /**
     * Initializes the cellSizeSlider with the current value and minimum value
     * 
     * @param current
     * @param min
     */
    private void initializeCellSizeSlider(double current, double min) {
        cellSizeSlider.setMin(min);
    	cellSizeSlider.setMax(150);
    	cellSizeSlider.setValue(current);
    	cellSizeLabel.setText(Double.toString(cellSizeSlider.getValue()));
        
        cellSizeSlider.valueProperty().addListener((ObservableValue<?
                    extends Number> ov, Number old_size, Number new_size) -> {
                cellSizeLabel.setText(Double.toString(new_size.intValue()));
                cellSize = new_size.intValue();
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
        fpsSlider.setMinorTickCount(0);
        fpsLabel.setText("10");
        fpsSlider.valueChangingProperty().addListener((ObservableValue<? extends Boolean> obs, Boolean wasChanging, Boolean isChanging) -> {
            if (!isChanging) {
                int newValue = (int)fpsSlider.getValue();
                fpsLabel.setText(Integer.toString(newValue));
                if(timeline.getStatus() == Animation.Status.RUNNING) {
                    timeline.stop();
                    initializeKeyFrame(newValue);
                    timeline.play();
                } else {
                    initializeKeyFrame(newValue);
                }
                
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
            Stopwatch sw = new Stopwatch("Next generation threading");
            sw.start();
            //Thread newGenerationThread = new Thread() {
            //    public void run() {
                    gol.update();
            /*    }
            };
            newGenerationThread.start();

            try {
                newGenerationThread.join();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }*/
            draw();
            sw.stop();
        });
        timeline.getKeyFrames().clear();
        timeline.getKeyFrames().add(0, keyFrame);
    }
    
    public void setMainStageDialogBoxes(Stage mainStage) {
        dialogBoxes.setMainStage(mainStage);
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
        file.getItems().get(2).setAccelerator(new KeyCodeCombination(KeyCode.L, KeyCombination.SHORTCUT_DOWN, KeyCombination.SHIFT_DOWN));
        file.getItems().get(3).setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.SHORTCUT_DOWN));
        file.getItems().get(4).setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.SHORTCUT_DOWN));
        
        //Edit Menu
        edit.getItems().get(0).setAccelerator(new KeyCodeCombination(KeyCode.P, KeyCombination.SHORTCUT_DOWN));
        edit.getItems().get(1).setAccelerator(new KeyCodeCombination(KeyCode.X, KeyCombination.SHORTCUT_DOWN, KeyCombination.SHIFT_DOWN));
        edit.getItems().get(2).setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.SHORTCUT_DOWN));
    }
}
