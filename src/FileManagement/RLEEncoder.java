package FileManagement;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import GameOfLife.FixedBoard;
import GameOfLife.MetaData;
import util.DialogBoxes;


/**
 * This class handles the interpretation of the board and translates it to Run Length Encoding format
 * Three methods that interprets the meta data and one method that parses the byte[][] board
 *
 * @author Stian Reistad Røgeberg, Robin Sean Aron David Lundh, Terje Leirvik.
 */
public class RLEEncoder {
    private byte[][] board;
    private MetaData metadata;
    private Path filePath;
    private StringBuffer rleString;

    /**
     * Constructor that creates an RLEDecoder object.
     * @param b The board that we want to parse and save to file
     * @param f The file to save the board to
     */
    public RLEEncoder(FixedBoard b, File f) {
    metadata = b.getMetaData();
    board = b.getBoardReference();
    filePath = f.toPath();
    }

    /**
     * Creates a StringBuffer and encodes the board as well as the associated metadata before writing
     * the string to the file specified in the constructor.
     *
     * @return true if the writing to file is successful
     */
    public boolean encode() {
            rleString = new StringBuffer();
            Charset charset = Charset.forName("UTF-8");

            try (BufferedWriter bw = Files.newBufferedWriter(filePath, charset)) {
                    encodeMetaData();
                    encodeBoardSize();
                    encodeRuleString();
                    encodeBoard();

                    bw.write(rleString.toString());
            } catch (IOException ioE) {
            DialogBoxes.infoBox("Error!", "An unknown error occurred!", "The following error occurred when trying to save the game: " + ioE.getMessage());
            return false;
            }
            return true;
    }

     /**
     * Encodes the associated metadata and adds it to the StringBuffer.<p>
     *
     *This method is not meant to be called directly, but rather through the encode() method.
     */
    public void encodeMetaData() {
        rleString.append("#N ");
        rleString.append(metadata.getName());
        rleString.append("\r\n");
        rleString.append("#O ");
        rleString.append(metadata.getAuthor());
        rleString.append("\r\n");
        String[] comments = metadata.getComment().split("\\r?\\n");

        for(String comment : comments) {
            rleString.append("#C ");
            rleString.append(comment);
            rleString.append("\r\n");
        }
    }

     /**
     * Encodes the board size and adds it to the StringBuffer.<p>
     *
     *This method is not meant to be called directly, but rather through the encode() method.
     */
    private void encodeBoardSize() {
        rleString.append("x = ");
        rleString.append(board[0].length);
        rleString.append(", y = ");
        rleString.append(board.length);
        rleString.append(", ");
    }
     /**
     * Encodes the game's rule string and adds it to the StringBuffer.<p>
     *
     *This method is not meant to be called directly, but rather through the encode() method.
     */
    private void encodeRuleString() {
        String[] rule = metadata.getRuleString();
        rleString.append("rule = B");
        rleString.append(rule[0]);
        rleString.append("/S");
        rleString.append(rule[1]);
        rleString.append("\r\n");
    }

    /**
     * This method parses the byte[][] game board and translates it to Run Length Encoding(RLE) format
     *
     */
    private void encodeBoard() {
        int count = 1;
        int currentCell = -1;

        for(int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[0].length; col++) {
                currentCell = board[row][col];
                if ((col != board[0].length - 1) && (currentCell == board[row][col + 1])) {
                    count++;
                } else {
                    rleString.append((count > 1) ? count : "");
                    rleString.append((currentCell == 1) ? "o" : "b");
                    count = 1;
                }
            }
            if (count > 1) {
                rleString.append((count > 1) ? count : "");
                rleString.append((currentCell == 1) ? "o" : "b");
                count = 1;
            }
            rleString.append((row != board.length-1) ? "$" : "!");
        }
    }
}
