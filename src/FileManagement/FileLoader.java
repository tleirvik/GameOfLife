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
    private BufferedReader reader;
    private RLEDecoder rleDecoder;
    private List<String> RLEdata;

    /**
     *  Constructs a FileLoader object and instantiates an ArrayList for
     *  storing RLE data and a StringBuilder for creating a String from
     *  a file
     *
     *  Det er bad practice å sende åpne filer mellom klasser i Java.
     *  @author tleirvik, rlundh, srrogeberg
     *  @version 2.0 Created this class to remove file handling from the decoder and encoder
     *
     *  @see RLEDecoder
     *  @see RLEDecoder
     *
     */
    public FileLoader() {
        sb = new StringBuilder();
        RLEdata = new ArrayList<>();
    }

    public boolean readGameBoardFromDisk(File file) {
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

        String line = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(file.getAbsolutePath()))) {
            while ((line = reader.readLine() ) != null) {
                RLEdata.add(line);
            }
        } catch (FileNotFoundException fnfE) {
            DialogBoxes.infoBox("Error!", "File was not found", fnfE.getMessage());
            return false;
        } catch (IOException ioE) {
            DialogBoxes.infoBox("Error!", "An unknown Input/Output error occurred", ioE.getMessage());
            return false;
        }
        return true;
    }

    public boolean readGameBoardFromURL(String urlString) {
        try {
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()))) {
                String line = null;
                while ((line = reader.readLine()) != null) {
                    RLEdata.add(line);
                    // sb.append(line + "\n");
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

    /**
     *  Return the associated MetaData object
     * @return MetaData Returns the boards MetaData
     */
    public MetaData getMetadata() {
        return rleDecoder.getMetadata();
    }
}
