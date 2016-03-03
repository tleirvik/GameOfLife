package GameOfLife;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import FileManagement.RLEDecoder;
import javafx.animation.Animation;
import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;

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
    @FXML private Label cellSizeLabel;
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

    private Timeline timeline;
    private final GameController gController = new GameController();

    //Hentet fra modellen
    private boolean[][] grid;

    private int rows	= 1000; //Bør hentes fra gController (?)
    private int columns = 1000; //Bør hentes fra gController (?)

    //Slidere kan manipulere disse verdiene
    private double cellSize	= 10;

    //Standard farger (Om ikke annet er spesifisert)
    private Color stdAliveCellColor	= Color.BLACK;
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
    	timeline = new Timeline();
		timeline.setCycleCount(Animation.INDEFINITE);
		Duration duration = Duration.millis(100);

		KeyFrame keyFrame = new KeyFrame(duration, (ActionEvent e) -> {
			long startTime = System.nanoTime();
			gController.play();
			long endTime = System.nanoTime();
			long duration2 = (endTime - startTime) / 100000;
			System.out.println("Next Generation: " +duration2);

			startTime = System.nanoTime();
			grid = gController.getBooleanGrid();
			endTime = System.nanoTime();
			duration2 = (endTime - startTime) / 100000;
			System.out.println("Get grid: " +duration2);

			startTime = System.nanoTime();
			draw();
			endTime = System.nanoTime();
			duration2 = (endTime - startTime) / 100000;
			System.out.println("Draw: " +duration2);
		});
		timeline.getKeyFrames().add(keyFrame);

    	cellSizeSlider.setMin(1);
    	cellSizeSlider.setMax(150);
    	cellSizeSlider.setValue(cellSize);
    	cellSizeLabel.setText(Double.toString(cellSizeSlider.getValue()));

    	cellSizeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
            	fitToView.setSelected(false);
            	cellSizeLabel.setText(Double.toString(new_val.intValue()));
            	cellSize = new_val.intValue();
                draw();
            }
        });

    	fitToView.selectedProperty().addListener(e -> {
    		if(fitToView.isSelected()) {
    			double cellWidth = gameCanvas.getWidth() / columns;
    			double cellHeight = gameCanvas.getHeight() / rows;

    			if(cellWidth < cellHeight)
    				cellSize = cellWidth;
    			else
    				cellSize = cellHeight;

    			centerBoard();
    			draw();
    		}
		});


        //Finn posisjonen til musen når det zoomes inn
    	//Kalkuler differansen fra musen til canvasen sitt midtpunkt
    	//Flytt Canvasen med den nye differanseverdien
    	gameCanvas.setOnScroll(new EventHandler<ScrollEvent>() {
			@Override
			public void handle(ScrollEvent event) {
				double scrollLocation_X = event.getX();
				double scrollLocation_Y = event.getY();
				double scrollAmount = event.getDeltaY();
				double zoomFactor = 1.01;

				//System.out.println("Museposisjoner X - Y: " + event.getX() + " " + event.getY());

				if(scrollAmount < 0) {
					zoomFactor = 1/zoomFactor;
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
			}
    	});

    	/*
    	 * 		CANVAS
    	 */

    	toggleGrid.selectedProperty().addListener(e -> {
    		drawGrid = !toggleGrid.isSelected();
    		draw();
		});

    	gameCanvas.heightProperty().bind(canvasParent.heightProperty());
    	gameCanvas.widthProperty().bind(canvasParent.widthProperty());

    	gameCanvas.heightProperty().addListener(e -> {
    		draw();
    	});
    	gameCanvas.widthProperty().addListener(e -> {
    		draw();
    	});

		gameCanvas.addEventHandler(MouseEvent.MOUSE_PRESSED,new EventHandler<MouseEvent>() {
		   @Override
		   public void handle(MouseEvent e) {
			   if(drawMode) {//TEGNEMODUS
				   double bClick_X = e.getX();
				   double bClick_Y = e.getY();

				   if( isXInsideGrid(bClick_X) && isYInsideGrid(bClick_Y) )
				   {

					   int row    = (int) ((bClick_Y - (getGridStartPosY() - getBoardHeight())) / cellSize) - rows;
					   int column = (int) ((bClick_X - (getGridStartPosX() - getBoardWidth())) / cellSize) - columns;

					   if (holdingPattern) {
						   // drawObject(row, column, pattern)
						   boolean[][] testArray = new boolean[][] {
							   {false, true, false},
							   {false, false, true},
							   {true, true, true}
						   };

						   final int M = testArray.length;
						   final int N = testArray[0].length;
						   boolean[][] ret = new boolean[N][M];
						   for (int r = 0; r < M; r++) {
							   for (int c = 0; c < N; c++) {
								   ret[c][M-1-r] = testArray[r][c];
						       }
						   }

						   int mid = (0 + ret.length - 1) / 2;

						   for(int i = 0; i < ret.length; i++) {
							   for(int j = 0; j < ret[i].length; j++) {
								   //gameCanvas.getGraphicsContext2D().drawImage(wImage, i, j);
								   gController.setCellAliveStatus(row + i -mid, column + j -mid, ret[i][j]);
							   }
						   }

						   // holdingPattern = false;
					   } else {
						   cellDraw = !gController.getCellAliveStatus(row, column);
						   gController.setCellAliveStatus(row, column, !gController.getCellAliveStatus(row, column));
					   }
					   grid = gController.getBooleanGrid();

					   draw();
				   }

				   int row		= 0;
				   int column	= 0;

			   } else {//FLYTTEFUNKSJON
				   fitToView.setSelected(false);
				   offsetBegin_X = e.getX() - getGridStartPosX();
				   offsetBegin_Y = e.getY() - getGridStartPosY();
	   			}
		   }
		});

		gameCanvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
    		@Override
    		public void handle(MouseEvent e) {
    			if(drawMode) {
    				double bClick_X = e.getX();
    				double bClick_Y = e.getY();

    				if( isXInsideGrid(bClick_X) && isYInsideGrid(bClick_Y) && !holdingPattern )
					{

    					int row    = (int) ((bClick_Y - (getGridStartPosY() - getBoardHeight())) / cellSize) - rows;
 					   	int column = (int) ((bClick_X - (getGridStartPosX() - getBoardWidth())) / cellSize) - columns;

						gController.setCellAliveStatus(row, column, !gController.getCellAliveStatus(row, column));
						grid = gController.getBooleanGrid();
						draw();
					}
				} else {//FLYTTEFUNKSJON
    				offset_X = e.getX() - offsetBegin_X;
    				offset_Y = e.getY() - offsetBegin_Y;
    				draw();
				}
			}
    	});
	}

    //================================================================================
    // GUI Event handlers
    //================================================================================

    @FXML
    public void openNewGame() {
    	openNewGameDialog();
       	centerBoard();

		//Turn off grid if size is under 8
		if(cellSize < 8)
			toggleGrid.setSelected(true);

		gController.newGame(false, rows, columns); //send parametrene videre
		grid = gController.getBooleanGrid();
		draw();
    }

    /**
     * This method launches a FileChooser and lets the user select a file.
     * If the file is not null it creates an object of type RLEDecoder and
     * calls the method decode(). And starts a new game with the the selected
     * pattern from file.
     *
     * @return void
     * @see RLEDecoder.java
     */
    @FXML
    public void loadRLE() {

    	boolean isDynamic = false; //La bruker velge om brettet skal kunne øke i bredde/høyde

    	//Er dette en billig hack???
    	Stage mainStage = (Stage) gameCanvas.getScene().getWindow();

    	FileChooser fileChooser = new FileChooser();
    	 fileChooser.setTitle("Open Resource File");
    	 fileChooser.getExtensionFilters().addAll(
    	         new ExtensionFilter("RLE files", "*.rle"),
    	         new ExtensionFilter("All Files", "*.*"));

    	 File selectedFile = fileChooser.showOpenDialog(mainStage);
    	 if (selectedFile != null) {
    		 RLEDecoder rledec = new RLEDecoder(selectedFile);
			 if (!rledec.decode()) {
				 statusBar.setText("An error occured trying to read the file");
				 return;
			 }
			 gController.newGame(rledec.getBoard(), isDynamic);
			 grid = gController.getBooleanGrid();
			 draw();
    	 }
    }

    @FXML
    public void play() {
		if(timeline == null || timeline.getStatus() != Status.RUNNING) {
            timeline.play();
		}
    }

    @FXML
    public void pause() {
    	timeline.stop();
    	if(timeline != null && timeline.getStatus() == Status.RUNNING) {
    		timeline.stop();
    	}
    }

    @FXML
    public void restart() {

    }

    /**
     *  Method that changes the background color according to
     *  the value selected from the ColorPicker
     *
     *  @return void
     */
    @FXML
    public void changeBackgroundColor() {
		stdBackgroundColor = backgroundColorPicker.getValue();
    }

    /**
     *  Method that changes the board color according to
     *  the value selected from the ColorPicker
     *
     *  @return void
     */
    @FXML
    public void changeBoardColor() {
    	stdBoardColor = boardColorPicker.getValue();
    }

    /**
     *  Method that changes the cell color according to
     *  the value selected from the ColorPicker
     *
     *  @return void
     */
    @FXML
    public void changeCellColor() {
		stdAliveCellColor = cellColorPicker.getValue();
    }

    /**
     *  Method that changes the grid color according to
     *  the value selected from the ColorPicker
     *
     *  @return void
     */
    @FXML
    public void changeGridColor() {
		stdGridColor = gridColorPicker.getValue();
    }

    //Funksjon som skal gi brukeren mulighet til å flytte grid-en
    //Endrer offset-verdiene over for å tilby dette til draw()-funksjonene
    @FXML
    void moveGrid(MouseDragEvent event) {
    	System.out.println("Entered moveGrid");
    	offset_X += event.getX();
    	offset_Y += event.getY();
    	draw();
    }

    @FXML
    void toggleDrawMove(ActionEvent event) {
    	drawMode = !drawMode;

    	if(drawMode) toggleDrawMove.setText("Draw Mode");
    	else toggleDrawMove.setText("Move Mode");
    }

	//================================================================================
    // Draw methods
    //================================================================================

    //Strengt talt ikke nødvendig med 0, men man kan bytte det med noe annet
    public double getGridStartPosX() {
    	return 0 + offset_X;
    }

    //Strengt talt ikke nødvendig med 0, men man kan bytte det med noe annet
    public double getGridStartPosY() {
    	return 0 + offset_Y;
    }

    public double getBoardWidth() {
    	return cellSize * columns;
    }

    public double getBoardHeight() {
    	return cellSize * rows;
    }

    public boolean isXInsideGrid(double posX) {
    	return ((posX >= getGridStartPosX()) && (posX <= getGridStartPosX() + getBoardWidth()));
    }

    public boolean isYInsideGrid(double posY) {
    	return ((posY >= getGridStartPosY()) && (posY <= getGridStartPosY() + getBoardHeight()));
    }

    private void centerBoard() {
    	offset_X = gameCanvas.getWidth() / 2 - (getBoardWidth() / 2);
		offset_Y = gameCanvas.getHeight() / 2 - (getBoardHeight() / 2);
    }

    public void draw() {
    	//If the grid is not retrieved yet, do not run the draw function
    	if(grid == null)
    		return;

    	double start_X = getGridStartPosX();
		double start_Y = getGridStartPosY();
		double boardWidth = getBoardWidth();
		double boardHeight = getBoardHeight();

		GraphicsContext gc = gameCanvas.getGraphicsContext2D();

		gc.clearRect(0, 0, gameCanvas.widthProperty().intValue(),
		gameCanvas.heightProperty().intValue());

		if(drawBackground) {
			gc.setFill(stdBackgroundColor);
			gc.fillRect(0, 0, gameCanvas.widthProperty().intValue(),gameCanvas.heightProperty().intValue());
		}

		if(drawBoardBackground) {
			gc.setFill(stdBoardColor);
			gc.fillRect(start_X, start_Y, boardWidth, boardHeight);
		}

		double x = start_X;
		double y = start_Y;

		for(int row = 0; row < grid.length; row++) {
			for(int col = 0; col < grid[row].length; col++) {

		        if(grid[row][col]) { //Hvis cellen lever
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
		if(drawGrid) drawGridLines(gc);
    }

    public void drawGridLines(GraphicsContext gc) {

    	gc.setLineWidth(stdGridLineWidth);
    	gc.setStroke(stdGridColor);

    	double start_X = getGridStartPosX();
		double start_Y = getGridStartPosY();

        //Slutten av strekene vil være startposisjonen + bredden/høyde til alle cellene lagt sammen
        double end_X = start_X + getBoardWidth();
        double end_Y = start_Y + getBoardHeight();

    	//For hver rad, tegn en horisontal strek
        for(int y = 0; y <= rows; y++) {
        	gc.strokeLine(start_X, start_Y + (cellSize * y), end_X, start_Y + (cellSize * y));
        }

      //For hver kolonne, tegn en vertikal strek
        for(int x = 0; x <= columns; x++) {
        	gc.strokeLine(start_X + (cellSize * x), start_Y, start_X + (cellSize * x), end_Y);
        }


        //EVENTUELLE DEBUG-TEGNINGER
        gc.setFill(stdAliveCellColor);
        gc.fillOval(gameCanvas.getWidth()/2-2, gameCanvas.getHeight()/2-2, 5, 5);

        gc.setFill(Color.RED);
        gc.fillOval(start_X + (getBoardWidth() / 2) - 2, start_Y + (getBoardHeight() / 2) - 2, 5, 5);

    }
  //================================================================================
  // Dialog boxes
  //================================================================================

    private void openNewGameDialog() {
    	Dialog<Pair<Integer, Integer>> dialog = new Dialog<>();
    	dialog.setTitle("New Game");
    	dialog.setHeaderText("Start a new game");
    	ButtonType OKButtonType = new ButtonType("OK", ButtonData.OK_DONE);
    	dialog.getDialogPane().getButtonTypes().addAll(OKButtonType, ButtonType.CANCEL);

    	GridPane grid = new GridPane();
    	grid.setHgap(10);
    	grid.setVgap(10);
    	grid.setPadding(new Insets(20, 150, 10, 10));
    	TextField rowValue = new TextField();
    	rowValue.setPromptText("Rows");
    	TextField columnValue = new TextField();
    	columnValue.setPromptText("Columns");

    	grid.add(new Label("Rows:"), 0, 0);
    	grid.add(rowValue, 1, 0);
    	grid.add(new Label("Columns:"), 0, 1);
    	grid.add(columnValue, 1, 1);

    	Node OKButton = dialog.getDialogPane().lookupButton(OKButtonType);
    	OKButton.setDisable(true);

    	rowValue.textProperty().addListener((observable, oldValue, newValue) -> {
    	    OKButton.setDisable(newValue.trim().isEmpty());
    	});

    	dialog.getDialogPane().setContent(grid);
    	Platform.runLater(() -> rowValue.requestFocus());

    	// Convert the result to a username-password-pair when the login button is clicked.
    	dialog.setResultConverter(dialogButton -> {
    	    if (dialogButton == OKButtonType) {
    	        rows = Integer.parseInt(rowValue.getText());
    	        columns = Integer.parseInt(columnValue.getText());
    	    }
    	    return null;
    	});

    	Optional<Pair<Integer, Integer>> result = dialog.showAndWait();

    	result.ifPresent(usernamePassword -> {
    	    System.out.println("Username=" + usernamePassword.getKey() + ", Password=" + usernamePassword.getValue());
    	});
    }

    @FXML
    private void openOptions() {
    	Dialog<Pair<Integer, Integer>> dialog = new Dialog<>();
    	dialog.setTitle("New Game");
    	dialog.setHeaderText("Start a new game");
    	ButtonType OKButtonType = new ButtonType("OK", ButtonData.OK_DONE);
    	dialog.getDialogPane().getButtonTypes().addAll(OKButtonType, ButtonType.CANCEL);

    	GridPane grid = new GridPane();
    	grid.setHgap(10);
    	grid.setVgap(10);
    	grid.setPadding(new Insets(20, 150, 10, 10));
    	TextField rowValue = new TextField();
    	rowValue.setPromptText("Rows");
    	TextField columnValue = new TextField();
    	columnValue.setPromptText("Columns");

    	grid.add(new Label("Rows:"), 0, 0);
    	grid.add(rowValue, 1, 0);
    	grid.add(new Label("Columns:"), 0, 1);
    	grid.add(columnValue, 1, 1);

    	Node OKButton = dialog.getDialogPane().lookupButton(OKButtonType);
    	OKButton.setDisable(true);

    	rowValue.textProperty().addListener((observable, oldValue, newValue) -> {
    	    OKButton.setDisable(newValue.trim().isEmpty());
    	});

    	dialog.getDialogPane().setContent(grid);
    	Platform.runLater(() -> rowValue.requestFocus());

    	// Convert the result to a username-password-pair when the login button is clicked.
    	dialog.setResultConverter(dialogButton -> {
    	    if (dialogButton == OKButtonType) {
    	        rows = Integer.parseInt(rowValue.getText());
    	        columns = Integer.parseInt(columnValue.getText());
    	    }
    	    return null;
    	});

    	Optional<Pair<Integer, Integer>> result = dialog.showAndWait();

    	result.ifPresent(usernamePassword -> {
    	    System.out.println("Username=" + usernamePassword.getKey() + ", Password=" + usernamePassword.getValue());
    	});
    }

    /**
     * Static function with the purpose of "throwing" dialog boxes
     * @param title The title of the dialog box
     * @param headerText The header text of the dialog box
     * @param contentText The content text of the dialog box
     */
    public static void infoBox(String title, String headerText, String contentText) {
    	Alert alert = new Alert(AlertType.WARNING);
    	alert.setTitle(title);
    	alert.setHeaderText(headerText);
    	alert.setContentText(contentText);

    	alert.showAndWait();
    }
}