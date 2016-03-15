package FileManagement;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import GameOfLife.MetaData;
import GameOfLife.PatternFormatException;
import GameOfLife.ViewController;
import java.util.List;
import util.DialogBoxes;

/**
 * 
 * @author Stian Reistad Røgeberg, Robin Sean Aron David Lundh, Terje Leirvik.
 */
public class RLEDecoder {
    private MetaData metadata;
    private boolean[][] board;
    private List<String> RLEdata;


    /**
     * Constructs a RLEDecoder with file as input
     *
     * @param RLEdata
     */
    public RLEDecoder(List<String> RLEdata) {
        this.RLEdata = RLEdata;
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
     * @throws PatternFormatException Throws an exception if the method is unable to
     * parse the RLE file
     */
    public boolean decode() {
        getMetaData();

        try {
            getBoardSize();
        } catch (PatternFormatException pfE) {
            DialogBoxes.infoBox("Error!", "The file is not in a compatible format", "The following error occurred trying to interpret board size: " + pfE.getMessage());
            return false;
        }

        setBoardFalse();

        try {
            getGameRuleString();
        } catch (PatternFormatException pfE) {
            DialogBoxes.infoBox("Error!", "The file is not in a compatible format", "The following error occurred trying to interpret game rules: " + pfE.getMessage());
            return false;
        }

        try {
            parseBoard();
        } catch (PatternFormatException pfE) {
            DialogBoxes.infoBox("Error!", "The file is not in a compatible format", "The following error occurred trying to interpret board content " + pfE.getMessage());
            return false;
        }

        return true;
    }

    /**
     * Reads the metadata from the previously loaded contents and stores it in a new MetaData-object.
     * If the metadata is incorrectly formatted then it will not be stored.
     *
     * This method is not meant to be called directly, but rather through the decode() method in
     * a RLEDecoder-object.
     *
     * @throws PatternFormatException
     * @see MetaData.java
     */
    private void getMetaData() {
        metadata = new MetaData();
        StringBuilder name = new StringBuilder();
        StringBuilder author = new StringBuilder();
        StringBuilder comment = new StringBuilder();

        for (int i = 0; i < RLEdata.size(); i++) {
            String line = RLEdata.get(i);

            if (line.contains("#N")) {
                    String tempString = line.replaceAll("(#N )", "");
                    name.append(tempString);
            } else if (line.contains("#C")) {
                    String tempString = line.replaceAll("(#C )", "");
                    comment.append(tempString);
                    comment.append("\n");
            } else if (line.contains("#c")) {
                    String tempString = line.replaceAll("(#c )", "");
                    comment.append(tempString);
                    comment.append("\n");
            } else if (line.contains("#O")) {
                    String tempString = line.replaceAll("(#O )", "");
                    author.append(tempString);
            }
        }
        metadata.setName(name.toString());
        metadata.setAuthor(author.toString());
        metadata.setComment(comment.toString());

        // This for-loop needs to decrement i to account for the
        // changes in RLEdata when removing an element (subtracts
        // one from the indices).
        for(int i = 0; i < RLEdata.size(); i++) {
            if(RLEdata.get(i).startsWith("#")) {
                RLEdata.remove(i);
                i--;
            }
        }
    }

    /**
     * Reads the board's X and Y size from the previously loaded contents and creates a two-dimensional boolean grid
     *
     * This method is not meant to be called directly, but rather through the decode() method in
     * a RLEDecoder-object.
     *
     * @throws PatternFormatException
     * @return void
     *
     */
    private void getBoardSize() throws PatternFormatException {
        Pattern RLEpatternY = Pattern.compile("([yY][\\s][=][\\s])([\\d]+)");
        Pattern RLEpatternX = Pattern.compile("([xX][\\s][=][\\s])([\\d]+)");

        boolean foundRows = false;
        boolean foundColumns = false;

        int rows = 0;
        int columns = 0;

        for (int i = 0; i < RLEdata.size(); i++) {
            String line = RLEdata.get(i);
            Matcher RLEmatcherY = RLEpatternY.matcher(line);
            Matcher RLEmatcherX = RLEpatternX.matcher(line);

            if(RLEmatcherY.find()) {
                    rows = Integer.parseInt(RLEmatcherY.group(2));
                    foundRows = true;
            }

            if(RLEmatcherX.find()) {
                    columns = Integer.parseInt(RLEmatcherX.group(2));
                    foundColumns = true;
            }

            if(foundRows && foundColumns) {
                board = new boolean[rows][columns];
                return;
            }
        }

        if(!foundRows && !foundColumns) {
            throw new PatternFormatException("X and Y values could not be parsed from RLE-file");
        } else if(!foundRows) {
            throw new PatternFormatException("Y values could not be parsed from RLE-file");
        } else if(!foundColumns) {
            throw new PatternFormatException("X values could not be parsed from RLE-file");
        }
    }

    /**
     * Reads the game's rulestring from the previously loaded contents and stores it in a MetaData-object.
     *
     * This method is not meant to be called directly, but rather through the decode() method in
     * a RLEDecoder-object.
     *
     * @throws PatternFormatException
     * @return void
     */
    private void getGameRuleString() throws PatternFormatException {
        Pattern RLEpatternRules =
                Pattern.compile("rule[\\s]=[\\s][bB]([\\d]+)/[sS]([\\d]+)");
        String[] SBrules = new String[2];
        boolean foundRules = false;

        for (int i = 0; i < RLEdata.size(); i++) {
            String line = RLEdata.get(i);
            Matcher RLEmatcherRules = RLEpatternRules.matcher(line);

            if (RLEmatcherRules.find()) {
                SBrules[0] = RLEmatcherRules.group(1);
                SBrules[1] = RLEmatcherRules.group(2);
                foundRules = true;
                RLEdata.remove(i);
                metadata.setRuleString(SBrules);
                return;
            }
        }

        RLEpatternRules =
                Pattern.compile("rule[\\s]=[\\s]([\\d]+)/([\\d]+)");
        for(int i = 0; i < RLEdata.size(); i++) {
            String line = RLEdata.get(i);
            Matcher RLEmatcherRules = RLEpatternRules.matcher(line);

            if(RLEmatcherRules.find()) {
                SBrules[0] = RLEmatcherRules.group(1);
                SBrules[1] = RLEmatcherRules.group(2);
                foundRules = true;
                RLEdata.remove(i);
                metadata.setRuleString(SBrules);
                return;
            }
        }
        
        RLEpatternRules =
                Pattern.compile("rule[\\s]=[\\s][sS]([\\d]+)/[bB]([\\d]+)");
        for (int i = 0; i < RLEdata.size(); i++) {
            String line = RLEdata.get(i);
            Matcher RLEmatcherRules = RLEpatternRules.matcher(line);

            if (RLEmatcherRules.find()) {
                SBrules[0] = RLEmatcherRules.group(2);
                SBrules[1] = RLEmatcherRules.group(1);
                foundRules = true;
                RLEdata.remove(i);
                metadata.setRuleString(SBrules);
                return;
            }
        }
        
        if(!foundRules) {
            throw new PatternFormatException("Game rules could not be parsed from RLE-file");
        }
    }


    /**
     * This method parses the board cells from the previously
     * loaded contents.
     *
     * This method is not meant to be called directly, but rather through the decode() method in
     * a RLEDecoder-object.
     *
     * @throws PatternFormatException
     * @return void
     */
    public void parseBoard() throws PatternFormatException{
        // Pattern RLEpattern = Pattern.compile("([0-9]+(?=[bBoO]))|([bBoO])");
    	Pattern RLEpattern = Pattern.compile("([0-9]+[bBoO])|([bBoO])");
        String tempString = "";

        for (String s : RLEdata) {
        	tempString += s;
        }

        String[] tempRLEArray = tempString.split("\\$");

        for(int row = 0; row < tempRLEArray.length; row++) {
            Matcher RLEMatcher = RLEpattern.matcher(tempRLEArray[row]);
            int column = 0;

            while (RLEMatcher.find()) {
            	if (RLEMatcher.group(2) != null) {
                	if (RLEMatcher.group(2).matches("[oO]")) {
                		board[row][column] = true;
    	        	}
                	column++;
                } else if (RLEMatcher.group(1) != null) {
                	Pattern p = Pattern.compile("([0-9]+)([oObB])");
                	Matcher m = p.matcher(RLEMatcher.group(1));

                	m.find();
                	int number = Integer.parseInt(m.group(1));

                	while (number-- != 0) {
		            	if(m.group(2).matches("[oO]")) {
		            		board[row][column] = true;
		            	}
		            	column++;
		            }
                }
            } // End of first while-loop
        } // End of for-loop
    } // End of method parseBoard()

    /**
     * Method that returns the board contained in this class
     * @return board Returns the boolean[][] board
     * contained in this class
     */
    public boolean[][] getBoard() {
            return this.board;
    }

    public MetaData getMetadata() {
        return this.metadata;
    }

    /**
     * Method that iterates the boolean[][] board and
     * sets every position to false. This solves the "problem"
     * that in the RLE format dead cells doesn't necessairly
     * have to be specified.
     * @return void
     */
    void setBoardFalse() {
        for (boolean[] board1 : board) {
            for (int col = 0; col < board[0].length; col++) {
                board1[col] = false;
            }
        }
    }
}
