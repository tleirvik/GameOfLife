package GameOfLife;

import FileManagement.FileLoader;
import java.io.File;
import java.io.IOException;

import FileManagement.RLEDecoder;
import FileManagement.RLEEncoder;
import Listeners.ButtonListener;
import javafx.animation.Animation;
import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Duration;
import util.DialogBoxes;

/**
 *Denne klassen lytter på hendelser i .fxml
 *
 */
public class ViewController {

	//================================================================================
    // JavaFX Fields
    //================================================================================

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
    @FXML private CheckBox fitToView;
    @FXML private Pane canvasParent;
    @FXML private Canvas gameCanvas;
    @FXML private ToggleButton toggleDrawMove;
    @FXML private Text textNextGeneration;
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
    private int fps = 8;

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
    private boolean cellDraw = false; //Inverter for å tegne andre fargen

	//================================================================================
    // Listeners
    //================================================================================

    public void initialize() {
    	cellSizeSlider.setMin(1);
    	cellSizeSlider.setMax(150);
    	cellSizeSlider.setValue(cellSize);
    	cellSizeLabel.setText(Double.toString(cellSizeSlider.getValue()));

    	fpsSlider.setMin(0);
    	fpsSlider.setMax(60);
    	fpsSlider.setValue((fps));
    	fpsSlider.valueProperty().addListener((ObservableValue<?
                extends Number> ov, Number old_val, Number new_val) -> {
            fpsLabel.setText(Integer.toString(new_val.intValue()));
            fps = new_val.intValue();
            timeline.setRate(fps);
            });

        //Finn posisjonen til musen når det zoomes inn
    	//Kalkuler differansen fra musen til canvasen sitt midtpunkt
    	//Flytt Canvasen med den nye differanseverdien
    	/*
    	 * 		CANVAS
    	 */
    	gameCanvas.heightProperty().bind(canvasParent.heightProperty());
    	gameCanvas.widthProperty().bind(canvasParent.widthProperty());

    	gameCanvas.heightProperty().addListener(e -> {
            draw();
    	});
    	gameCanvas.widthProperty().addListener(e -> {
            draw();
    	});

    	dialogBoxes = new DialogBoxes();
        initializeMouseEventHandlers();
        gController = new GameController();

    } // end initialize

    //================================================================================
    // GUI Event handlers
    //================================================================================

    /**
     * This method instansiate a new GameController
     * and calls a dialog box for input. The it center the board and
     * draws the board
     */
    @FXML
    public void openNewGame() {
        if (isTimelineRunning()) {
            timeline.stop();
            openNewGameDialog();
            centerBoard();

            gController.newGame(false, rows, columns); // send parametrene videre
            grid = gController.getGameBoard();
            draw();
        } else {
            openNewGameDialog();
            // metadata = new MetaData();
            centerBoard();

            gController.newGame(false, rows, columns); // send parametrene videre
            grid = gController.getGameBoard();
            draw();
        }
    }

    /**
     * This method launches a FileChooser and lets the user select a file.
     * If the file is not null it creates an object of type RLEDecoder and
     * calls the method decode(). And starts a new game with the the selected
     * pattern from file.
     *
     * @return void
     * @see RLEDecoder
     */
    @FXML
    public void loadGameBoardFromDisk() {
        boolean isDynamic = false; //La bruker velge om brettet skal kunne øke i bredde/høyde
        statusBar.setText("");
        Stage mainStage = (Stage) gameCanvas.getScene().getWindow();

        if(isTimelineRunning()) {
            timeline.stop();
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("RLE files", "*.rle"),
                new ExtensionFilter("All Files", "*.*"));

        File selectedFile = fileChooser.showOpenDialog(mainStage);
        if (selectedFile != null) {
            FileLoader fileLoader = new FileLoader();
            if (!fileLoader.readGameBoardFromDisk(selectedFile)) {
                statusBar.setText("Could not open file!");
                return;
            }

            RLEDecoder rledec = new RLEDecoder(fileLoader.getRLEdata());
            if (!rledec.decode()) {
                statusBar.setText("An error occured trying to read the file");
                return;
            }
            commonBehaviorInRLE(gController, rledec);
        }
    }
    /**
     *  Trenger nytt navn :)
     */
    private void commonBehaviorInRLE(GameController gController, RLEDecoder rledec) {
        if(gController == null) {
            gController = new GameController();
            rledecMetode(gController, rledec);
            centerBoardAndDraw();
        } else if(timeline != null) {
            timeline.stop();
            rledecMetode(gController, rledec);
            centerBoardAndDraw();
        } else {
            rledecMetode(gController, rledec);
            centerBoardAndDraw();
        }
    }

    /**
     *  Trenger nytt navn :)
     */
    private void centerBoardAndDraw() {
        if (gController == null) {
            gController = new GameController();
        } else {
            grid = gController.getGameBoard();
            centerBoard();
            draw();
        }

    }
    /**
     *  Trenger nytt navn :)
     */
    private void rledecMetode(GameController gController, RLEDecoder rledec) {
        boolean isDynamic = false;
        gController.newGame(rledec.getBoard(), isDynamic);
        gController.setMetaData(rledec.getMetadata());
        rows = rledec.getBoard().length;
        columns = rledec.getBoard()[0].length;
    }
    /**
     *
     * BUG. Må kjøre metoden to ganger for at det skal fungere. gController må
     * trolig instansieres et eller annet sted.
     */
    public void loadGameBoardFromURL() {
    	boolean isDynamic = false; //La bruker velge om brettet skal kunne øke i bredde/høyde
        statusBar.setText("");
        FileLoader fileLoader = new FileLoader();
        
        if(!fileLoader.readGameBoardFromURL(dialogBoxes.urlDialogBox())) {
            statusBar.setText("Could not load file from URL!");
            return;
        }

        RLEDecoder rledec = new RLEDecoder(fileLoader.getRLEdata());
        if (!rledec.decode()) {
            statusBar.setText("An error occured trying to read the file");
            return;
        }
        commonBehaviorInRLE(gController, rledec);
    }

    /**
     * This method launches a FileChooser and lets the user specify a
     * filename and save the current board.
     * If the file is not null it creates an object of type RLEEncoder and
     * calls the method encode().
     *
     * @return void
     * @throws IOException
     * @see RLEDecoder
     */
    @FXML
    public void saveRLE() {
    	Stage mainStage = (Stage) gameCanvas.getScene().getWindow();

    	if (gController.getBoard() != null) {
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
        if(gController != null) {
            if(timeline == null || timeline.getStatus() != Status.RUNNING) {
                timeline = new Timeline();
                timeline.setCycleCount(Animation.INDEFINITE);
                Duration duration = Duration.millis(1000 / fps);
                KeyFrame keyFrame;
                keyFrame = new KeyFrame(duration, (ActionEvent e) -> {
                    long startTime = System.nanoTime();
                    gController.play();
                    long endTime = System.nanoTime();
                    long duration2 = (endTime - startTime) / 1000000;
                    System.out.println("Next Generation: " + duration2);

                    startTime = System.nanoTime();
                    grid = gController.getGameBoard();
                    endTime = System.nanoTime();
                    duration2 = (endTime - startTime) / 1000000;
                    System.out.println("Get grid: " + duration2);

                    startTime = System.nanoTime();
                    draw();
                    endTime = System.nanoTime();
                    duration2 = (endTime - startTime) / 1000000;
                    System.out.println("Draw: " + duration2);
                });
            timeline.getKeyFrames().add(keyFrame);
            }
            timeline.play();
        }
    }

    @FXML
    public void pause() {
        if(gController != null) {
            timeline.stop();
            if(timeline != null && timeline.getStatus() == Status.RUNNING) {
                timeline.pause();
            }
        }
    }

    @FXML
    public void restart() {
        if(gController != null && timeline.getStatus() == Status.PAUSED) {
            timeline.play();
        }
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
        if((gController != null) && (gController.getBoard() != null)) {
            stdBoardColor = boardColorPicker.getValue();
            draw();
        }
    }

    /**
     *  Method that changes the cell color according to
     *  the value selected from the ColorPicker
     *
     */
    @FXML
    public void changeCellColor() {
        if(gController != null) {
            stdAliveCellColor = cellColorPicker.getValue();
            draw();
        }
    }

    /**
     *  Method that changes the grid color according to
     *  the value selected from the ColorPicker
     *
     */
    @FXML
    public void changeGridColor() {
        if(gController != null) {
            stdGridColor = gridColorPicker.getValue();
            draw();
        }
    }

    //Funksjon som skal gi brukeren mulighet til å flytte grid-en
    //Endrer offset-verdiene over for å tilby dette til draw()-funksjonene

    /**
     *
     * @param
     */
    @FXML
    public void moveGrid() {
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
                System.out.println("Cellebredde før: " + getBoardWidth());
                System.out.println("Cellehøyde før: " + getBoardHeight());

                cellSize *= zoomFactor;

                System.out.println("Cellebredde etter: " + getBoardWidth());
                System.out.println("Cellehøyde etter: " + getBoardHeight());

                offset_X += (scrollLocation_X - center_X);
                offset_Y += (scrollLocation_Y - center_Y);

                System.out.println("Offsetverdier etter: " + offset_X + " " + offset_Y);
                System.out.println();
                draw();
            } else {
                cellSize *= zoomFactor;
                draw();
            }
        });
    }

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
    public void handleMouseDrag(MouseEvent e) {

    }

    @FXML
    public void handleMouseClick(MouseEvent e) {

    }

    @FXML
    public void handleFitToView() {
        if(gController != null) {
            double cellWidth = gameCanvas.getWidth() / columns;
            double cellHeight = gameCanvas.getHeight() / rows;

            if(cellWidth < cellHeight) {
                cellSize = cellWidth;
            } else {
                cellSize = cellHeight;
            }
            centerBoard();
            draw();
        }
    }

    @FXML
    public void handleToggleGrid() {
        if(gController != null) {
            drawGrid = !toggleGrid.isSelected();
            draw();
        }
    }


    // sliderens verdi må endres ved fit to view
    @FXML
    public void handleCellSizeSlider() {

            cellSizeSlider.valueProperty().addListener((ObservableValue<?
                    extends Number> ov, Number old_val, Number new_val) -> {
                fitToView.setSelected(false);
                cellSizeLabel.setText(Double.toString(new_val.intValue()));
                cellSize = new_val.intValue();
                centerBoard();
                draw();
            });

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
    	return cellSize * columns;
    }

    private double getBoardHeight() {
    	return cellSize * rows;
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
    	//If the grid is not retrieved yet, do not run the draw function
    	if(grid != null) {
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

            for (byte[] grid1 : grid) {
                for (int col = 0; col < grid1.length; col++) {
                    if (grid1[col] == 1) {
                        //Hvis cellen lever
                        gc.setFill(stdAliveCellColor);
                        gc.fillRect( x, y, cellSize, cellSize);
                        x += cellSize; //Plusser på for neste kolonne
                    } else {
                        x += cellSize; //Plusser på for neste kolonne
                    }
                }
                x = start_X; //Reset X-verdien for neste rad
                y += cellSize; //Plusser på for neste rad
            }

            //Bruker kan bestemme om grid skal tegnes
            if(drawGrid) {
                drawGridLines(gc);
            }
        } // end if
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
        for(int y = 0; y <= rows; y++) {
            gc.strokeLine(start_X, start_Y + (cellSize * y),
                    end_X, start_Y + (cellSize * y));
        }

        // For hver kolonne, tegn en vertikal strek
        for(int x = 0; x <= columns; x++) {
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

    private void openNewGameDialog() {
    	int[] rowCol;
    	rowCol = dialogBoxes.openNewGameDialog();
        if(rowCol != null) {
            rows = rowCol[0];
            columns = rowCol[1];
        }
    }

    /**
     * This method launches a dialog box where the user can specify
     * meta data for the game board.
     * Sets the meta data object with the information the user provides.
     *
     * @return void
     */
    private void metaDataDialogBox() {
    	dialogBoxes.metaDataDialogBox(gController.getBoard().getMetaData());
    }

    @FXML
    private void openOptions() {

    }

    /**
     * This method returns true if the timeline is running
     *
     * @return boolean True if timeline is running
     */
    private boolean isTimelineRunning() {
    	return (timeline != null) && (timeline.getStatus() == Status.RUNNING);
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
                        cellDraw = !gController.getCellAliveStatus(row, column);
                        gController.setCellAliveStatus(row, column, !gController.getCellAliveStatus(row, column));
                    }
                    grid = gController.getGameBoard();
                    draw();
                }

                int row = 0;
                int column = 0;

            } else {//FLYTTEFUNKSJON
                fitToView.setSelected(false);
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
                    if((row < gController.getGameBoard().length) &&
                            (column < gController.getGameBoard().length)) {
                        gController.setCellAliveStatus(row, column, !gController.getCellAliveStatus(row, column));
                        grid = gController.getGameBoard();
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
    }
}
