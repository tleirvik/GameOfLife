package FileManagement;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
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
    private List<String> RLEdata;

    /**
     * Constructs a RLEDecoder with file as input
     *
     * @param
     */
    public RLEDecoder(List<String> RLEdata) {
        this.RLEdata = RLEdata;
    }

    /**
     * "Samlemetode" that controls the flow of the RLE file parsing
     * This method calls other methods in this class with the purpose of
     * doing the following:
     * <p>
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
     * @return boolean Returns true if the method parses the board with causing an
     * Exception
     * @throws PatternFormatException Throws an exception if the method is unable to
     *                                parse the RLE file
     */
    public boolean decode() throws PatternFormatException {
        try {
            parseMetadata();
        } catch (PatternFormatException pfE) {
            DialogBoxes.infoBox("Error!", "The file is not in a compatible format", "The following error occurred trying to interpret board content " + pfE.getMessage());
            return false;
        }
        removeComments();
        System.out.println("RLEdata" + RLEdata);
        try {
            parseBoard();
        } catch (PatternFormatException pfE) {
            DialogBoxes.infoBox("Error!", "The file is not in a compatible format", "The following error occurred trying to interpret board content " + pfE.getMessage());
            return false;
        }
        return true;
    }
    private void removeComments() {
        for(int i = 0; i < RLEdata.size(); i++) {
            if(RLEdata.get(i).startsWith("#")) {
                RLEdata.remove(i);
                //i--;
            }
        }

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
    private void parseMetadata() throws PatternFormatException {
        metadata = new MetaData();
        StringBuilder name = new StringBuilder();
        StringBuilder author = new StringBuilder();
        StringBuilder comment = new StringBuilder();
        Pattern RLEpatternY = Pattern.compile("([yY][\\s][=][\\s])([\\d]+)");
        Pattern RLEpatternX = Pattern.compile("([xX][\\s][=][\\s])([\\d]+)");

        String line = null;
        for (Object item : RLEdata) {
            line = item.toString();
            // System.out.println("i:" + item.toString());
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
                    System.out.println("Board created :" + board + " x: " + columns + " y: " + rows);
                    return;
                }

                if (!foundRows && !foundColumns) {
                    throw new PatternFormatException("X and Y values could not be parsed from RLE-file");
                } else if (!foundRows) {
                    throw new PatternFormatException("Y values could not be parsed from RLE-file");
                } else if (!foundColumns) {
                    throw new PatternFormatException("X values could not be parsed from RLE-file");
                }
            }
        }
    }

    private void parseBoard() throws PatternFormatException {
        String line = null;
        System.out.println("parseBoard");
        int row = 0, col = 0;
        final char lineBreak = '$';
        System.out.println(RLEdata.toString());
        for (Object item : RLEdata) {
            // System.out.println(item);
            line = item.toString();
            // System.out.println(line.length());
            //System.out.println("Hallo" + item.toString());
            int charNumber = 0; // Antall forekomster av en celle
            for (int j = 0; j < line.length(); j++) {
                System.out.println(j);
                if (Character.isDigit(line.charAt(j))) {
                    if (charNumber == 0) {
                        charNumber = Character.getNumericValue(line.charAt(j));
                    } else {
                        charNumber *= 10;
                        charNumber += Character.getNumericValue(line.charAt(j));
                    }
                } else if (line.charAt(j) == 'o' || line.charAt(j) == 'O') {
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
                } else if (line.charAt(j) == 'b' || line.charAt(j) == 'B') {
                    if (charNumber != 0) {
                        while (charNumber != 0) {
                            charNumber--;
                            col++;
                        }
                    } else {
                        col++;
                    }
                } else if (line.charAt(j) == lineBreak) {
                    row++;
                    col = 0;
                    charNumber = 0;
                }
            }
        }
    }

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
}
