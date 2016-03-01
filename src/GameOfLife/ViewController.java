package GameOfLife;

import java.io.File;
import java.io.IOException;

import FileManagement.RLEDecoder;
import javafx.animation.Animation;
import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *Denne klassen lytter på hendelser i .fxml
 *
 */
public class ViewController {

	//================================================================================
    // JavaFX Fields
    //================================================================================
	
	@FXML private Button stopButton;

	@FXML private Button playButton;

    @FXML private Button clearBoard;

    @FXML private ColorPicker gridColor;

    @FXML private ColorPicker cellColor;

    @FXML private Slider cellWidthSlider;

    @FXML private Label cellWidthLabel;

    @FXML private Slider cellHeightSlider;

    @FXML private Label cellHeightLabel;
    
    @FXML private CheckBox bindSizeSliders;
    
    @FXML private CheckBox toggleGrid;

    @FXML private Pane canvasParent;

    @FXML private Canvas gameCanvas;

    @FXML private ToggleButton toggleDrawMove;

    @FXML private Text textNextGeneration;

    @FXML private MenuItem filePicker;

	//================================================================================
    // Property fields
    //================================================================================

    private Timeline timeline;

    private final GameController gController = new GameController();

    //Hentet fra modellen
    private boolean[][] grid;

    private int rows	= 21; //Bør hentes fra gController (?)
    private int columns = 21; //Bør hentes fra gController (?)

    //Slidere kan manipulere disse verdiene
    private double cellWidth	= 10;
    private double cellHeight	= 10;

    //Settes når et nytt spill lages
    //Er minimumsverdiene til hver celle basert på antall og størrelse på canvas i minste vindusstørrelse
    private double cellWidthMin;
	private double cellHeightMin;
    
    //Standard farger (Om ikke annet er spesifisert)
    private Color stdAliveCellColor	= Color.BLACK;
    private Color stdDeadCellColor	= Color.GHOSTWHITE;

    private Color stdBgColor	= Color.LIGHTGREY; //Rundt grid

    private Color stdGridColor	= Color.GREY;

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

    private boolean drawGrid				= true;
    private boolean drawBackground			= true;	 //Farger bakgrunnen rundt canvas, eller hele bakgrunnen om drawDeadCells er av
    private boolean drawDeadCells			= true; //Optimaliserer koden NOE SINNSYKT!
    
    private boolean listenersInitialized 	= false; //Enkel sjekk så initiateListeners ikke kjører mer enn en gang
    
    
    //
    //	CANVAS MOVE/DRAW FLAGS
    //
    private boolean holdingPattern			= false; //Find out if user is holding pattern
    private boolean drawMode	 			= false; //False for move, true for draw
    private boolean cellDraw				= false; //Inverter for å tegne andre fargen

	//================================================================================
    // Listeners
    //================================================================================

    public void initiateListeners() {

    	listenersInitialized = true;

    	cellHeightSlider.setMin(1);
    	cellHeightSlider.setMax(500);
    	cellHeightSlider.setValue(20);

    	cellWidthSlider.setMin(1);
    	cellWidthSlider.setMax(500);
    	cellWidthSlider.setValue(20);

    	cellHeightSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
            	cellHeightLabel.setText(Integer.toString(new_val.intValue()));
            	cellHeight=new_val.intValue();
            	offset_Y  += gameCanvas.getHeight()/2 - (cellHeight * rows /2);
                draw();
            }
        });

    	cellWidthSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
            	cellWidthLabel.setText(Integer.toString(new_val.intValue()));
            	
            	cellWidth = new_val.intValue();
            	offset_X  = gameCanvas.getWidth()/2 - (cellWidth * columns /2);
                draw();
            }
        });;

        bindSizeSliders.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) {
            	if(new_val == true) {
               		cellHeightSlider.valueProperty().bindBidirectional(cellWidthSlider.valueProperty());
            	} else {
            		cellHeightSlider.valueProperty().unbindBidirectional(cellWidthSlider.valueProperty());
            	}
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
		        double center_X = ( gameCanvas.getWidth()  / 2 );
		        double center_Y = ( gameCanvas.getHeight() / 2 );
		        
		        //Finner hvor startposisjonen ligger øverst i venstre hjørnet av Canvas.
		        double start_X = center_X - ( ( cellWidth  * columns ) / 2) - offset_X;
		        double start_Y = center_Y - ( ( cellHeight * rows    ) / 2) - offset_Y;
				
				
				//Variablene brukt til å tegne firkanten. Plusses med bredden/høyden etter hver iterasjon i for-løkken
				//offset-verdien bestemmer hvor grid-en skal tegnes avhengig av om brukeren har flyttet den ved å dra den (onDrag-funksjon)
		        
				//Finn ut om brukeren har musen over grid-et
				if(	scrollLocation_X >= ( ( gameCanvas.getWidth() / 2 ) - offset_X) - ( ( cellWidth * columns ) / 2 ) &&
					scrollLocation_X <= ( ( gameCanvas.getWidth() / 2 ) - offset_X) + ( ( cellWidth * columns ) / 2 ))
				{

					if(	scrollLocation_Y >= ( ( gameCanvas.getHeight() / 2 ) - offset_Y) - ( ( cellHeight * rows ) / 2 ) &&
						scrollLocation_Y <= ( ( gameCanvas.getHeight() / 2 ) - offset_Y) + ( ( cellHeight * rows ) / 2 ))
					{	
						
						//double mousePosXToStartX = scrollLocation_X - start_X;
						//double mousePosYToStartY = scrollLocation_Y - start_Y;
						
						System.out.println("Cellebredde før: "+cellWidth*columns);
						System.out.println("Cellehøyde før: "+cellHeight*rows);
						
						cellWidth  *= zoomFactor;
						cellHeight *= zoomFactor;
						
						System.out.println("Cellebredde etter: "+cellWidth*columns);
						System.out.println("Cellehøyde etter: "+cellHeight*rows);
						
						//double start_X2 = center_X - ( ( cellWidth  * columns ) / 2) - offset_X;
				        //double start_Y2 = center_Y - ( ( cellHeight * rows    ) / 2) - offset_Y;
						
						//double mousePosXToStartX2 = scrollLocation_X - start_X2;
						//double mousePosYToStartY2 = scrollLocation_Y - start_Y2;
						
				        //double start_XDIFF = start_X - start_X2;
				        //double start_YDIFF = start_Y - start_Y2;
				        
						//double mousePosXToStartXDIFF = mousePosXToStartX2 - mousePosXToStartX;
						//double mousePosYToStartYDIFF = mousePosYToStartY2 - mousePosYToStartY;
						
						System.out.println("Offsetverdier før: " + offset_X + " " + offset_Y);
						//System.out.println("MousePosDiffverdier: " + mousePosXToStartXDIFF + " " + mousePosYToStartYDIFF);
						
						//offset_X += start_XDIFF * -.5;
						//offset_Y += start_YDIFF * -.5;
						
						offset_X += (scrollLocation_X - center_X);
						offset_Y += (scrollLocation_Y - center_Y);
						
						System.out.println("Offsetverdier etter: " + offset_X + " " + offset_Y);
						System.out.println();
						draw();
					}
					
				} else {
					//Ikke zoom ut forbi minimumsverdien til cellene
					//cellWidth  *= zoomFactor;
					//cellHeight *= zoomFactor;
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
					   
					   int row    = (int) ( (bClick_Y - ( getGridStartPosY() - cellHeight * rows	 ) ) / cellHeight) - rows;
					   int column = (int) ( (bClick_X - ( getGridStartPosX() - cellWidth  * columns ) ) / cellWidth ) - columns;
					   
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
						
    					int row    = (int) ( (bClick_Y - ( getGridStartPosY() - cellHeight * rows	 ) ) / cellHeight) - rows;
 					   	int column = (int) ( (bClick_X - ( getGridStartPosX() - cellWidth  * columns ) ) / cellWidth ) - columns;
						
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
    public void newGame() {
    	
    	if(!listenersInitialized) initiateListeners();
    	
    	//Reset offset
		offset_X = gameCanvas.getWidth()/2 - (cellWidth * columns /2);
		offset_Y = gameCanvas.getHeight()/2 - (cellHeight * rows /2);
		
		//Turn off grid if size is under 8
		if(cellWidth < 8 || cellHeight < 8) drawGrid = false;
		
		//Lag pop-up-box der brukeren kan velge parametre til spillet
		timeline = new Timeline();
		timeline.setCycleCount(Animation.INDEFINITE);
		Duration duration = Duration.millis(100);
		KeyFrame keyFrame = new KeyFrame(duration, (ActionEvent e) -> {
			
			long startTime = System.nanoTime();
			gController.play();
			long endTime = System.nanoTime();
			long duration2 = (endTime - startTime) / 1000000;
			System.out.println("Next Generation: " +duration2);
		   
			startTime = System.nanoTime();
			grid = gController.getBooleanGrid();
			endTime = System.nanoTime();
			duration2 = (endTime - startTime) / 1000000;
			System.out.println("Get grid: " +duration2);
		
		startTime = System.nanoTime();
		draw();
		endTime = System.nanoTime();
		duration2 = (endTime - startTime) / 1000000;
		System.out.println("Draw: " +duration2);
		});
		
		
		
		timeline.getKeyFrames().add(keyFrame);
		
		gController.newGame(false, rows, columns); //send parametrene videre
		
		grid = gController.getBooleanGrid();
		draw();
		
		//De faste verdiene er bredden på canvas når vinduet er forminsket til den
		//minste tllatte verdien
		cellWidthMin  = 600 / columns;
		cellHeightMin = 490 / rows;
    }
    
    @FXML
    public void loadRLE() throws PatternFormatException {

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
    	    	System.out.println(selectedFile.exists());

    	    	try {
    				RLEDecoder rledec = new RLEDecoder(selectedFile);
    				rledec.beginDecoding();
    				gController.newGame(rledec.getBoard(), isDynamic);
    			} catch (IOException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			} catch (PatternFormatException e) {
    				// Testing
    				Alert alert = new Alert(AlertType.ERROR);
    				alert.setTitle("Feil!");
    				alert.setHeaderText("Feil ved lesning av fil");
    				alert.setContentText("Feil RLE blablabla");

    				alert.showAndWait();
    			}
    	    	grid = gController.getBooleanGrid();
    	    	draw();
    	 }
    }
    @FXML
    public void options() {

    	holdingPattern = !holdingPattern;
    	gameCanvas.translateZProperty().add(20);

      }

    @FXML
    public void play() {
    	
		if(timeline == null || timeline.getStatus() != Status.RUNNING) {	
            timeline.play();
		}
    }
    
    @FXML
    public void stop() {
    	timeline.stop();
    	if(timeline != null && timeline.getStatus() == Status.RUNNING) {
    		timeline.stop();
    	}
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
    
public boolean isXInsideGrid(double posX) {
    	
    	double gridStartPosX = getGridStartPosX();
    	
    	if  ( posX  >= gridStartPosX && posX <= gridStartPosX + cellWidth * columns )
    		return true;
    	else
    		return false;
    }
    
    public boolean isYInsideGrid(double posY) {
    	
    	double gridStartPosY = getGridStartPosY();
    	
    	if  ( posY  >= gridStartPosY && posY <= gridStartPosY + cellHeight * rows )
    		return true;
    	else
    		return false;
    }
    
    public void draw() {
    	//If the grid is not retrieved yet, do not run the draw function
    	if(grid == null) return;
    	
		GraphicsContext gc = gameCanvas.getGraphicsContext2D();
		
		gc.clearRect(0, 0, gameCanvas.widthProperty().intValue(),
		gameCanvas.heightProperty().intValue());
		
		if(drawBackground) {
			gc.setFill(stdBgColor);
			gc.fillRect(0, 0, gameCanvas.widthProperty().intValue(),gameCanvas.heightProperty().intValue());
		}
		
		double start_X = getGridStartPosX();
		double start_Y = getGridStartPosY();
		
		double x = start_X;
		double y = start_Y;
		
		for(int row = 0; row < grid.length; row++) {
			for(int col = 0; col < grid[row].length; col++) {
				
		        if(grid[row][col]) { //Hvis cellen lever, tegn den hvit
		        	gc.setFill(stdAliveCellColor);
		        	gc.fillRect( /* xPos */ x, /* yPos */ y, /* Bredde */ cellWidth, /* Høyde */ cellHeight);
		        	x+=cellWidth; //Plusser på for neste kolonne
		        } else { //Gjør det samme, bare i svart hvis den er død
		        	if(drawDeadCells) {
		        		gc.setFill(stdDeadCellColor);
		        		gc.fillRect( /* xPos */ x, /* yPos */ y, /* Bredde */ cellWidth, /* Høyde */ cellHeight);
		        	}
		        	x+=cellWidth; //Plusser på for neste kolonne
		        }
			}
			x=start_X; //Reset X-verdien for neste rad
			y+=cellHeight; //Plusser på for neste rad
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
        double end_X = start_X + ( cellWidth  * columns );
        double end_Y = start_Y + ( cellHeight * rows    );

        //Tegn de første strekene (Hack siden det ikke funker i for-løkken)
        //gc.strokeLine(start_X, start_Y, end_X, start_Y);//Horisontal
        //gc.strokeLine(start_X, start_Y, start_X, end_Y);//Vertikal

    	//For hver rad, tegn en horisontal strek
        for(int y = 0; y <= rows; y++) {
        	gc.strokeLine(start_X, start_Y + (cellHeight * y), end_X, start_Y + (cellHeight * y));
        }

      //For hver kolonne, tegn en vertikal strek
        for(int x = 0; x <= columns; x++) {
        	gc.strokeLine(start_X + (cellWidth * x), start_Y, start_X + (cellWidth * x), end_Y);
        }
        
        
        //EVENTUELLE DEBUG-TEGNINGER
        gc.setFill(stdAliveCellColor);
        gc.fillOval(gameCanvas.getWidth()/2-2, gameCanvas.getHeight()/2-2, 5, 5);
        
        gc.setFill(Color.RED);
        gc.fillOval(start_X + ( ( cellWidth  * columns ) / 2) -2, start_Y + ( ( cellHeight  * rows ) / 2) -2, 5, 5);

    }
}