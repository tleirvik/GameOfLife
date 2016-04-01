package FileManagement;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import GameOfLife.MetaData;
import GameOfLife.PatternFormatException;

/**
 * This class handles the interpretation of RLE files. One method reads the meta data and creates the game board and
 * the second method translates the files to our byte[][] game board.
 * 
 * @author Stian Reistad Røgeberg, Robin Sean Aron David Lundh, Terje Leirvik.
 *
 * @version 4.0 This class has been the focus of our attention for some time and we developed several versions and
 * this is as of now our final version. Changed the interpretation logic from regex to if-else branching and we used
 * JProfiler to measure performance increase upwards of 4000%!
 */
public class RLEDecoder {
    private MetaData metadata;
    private byte[][] board;
    private BufferedReader reader;

    /**
     * Constructs a RLEDecoder with a BufferedReader as input
     *
     * @param reader BufferedReader file to be parsed
     */
    RLEDecoder(BufferedReader reader) {
        this.reader = reader;
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
    public void decode() throws PatternFormatException, IOException {
        parseMetadata(reader);
        parseBoard(reader);
    }


    /**
     * Reads the metadata from the previously loaded contents and stores it in a new MetaData-object.
     * If the metadata is incorrectly formatted then it will not be stored.
     *
     * This method is not meant to be called directly, but rather through the decode() method in
     * a RLEDecoder-object.
     *
     * @throws PatternFormatException Throws an exception if the method is unable to parse the RLE file
     * @see MetaData
     */
    private void parseMetadata(BufferedReader reader) throws PatternFormatException, IOException {
        metadata = new MetaData();
        StringBuilder name = new StringBuilder();
        StringBuilder author = new StringBuilder();
        StringBuilder comment = new StringBuilder();
        Pattern RLEpatternY = Pattern.compile("([yY][\\s][=][\\s])([\\d]+)");
        Pattern RLEpatternX = Pattern.compile("([xX][\\s][=][\\s])([\\d]+)");

        String line = null;
        while ((line = reader.readLine()) != null) {
            if (line.charAt(0) == '#') {
                final char tempChar = line.charAt(1);
                switch (tempChar) {
                    case 'N':
                        String tempString = line.replaceAll("(#N )", "");
                        name.append(tempString);
                        metadata.setName(name.toString());
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
                    board = new byte[rows+2][columns+2];
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

    /**
     * This method parses the RLE file and converts the contents to a byte[][] board.
     *
     * @param reader BufferedReader The file to be interpreted
     * @throws PatternFormatException Throws an exception if the method is unable to parse the RLE file
     * @throws IOException Other unspecified I/O related issues
     *
     * @version 3.0 Complete changed the logic. Converted from regex to if-else and
     * we observed a > 3800% performance gain. Large RLE files now usually never takes more than a few hundred
     * milisecounds compared to >40 seconds using regex
     *
     */
    private void parseBoard(BufferedReader reader) throws PatternFormatException, IOException {
        String line = null;
        int row = 1, col = 1;
        final char lineBreak = '$';

        while ((line = reader.readLine()) != null) {
            int charNumber = 0; // Antall forekomster av en celle
            for (int j = 0; j < line.length(); j++) {
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
                    if (charNumber != 0) {
                        while (charNumber != 0) {
                            charNumber--;
                            row++;
                        }
                    } else {
                        row++;
                    }
                    col = 1;
                }
            }
        }
    }

    /**
     * Method that returns the board contained in this class
     * @return board Returns the boolean[][] board contained in this class
     */
    public byte[][] getBoard() {
            return this.board;
    }

    /**
     * Method that returns the associated MetaData object that we read from the RLE files
     *
     * @return metadata A MetaData object that contains relevant metadata about the board
     */
    public MetaData getMetadata() {
        return this.metadata;
    }
}
