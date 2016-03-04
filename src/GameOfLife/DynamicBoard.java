package GameOfLife;

public class DynamicBoard extends Board{

	private byte[][] currentCells;
	
	public DynamicBoard(int rows, int columns) {
		super(rows, columns);
		
		currentCells = new byte[rows][columns];
		
		for(int row = 0; row < currentCells.length; row++) {
    		for(int col = 0; col < currentCells[row].length; col++) {
    			currentCells[row][col] = 0;
    		}
    	}
	}
					
	public void setNmbrOfRows(int rows) {
		
		//Array som lagrer informasjonen før radlengden økes eller minkes
		byte[][] copyCells = new byte[currentCells.length][currentCells[0].length];
		for(int row = 0; row < currentCells.length; row++) {
    		for(int col = 0; col < currentCells[row].length; col++) {
    			copyCells[row][col] = currentCells[row][col];
    		}
    	}
		
		
		//Finn den laveste arrayradlengden for å unngå array out of bounds i for-loopen
		int rowLength = rows;
		if(rowLength > copyCells.length) rowLength = copyCells.length;
		
		//Lag et nytt array med den nye radlengden
		currentCells = new byte[rows][currentCells[0].length];
		
		//Kopier tilbake gammel data
		//Om arrayet minker og noen celler havner utenfor så blir de borte
		for(int row = 0; row < rowLength; row++) {
    		for(int col = 0; col < copyCells[row].length; col++) {
    			currentCells[row][col] = copyCells[row][col];
    		}
    	}
	}
	
	public void setNmbrOfColumns(int columns) {
		
		//Array som lagrer informasjonen før kolonnelengden økes eller minkes
		byte[][] copyCells = new byte[currentCells.length][currentCells[0].length];
		for(int row = 0; row < currentCells.length; row++) {
    		for(int col = 0; col < currentCells[row].length; col++) {
    			copyCells[row][col] = currentCells[row][col];
    		}
    	}
		
		
		//Finn den laveste arraykolonnelengden for å unngå array out of bounds i for-loopen
		int columnLength = columns;
		if(columnLength > copyCells.length) columnLength = copyCells[0].length;
		
		//Lag et nytt array med den nye radlengden
		currentCells = new byte[columns][currentCells[0].length];
		
		//Kopier tilbake gammel data
		//Om arrayet minker og noen celler havner utenfor så blir de borte
		for(int row = 0; row < copyCells.length; row++) {
    		for(int col = 0; col < columnLength; col++) {
    			currentCells[row][col] = copyCells[row][col];
    		}
    	}
	}
	
	public int getRows() {
		return this.currentCells.length;
	}

	public int getColumns() {
		return this.currentCells[0].length;
	}

	@Override
	public byte[][] getCellArray() {
		byte[][] copy = new byte[currentCells.length][currentCells[0].length];

    	for(int row = 0; row < currentCells.length; row++) {
    		for(int col = 0; col < currentCells[row].length; col++) {

                copy[row][col] = currentCells[row][col];

    		}
    	}
    	return copy;
	}

	@Override
	public void setCellArray(boolean[][] inputBoard) {
		for(int row = 0; row < inputBoard.length; row++) {
    		for(int col = 0; col < inputBoard[row].length; col++) {

    			if(inputBoard[row][col])
    				currentCells[row][col] = 1;
    			else
    				currentCells[row][col] = 0;
    		}
    	}
	}

	@Override
	public boolean getCellAliveState(int row, int column) {
		byte cell = currentCells[row][column];

    	if (cell == 1)
    		return true;
    	else
    		return false;
	}

	@Override
	public void setCellAliveState(int row, int column, boolean isAlive) {
		if (isAlive)
    		currentCells[row][column] = 1;
    	else
    		currentCells[row][column] = 0;
		
	}

}
