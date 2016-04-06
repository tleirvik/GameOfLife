package GameOfLife;

import FileManagement.FileLoader;
import FileManagement.RLEDecoder;
import FileManagement.RLEEncoder;
import Listeners.ButtonListener;
import javafx.animation.Animation;
import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import util.DialogBoxes;

import java.io.File;
import java.io.IOException;

/**
 *Denne klassen lytter på hendelser i .fxml
 *
 */
public class ViewController {

    //================================================================================
    // JavaFX Fields
    //================================================================================

    @FXML private MenuItem openPatternEditor;
    @FXML private MenuItem savePicker;
    
    
    @FXML private Button playButton;
    @FXML private Button pauseButton;
    @FXML private Button restartButton;
    

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
    @FXML private ToggleButton toggleDrawMove;
    @FXML private Label statusBar;


    //================================================================================
    // Property fields
    //================================================================================

    private ButtonListener bl;

    private DialogBoxes dialogBoxes;
    private Timeline timeline;
    private GameController gController;

    private RLEDecoder rledec;

    //Hentet fra modellen
    private byte[][] grid;

    private int rows = 1000; //Bør hentes fra gController (?)
    private int columns = 1000; //Bør hentes fra gController (?)

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
    
    private Stage editor;

    //================================================================================
    // Listeners
    //================================================================================

    public void initialize() {
        gController = new GameController();
        
        initializeTimeline();
        initializeCellSizeSlider(1,1);
        initializeFpsSlider();
        initializeBindCanvasSize();

    	dialogBoxes = new DialogBoxes();
        initializeMouseEventHandlers();
    }

    //================================================================================
    // GUI Event handlers
    //================================================================================

    /**
     * This method instansiates a new GameController
     * and calls a dialog box for input.
     */
    @FXML
    public void newGame() {
        timeline.stop();
        int[] rowCol;
    	rowCol = dialogBoxes.openNewGameDialog();
        
        if(rowCol == null) {
            return;
            
        }
        
        gController.newGame(false, rowCol[1], rowCol[0]);        
        openGame();
    }
    
    @FXML
    public void openPatternEditor() {
        timeline.stop();

        editor = new Stage();
        editor.initModality(Modality.WINDOW_MODAL);
        editor.initOwner(gameCanvas.getScene().getWindow());
        
        try {
            FXMLLoader loader;
            loader = new FXMLLoader(getClass().getResource("PatternEditor.fxml"));
            
            BorderPane root = loader.load();
            editor.setResizable(false);
            
            // Muliggjør overføring av nødvendig data til editor.
            EditorController edController = loader.getController();
            // overføre data via setter ?
            edController.setPattern(grid, gController.getMetadata());

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource(
                    "patternEditor.css").toExternalForm());
            
            editor.setScene(scene);
            editor.setTitle("Pattern Editor");
            editor.show();
        } catch (IOException e) {
            DialogBoxes.infoBox("Error", "Could not open the Pattern Editor", 
                    e.getMessage());
        } 
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
            Stage mainStage = (Stage) gameCanvas.getScene().getWindow();
            
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("RLE files", "*.rle"),
                new ExtensionFilter("All Files", "*.*"));

            File selectedFile = fileChooser.showOpenDialog(mainStage);
            
            if(selectedFile != null) {
                if (!fileLoader.readGameBoardFromDisk(selectedFile)) {
                    statusBar.setText("Could not open file!");
                    return;
                }
            } else {
                return;
            }
        }
        
        byte[][] board = fileLoader.getBoard();
        rows = board.length;
        columns = board[0].length;
        gController.newGame(board, fileLoader.getMetadata());
        
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
        grid = gController.getBoardReference();
        rows = gController.getRows();
        columns = gController.getColumns();
        setLowestCellSize();
        centerBoard();
        draw();
        
        openPatternEditor.setDisable(false);
        playButton.setDisable(false);
        pauseButton.setDisable(false);
        restartButton.setDisable(false);
        savePicker.setDisable(false);
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
    	Stage mainStage = (Stage) gameCanvas.getScene().getWindow();

    	if (gController.getBoardReference() != null) {
    		metaDataDialogBox();
    	}

    	FileChooser fileChooser = new FileChooser();
    	fileChooser.setTitle("Save RLE Pattern to file");
        fileChooser.getExtensionFilters().add(
            new ExtensionFilter("RLE files", "*.rle"));
        File saveRLEFile = fileChooser.showSaveDialog(mainStage);

            if (saveRLEFile != null) {
                // Tror det er greit å try-catche i RLEEncoder, da er det ryddig i ViewController

                // Hentet hele brettet med en ny metode jeg lagde. Vi må huske på å gå igjennom den neste gang
                RLEEncoder rleenc = new RLEEncoder(gController.getBoard(), saveRLEFile);
                if (!rleenc.encode()) {
                        statusBar.setText("An error occured trying to save the file. Please try again.");
                        return;
                }
                System.out.println(saveRLEFile.getAbsolutePath());
                statusBar.setText("File saved to : " + saveRLEFile.getAbsolutePath());
            }
 	}

    @FXML
    public void play() {
        if(timeline != null || timeline.getStatus() != Status.RUNNING) {
            timeline.play();
        }
    }

    @FXML
    public void pause() {
        timeline.stop();
        if(timeline != null && timeline.getStatus() == Status.RUNNING) {
            timeline.pause();
        }
    }

    @FXML
    public void restart() {
        if(gController != null && timeline.getStatus() != Status.STOPPED) {
            timeline.stop();
        }
        gController.resetGame();
        draw();
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

    
    // HVOR KOMMER DISSE FRA? :)
    @FXML
    public void handleMouseDrag(MouseEvent e) {

    }

    // HVOR KOMMER DISSE FRA? :)
    @FXML
    public void handleMouseClick(MouseEvent e) {

    }

    @FXML
    public void handleFitToView() {
        double cellWidth = gameCanvas.getWidth() / (columns - 2);
        double cellHeight = gameCanvas.getHeight() / (rows - 2);

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

	//================================================================================
    // Draw methods
    //================================================================================

    //Strengt talt ikke nødvendig med 0, men man kan bytte det med noe annet
    private double getGridStartPosX() {
    	return 0 + offset_X;
    }

    //Strengt talt ikke nødvendig med 0, men man kan bytte det med noe annet
    private double getGridStartPosY() {
    	return 0 + offset_Y;
    }

    private double getBoardWidth() {
    	return cellSize * (columns-2);
    }

    private double getBoardHeight() {
    	return cellSize * (rows-2);
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
        if(grid == null) {
            return;
        }
        double start_X = Math.round(getGridStartPosX());
        double start_Y = Math.round(getGridStartPosY());
        double boardWidth = getBoardWidth();
        double boardHeight = getBoardHeight();

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
        for(int rows = 1; rows < grid.length - 1; rows++) {
            for(int cols = 1; cols < grid[0].length - 1; cols++ ) {
                if (grid[rows][cols]== 1) {
                    gc.fillRect(x, y, cellSize, cellSize);
                }
                x += cellSize; //Plusser på for neste kolonne
            }
            x = start_X; //Reset X-verdien for neste rad
            y += cellSize; //Plusser på for neste rad
        }

        if(drawGrid && grid.length < 100) {
            drawGridLines(gc);
            System.out.println(grid.length);
        }
    }

    private void drawGridLines(GraphicsContext gc) {
    	gc.setLineWidth(stdGridLineWidth);
    	gc.setStroke(stdGridColor);

    	double start_X = getGridStartPosX();
        double start_Y = getGridStartPosY();

        // Slutten av strekene vil være startposisjonen + bredden/høyde til alle cellene lagt sammen
        double end_X = start_X + getBoardWidth();
        double end_Y = start_Y + getBoardHeight();

    	// For hver rad, tegn en horisontal strek
        for(int y = 0; y <= rows-2; y++) {
            gc.strokeLine(start_X, start_Y + (cellSize * y),
                    end_X, start_Y + (cellSize * y));
        }

        // For hver kolonne, tegn en vertikal strek
        for(int x = 0; x <= columns-2; x++) {
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

    

    @FXML
    public void showStatistics() {
        timeline.stop();
        Statistics statistics = new Statistics(grid, 20, new MetaData());
        statistics.run();

        dialogBoxes.statisticsDialogBox(statistics.getStats());
    }
    /**
     * This method launches a dialog box where the user can specify
     * meta data for the game board.
     * Sets the meta data object with the information the user provides.
     *
     * @return void
     */
    private void metaDataDialogBox() {
    	dialogBoxes.metaDataDialogBox(gController.getMetadata());
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
                    int row = (int) ((bClick_Y - (getGridStartPosY() - getBoardHeight())) / cellSize) - rows;
                    int column = (int) ((bClick_X - (getGridStartPosX() - getBoardWidth())) / cellSize) - columns;

                    if(holdingPattern) {
//                                // drawObject(row, column, pattern)
//                                boolean[][] testArray = new boolean[][] {
//                                            {false, true, false},
//                                            {false, false, true},
//                                            {true, true, true}
//                                };
//
//                                final int M = testArray.length;
//                                final int N = testArray[0].length;
//                                boolean[][] ret = new boolean[N][M];
//                                for(int r = 0; r < M; r++) {
//                                    for (int c = 0; c < N; c++) {
//                                        ret[c][M-1-r] = testArray[r][c];
//                                    }
//                                }

//                                int mid = (0 + ret.length - 1) / 2;
//
//                                for(int i = 0; i < ret.length; i++) {
//                                    for(int j = 0; j < ret[i].length; j++) {
//                                        //gameCanvas.getGraphicsContext2D().drawImage(wImage, i, j);
//                                        gController.setCellAliveStatus(row + i -mid, column + j -mid, ret[i][j]);
//                                    }
//                                }
// holdingPattern = false;
                    } else {
                        drawCell = (gController.getCellAliveStatus(row, column) == 1);
                        gController.setCellAliveStatus(row, column, (drawCell ? (byte)1 : (byte)0));
                    }
                    grid = gController.getBoardReference();
                    draw();
                }

                int row = 0;
                int column = 0;

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

                    int row    = (int) ((bClick_Y - (getGridStartPosY() - getBoardHeight())) / cellSize) - rows;
                    int column = (int) ((bClick_X - (getGridStartPosX() - getBoardWidth())) / cellSize) - columns;

                    // unngår out of bounds exception,
                    // holder seg innenfor arrayets lengde
                    // tegner kun dersom man er innenfor lengde
                    if((row < grid.length) && (column < grid[0].length)) {
                        gController.setCellAliveStatus(row, column, (drawCell ? (byte)1 : (byte)0));
                        grid = gController.getBoardReference();
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
        gameCanvas.heightProperty().bind(canvasParent.heightProperty());
    	gameCanvas.widthProperty().bind(canvasParent.widthProperty());

    	gameCanvas.heightProperty().addListener(e -> {
            draw();
    	});
    	gameCanvas.widthProperty().addListener(e -> {
            draw();
    	});
    }

    private void initializeTimeline() {
        timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);
        initializeKeyFrame(30);
    }

    /**
     * Calculates and sets the cell size that is required to show
     * the entire board on the screen
     */
    private void setLowestCellSize() {
        double cellWidth = gameCanvas.getWidth() / columns;
        double cellHeight = gameCanvas.getHeight() / rows;

        if(cellWidth < cellHeight) {
            initializeCellSizeSlider(cellWidth, cellWidth);
        } else {
            initializeCellSizeSlider(cellHeight, cellHeight);
        }     
    }

    private void initializeKeyFrame(int fps) {
        Duration duration = Duration.millis(1000/fps);
        KeyFrame keyFrame;
        keyFrame = new KeyFrame(duration, (ActionEvent e) -> {
            long startTime = System.nanoTime();
            gController.play();
            long endTime = System.nanoTime();
            long duration2 = (endTime - startTime) / 1000000;
            System.out.println("Next Generation: " + duration2);

            startTime = System.nanoTime();
            draw();
            endTime = System.nanoTime();
            duration2 = (endTime - startTime) / 1000000;
            System.out.println("Draw: " + duration2);
        });
        timeline.getKeyFrames().clear();
        timeline.getKeyFrames().add(0, keyFrame);
    }
}
