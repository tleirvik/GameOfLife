/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FileManagement;

import GameOfLife.PatternFormatException;
import GameOfLife.ViewController;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Stian Reistad Røgeberg, Robin Sean Aron David Lundh, Terje Leirvik.
 */
public class FileLoader {
    private List<String> RLEdata;
    
    public FileLoader() {
        RLEdata = new ArrayList<>();
    }
    
    public boolean readGameBoardFromDisk(File file) {
        if(file == null) {
            ViewController.infoBox("Error!", "No such file!!", "Please try again!");
            return false;
        } else if(!file.isFile()) {
           ViewController.infoBox("Error!", "Invalid file!", "Please try again!");
           return false;
        } else if(!file.canRead()) {
            ViewController.infoBox("Error!", "Could not read file!", 
                    "Please try again!");
        }
        
        String line = null;
        try (BufferedReader reader = 
                new BufferedReader(new FileReader(file.getAbsolutePath()))) {
            while ((line = reader.readLine() ) != null) {
                    RLEdata.add(line);
            }
        } catch (FileNotFoundException fnfE) {
            ViewController.infoBox("Error!", "File was not found", fnfE.getMessage());
            return false;
        } catch (IOException ioE) {
            ViewController.infoBox("Error!", "An unknown Input/Output error occurred", ioE.getMessage());
            return false;
        }
        
        return true;
    }
    
    public void readGameBoardFromURL(String url) throws IOException,
        PatternFormatException {
        URL destination = new URL(url);
        URLConnection conn = destination.openConnection();
    }
    
    /**
     * Reads the contents of the input file and stores it in an ArrayList for future use.
     *
     *This method is not meant to be called directly, but rather through the decode() method in
     * a RLEDecoder-object.
     *
     * @param file
     * @throws FileNotFoundException
     * @throws IOException
     */
 
    
    /**
     * 
     * 
     * @return 
     */
    public List<String> getRLEdata() {
        return RLEdata;
    }
}
