package GameOfLife;

import javafx.animation.Animation;
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
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 *Denne klassen lytter på hendelser i .fxml
 *
 */
public class ViewController {

	@FXML private Button stopButton;

	@FXML private Button playButton;

    @FXML private Button clearBoard;

    @FXML private ColorPicker gridColor;

    @FXML private ColorPicker cellColor;

    @FXML private Slider cellWidthSlider;

    @FXML private Label cellWidthLabel;

    @FXML private Slider cellHeightSlider;

    @FXML private Label cellHeightLabel;

    @FXML private CheckBox toggleGrid1;

    @FXML private CheckBox toggleGrid;

    @FXML private VBox canvasParent;

    @FXML private Canvas gameCanvas;

    @FXML private ToggleButton toggleDrawMove;

    @FXML private Text textNextGeneration;

    private Timeline timeline;

    private final GameController gController = new GameController();
    private GraphicsContext gc;

    //Hentet fra modellen
    private boolean[][] grid;

    private int rows = 50; //Bør hentes fra gController (?)
    private int columns = 50; //Bør hentes fra gController (?)

    //Slidere kan manipulere disse verdiene
    private int cellWidth = 15;
    private int cellHeight = 15;

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
    private double offset_X = 0;
    private double offset_Y = 0;

    private boolean drawGrid				= true;
    private boolean drawMoveMode 			= true; //False for move, true for draw
    private boolean listenersInitialized 	= false; //Enkel sjekk så initiateListeners ikke kjører mer enn en gang
    private boolean holdingPattern			= false;

    /*
     *		v LISTENERS v
     */

    //Metode for
    public void initiateListeners() {

    	listenersInitialized = true;

    	/*
    	 * 		SLIDERS
    	 */

    	cellHeightSlider.setMin(1);
    	cellHeightSlider.setMax(1000);

    	cellWidthSlider.setMin(1);
    	cellWidthSlider.setMax(1000);



    	cellHeightSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {

            	cellHeightLabel.setText(Integer.toString(new_val.intValue()));
            	cellHeight=new_val.intValue();
                draw();
            }
        });

    	cellWidthSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {

            	cellWidthLabel.setText(Integer.toString(new_val.intValue()));
            	cellWidth=new_val.intValue();
                draw();
            }
        });



    	/*
    	 * 		CANVAS
    	 */

		gameCanvas.addEventHandler(MouseEvent.MOUSE_PRESSED,
				new EventHandler<MouseEvent>() {

			   @Override
			   public void handle(MouseEvent e) {

				   if(drawMoveMode) {//TEGNEMODUS
					   double bClick_X = e.getX();
					   double bClick_Y = e.getY();

				   //Sjekk om brukeren trykket utenfor grid-en ved å
				   //plusse center-verdien av grid-en med halvparten av dens størrelse
				   //både i bredden og i høyden
				   if(	bClick_X >= ( ( gameCanvas.getWidth() / 2 ) - offset_X) - ( ( cellWidth * columns ) / 2 ) &&
						bClick_X <= ( ( gameCanvas.getWidth() / 2 ) - offset_X) + ( ( cellWidth * columns ) / 2 ))
				   {
					   System.out.println("Innenfor kolonnene");//Debug



					   if(	bClick_Y >= ( ( gameCanvas.getHeight() / 2 ) - offset_Y) - ( ( cellHeight * rows ) / 2 ) &&
							bClick_Y <= ( ( gameCanvas.getHeight() / 2 ) - offset_Y) + ( ( cellHeight * rows ) / 2 ))
					   {
						   System.out.println("Innenfor radene");//Debug

						   int row = (int) (bClick_Y - ( ( ( gameCanvas.getHeight() / 2 ) - offset_Y ) - ( ( cellHeight * rows ) / 2 ) ) ) / cellHeight;
						   int column = (int) (bClick_X - ( ( ( gameCanvas.getWidth() / 2 ) - offset_X ) - ( ( cellWidth * rows ) / 2 ) ) ) / cellWidth;

						   if (holdingPattern) {
							   	// drawObject(row, column, pattern)

							   	  boolean[][] testArray = new boolean[][]{
							   		  {false, true, false},
							   		  {false, false, true},
							   		  {true, true, true}
							   	  };

						        int mid = (0 + testArray.length - 1) / 2;

							   	for(int i = 0; i < testArray.length; i++) {
						    		for(int j = 0; j < testArray[i].length; j++) {
						    			gController.setCellAliveStatus(row + i -mid, column + j -mid, testArray[i][j]);

						    		}

							   	}


								  // holdingPattern = false;
						   } else {
							   gController.setCellAliveStatus(row, column, !gController.getCellAliveStatus(row, column));
						   }
						   grid = gController.getBooleanGrid();
						   draw();
						   //Finn rad og kolonne til celle, getCell() og setIsAlive() til !getIsAlive();
						   //Cellekolonnen tror jeg må være lik hvor mange ganger man kan dele bClick_X i cellWidth (i heltall) og
						   //celleraden tror jeg må være lik hvor mange ganger man kan dele bClick_Y i cellHeight (i heltall)
						   //RETTELSE: Tror ikke man bør hente celle for å opprettholde en viss form for immutability (eller noe?)
						   //Heller lage en slags "gController.setCellAliveStatus(!gController.
					   }

				   }

			   int row		= 0;
			   int column	= 0;

		   } else {//FLYTTEFUNKSJON

			   			//Med +offset_X så husker den posisjonen til grid-en og jobber ut ifra den,
			   			//istedenfor å resette grid-en til midten av skjermen hver gang du trykker på skjermen
			   			//for å dra den.
			          	offsetBegin_X = e.getX() + offset_X;
			          	offsetBegin_Y = e.getY() + offset_Y;
		    	   }
		       }
		   });

		gameCanvas.addEventHandler(MouseEvent.MOUSE_DRAGGED,
			new EventHandler<MouseEvent>() {

		       @Override
		       public void handle(MouseEvent e) {
		    	   if(drawMoveMode) {

		    	   } else {//FLYTTEFUNKSJON
			           offset_X = offsetBegin_X - e.getX();
			           offset_Y = offsetBegin_Y - e.getY();
			           draw();
		    	   }
		       }
		   });
    }

    @FXML
    public void newGame() {
    	//Lag pop-up-box der brukeren kan velge parametre til spillet
        gController.newGame(false, rows, columns); //send parametrene videre
        gc = gameCanvas.getGraphicsContext2D();

        cellWidth  = gameCanvas.widthProperty().intValue() / columns;
        cellHeight = gameCanvas.heightProperty().intValue() / rows;


        if(!listenersInitialized) initiateListeners();


    }

    @FXML
    public void options() {

    	holdingPattern = !holdingPattern;

      }

    @FXML
    public void play() {

    		gController.play();

        	timeline = new Timeline();
            timeline.setCycleCount(Animation.INDEFINITE);
            Duration duration = Duration.millis(130);
            KeyFrame keyFrame = new KeyFrame(duration, (ActionEvent e) -> {
                   gController.play();
                   grid = gController.getBooleanGrid();
                   draw();
                });
            timeline.getKeyFrames().add(keyFrame);
            timeline.play();


            grid = gController.getBooleanGrid();

            /*canvasParent.prefWidth(Double.MAX_VALUE);
            canvasParent.prefHeight(Double.MAX_VALUE);
            gameCanvas.heightProperty().bind(canvasParent.heightProperty());
            gameCanvas.widthProperty().bind(canvasParent.widthProperty());*/


            draw();

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
    	drawMoveMode = !drawMoveMode;

    	if(drawMoveMode) {
    		toggleDrawMove.setText("Draw Mode");
    	} else {
    		toggleDrawMove.setText("Move Mode");
    	}


    }

    /*
     *		^ LISTENERS ^
     */


    /**
     * DRAW-METODER BURDE VÆRE STATIC (?) SÅ DE KAN KALLES FRA GAMECONTROLLER
     */
    public void draw() {
        gc.clearRect(0, 0, gameCanvas.widthProperty().intValue(),
        gameCanvas.heightProperty().intValue());

        gc.setFill(stdBgColor);
        gc.fillRect(0, 0, gameCanvas.widthProperty().intValue(),
                gameCanvas.heightProperty().intValue());

        //Midtpunkter av Canvas
        double center_X = ( gameCanvas.getWidth()  / 2 );
        double center_Y = ( gameCanvas.getHeight() / 2 );

        //Finner hvor startposisjonen ligger øverst i venstre hjørnet av Canvas.
        //Legger sammen bredden til alle kolonnene og deler på 2 for å finne midtpunktet,
        //for deretter å trekke produktet fra senterpunktsverdien. Finner altså startposisjonen:
        double start_X = center_X - ( ( cellWidth  * columns ) / 2);
        double start_Y = center_Y - ( ( cellHeight * rows    ) / 2); //Samme her, bare høyden og rader istedenfor


        //Variablene brukt til å tegne firkanten. Plusses med bredden/høyden etter hver iterasjon i for-løkken
        //offset-verdien bestemmer hvor grid-en skal tegnes avhengig av om brukeren har flyttet den ved å dra den (onDrag-funksjon)
        double x = start_X - offset_X;
        double y = start_Y - offset_Y;

        for(int row = 0; row < grid.length; row++) {
    		for(int col = 0; col < grid[row].length; col++) {
                if(grid[row][col]) { //Hvis cellen lever, tegn den hvit
                	gc.setFill(stdAliveCellColor);
                	gc.fillRect( /* xPos */ x, /* yPos */ y, /* Bredde */ cellWidth, /* Høyde */ cellHeight);

                	x+=cellWidth; //Plusser på for neste kolonne

                } else { //Gjør det samme, bare i svart hvis den er død
                	gc.setFill(stdDeadCellColor);
                	gc.fillRect( /* xPos */ x, /* yPos */ y, /* Bredde */ cellWidth, /* Høyde */ cellHeight);

                	x+=cellWidth; //Plusser på for neste kolonne
                }
    		}
    		x=start_X - offset_X; //Reset X-verdien for neste rad
    		y+=cellHeight; //Plusser på for neste rad
    	}

        //Bruker kan bestemme om grid skal tegnes
        if(drawGrid) drawGridLines();
    }

    /**
     *
     * @param gc
     */
    public void drawGridLines() {

    	gc.setLineWidth(stdGridLineWidth);
    	gc.setStroke(stdGridColor);

        //Midtpunkter av Canvas
        double center_X = ( gameCanvas.getWidth()  / 2 );
        double center_Y = ( gameCanvas.getHeight() / 2 );

        //Finner hvor startposisjonen ligger øverst i venstre hjørnet av Canvas.
        double start_X = center_X - ( ( cellWidth  * columns ) / 2) - offset_X;
        double start_Y = center_Y - ( ( cellHeight * rows    ) / 2) - offset_Y;

        //Slutten av streken vil være halvparten av grid-en * 2 (altså hele grid-en)
        double end_X = center_X + ( ( cellWidth  * columns ) / 2) - offset_X;
        double end_Y = center_Y + ( ( cellHeight * rows    ) / 2) - offset_Y;

        System.out.println("start_X = " + start_X);
        System.out.println("start_Y = " + start_Y);
        System.out.println("end_X = " + end_X);
        System.out.println("end_Y = " + end_Y);

        //Tegn de første strekene (Hack siden det ikke funker i for-løkken)
        gc.strokeLine(start_X, start_Y, end_X, start_Y);//Horisontal
        gc.strokeLine(start_X, start_Y, start_X, end_Y);//Vertikal

    	//For hver rad, tegn en horisontal strek
        for(int y = 1; y <= rows; y++) {

        	gc.strokeLine(start_X, start_Y + (cellHeight * y), end_X, start_Y + (cellHeight * y));
        }

      //For hver kolonne, tegn en vertikal strek
        for(int x = 1; x <= columns; x++) {

        	gc.strokeLine(start_X + (cellWidth * x), start_Y, start_X + (cellWidth * x), end_Y);

        }
    }
}