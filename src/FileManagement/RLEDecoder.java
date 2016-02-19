package FileManagement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RLEDecoder {

	private File file;
	private boolean[][] board;
	private String RLEString;

	public RLEDecoder(File file) throws IOException {

		this.file = file;
	}

	public boolean beginDecoding() throws IOException {

		if(!readFile(file)) {
			System.out.println("Filen ble ikke lest.");
			return false;
		}

		if(!getBoardSize()) {
			System.out.println("Kunne finne finne brettstørrelsen");
			return false;
		}

		getMetaData();

		// Setting the board to default false
		for(int row = 0; row < board.length; row++) {
    		for(int col = 0; col < board[0].length; col++) {
                board[row][col] = false;
    		}
		}

    	parseBoard();

		return true;
	}
	private boolean readFile(File file) throws FileNotFoundException {

		StringBuilder sb = new StringBuilder();
		String line = null;

		try (BufferedReader reader = new BufferedReader(new FileReader(file.getAbsolutePath()))) {

			while ((line = reader.readLine() ) != null) {
				sb.append(line);
			}
			reader.close();
			RLEString = sb.toString();
			return true;

		} catch (FileNotFoundException io) {
			System.out.println(io.getMessage());
		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
		}

		return false;
	}
	private boolean getBoardSize() {
		boolean error = false;

	    Pattern RLEpatternX = Pattern.compile("[xX][\\s][=][\\s][\\d]+");
	    Matcher RLEMatcherX = RLEpatternX.matcher(RLEString);

	    if(!RLEMatcherX.find()) error = true;

	    Pattern RLEpatternY = Pattern.compile("y = \\d+");
	    Matcher RLEMatcherY = RLEpatternY.matcher(RLEString);

	    if(!RLEMatcherY.find()) error = true;

	    if (error) return false;

	    int column = Integer.parseInt(RLEMatcherX.group().replaceAll("[\\D]", ""));
	    int row = Integer.parseInt(RLEMatcherY.group().replaceAll("[\\D]", ""));

	    board = new boolean[row][column];
	    return true;

	}

	private void getMetaData() {

	}

	public void parseBoard() {

		//Splitter arrayet på dollartegn for å markere linjeskift
		String[] tempRLEString = RLEString.split("[$]");
	    Pattern RLEpattern = Pattern.compile("([0-9]+(?=[bBoO]))|([bBoO])");

	    System.out.println(tempRLEString[0]);

	    //Iterer gjennom hver "linje"
	    for(int i = 0; i < tempRLEString.length; i++) {

    		System.out.println("rad: "+i);
	    	int columnPos = 0;

	    	Matcher RLEMatcher = RLEpattern.matcher(tempRLEString[i]);

	    	while (RLEMatcher.find()) {
	    		System.out.println("1: "+columnPos);


		        if (RLEMatcher.group().matches("[oObB]")) {

		        	//Setter bare levende celler, men teller også de døde
		        	if(RLEMatcher.group().matches("[oO]")) board[i][columnPos] = true;
		        	columnPos++;
		        	System.out.println("2: "+columnPos);

		        }  else {
		            int number = Integer.parseInt(RLEMatcher.group());
		            RLEMatcher.find();
		            while (number-- != 0) {
		            	if(RLEMatcher.group().matches("[oO]")) board[i][columnPos] = true;
		            	columnPos++;
		            	System.out.println("3: "+columnPos);
		            }
		        }
		    }
	    }

	    for(int row = 0; row < board.length; row++) {
	    	for(int col = 0; col < board.length; col++) {
		    	if(board[row][col]) System.out.print("O ");
		    	else System.out.print("X ");
		    }
	    	System.out.println("");
	    }

	}

	public boolean[][] getBoard() {
		return this.board;
	}

}
