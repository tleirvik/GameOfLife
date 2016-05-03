package Model.FileManagement;

import Model.FileManagement.Decoders.*;
import Model.FileManagement.Decoders.RLEDecoder;
import Model.FileManagement.Encoders.RLEEncoder;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import Model.GameOfLife.MetaData;
import Model.GameOfLife.PatternFormatException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import Model.util.DialogBoxes;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;

/**
 * This class opens a file from disk or a an URL. It passes the file to the RLEDecoder for decoding of the RLE file
 *
 * @author Stian Reistad Røgeberg, Robin Sean Aron David Lundh, Terje Leirvik.
 * @version 3.0 Created this class to separate file handling from the decoder and encoder
 * @see RLEDecoder
 * @see RLEEncoder
 */
public class FileLoader {
    private Decoder decoder;
    
    public boolean loadBoard(File f, EncodeType type) throws IOException, PatternFormatException {
        if (!f.canRead()) {
            DialogBoxes.openAlertDialog(Alert.AlertType.ERROR, "Error!", "Could not read file!",
                    "Please check your file permissions!");
            return false;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(f.getAbsolutePath()))) {
            decodeBoard(reader, type);
        }  catch (FileNotFoundException fnfE) {
            DialogBoxes.openAlertDialog(Alert.AlertType.ERROR, "Error!", "File was not found", fnfE.getMessage());
            return false;
        } catch (IOException ioE) {
            DialogBoxes.openAlertDialog(Alert.AlertType.ERROR, "Error!", "An unknown Input/Output error occurred", ioE.getMessage());
            return false;
        }
        return true;
    }
    
    public void loadBoardFromURL(URL url, EncodeType type) {
        try {
            URLConnection conn = url.openConnection();
            //TODO: 01.05.2016 Logger skal byttes ut?
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                decodeBoard(reader, type);
            } catch (PatternFormatException ex) {
                Logger.getLogger(FileLoader.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(FileLoader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void decodeBoard(BufferedReader reader, EncodeType type) throws IOException, PatternFormatException {
        switch(type) {
            case RLE:
                decoder = new RLEDecoder(reader);
                break;
            case LIFE105: //Switch-fall
            case LIFE106:
                String encodeType = reader.readLine();
                if(encodeType.matches("#Life 1.05")) {

                } else if(encodeType.matches("#Life 1.06")) {

                } else {
                    //TODO: 01.05.2016 Kunne ikke finne ut typen fil-dialogboks her
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
