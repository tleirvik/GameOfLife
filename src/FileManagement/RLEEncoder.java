package FileManagement;

import GameOfLife.GameOfLife;
import GameOfLife.MetaData;
import util.DialogBoxes;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;


/**
 * This class handles the interpretation of the board and translates it to Run Length Encoding format
 * Three methods that interprets the meta data and one method that parses the byte[][] board
 *
 * @author Stian Reistad Røgeberg, Robin Sean Aron David Lundh, Terje Leirvik.
 */
public class RLEEncoder {
    private final MetaData metadata;
    private final Path filePath;
    private final StringBuffer rleString;
    private final GameOfLife game;

    /**
     * Constructor that creates an RLEDecoder object.
     *
     */
    public RLEEncoder(GameOfLife game, File f) {
        metadata = game.getMetaData();
        this.game = game;
        filePath = f.toPath();
        rleString = new StringBuffer();
    }

    /**
     * Creates a StringBuffer and encodes the board as well as the associated metadata before writing
     * the string to the file specified in the constructor.
     *
     * @return true if the writing to file is successful
     */
    public boolean encode() {
            Charset charset = Charset.forName("UTF-8");

            try (BufferedWriter bw = Files.newBufferedWriter(filePath, charset)) {
                    encodeMetaData();
                    encodeBoardSize();
                    encodeRuleString();
                    writeMetadata(bw);
                    encodeBoard();
                    writeBoard(bw);
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
        rleString.append(game.getColumns() -2);
        rleString.append(", y = ");
        rleString.append(game.getRows() -2);
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
        // Bør bytte til StringBuilder fordi den er raskere StringBuffer
        int count = 1;
        int previous = -1;

        for (int row = 0; row < game.getRows(); row++) {
            for (int col = 0; col < game.getColumns(); col++) {
                final int nextPosition = col + 1;
                final byte currentCell = game.getCellAliveState(row, col);
                if (col < game.getColumns() && currentCell == nextPosition) {
                    count++;
                } else {
                    rleString.append(count > 1 ? count : "" ).append(currentCell == 1 ? "o" : "b");
                    count = 1;
                }

                /*
                while (col < board[0].length && currentCell == nextPosition) {
                    count++;
                }
                */

                if (count > 1) {
                    rleString.append((count >1) ? count : "");
                    rleString.append(currentCell == 1 ? "o" : "b");
                    count = 1;
                }
                /*
                else {
                    rleString.append(currentCell == 1 ? "o" : "b");
                }
                //rleString.append(count).append(currentCell == 1 ? "o" : "b");
                //rleString.append(count > 1 ? count : "" ).append(currentCell == 1 ? "o" : "b");
                */
            }
            rleString.append("$");
        }
        rleString.append("!");
    }

    private void writeMetadata(BufferedWriter bw) throws IOException {
        bw.write(rleString.toString());
        rleString.delete(0, rleString.length());
    }

    private void writeBoard(BufferedWriter bw) throws IOException {
        int offset = 0;
        final int offsetValue = 79;
        final String tempRleString = rleString.toString();
        while(offset < tempRleString.length()) {
            if(tempRleString.length() - offset < offsetValue) {
                bw.write(rleString.toString(), offset, tempRleString.length() - offset);
                return;
            }
            bw.write(rleString.toString(), offset, offsetValue);
            bw.write("\r\n");
            offset += offsetValue;
        }

    }
}