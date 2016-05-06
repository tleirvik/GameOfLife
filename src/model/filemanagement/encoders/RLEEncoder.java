package model.filemanagement.encoders;

import model.util.DialogBoxes;
import javafx.scene.control.Alert;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import model.gameoflife.GameOfLife;


/**
 * This class handles the interpretation of the board and translates it to Run Length Encoding format
 * Three methods that interprets the meta data and one method that parses the byte[][] board
 *
 */
public class RLEEncoder extends Encoder {

    /**
     * Constructor that creates a RLEDecoder object.
     *
     * @param game The {@link GameOfLife} object to decode
     * @param f The {@link File} to save
     */
    public RLEEncoder(GameOfLife game, File f) {
        super(game, f);
    }

    /**
     * This is the public interface that starts the decoding of the file
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
            DialogBoxes.openAlertDialog(Alert.AlertType.ERROR,"Error!", 
                    "An unknown error occurred!", 
                    "The following error occurred when trying to save the game: " 
                            + ioE.getMessage());
            return false;
        }
            return true;
    }

    /**
     * Encodes the associated metadata and adds it to the {@link StringBuilder}.<p>
     *
     *This method is not meant to be called directly, but rather through the encode() method.
     */
    private void encodeMetaData() {
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
     * Encodes the board size and adds it to the {@link StringBuilder}.<p>
     *
     *This method is not meant to be called directly, but rather through the encode() method.
     */
    private void encodeBoardSize() {
        rleString.append("x = ");
        rleString.append(game.getColumns());
        rleString.append(", y = ");
        rleString.append(game.getRows());
        rleString.append(", ");
    }
     /**
     * Encodes the game's rule string and adds it to the {@link StringBuilder}.<p>
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
     * This method encodes the file to RLE format using a {@link StringBuilder}
     */
    private void encodeBoard() {
        int count = 1;
        int previous = -1;
        
        int o = 0;
        int b = 0;
        
        byte currentCell;

        for (int row = 0; row < game.getRows(); row++) {
            for (int col = 0; col < game.getColumns(); col++) {
                currentCell = game.getCellAliveState(row, col);
                    
                if (currentCell == 1) {
                    o++;
                    if (b != 0) {
                        rleString.append((b > 1) ? b : "").append("b");
                        b = 0;
                    }
                }

                if (currentCell == 0) {
                    b++;
                    if (o != 0) {
                        rleString.append((o > 1) ? o : "").append("o");
                        o = 0;
                    }
                }
            } // end inner loop
            if (o > 0 ) {
                rleString.append((o > 1) ? o : "").append("o");
                o = 0; 
            } else if( b > 0) {
                rleString.append((b > 1) ? b : "").append("b");
                b = 0;
            }
            rleString.append("$");
        } // end outer loop
        rleString.append("!");
    }

    /**
     * Writes the meta data to file
     * @param bw The {@link BufferedWriter} to use for saving the data
     * @throws IOException If an unspecified I/O error occurs
     * @see model.gameoflife.MetaData
     */
    private void writeMetadata(BufferedWriter bw) throws IOException {
        bw.write(rleString.toString());
        rleString.delete(0, rleString.length());
    }

    /**
     * Writes the translated board to file
      * @param bw The {@link BufferedWriter} to use for saving the data
     * @throws IOException If an unspecified I/O error occurs
     */
    private void writeBoard(BufferedWriter bw) throws IOException {
        int offset = 0;
        final int offsetValue = 79;
        final String tempRleString = rleString.toString();
        
        while (offset < tempRleString.length()) {
            if (tempRleString.length() - offset < offsetValue) {
                bw.write(rleString.toString(), offset, tempRleString.length() - offset);
                return;
            }
            bw.write(rleString.toString(), offset, offsetValue);
            bw.write("\r\n");
            offset += offsetValue;
        }
    }
    @Override
    public String toString() {
        return rleString.toString();
    }
}