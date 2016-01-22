package controller;

import com.sun.prism.paint.Color;

import game.StaticBoard;

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
    	
    	int columns = 50, rows = 50;
    	
    	board = new StaticBoard(columns,rows);
    	board.populateBoard();
    	
    	
    	gc = gameCanvas.getGraphicsContext2D();
    	
    	int xPos = 0, yPos = 0;
    	int width = 20, height = 20;
    	
    	for(int i = 0; i < columns; i++) {
			for(int j = 0; j < rows; j++) {
				if(board.boardGrid[i][j].isAlive == true) {
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
    	
    	/* Les antall rows & columns fra board,
		 * lag en grid av x * y antall rektangler
		 * og iterer igjennom den for � tegne om cellen lever eller ikke gj�r det.
		 */
    	
    }
    
    @FXML
    void stepGoL(ActionEvent event) {
    	
    	int xPos = 0, yPos = 0;
    	int width = 20, height = 20;
    	
    	board.nextGeneration();
    	for(int i = 0; i < 50; i++) {
			for(int j = 0; j < 50; j++) {
				if(board.boardGrid[i][j].isAlive == true) {
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
	}
}
