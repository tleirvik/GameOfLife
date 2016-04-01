/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FileManagement;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import GameOfLife.MetaData;
import GameOfLife.PatternFormatException;
import util.DialogBoxes;

/**
 * This class opens a file from disk or a an URL. It passes the file to the RLEDecoder for decoding of the RLE file
 *
 * @author Stian Reistad Røgeberg, Robin Sean Aron David Lundh, Terje Leirvik.
 * @version 3.0 Created this class to separate file handling from the decoder and encoder
 * @see RLEDecoder
 * @see RLEEncoder
 */
public class FileLoader {
    private RLEDecoder rleDecoder;

    /**
     *  This method takes a file as input and uses a BufferedReader to read the file. Instantiates a RLEDecoder object
     *  and calls the decode() method in RLEDecoder for interpreting the file.
     * @param file File to be read
     * @return boolean True if the file is parsed by the RLEDecoder
     * @throws PatternFormatException Throws an exception if the method is unable to parse the RLE file
     */
    public boolean readGameBoardFromDisk(File file) throws PatternFormatException {
        if (!file.canRead()) {
            DialogBoxes.infoBox("Error!", "Could not read file!",
                    "Please check your file permissions!");
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file.getAbsolutePath()))) {
            rleDecoder = new RLEDecoder(reader);
            rleDecoder.decode();
        }  catch (FileNotFoundException fnfE) {
            DialogBoxes.infoBox("Error!", "File was not found", fnfE.getMessage());
            return false;
        } catch (PatternFormatException pfE) {
            DialogBoxes.infoBox("Error!", "The file is not in a compatible format",
                    "The following error occurred trying to interpret game rules: " + pfE.getMessage());
            return false;
        } catch (IOException ioE) {
            DialogBoxes.infoBox("Error!", "An unknown Input/Output error occurred", ioE.getMessage());
            return false;
        }
        return true;
    }

    /**
     * This method takes a String as input and opens a connection to the specified URL and downloads the file using
     * InputStreamReader. Instantiates a RLEDecoder object and calls the decode() method in RLEDecoder.
     * @param urlString String that represents the URL where the file resides
     * @return boolean True if the file is downloaded and is parsed by the RLEDecoder.
     * @throws PatternFormatException Throws an exception if the method is unable to parse the RLE file
     */
    public boolean readGameBoardFromURL(String urlString) throws PatternFormatException {
        try {
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                rleDecoder = new RLEDecoder(reader);
                rleDecoder.decode();
            } catch (IOException ioE) {
                DialogBoxes.infoBox("Error!", "", "");
                return false;
            }
        } catch (MalformedURLException muE) {
            DialogBoxes.infoBox("Error!", "Invalid URL!", "Please check URL " +
                    "and try again.");
            return false;
        } catch (PatternFormatException pfE) {
            DialogBoxes.infoBox("Error!", "The file is not in a compatible format",
                    "The following error occurred trying to interpret game rules: " + pfE.getMessage());
            return false;
        } catch (IOException ex) {
            DialogBoxes.infoBox("Error!", "", "");
            return false;
        }
        return true;
    }

    /**
     * Returns the associated MetaData object
     *
     * @return MetaData Returns the boards MetaData(From the RLEDecoder object)
     * @see RLEDecoder
     */
    public MetaData getMetadata() {
        return rleDecoder.getMetadata();
    }

    /**
     *  Returns the associated board(From the RLEDecoder object)
     *
     * @return Board Returns a byte[][] board
     * @see RLEDecoder
     */
    public byte[][] getBoard() {
        return rleDecoder.getBoard();
    }
}
