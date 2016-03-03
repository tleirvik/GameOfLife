package FileManagement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import GameOfLife.MetaData;
import GameOfLife.PatternFormatException;
import GameOfLife.ViewController;

public class RLEDecoder {

	private File file;

	private MetaData metadata;
	private boolean[][] board;
	private ArrayList<String> RLEString = new ArrayList<String>();


	/**
	 * Constructs a RLEDecoder with file as input
	 *
	 * @param  file
	 *         File to be read and interpreted
	 * @throws IOException
	 *         Throws an IOException if file cannot be read, found or other
	 * 		   IO related exception
	 */
	public RLEDecoder(File file) {

		this.file = file;
	}

	/**
	 * "Samlemetode" that controls the flow of the RLE file parsing
	 * This method calls other methods in this class with the purpose of
	 * doing the following:
	 *
	 * 1. Open a file via FileChooser and read the file to a String
	 * 2. Read the x and y value from the String and create a board with those
	 * values.
	 * 3. Read the file meta data(creator, name etc.) and do something
	 * with it(Too be implemented)
	 * 4. Interpret the RLE String char by char and convert to a board
	 * consisting of dead and alive cells(We create an empty board with
	 * dead cells and iterate the board and setting alive cells to
	 * true according to the RLE file read)
	 *
	 *
	 * @return boolean Returns true if the method parses the board with causing an
	 * Exception
	 * @throws IOException Throws IOException if file cannot be read, found or other
	 * IO related exception
	 * @throws PatternFormatException Throws an exception if the method is unable to
	 * parse the RLE file
	 */
	public boolean decode() {

		try {
			readFile(file);
		} catch (FileNotFoundException fnfE) {
			ViewController.infoBox("Error!", "File was not found", fnfE.getMessage());
			return false;
		} catch (IOException ioE) {
			ViewController.infoBox("Error!", "An unknown Input/Output error occured", ioE.getMessage());
			return false;
		}

		try {
			getMetaData();
		} catch (PatternFormatException pfE) {
			ViewController.infoBox("Error!", "The file is not in a compatible format", "The following error occured trying to interpret metadata: " + pfE.getMessage());
			return false;
		}

		try {
			getBoardSize();
		} catch (PatternFormatException pfE) {
			ViewController.infoBox("Error!", "The file is not in a compatible format", "The following error occured trying to interpret board size: " + pfE.getMessage());
			return false;
		}

		try {
			parseBoard();
		} catch (PatternFormatException pfE) {
			ViewController.infoBox("Error!", "The file is not in a compatible format", "The following error occured trying to interpret board content " + pfE.getMessage());
			return false;
		}


		return true;
	}
	/**
	 * Method that reads a fiel and converts it to a String
	 *
	 * @param file
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private boolean readFile(File file) throws FileNotFoundException, IOException {

		StringBuilder sb = new StringBuilder();
		String line = null;

		BufferedReader reader = new BufferedReader(new FileReader(file.getAbsolutePath()));

		try {
			while ((line = reader.readLine() ) != null) {
				RLEString.add(line);
			}


			System.out.println(sb.toString());
			return true;

		} catch (FileNotFoundException io) {
			System.out.println(io.getMessage());
			ViewController.infoBox(io.getMessage(), io.getMessage(), io.getMessage());
		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				throw new IOException(e.getMessage());
			}
		}

		return false;
	}
	/**
	 *  too bee implemented
	 */
	private void getMetaData() throws PatternFormatException {

		metadata = new MetaData();
		StringBuilder name = new StringBuilder();
		StringBuilder comment = new StringBuilder();
		StringBuilder author = new StringBuilder();

		for (int i = 0; i < RLEString.size(); i++) {
			String line = RLEString.get(i);
			System.out.println(line);

			if (line.contains("#N")) {
				String tempString = line.replaceAll("[#N]", "");
				name.append(tempString);
				RLEString.remove(i);
			} else if (line.contains("#C")) {
				String tempString = line.replaceAll("[#C]", "");
				comment.append(tempString);
				comment.append("\n");
				RLEString.remove(i);
			} else if (line.contains("#c")) {
				String tempString = line.replaceAll("[#c]", "");
				comment.append(tempString);
				comment.append("\n");
				RLEString.remove(i);
			} else if (line.contains("#O")) {
				String tempString = line.replaceAll("[#O]", "");
				author.append(tempString);
				RLEString.remove(i);
			}
		}
/*
		metadata.setAuthor(author.toString());
		metadata.setComment(comment.toString());
		metadata.setName(name.toString());
		System.out.println(metadata.getAuthor());
		System.out.println(metadata.getName());
		System.out.println(metadata.getComment());
		System.out.println("METADATA END");

		for (String temp : RLEString) {
			System.out.println(temp);
		}
*/

		/*

		StringBuilder tempRLEString = new StringBuilder();

		Scanner scanner = new Scanner(RLEString);
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();


			if (line.contains("#N")) {
				String tempString = line.replaceAll("[#N]", "");
				name.append(tempString);
			} else if (line.contains("#C")) {
				String tempString = line.replaceAll("[#C]", "");
				comment.append(tempString);
				comment.append("\n");
			} else if (line.contains("#c")) {
				String tempString = line.replaceAll("[#c]", "");
				comment.append(tempString);
				comment.append("\n");
			} else if (line.contains("#O")) {
				String tempString = line.replaceAll("[#O]", "");
				author.append(tempString);
			} else {
				tempRLEString.append(line + "\n");
			}
		}
		scanner.close();
		metadata.setAuthor(author.toString());
		metadata.setComment(comment.toString());
		metadata.setName(name.toString());
		System.out.println(metadata.getAuthor());
		System.out.println(metadata.getName());
		System.out.println(metadata.getComment());
		System.out.println("METADATA END");

		RLEString = tempRLEString.toString();

		System.out.println(RLEString);
		*/

	}
	private boolean getBoardSize() throws PatternFormatException {
		boolean error = false;

	    Pattern RLEpatternX = Pattern.compile("[xX][\\s][=][\\s][\\d]+");
	    Matcher RLEMatcherX = null;
	    Matcher RLEMatcherY = null;


	    for (String temp : RLEString) {
	    	RLEMatcherX = RLEpatternX.matcher(temp);
	    	try {
		    	if(!RLEMatcherX.find()) {
		    		error = true;
		    		throw new PatternFormatException();
		    	}
		    } catch (PatternFormatException pfe) {
		    	System.out.println("catch pfe - BoardSize");
				// ViewController.infoBox("Feil ved innlesning av fil:" + pfe.getMessage(),"Feil ved innlesning av fil", "Feil ved innlesning av fil");
				throw new PatternFormatException(pfe);
		    } finally {
		    	System.out.println("finally - BoardSize");
		    }
		}

	    Pattern RLEpatternY = Pattern.compile("y = \\d+");

	    for (String temp : RLEString) {
	    	RLEMatcherY = RLEpatternY.matcher(temp);
	    try {
	    	if(!RLEMatcherY.find()) {
	    		error = true;
	    		throw new PatternFormatException();
	    	}
	    } catch (PatternFormatException pfe) {
	    	System.out.println("catch pfe - Y");
			ViewController.infoBox("Feil ved innlesning av fil:" + pfe.getMessage(),"Feil ved innlesning av fil", "Feil ved innlesning av fil");
	    } finally {
	    	System.out.println("finally - Y");
	    }

	    if (error) return false;

	    int column = Integer.parseInt(RLEMatcherX.group().replaceAll("[\\D]", ""));
	    int row = Integer.parseInt(RLEMatcherY.group().replaceAll("[\\D]", ""));

	    board = new boolean[row][column];


	    }
	    return true;
	}



	public void parseBoard() throws PatternFormatException{

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

	/**
	 * Method that returns the board contained in this class
	 * @return board Method that returns the boolean[][] board
	 * contained in this class
	 */
	public boolean[][] getBoard() {
		return this.board;
	}

	/**
	 * Method that iterates the boolean[][] board and
	 * sets every position to false. This solves the "problem"
	 * that in the RLE format dead cells doesn't necessairly
	 * have to be specified.
	 * @return void
	 */
	void setBoardFalse() {
		for(int row = 0; row < board.length; row++) {
    		for(int col = 0; col < board[0].length; col++) {
                board[row][col] = false;
    		}
		}
	}
}
