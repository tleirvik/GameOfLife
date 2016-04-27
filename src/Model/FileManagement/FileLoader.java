/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.FileManagement;

import Model.FileManagement.Decoders.*;
import Model.FileManagement.Decoders.RLEDecoder;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import Model.GameOfLife.MetaData;
import Model.GameOfLife.PatternFormatException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;

import Model.util.DialogBoxes;

/**
 * This class opens a file from disk or a an URL. It passes the file to the RLEDecoder for decoding of the RLE file
 *
 * @author Stian Reistad Røgeberg, Robin Sean Aron David Lundh, Terje Leirvik.
 * @version 3.0 Created this class to separate file handling from the decoder and encoder
 * @see RLEDecoder
 * @see RLEEncoder
 */
public class FileLoader {
    private File patternDirectory = new File("Patterns");
    private Decoder decoder;

    public void initialize() {
        patternDirectory.mkdir(); //Returns true false if folder is created/exists
        System.out.println(patternDirectory.getAbsolutePath());
    }
    
    private String getFileExtension(String fileName) {
            return fileName.substring(fileName.lastIndexOf("."));
    }
    
    public File[] getPatternDirectoryFiles() {
        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                String fileExtension = getFileExtension(name);
                //True om den er RLE, false hvis den er noe annet
                return fileExtension.equals(".rle");
            }
        };
        
        return patternDirectory.listFiles(filter);
    }
    /**
     *  This method takes a file as input and uses a BufferedReader to read the file. Instantiates a RLEDecoder object
     *  and calls the decode() method in RLEDecoder for interpreting the file.
     * @param file File to be read
     * @return boolean True if the file is parsed by the RLEDecoder
     */
    public boolean readGameBoardFromDisk(File file) {
        if (!file.canRead()) {
            DialogBoxes.infoBox("Error!", "Could not read file!",
                    "Please check your file permissions!");
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file.getAbsolutePath()))) {
            readGameBoard(reader, getFileExtension(file.getName()));
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
    public boolean readGameBoardFromURL(String urlString) {
        try {
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                readGameBoard(reader, getFileExtension(url.getPath()));
                
                /*
                //SAVE LOADED FILE TO FOLDER
                //if(downloadFiles == true) {
                String fileName = decoder.getMetadata().getName().replaceAll("[^a-zA-Z0-9]", "").toLowerCase(); //FILNAVNET
                File pattern = new File(patternDirectory.getAbsolutePath() + "\\" + fileName + ".rle"); //HVOR DEN LAGRES
                Files.copy(url.openStream(), pattern.toPath()); //KOPIERING HER
                //SAVE LOADED FILE TO FOLDER
                */
                
            } catch (IOException ioE) {
                DialogBoxes.infoBox("Error!", "", ioE.getMessage());
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
            DialogBoxes.infoBox("Error!", "Unknown IO Exception:", ex.getMessage());
            return false;
        }
        
        return true;
    }
    
    public void readGameBoard(BufferedReader reader, String fileExtension) throws PatternFormatException, IOException {
        if(fileExtension.matches(".rle")) {
            decoder = new RLEDecoder(reader);
            
        } else if(fileExtension.matches(".life|.lif")) {
            String patternType = reader.readLine();
            
            if(patternType.matches("#Life 1.05")) {
                decoder = new Life105Decoder(reader);
                
            } else if(patternType.matches("#Life 1.06")) {
                decoder = new Life106Decoder(reader);
                
            } else {
                throw new PatternFormatException("The header does not contain '#Life 1.05' or '#Life 1.06' on line 1.");
                
            }
        } else if(fileExtension.matches("Unknown")) { //ONLY FROM URL
            //IMPLEMENT FUNCTIONALITY FOR USER TO INPUT FILE FORMAT (DIALOG?)
            
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
