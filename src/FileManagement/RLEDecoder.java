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

    private BufferedReader reader;
    private MetaData metadata;
    private byte[][] board;
    private List<String> RLEdata;

    /**
     * Constructs a RLEDecoder with file as input
     *
     * @param
     */
    public RLEDecoder() {
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
    public boolean decode() throws IOException, PatternFormatException {

        rleDecoder = new RLEDecoder();
        long startTime = System.currentTimeMillis();

        try {
            rleDecoder.parseMetadata(reader);
        } catch (IOException ioE) {

        }

        try {
            rleDecoder.parseBoard(reader);
        } catch (PatternFormatException pfE) {
            DialogBoxes.infoBox("Error!", "The file is not in a compatible format", "The following error occurred trying to interpret board content " + pfE.getMessage());
            return false;
        }
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println("Tid i ms: " + elapsedTime);
        return true;
    }

    /* catch (PatternFormatException pfE) {
            DialogBoxes.infoBox("Error!", "The file is not in a compatible format", "The following error occurred trying to interpret board size: " + pfE.getMessage());
            return false;
        }

        */
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


    /**
     * Reads the game's rulestring from the previously loaded contents and stores it in a MetaData-object.
     *
     * This method is not meant to be called directly, but rather through the decode() method in
     * a RLEDecoder-object.
     *
     * @throws PatternFormatException
     * @return void
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
