package model.filemanagement;

import model.filemanagement.decoders.Decoder;
import model.filemanagement.decoders.Life105Decoder;
import model.filemanagement.decoders.Life106Decoder;
import model.filemanagement.decoders.RLEDecoder;
import model.gameoflife.MetaData;
import model.gameoflife.PatternFormatException;
import model.util.DialogBoxes;
import javafx.scene.control.Alert;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import model.filemanagement.encoders.RLEEncoder;

/**
 * This class opens a file from disk or a an URL. It passes the file to the RLEDecoder for decoding of the RLE file
 *
 * @version 3.0 Created this class to separate file handling from the decoder and encoder
 * @see RLEDecoder
 * @see RLEEncoder
 */
public class FileLoader {
    private Decoder decoder;

    /**
     * This method loads a game board from file
     * @param f The {@link File} to load
     * @param type The {@link EncodeType} of the board to load
     * @return true if the board could be loaded. Otherwise false
     */
    public boolean loadBoard(File f, EncodeType type) {
        if (!f.canRead()) {
            DialogBoxes.openAlertDialog(Alert.AlertType.ERROR, "Error!", 
                    "Could not read file!", "Please check your file permissions!");
            return false;
        }

        try (BufferedReader reader = new BufferedReader(
                new FileReader(f.getAbsolutePath()))) {
            decodeBoard(reader, type);
        }  catch (FileNotFoundException fnfE) {
            DialogBoxes.openAlertDialog(Alert.AlertType.ERROR, "Error!", 
                    "File was not found", fnfE.getMessage());
            return false;
        } catch (IOException ioE) {
            DialogBoxes.openAlertDialog(Alert.AlertType.ERROR, "Error!", 
                    "An unknown Input/Output error occurred", ioE.getMessage());
            return false;
        } catch (PatternFormatException pfE) {
            DialogBoxes.openAlertDialog(Alert.AlertType.ERROR, "Error!", 
                    "The board could not be parsed correctly", pfE.getMessage());
            return false;
        }
        return true;
    }
    /**
     * This method loads a game board from a URL
     * @param url The {@link URL} to load
     * @param type The {@link EncodeType} of the board to load
     * @return true if the board could be loaded. Otherwise false
     */
    public boolean loadBoardFromURL(URL url, EncodeType type) {
        URLConnection conn;
        try {
            conn = url.openConnection();
        } catch (IOException ioE) {
            DialogBoxes.openAlertDialog(Alert.AlertType.ERROR, "Error!", 
                    "An unknown Input/Output error occurred", ioE.getMessage());
            return false;
        }
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            decodeBoard(reader, type);
        } catch (PatternFormatException ex) {
            DialogBoxes.openAlertDialog(Alert.AlertType.ERROR, "Error!", 
                    "The board could not be parsed correctly", ex.getMessage());
            return false;
        } catch (IOException ioE) {
            DialogBoxes.openAlertDialog(Alert.AlertType.ERROR, "Error!", 
                    "An unknown Input/Output error occurred", ioE.getMessage());
            return false;
        }
        
        return true;
    }

    /**
     * This method decodes the loaded board
     * @param reader The {@link BufferedReader} to decode
     * @param type The {@link EncodeType} of the board to decode
     * @throws IOException If an unspecified I/O error occurs
     * @throws PatternFormatException If the board could not be interpreted
     */
    private void decodeBoard(BufferedReader reader, EncodeType type) throws IOException, PatternFormatException {
        switch(type) {
            case RLE:
                decoder = new RLEDecoder(reader);
                break;
            case LIFE105: //Switch-fall
            case LIFE106:
                String encodeType = reader.readLine();
                if(encodeType.matches("#Life 1.05")) {
                    decoder = new Life105Decoder(reader);
                } else if(encodeType.matches("#Life 1.06")) {
                    decoder = new Life106Decoder(reader);
                } else {
                    DialogBoxes.openAlertDialog(Alert.AlertType.ERROR, "Error!", 
                    "Could not determine the encode type.", "Please ensure that "
                            + "\"#Life 1.05\" or \"#Life 1.06\" is written on line 1.");
                }
                break;
        }
        decoder.decode();
    }

    /**
     * Returns the associated MetaData object
     *
     * @return MetaData Returns the boards MetaData(From the RLEDecoder object)
     * @see RLEDecoder
     */
    public MetaData getMetadata() {
        return decoder.getMetadata();
    }

    /**
     *  Returns the associated board(From the RLEDecoder object)
     *
     * @return Board Returns a byte[][] board
     * @see RLEDecoder
     */
    public byte[][] getBoard() {
        return decoder.getBoard();
    }
}
