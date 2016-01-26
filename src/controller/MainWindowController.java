package controller;

import com.sun.prism.paint.Color;

import game.StaticBoard;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public class MainWindowController {

	@FXML
    private Canvas gameCanvas;
	
	private GraphicsContext gc;
	
	private StaticBoard board;
	
    @FXML
    void startGoL(ActionEvent event) {
    	
    	int columns = 20, rows = 20;
    	
    	board = new StaticBoard(columns,rows);
    	board.populateBoard();
    	
    	
    	gc = gameCanvas.getGraphicsContext2D();
    	
    	int xPos = 0, yPos = 0;
    	int width = 10, height = 10;
    	
    	for(int i = 0; i < columns; i++) {
			for(int j = 0; j < rows; j++) {
				if(board.getBoardGrid()[i][j].getIsAlive() == true) {
					
					//Set fargen til gr�nn om cellen lever
					
					gc.setFill(Paint.valueOf("BLACK"));
					//Lag en firkant
					gc.fillRect(xPos, yPos, width, height);
					//�k x-verdien med bredden + en marg
					xPos+=width + 2;
					
					System.out.print("O ");
					
				} else {
					//Set fargen til r�d om cellen lever
					gc.setFill(Paint.valueOf("WHITE"));
					//Lag en firkant
					gc.fillRect(xPos, yPos, width, height);
					//�k x-verdien med bredden + en marg
					xPos+=width + 2;
					
					System.out.print("X ");
				}
			}
			//�k y-verdien med h�yden + en marg for � starte neste linje
			xPos=0;
			yPos+= height + 2;
			
			System.out.println("");
		}
    	System.out.println("");
    	
    	/* Les antall rows & columns fra board,
		 * lag en grid av x * y antall rektangler
		 * og iterer igjennom den for � tegne om cellen lever eller ikke gj�r det.
		 */
    	
    }
    
    Task<Void> task = new Task<Void>() {
	    @Override public Void call() throws InterruptedException {
	        final int max = 1000000;
	        for (int i = 1; i <= max; i++) {
	            stepGoL2();
	            Thread.sleep(250);
	        }
	        return null;
	    }
	};
    
    @FXML
    void stepGoL(ActionEvent event) {
    	
    	new Thread(task).start();
    	
	}
    
    void stepGoL2() {
			int xPos = 0, yPos = 0;
			int width = 10, height = 10;
			
			gc.clearRect(0, 0, 500, 500);
			
			board.nextGeneration();
			for(int i = 0; i < 20; i++) {
				for(int j = 0; j < 20; j++) {
					if(board.getBoardGrid()[i][j].getIsAlive() == true) {
						//Set fargen til gr�nn om cellen lever
						gc.setFill(Paint.valueOf("BLACK"));
						//Lag en firkant
						gc.fillRect(xPos, yPos, width, height);
						//�k x-verdien med bredden + en marg
						xPos+=width + 2;
						
						System.out.print("O ");
						
					} else {
						//Set fargen til r�d om cellen lever
						gc.setFill(Paint.valueOf("WHITE"));
						//Lag en firkant
						gc.fillRect(xPos, yPos, width, height);
						//�k x-verdien med bredden + en marg
						xPos+=width + 2;
						
						System.out.print("X ");
					}
				}
				//�k y-verdien med h�yden + en marg for � starte neste linje
				xPos=0;
				yPos+= height + 2;
				
				System.out.println("");
			}
			System.out.println("");
    }
}
