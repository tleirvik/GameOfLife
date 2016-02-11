package GameOfLife;

import java.util.Random;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 *Denne klassen lytter på hendelser i .fxml
 *
 */
public class ViewController {
    
    @FXML private Button stopButton;

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
    
    private final GameController gController = new GameController();
    private GraphicsContext gc;
    
    //Hentet fra modellen
    private boolean[][] grid;
    
    private int rows = 10; //Bør hentes fra gController (?)
    private int columns = 10; //Bør hentes fra gController (?)
    
    //Slidere kan manipulere disse verdiene
    private int cellWidth = 50;
    private int cellHeight = 50;
    
    //Variabler trengt for å kalkulere differansen
    //mellom start og slutt for å finne ut hvor langt brukeren har dratt brettet
    private double offsetBegin_X = 0;
    private double offsetBegin_Y = 0;
    
    //Offset-verdi som blir enten positiv eller negativ avhengig av
    //hvordan brukeren flytter (canvas onDrag) grid-en
    private double offset_X = 0;
    private double offset_Y = 0;
    

    /*
     *		v LISTENERS v
     */

    @FXML
    public void newGame() {
    	//Lag pop-up-box der brukeren kan velge parametre til spillet
        gController.newGame(); //send parametrene videre
        gc = gameCanvas.getGraphicsContext2D();
    }

    @FXML
    public void play() {
    	
        gController.play();
        grid = gController.getBooleanGrid();
        
        gameCanvas.heightProperty().bind(canvasParent.heightProperty());
        gameCanvas.widthProperty().bind(canvasParent.widthProperty());
        
        draw();
        
        
        gameCanvas.addEventHandler(MouseEvent.MOUSE_PRESSED, 
        		new EventHandler<MouseEvent>() {
		       @Override
		       public void handle(MouseEvent e) {
		    	   //Med +offset_X så husker den posisjonen til grid-en og jobber ut ifra den,
		    	   //istedenfor å resette grid-en til midten av skjermen hver gang du trykker på skjermen
		    	   //for å dra den.
		           offsetBegin_X = e.getX() + offset_X;
		           offsetBegin_Y = e.getY() + offset_Y;
		       }
		   });
        
		gameCanvas.addEventHandler(MouseEvent.MOUSE_DRAGGED,
		   new EventHandler<MouseEvent>() {
		       @Override
		       public void handle(MouseEvent e) {
		           offset_X = offsetBegin_X - e.getX();
		           offset_Y = offsetBegin_Y - e.getY();
		           draw();
		       }
		   });
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
    
    /*
     *		^ LISTENERS ^
     */
    
    
    /**
     * DRAW-METODER BURDE VÆRE STATIC (?) SÅ DE KAN KALLES FRA GAMECONTROLLER
     */
    public void draw() {
        gc.clearRect(0, 0, gameCanvas.widthProperty().intValue(),
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
                	gc.setFill(Color.WHITE);
                	gc.fillRect( /* xPos */ x, /* yPos */ y, /* Bredde */ cellWidth, /* Høyde */ cellHeight);
                	
                	x+=cellWidth; //Plusser på for neste kolonne
                	
                } else { //Gjør det samme, bare i svart hvis den er død
                	gc.setFill(Color.RED);
                	gc.fillRect( /* xPos */ x, /* yPos */ y, /* Bredde */ cellWidth, /* Høyde */ cellHeight);
                	
                	x+=cellWidth; //Plusser på for neste kolonne
                }
    		}
    		x=start_X - offset_X; //Reset X-verdien for neste rad
    		y+=cellHeight; //Plusser på for neste rad
    	}
        
        gc.setStroke(Color.RED);
        gc.setLineWidth(2.0);
        drawGridLines();
    }

    /**
     *
     * @param gc
     */
    public void drawGridLines() {
        
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
