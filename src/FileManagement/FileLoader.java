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
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import GameOfLife.MetaData;
import GameOfLife.PatternFormatException;
import util.DialogBoxes;

/**
 * @author Stian Reistad Røgeberg, Robin Sean Aron David Lundh, Terje Leirvik.
 */
public class FileLoader {

    private StringBuilder sb;
    private byte[][] board;
    private BufferedReader reader;
    private RLEDecoder rleDecoder;
    private List<String> RLEdata;

    /**
     *  Constructs an object and instantiates an ArrayList
     *
     *
     */
    public FileLoader() {
        // sb = new StringBuilder();
        RLEdata = new ArrayList<>();
    }

    public boolean readGameBoardFromDisk(File file) {
        long startTime = System.currentTimeMillis();

        if (file == null) {
            DialogBoxes.infoBox("Error!", "No such file!!", "Please try again!");
            return false;
        } else if (!file.isFile()) {
            DialogBoxes.infoBox("Error!", "Invalid file!", "Please try again!");
            return false;
        } else if (!file.canRead()) {
            DialogBoxes.infoBox("Error!", "Could not read file!",
                    "Please try again!");
        }
        // rleDecoder = new RLEDecoder();

        String line = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(file.getAbsolutePath()))) {
            while ((line = reader.readLine() ) != null) {
                RLEdata.add(line);
            }
            // rleDecoder.parseMetadata(reader);
            // rleDecoder.parseBoard(reader);
        } catch (FileNotFoundException fnfE) {
            DialogBoxes.infoBox("Error!", "File was not found", fnfE.getMessage());
            return false;
        } catch (IOException ioE) {
            DialogBoxes.infoBox("Error!", "An unknown Input/Output error occurred", ioE.getMessage());
            return false;
        }

        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println("Tid i ms: " + elapsedTime);
        return true;
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



    public boolean readGameBoardFromURL(String urlString) {
        try {
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()))) {
                String line = null;
                while ((line = reader.readLine()) != null) {

                    sb.append(line + "\n");
                }
                // System.out.println(sb.toString());
            } catch (IOException ioE) {
                DialogBoxes.infoBox("Error!", "", "");
                return false;
            }
        } catch (MalformedURLException muE) {
            DialogBoxes.infoBox("Error!", "Invalid URL!", "Please check URL " +
                    "and try again.");
            return false;
        } catch (IOException ex) {
            DialogBoxes.infoBox("Error!", "", "");
            return false;
        }
        return true;
    }
    public List<String> getRLEdata() {
        return RLEdata;
    }

    public byte[][] getBoard() {
        return rleDecoder.getBoard();
    }

    public MetaData getMetadata() {
        return rleDecoder.getMetadata();
    }
}

// x = 1714, y = 1647, rule = s23/b3



// If line contains rule/size do that
// Mest effektive ville være å gjøre dette
// Utenfor while loop, som burde vøre dedikert
// Til lesing av brett
// IF LINE NOT CONTAINS # OR RULE/SIZE: break;