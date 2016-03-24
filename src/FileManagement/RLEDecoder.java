package FileManagement;

import java.io.BufferedReader;
import java.io.IOException;
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
    private byte[][] board;
    private BufferedReader reader;


    /**
     * Constructs a RLEDecoder with file as input
     *
     * @param
     */
    public RLEDecoder() {

    }

    public void parseBoard(BufferedReader reader)  throws IOException, PatternFormatException {
        String line = null;
        System.out.println("Hallo!");
        int row = 0, col = 0;
        final char lineBreak = '$';
        while ((line = reader.readLine()) != null) { //Bør være dedikert til lesing av brett
            int charNumber = 0; // Antall forekomster av en celle
            for (int i = 0; i < line.length(); i++) {
                if (Character.isDigit(line.charAt(i))) {
                    if (charNumber == 0) {
                        charNumber = Character.getNumericValue(line.charAt(i));
                    } else {
                        charNumber *= 10;
                        charNumber += Character.getNumericValue(line.charAt(i));
                    }
                } else if (line.charAt(i) == 'o' || line.charAt(i) == 'O') {
                    if (charNumber != 0) { //Iterer gjennom antall forekomster og sett celle til levende
                        while (charNumber != 0) {
                            board[row][col] = 1;
                            charNumber--;
                            col++;
                        }
                    } else {
                        board[row][col] = 1;
                        col++;
                    }
                } else if (line.charAt(i) == 'b' || line.charAt(i) == 'B') {
                    if (charNumber != 0) {
                        while (charNumber != 0) {
                            charNumber--;
                            col++;
                        }
                    } else {
                        col++;
                    }
                } else if (line.charAt(i) == lineBreak) {
                    row++;
                    col = 0;
                    charNumber = 0;
                }
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
    /*
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
                board = new byte[rows][columns];
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
*/
    /**
     * Reads the game's rulestring from the previously loaded contents and stores it in a MetaData-object.
     *
     * This method is not meant to be called directly, but rather through the decode() method in
     * a RLEDecoder-object.
     *
     * @throws PatternFormatException
     * @return void
     */
    /*
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

*/
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
    /*
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
                		board[row][column] = 1;
    	        	}
                	column++;
                } else if (RLEMatcher.group(1) != null) {
                	Pattern p = Pattern.compile("([0-9]+)([oObB])");
                	Matcher m = p.matcher(RLEMatcher.group(1));

                	m.find();
                	int number = Integer.parseInt(m.group(1));

                	while (number-- != 0) {
		            	if(m.group(2).matches("[oO]")) {
		            		board[row][column] = 1;
		            	}
		            	column++;
		            }
                }
            } // End of first while-loop
        } // End of for-loop
    } // End of method parseBoard()

*/
    /**
     * Method that returns the board contained in this class
     * @return board Returns the boolean[][] board
     * contained in this class
     */
    public byte[][] getBoard() {
            return this.board;
    }

    public MetaData getMetadata() {
        return this.metadata;
    }

    /**
     * Reads the metadata from the previously loaded contents and stores it in a new MetaData-object.
     * If the metadata is incorrectly formatted then it will not be stored.
     *
     * This method is not meant to be called directly, but rather through the decode() method in
     * a RLEDecoder-object.
     *
     * @throws PatternFormatException
     * @see MetaData
     */
    public void parseMetadata(BufferedReader reader) throws IOException {
        metadata = new MetaData();
        StringBuilder name = new StringBuilder();
        StringBuilder author = new StringBuilder();
        StringBuilder comment = new StringBuilder();
        Pattern RLEpatternY = Pattern.compile("([yY][\\s][=][\\s])([\\d]+)");
        Pattern RLEpatternX = Pattern.compile("([xX][\\s][=][\\s])([\\d]+)");

        String line = null;
        while ((line = reader.readLine()) != null) { // OG INNEHOLDER # ?
            if (line.charAt(0) == '#') {

                final char tempChar = line.charAt(1);
                switch (tempChar) {
                    case 'N':
                        String tempString = line.replaceAll("(#N )", "");
                        name.append(tempString);
                        metadata.setName(name.toString());
                        // System.out.println(metadata.getName());
                        System.out.println(name.toString());
                        break;
                    case 'C':
                        String tempString2 = line.replaceAll("(#C )", "");
                        comment.append(tempString2);
                        comment.append("\n");
                        metadata.setComment(comment.toString());
                        break;
                    case 'c':
                        String tempString3 = line.replaceAll("(#c )", "");
                        comment.append(tempString3);
                        comment.append("\n");
                        metadata.setComment(comment.toString());
                        break;
                    case 'O':
                        String tempString4 = line.replaceAll("(#O )", "");
                        author.append(tempString4);
                        metadata.setAuthor(author.toString());

                }

            } else {
                boolean foundRows = false;
                boolean foundColumns = false;
                Matcher RLEmatcherY = RLEpatternY.matcher(line);
                Matcher RLEmatcherX = RLEpatternX.matcher(line);
                int rows = 0;
                int columns = 0;

                if (RLEmatcherY.find()) {
                    rows = Integer.parseInt(RLEmatcherY.group(2));
                    foundRows = true;
                }

                if (RLEmatcherX.find()) {
                    columns = Integer.parseInt(RLEmatcherX.group(2));
                    foundColumns = true;
                }

                if (foundRows && foundColumns) {
                    board = new byte[rows][columns];
                    System.out.println("Board created :" + board);
                    return;
                }
                if (!foundRows && !foundColumns) {
                    //throw new PatternFormatException("X and Y values could not be parsed from RLE-file");
                } else if (!foundRows) {
                    //throw new PatternFormatException("Y values could not be parsed from RLE-file");
                } else if (!foundColumns) {
                    //throw new PatternFormatException("X values could not be parsed from RLE-file");
                }
            }
        }
    }
}
